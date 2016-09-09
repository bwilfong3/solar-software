import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

	static final String 	JDBC_DRIVER = "com.mysql.jdbc.Driver";
		// JDBC driver name and database URL - for MySQL
	Connection connection;
	boolean connectionSuccessful;
	String input, userName;
	char[] password;
	
	DatabaseConnector(){
		try{
			Class.forName( JDBC_DRIVER ); // load database driver class
			System.out.println("Please enter your username:");
			userName = System.console().readLine();
			System.out.println("Please enter your password:");
			password = System.console().readPassword();
			connection = DriverManager.getConnection
					( "jdbc:mysql://localhost/solararmy?autoReconnect=true&useSSL=false", 
							userName, password.toString());
			} 
	
		catch(SQLException sqle){
			sqle.printStackTrace();
			System.out.println("SQL Exception occurred. Connection Unsuccessful");
			connectionSuccessful = false;
		}
		catch(ClassNotFoundException cnfe){
			System.out.println("Error loading JDBC driver.");
			
			cnfe.printStackTrace();
			
			connectionSuccessful = false;
		}
		
		connectionSuccessful = true;
		
		System.out.println("Connection to database was successful.");
	}
	
	boolean isConnection(){
		return connectionSuccessful;
	}
}
