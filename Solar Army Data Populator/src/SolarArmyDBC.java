// This class will be used to execute some of the specific queries needed for the program

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Queue;

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
	
	public boolean checkIfTemplateExists(String tFileName){
		try{
		String queryString = "SELECT * FROM template WHERE t_file_name = ?;";
		PreparedStatement ps = connection.prepareStatement(queryString);
		ps.setString(1,  tFileName);
		
		ResultSet resultSet = ps.executeQuery();// query database
		 if(!resultSet.next())
		 {
			 System.out.println( tFileName + " does not yet exist in the database.");
			 resultSet.close();
			 return false;
		 }
		 else
		 {
			System.out.printf( "Template file already exists in database.");
			resultSet.close();
			getDataFiles(tFileName);
			return true;
		 }
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	private void getDataFiles(String tFileName){
		try{
		String queryString = "SELECT d_file_name FROM data_file WHERE uid = " +
					         "(SELECT uid FROM associated_with_data WHERE t_file_name = ?);";
		PreparedStatement ps = connection.prepareStatement(queryString);
		ps.setString(1,  tFileName);
		
		ResultSet resultSet = ps.executeQuery();
		
		if(!resultSet.next())
			System.out.println("No data files yet associated with " + tFileName);
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
}