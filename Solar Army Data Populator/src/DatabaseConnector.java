import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

	static final String 	JDBC_DRIVER = "com.mysql.jdbc.Driver";
		// JDBC driver name and database URL - for MySQL
	Connection connection;
	boolean connectionSuccessful = false;
	boolean authenticated = false;
	String input, userName;
	char[] password;
	int attempts = 0;
	
	DatabaseConnector(){

		try{
			Class.forName( JDBC_DRIVER );// load database driver class
			connectionSuccessful = true;
		}
		catch(ClassNotFoundException cnfe){
			System.out.println("Error loading JDBC driver.");
			cnfe.printStackTrace();
			connectionSuccessful = false;
		}
		if(connectionSuccessful){
			
			do{
				try{
					if (attempts > 0)
						 System.out.println("Please re-enter your credentials");
					
					System.out.println("Please enter your username:");
					userName = System.console().readLine();
					System.out.println("Please enter your password:");
					password = System.console().readPassword();
					attempts++; // increment the amount of log in attempts tried.
					connection = DriverManager.getConnection
							("","");
					authenticated = true; // exception was NOT thrown
					connectionSuccessful = true;
				} 
	
				catch(SQLException sqle){
					System.out.println("Error authenticating with database.");
					authenticated = false;
				}
								
			}while(!authenticated && attempts < 3);
			
			connectionSuccessful = authenticated; // Did you finally authenticate?
		}
			
		if(!connectionSuccessful)
			System.out.println("Unable to connect to database. Please contact Ben if you are having issues.");
	}
	
	boolean isConnection(){
		return connectionSuccessful;
	}
}
