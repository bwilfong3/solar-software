// This class will be used to execute some of the specific queries needed for the program

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
}