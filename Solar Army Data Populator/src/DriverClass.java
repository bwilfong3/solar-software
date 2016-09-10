import java.io.File;

import javax.swing.JFileChooser;

public class DriverClass {

    public static void main(String[] args) {
        
        File template, data;
        boolean keepGoing = true, validInput = true;
        String[] input;
        int xStart = 0,
        	yStart = 0,
        	xEnd = 0,
        	yEnd = 0;
        
// ===================================================================================
// Connect to database
        DatabaseConnector dbc = new DatabaseConnector();
                
        if (dbc.connectionSuccessful){
            System.out.println("Connection to database successful.");
            System.out.println("Welcome to the solar army data populator\n." + 
                               "Options:\n add\n quit");
            
            keepGoing = System.console().readLine() != "quit";
        }
        
        while(keepGoing){

// ===================================================================================
// Grab the appropriate files for parsing

	        JFileChooser fileChooser = new JFileChooser();
	        
	        System.out.println("Please select a template file for the data.");
	        fileChooser.setDialogTitle("Please select a template file for the data.");
	
	        int returnValue = fileChooser.showOpenDialog(null);
	
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	          template = fileChooser.getSelectedFile();
	          
	          System.out.println(template.getName());
	          
	          // get data about file using name. Is it new? Has R or B already been done?
	          // Check to see if a file name exists for either.
	          
	          // System.out.println("Files using Template:" + template);
	          // System.out.println(f1 + '\n', f2);
	        }
	        
	        else{
	            System.out.println("No template file was chosen. Terminating program.");
	            return;
	        }
	        
	        System.out.println("Please select the data file pertaining to this template.");
	        fileChooser.setDialogTitle("Please select a data file for the for the template.");
	        
	        returnValue = fileChooser.showOpenDialog(null);
	
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	          data = fileChooser.getSelectedFile();
	
	          System.out.println(data.getName());
	        }
	        
	        else{
	            System.out.println("No data file was chosen. Terminating program.");
	            return;
	        }
	        
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
	        

// ===================================================================================
// Get the pertinent information from each file
        
	        SolarDataParser sdp = new SolarDataParser();
	        
	        sdp.parseTemplateElementData(template);
	        sdp.parseTemplateRatioData(template);
	        sdp.parseResultsData(data, xStart, yStart, xEnd, yEnd); // get data from appropriate coordinates
	        

// ===================================================================================
// Put the data in the database
        

        }
    }
}
