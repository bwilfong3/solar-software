// This class will be used to execute some of the specific queries needed for the program

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Queue;
import java.util.Vector;

public class SolarArmyDBC extends DatabaseConnector{
	final String templateExistsQuery = "SELECT * FROM template WHERE t_file_name = ?";
		// t_file_name

	final String addTemplate = "INSERT INTO template VALUES(?,?,?)";
		// t_file_name, submitted_by, ratio_data

	final String associateElements = "INSERT INTO composes(?,?,?,?,?)";
		// t_file_name, atomicSymbol, pos, concentration, salt_used
	
	final String dataExistsQuery = "SELECT * FROM data_file WHERE uid = ?";
		// uid
	
	final String addDataFile = "INSERT INTO data_file VALUES(?,?,?,?)";
		// uid, d_file_name, submitted_by, data_readings
	
	final String associateDataFile = "INSERT INTO associated_with_data VALUES(?,?)";
		// t_file_name, uid
	
	Queue<PreparedStatement> q;
	
	public boolean checkIfUIDExists(int uid){
		try{
		String queryString = "SELECT * FROM plate_data WHERE plate_no = ?;";
		PreparedStatement ps = connection.prepareStatement(queryString);
		ps.setInt(1,  uid);
		
		ResultSet resultSet = ps.executeQuery();// query database
		 if(!resultSet.next())
		 {
			 System.out.println( "Plate " + uid + " does not yet exist in the database. " +
					 						 	  "File queued for submission.");
			 resultSet.close();
			 return false;
		 }
		 else
		 {
			System.out.println( "Template file exists in database.");
			System.out.println("Data from template:" +
								resultSet.getString(2) +
								'\n' + "Submitted by: " + resultSet.getString(1));
			resultSet.close();
			getDataFiles(uid);
			return true;
		 }
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	private void getDataFiles(int uid){
		try{
		String queryString = "SELECT plate_type FROM result WHERE uid = ?";
		PreparedStatement ps = connection.prepareStatement(queryString);
		ps.setInt(1,  uid);
		
		ResultSet resultSet = ps.executeQuery();
		
		if(!resultSet.next())
			System.out.println("No data files yet associated with plate " + uid);
		else{
			do{
				System.out.println(resultSet.getString("d_file_name"));
			}while(resultSet.next());
		}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void getTemplateData(int uid){
		// query to select and display template data from database
	}
	
	public boolean checkIfColorExists(int uid, String color){
		try{
		String queryString = "SELECT * FROM result WHERE plate_no = ? AND plate_type = ?;";
		PreparedStatement ps = connection.prepareStatement(queryString);
		ps.setInt(1,  uid);		
		ps.setString(2, color);
		
		ResultSet resultSet = ps.executeQuery();// query database
		 if(!resultSet.next())
		 {
			 System.out.println( "Data file with " + color + " on lead does not yet exist in database. Queued to be stored");
			 resultSet.close();
			 return false;
		 }
		 else
		 {
			System.out.println( color + " lead on plate already exists for this template. \n" +
										"If both black and red exist for this template, ctrl + c to quit and use a different one.");

			resultSet.close();
			getDataFiles(uid);
			return true;
		 }
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}		
	}
	

	public boolean sendTemplateData(int uid, String plateRunner, Vector<String> ratios, Vector<ElementEntry> elements){
		boolean successfulSend = false;
		try{
		String statement = "INSERT INTO plate_data VALUES (?,?)";
		
		PreparedStatement ps = connection.prepareStatement(statement);
		ps.setInt(1, uid);
		ps.setString(2, plateRunner);
		ps.execute();
		
		statement = "INSERT INTO element_data VALUES (?,?,?,?,?)";
		for (int i = 0; i < elements.size(); i++){
			ps = connection.prepareStatement(statement);
			ps.setInt(1,uid);
			ps.setString(2, elements.get(i).atomicSymbol);
			ps.setString(3, elements.get(i).salt);
			ps.setInt(4, i); // add position of element in ratios 
			ps.setString(5, elements.get(i).concentration);
			ps.execute();
		}
		
		statement = "INSERT INTO ratio_data VALUES (?,?,?,?)";
		
		for (int i = 0; i < ratios.size(); i++)
		{
			ps = connection.prepareStatement(statement);
			ps.setInt(1, uid);
			ps.setInt(2, i / 6); // row,column index, cell # 5 would be  (0,5)
								 // cell # 6 would be (1,0)
			ps.setInt(3, i % 6); // cell # 0 would be (0,0)
								 // cell # 35 would be (5,5)
			ps.setString(4, ratios.get(i));
			ps.execute();
		}
		
		successfulSend = true; // valid input into database
		}
		catch(SQLException sqle){
			System.out.println("Error sending template data to database.");
			sqle.printStackTrace(); 
		}
		
		return successfulSend;
	}
	
	
	// **** NOTES
	// the ratio from reading-to-standard value is almost functioning properly, except
	// when the standard is 0. 0/0 returns NaN and anything/0 is returning infinity.
	// Should we do the average of the standard row readings? Either way,
	// it will be convenient to have this data ready-to-use so all we have to do
	// is query it when looking for "hits."
	// TODO: We need to have an algorithm that will pull template data if the
	// UID is already in the database. This will be used for displaying on the 
	// console window.
	// There is also a bug right now if the template exists in the database. 
	// Its a syntax error somewhere. Check it out.
	// Lastly: Make sure that EVERY plate is 6x6 in both ratios and readings.
	// If it is, the algorithm is fine. Otherwise, we're screwed. EXTRA: an algorithm may need
	// to be written to handle the reverse input of the data.
	// ONE MORE NOTE: Right now, if an exception is thrown while adding template data,
	// there exists a possibility that some data may be entered while the rest won't.
	// I.E a plate_data record is made but not its corresponding elements.
	// Find a way to preserve the integrity of the database if that happens.
	
	public boolean sendResultsData(int uid, String color, Vector<Double> readings){
		boolean successfulSend = false;
		Double leftAverage = getLeftAverage(readings);
		Double rightAverage = getRightAverage(readings);
		
		System.out.println("Average of left column: " + leftAverage);
		System.out.println("Average of right column: " + rightAverage);
		try{
		String statement = "INSERT INTO result VALUES (?,?,?,?,?,?)";
		PreparedStatement ps = connection.prepareStatement(statement);
		for(int i = 0; i < readings.size(); i++){
			ps.setInt(1, uid);
			ps.setString(2, color);
			ps.setInt(3, i / 6); // row,column index, cell # 5 would be  (0,5)
			 // cell # 6 would be (1,0)
			ps.setInt(4, i % 6); // cell # 0 would be (0,0)
			 // cell # 35 would be (5,5)
			ps.setDouble(5, readings.get(i));
			ps.setDouble(6, (readings.get(i)/leftAverage) * 100); // reading over left average
			ps.setDouble(7, (readings.get(i)/rightAverage)  * 100); // reading over right average
			System.out.println((readings.get(i)/readings.get((i/6) * 6 )) * 100);
				// get the leftmost reading of the row (the standard) and calculate "hit" %
				// integer division will make it so that (i/6) * 6 is always the leftmost cell in the row,
				// i.e.: cell # 25 -> 25/6 = 4 * 6 = cell 24 is the standard
			ps.execute();
		}
		successfulSend = true;
		}
		catch(SQLException sqle){
			System.out.println("Error sending data readings to database.");
			sqle.printStackTrace(); 
		}
		return successfulSend;
	}
	
	private Double getLeftAverage(Vector<Double> readings){
		Double result = 0.0;
		
		for(int i = 0; i < 36; i += 6) // get the leftmost values for all 6 rows
			result += readings.get(i);

		result /= 6;
		
		return result;
			
	}
	private Double getRightAverage(Vector<Double> readings){
		Double result = 0.0;
		
		for(int i = 5; i < 36; i += 6) // get the rightmost values for all 6 rows
			result += readings.get(i);

		result /= 6;
		
		return result;
	}
}