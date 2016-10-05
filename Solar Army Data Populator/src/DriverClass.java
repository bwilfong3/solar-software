import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;

public class DriverClass {

    public static void main(String[] args) {
        
        File template = null, data = null;
        boolean keepGoing = true, validInput = true, templateExists = false;
        String[] input;
        int xStart = 0,
        	yStart = 0,
        	xEnd = 0,
        	yEnd = 0,
        	uid = -1;
        String color, plateRunner = "";
        Vector<String> ratios = new Vector<String>();
        Vector<Double> readings = new Vector<Double>();
        Vector<ElementEntry> elements = new Vector<ElementEntry>();
        
// ===================================================================================
// Connect to database
        SolarArmyDBC dbc = new SolarArmyDBC();
                
        if (dbc.connectionSuccessful){
            System.out.println("Connection to database successful.");
            System.out.println("Welcome to the solar army data populator\n" + 
                               "Options:\n  add\n  quit");
            
            keepGoing = !System.console().readLine().equals("quit");
        }
        else
        	return;
        
        while(keepGoing){ // start user interface to repeatedly add data
        	
        	// flush out the data structures after use
        	ratios.clear();
        	elements.clear();
        	readings.clear();

// ===================================================================================
// Grab the appropriate files for parsing

	        JFileChooser fileChooser = new JFileChooser();

	        System.out.println("Enter the unique ID for the template file"); 
	        	// we use this to see if we need to get a template file or
	        	// if we already have that one in the database.
	        
	        boolean badSelection = true; // flag to show bad input

	        int returnValue; // Flag for decision made by JFileChooser
	          
	        do{	        	  
	        	try{
	        		uid = Integer.parseInt(System.console().readLine()); // get string input ---> text
	        		templateExists = dbc.checkIfUIDExists(uid); // query database to check existence
	        		badSelection = false; // break out of loop for good input
	        	}
	        	  catch(NumberFormatException nfe){ // for bad input
	        		  System.out.println("Invalid numeric ID input. Please enter a new UID or press control + c to quit");
	        	  }
	          }while(badSelection); // keep going until we get something valid
	        
	        if(!templateExists){ // if we didn't have a template in the database, prompt the user for one
		        System.out.println("Please select a template file for the data.");
		        fileChooser.setDialogTitle("Please select a template file for the data.");
		
		        returnValue = fileChooser.showOpenDialog(null); // open file chooser
		
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		          template = fileChooser.getSelectedFile();
		          
		          System.out.println("Template file chosen: " + template.getName());
		          System.out.println("Who ran this plate?");
		          plateRunner = System.console().readLine();
		        }
		        
		        else{
		            System.out.println("No template file was chosen. Terminating program.");
		            return;
		        }
	        }
	       
		    System.out.println("Please select the data file pertaining to this template.");
		    fileChooser.setDialogTitle("Please select a data file for the for the template.");
		        
		    returnValue = fileChooser.showOpenDialog(null);
		
		    if (returnValue == JFileChooser.APPROVE_OPTION){
		          
		    	data = fileChooser.getSelectedFile();
	
		        if(data.getName().toLowerCase().contains("black"))
		        	color = "black";
		        	  	        	  
		        else if(data.getName().toLowerCase().contains("red"))
		        	color = "red";
		        
		    	else{
		    		System.out.println("Which color lead was used on this plate? (Red/black)");
		    		color = System.console().readLine().toLowerCase();
		    	}
        	}   
        	else{
		        System.out.println("No data file was chosen. Terminating program.");
		            return;
		    }
		          System.out.println("Color on lead: " + color);
	        	
	         System.out.println("Data file chosen: " + data.getName());  
	        
	         /*// currently leaving this out because the coordinates are the same every time so far
	        do{
		        System.out.println("Please enter the starting x and y coordinates from the data file in the form x,y");
		        input = System.console().readLine().split("[,]");
		        
		        try{
		        	xStart = Integer.parseInt(input[0]);
		        	yStart = Integer.parseInt(input[1]);
		        	System.out.println("(" + xStart + "," + yStart + ") chosen as ending coordinates in data");
		        	validInput = true;
		        }
		        catch(NumberFormatException nfe){
		        	validInput = false;
		        	System.out.println("Invalid format of coordinates. Please input starting coordinates in form x,y");
		        }
		    } while (!validInput);
	        */ 
	        /*
	        do{
	        	System.out.println("Please enter the ending x and y coordinates from the data file in the form x,y");
		        input = System.console().readLine().split("[,]");
		        
		        try{
		        	xEnd = Integer.parseInt(input[0]);
		        	yEnd = Integer.parseInt(input[1]);
		        	System.out.println("(" + xEnd + "," + yEnd + ") chosen as ending coordinates in data");
		        	validInput = true;
		        }
		        catch(NumberFormatException nfe){
		        	validInput = false;
		        	System.out.println("Invalid format of coordinates. Please input ending coordinates in form x,y");
		        }
		    } while (!validInput);
	        */

// ===================================================================================
// Get the pertinent information from each file
        
	        System.out.println("\n======================================= Data gathered from files =======================================\n");
	        
	        SolarDataParser sdp = new SolarDataParser();
	        
	        if(!templateExists){ // get template data from excel file and display it
		        System.out.println("Template Data:");
		        ratios = sdp.parseTemplateRatioData(template);
		        System.out.println(); // formatting
		        System.out.println("Elements extracted from template file:");
		        elements = sdp.parseTemplateElementData(template);
		    }
	        
	       // else
	        //	dbc.getTemplateData(uid);
	        
	        System.out.println("Data extracted from data file (Precision up to 5 decimal places shown for formatting. "
	        				 + "Trailing digits still stored.)");
	        //readings = sdp.parseResultsData(data, xStart, yStart, xEnd, yEnd); // for when the data can be inverted
	        readings = sdp.parseResultsData(data, 1, 1, 7, 7); // get data from appropriate coordinates
	        
	        System.out.println(); // formatting
	        System.out.println("Is this the data you want to enter into the database? Enter y/n");
	        
	        
	        if(System.console().readLine().toLowerCase().equals("y")){
	        	System.out.println("Data submitted to database.");
	        	if(!templateExists)
	        		if(!dbc.sendTemplateData(uid, plateRunner, ratios, elements)){
	        			System.out.println("Error adding template to database. Terminating program.");
	        			return;
	        		}
	        	
	        	dbc.sendResultsData(uid,color,readings);
	        }
	        else
	        	System.out.println("Data not submitted to database");
	        
	        System.out.println("Do you wish to continue populating the database? Enter y/n");
        	keepGoing = System.console().readLine().toLowerCase().equals("y");
	        

// ===================================================================================
// Put the data in the database
        

        }
    }
}
