// This class will be used to execute some of the specific queries needed for the program

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Vector;

public class SolarArmyDBC extends DatabaseConnector{
	
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
			// ======= Formatting =======
			System.out.println( "Template file exists in database.");
			System.out.println("Data from template #" + uid + ":\n");			
			outputTemplateData(uid);
			System.out.println("Submitted by: " + resultSet.getString(2));
			// ======= Formatting =======
			
			resultSet.close();

			return true;
		 }
		}
		catch(SQLException e){
			return false;
		}
	}
	
	public void outputTemplateData(int uid){
		String[][] cells = new String[6][6]; // for holding the template data
		
		String queryString = "SELECT row_index, col_index, ratio FROM ratio_data WHERE plate_no = ?";
		try{
		PreparedStatement ps = connection.prepareStatement(queryString);
		ps.setInt(1, uid);
		ResultSet resultSet = ps.executeQuery();
		
		while(resultSet.next()){
			cells[resultSet.getInt(1)][resultSet.getInt(2)] = resultSet.getString(3); 
			// take the row and column and put the data in there
		}
		
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++)
				System.out.print(cells[i][j] + " ");
			System.out.println(); // formatting
		}
		resultSet.close();
		
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println("Exception occured");
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
		long dataID = System.nanoTime();
		
		System.out.println("Data ID: " + dataID);
		System.out.println("Average of left column: " + leftAverage);
		System.out.println("Average of right column: " + rightAverage);
		try{
		String statement = "INSERT INTO result VALUES (?,?,?,?,?,?,?,?)";
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
			ps.setLong(8, dataID); // unique identifier for this reading file
			
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