import java.io.File;

import javax.swing.JFileChooser;

public class DriverClass {
	
	public static void main(String[] args) {
		
		File template, data;
		
// ===================================================================================
// Grab the appropriate files for parsing

        JFileChooser fileChooser = new JFileChooser();
        
        fileChooser.setDialogTitle("Please select a template file for the data.");

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
          template = fileChooser.getSelectedFile();

          System.out.println(template.getName());
        }
        
        else{
        	System.out.println("No template file was chosen. Terminating program.");
        	return;
        }
        
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

// ===================================================================================
// Get the pertinent information from each file
        
        SolarDataParser sdp = new SolarDataParser();
        
        sdp.parseTemplateElementData(template);
        sdp.parseTemplateRatioData(template);
        sdp.parseResultsData(data);
        
// ===================================================================================
// Put the data in the database
        
		DatabaseConnector dbc = new DatabaseConnector();

	}
}
