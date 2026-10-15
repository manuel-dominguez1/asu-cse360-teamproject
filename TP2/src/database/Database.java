package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import entityClasses.User;
import entityClasses.Lesson;
import guiTools.EmailAddressRecognizer;
import guiTools.passwordRecognizer;
import guiTools.TitleRecognizer;
import guiTools.DescriptionRecognizer;

/*******
 * <p> Title: Database Class. </p>
 * 
 * <p> Description: This is an in-memory database built on H2.  Detailed documentation of H2 can
 * be found at https://www.h2database.com/html/main.html (Click on "PDF (2MB)" on the l3ft side
 * of the page under the heading "Reference" for a PDF of 438 pages.)  This class leverages H2
 * and provides numerous special supporting methods.
 * </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 2.00		2025-04-29 Updated and expanded from the version produce by Pravalika 
 * 							Mukkiri and Ishwarya Hidkimath Basavaraj
 * @version 2.01		2025-12-17 Minor updates for Spring 2026
 */

/*
 * The Database class is responsible for establishing and managing the connection to the database,
 * and performing operations such as user registration, login validation, handling invitation 
 * codes, and numerous other database related functions.
 */
public class Database {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	//  Shared variables used within this class
	private Connection connection = null;		// Singleton to access the database 
	private Statement statement = null;			// The H2 Statement is used to construct queries
	
	// These are the easily accessible attributes of the currently logged-in user
	// This is only useful for single user applications
	private String currentUsername;
	private String currentPassword;
	private String oneTimePassword;
	private String currentFirstName;
	private String currentMiddleName;
	private String currentLastName;
	private String currentPreferredFirstName;
	private String currentEmailAddress;
	private boolean currentAdminRole;
	private boolean currentNewRole1;
	private boolean currentNewRole2;
	private int currentId;
	private String currentTitle;
	private String currentDescription;
	private String currentAuthor;
	

	/*******
	 * <p> Method: Database </p>
	 * 
	 * <p> Description: The default constructor used to establish this singleton object.</p>
	 * 
	 */
	
	public Database () {
		
	}
	
	
/*******
 * <p> Method: connectToDatabase </p>
 * 
 * <p> Description: Used to establish the in-memory instance of the H2 database from secondary
 *		storage.</p>
 *
 * @throws SQLException when the DriverManager is unable to establish a connection
 * 
 */
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	
/*******
 * <p> Method: createTables </p>
 * 
 * <p> Description: Used to create new instances of the two database tables used by this class.</p>
 * 
 */
	private void createTables() throws SQLException {
		// Create the user database
		String userTable = "CREATE TABLE IF NOT EXISTS userDB ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "oneTimePassword VARCHAR(255),"
				+ "firstName VARCHAR(255), "
				+ "middleName VARCHAR(255), "
				+ "lastName VARCHAR (255), "
				+ "preferredFirstName VARCHAR(255), "
				+ "emailAddress VARCHAR(255), "
				+ "adminRole BOOL DEFAULT FALSE, "
				+ "newRole1 BOOL DEFAULT FALSE, "
				+ "newRole2 BOOL DEFAULT FALSE)";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	    		+ "emailAddress VARCHAR(255), "
	            + "role VARCHAR(10))";
	    statement.execute(invitationCodesTable);
	    
	    // Create the lessons database
	    String lessonTable = "CREATE TABLE IF NOT EXISTS lessonsLearned ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "title VARCHAR(255) UNIQUE, "
				+ "description VARCHAR(255), "
				+ "author VARCHAR(500))";
		statement.execute(lessonTable);
	}


/*******
 * <p> Method: isDatabaseEmpty </p>
 * 
 * <p> Description: If the user database has no rows, true is returned, else false.</p>
 * 
 * @return true if the database is empty, else it returns false
 * 
 */
	public boolean isDatabaseEmpty() {
		String query = "SELECT COUNT(*) AS count FROM userDB";
		try {
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count") == 0;
			}
		}  catch (SQLException e) {
	        return false;
	    }
		return true;
	}
	
	
/*******
 * <p> Method: getNumberOfUsers </p>
 * 
 * <p> Description: Returns an integer .of the number of users currently in the user database. </p>
 * 
 * @return the number of user records in the database.
 * 
 */
	public int getNumberOfUsers() {
		String query = "SELECT COUNT(*) AS count FROM userDB";
		try {
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count");
			}
		} catch (SQLException e) {
	        return 0;
	    }
		return 0;
	}

/*******
 * <p> Method: register(User user) </p>
 * 
 * <p> Description: Creates a new row in the database using the user parameter. </p>
 * 
 * @throws SQLException when there is an issue creating the SQL command or executing it.
 * 
 * @param user specifies a user object to be added to the database.
 * 
 */
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO userDB (userName, password, oneTimePassword, firstName, middleName, "
				+ "lastName, preferredFirstName, emailAddress, adminRole, newRole1, newRole2) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			currentUsername = user.getUserName();
			pstmt.setString(1, currentUsername);
			
			currentPassword = user.getPassword();
			pstmt.setString(2, currentPassword);

			pstmt.setString(3, "");
			
			currentFirstName = user.getFirstName();
			pstmt.setString(4, currentFirstName);
			
			currentMiddleName = user.getMiddleName();			
			pstmt.setString(5, currentMiddleName);
			
			currentLastName = user.getLastName();
			pstmt.setString(6, currentLastName);
			
			currentPreferredFirstName = user.getPreferredFirstName();
			pstmt.setString(7, currentPreferredFirstName);
			
			currentEmailAddress = user.getEmailAddress();
			pstmt.setString(8, currentEmailAddress);
			
			currentAdminRole = user.getAdminRole();
			pstmt.setBoolean(9, currentAdminRole);
			
			currentNewRole1 = user.getNewRole1();
			pstmt.setBoolean(10, currentNewRole1);
			
			currentNewRole2 = user.getNewRole2();
			pstmt.setBoolean(11, currentNewRole2);
			
			pstmt.executeUpdate();
		}
		
	}
	
	/*******
	 * <p> Method: register(Lesson lesson) </p>
	 * 
	 * <p> Description: Creates a new row in the database using the lesson parameter.
	 * The database automatically generates the lesson ID.</p>
	 * 
	 * @param lesson specifies a lesson object to be added to the database.
	 * 
	 * @return true if the lesson was successfully added to the database, otherwise false
	 * 
	 */
		public boolean register(Lesson lesson) {
			String insertLesson = "INSERT INTO lessonsLearned (title, description, author) "
					+ "VALUES (?, ?, ?)";
			try (PreparedStatement pstmt = connection.prepareStatement(insertLesson)) {
					
				currentTitle = lesson.getTitle();
				pstmt.setString(1, currentTitle);
				
				currentDescription = lesson.getDescription();
				pstmt.setString(2, currentDescription);
				
				currentAuthor = lesson.getAuthor();			
				pstmt.setString(3, currentAuthor);
				
				int rowsAdded = pstmt.executeUpdate();
				return rowsAdded == 1;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			
			
		}
	
/*******
 *  <p> Method: List getUserList() </p>
 *  
 *  <P> Description: Generate an List of Strings, one for each user in the database,
 *  starting with {@literal <Select User>} at the start of the list. </p>
 *  
 *  @return a list of userNames found in the database.
 */
	public List<String> getUserList () {
		List<String> userList = new ArrayList<String>();
		userList.add("<Select a User>");
		String query = "SELECT userName FROM userDB";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				userList.add(rs.getString("userName"));
			}
		} catch (SQLException e) {
	        return null;
	    }
//		System.out.println(userList);
		return userList;
	}
	
/*******
 *  <p> Method: List getFullUserList() </p>
 *   
 *  <P> Description: Generate a list of users, one for each user in the database.</p>
 *  
 *  @return a list of Users found in the database.
 *  
 */
	public List<User> getFullUserList () {
		List<User> userList = new ArrayList<User>();
		String query = "SELECT * FROM userDB";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				userList.add(new User(rs.getString("userName"),"" ,rs.getString("firstName"), rs.getString("middleName"),
						rs.getString("lastName"), rs.getString("preferredFirstName"),rs.getString("emailAddress"),
						rs.getBoolean("adminRole"),rs.getBoolean("newRole1"),rs.getBoolean("newRole2")));
			}
		} catch (SQLException e) {
		       return userList;
		}
		return userList;
	}
	
	/*******
	 *  <p> Method: List getLessonList() </p>
	 *  
	 *  <P> Description: Generate an List of Strings, one for each lesson in the database,
	 *  starting with {@literal <Select Lesson>} at the start of the list. </p>
	 *  
	 *  @return a list of lesson titles found in the database.
	 */
		public List<String> getLessonList () {
			List<String> lessonList = new ArrayList<String>();
			lessonList.add("<Select a Lesson>");
			String query = "SELECT title FROM lessonsLearned";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					lessonList.add(rs.getString("title"));
				}
			} catch (SQLException e) {
		        return null;
		    }
			//System.out.println(lessonList);
			return lessonList;
		}
		
	/*******
	 *  <p> Method: List getFullLessonList() </p>
	 *   
	 *  <P> Description: Generate a list of lessons, one for each lesson in the database.</p>
	 *  
	 *  @return a list of Lessons found in the database.
	 *  
	 */
		public List<Lesson> getFullLessonList () {
			List<Lesson> lessonList = new ArrayList<Lesson>();
			String query = "SELECT * FROM lessonsLearned";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					lessonList.add(new Lesson(rs.getInt("id"),rs.getString("title"),
							rs.getString("description"), rs.getString("author")));
				}
			} catch (SQLException e) {
			       return lessonList;
			}
			return lessonList;
		}
	
/*******
 * <p> Method: boolean loginAdmin(User user) </p>
 * 
 * <p> Description: Check to see that a user with the specified username, password, and role
 * 		is the same as a row in the table for the username, password, and role. </p>
 * 
 * @param user specifies the specific user that should be logged in playing the Admin role.
 * 
 * @return true if the specified user has been logged in as an Admin else false.
 * 
 */
	public boolean loginAdmin(User user){
		// Validates an admin user's login credentials so the user can login in as an Admin.
		String query = "SELECT * FROM userDB WHERE userName = ? AND password = ? AND "
				+ "adminRole = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			return rs.next();	// If a row is returned, rs.next() will return true		
		} catch  (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	
/*******
 * <p> Method: boolean loginRole1(User user) </p>
 * 
 * <p> Description: Check to see that a user with the specified username, password, and role
 * 		is the same as a row in the table for the username, password, and role. </p>
 * 
 * @param user specifies the specific user that should be logged in playing the Student role.
 * 
 * @return true if the specified user has been logged in as an Student else false.
 * 
 */
	public boolean loginRole1(User user) {
		// Validates a student user's login credentials.
		String query = "SELECT * FROM userDB WHERE userName = ? AND password = ? AND "
				+ "newRole1 = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch  (SQLException e) {
		       e.printStackTrace();
		}
		return false;
	}

	/*******
	 * <p> Method: boolean loginRole2(User user) </p>
	 * 
	 * <p> Description: Check to see that a user with the specified username, password, and role
	 * 		is the same as a row in the table for the username, password, and role. </p>
	 * 
	 * @param user specifies the specific user that should be logged in playing the Reviewer role.
	 * 
	 * @return true if the specified user has been logged in as an Student else false.
	 * 
	 */
	// Validates a reviewer user's login credentials.
	public boolean loginRole2(User user) {
		String query = "SELECT * FROM userDB WHERE userName = ? AND password = ? AND "
				+ "newRole2 = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch  (SQLException e) {
		       e.printStackTrace();
		}
		return false;
	}
	
	
	/*******
	 * <p> Method: boolean doesUserExist(User user) </p>
	 * 
	 * <p> Description: Check to see that a user with the specified username is  in the table. </p>
	 * 
	 * @param userName specifies the specific user that we want to determine if it is in the table.
	 * 
	 * @return true if the specified user is in the table else false.
	 * 
	 */
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM userDB WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	/*******
	 * <p> Method: boolean doesLessonExist(String title) </p>
	 * 
	 * <p> Description: Check to see that a lesson with the specified title is in the table. </p>
	 * 
	 * @param title specifies the specific lesson that we want to determine if it is in the table.
	 * 
	 * @return true if the specified lesson is in the table else false.
	 * 
	 */
	// Checks if a lesson already exists in the database based on its title.
	public boolean doesLessonExist(String title) {
	    String query = "SELECT COUNT(*) FROM lessonsLearned WHERE title = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, title);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the lesson exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume lesson doesn't exist
	}
	
	/*******
	 * <p> Method: boolean doesLessonExist(int id) </p>
	 * 
	 * <p> Description: Check to see that a lesson with the specified id is in the table. </p>
	 * 
	 * @param id specifies the specific lesson that we want to determine if it is in the table.
	 * 
	 * @return true if the specified lesson is in the table else false.
	 * 
	 */
	// Checks if a lesson already exists in the database based on its id.
	public boolean doesLessonExist(int id) {
	    String query = "SELECT COUNT(*) FROM lessonsLearned WHERE id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setInt(1, id);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the lesson exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume lesson doesn't exist
	}


	
	/*******
	 * <p> Method: int getNumberOfRoles(User user) </p>
	 * 
	 * <p> Description: Determine the number of roles a specified user plays. </p>
	 * 
	 * @param user specifies the specific user that we want to determine if it is in the table.
	 * 
	 * @return the number of roles this user plays (0 - 5).
	 * 
	 */	
	// Get the number of roles that this user plays
	public int getNumberOfRoles (User user) {
		int numberOfRoles = 0;
		if (user.getAdminRole()) numberOfRoles++;
		if (user.getNewRole1()) numberOfRoles++;
		if (user.getNewRole2()) numberOfRoles++;
		return numberOfRoles;
	}	

	
	/*******
	 * <p> Method: String generateInvitationCode(String emailAddress, String role) </p>
	 * 
	 * <p> Description: Given an email address and a roles, this method establishes and invitation
	 * code and adds a record to the InvitationCodes table.  When the invitation code is used, the
	 * stored email address is used to establish the new user and the record is removed from the
	 * table.</p>
	 * 
	 * @param emailAddress specifies the email address for this new user.
	 * 
	 * @param role specified the role that this new user will play.
	 * 
	 * @return the code of six characters so the new user can use it to securely setup an account.
	 * 
	 */
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode(String emailAddress, String role) {
	    String code = UUID.randomUUID().toString().substring(0, 6); // Generate a random 6-character code
	    String query = "INSERT INTO InvitationCodes (code, emailaddress, role) VALUES (?, ?, ?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.setString(2, emailAddress);
	        pstmt.setString(3, role);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return code;
	}

	
	/*******
	 * <p> Method: int getNumberOfInvitations() </p>
	 * 
	 * <p> Description: Determine the number of outstanding invitations in the table.</p>
	 *  
	 * @return the number of invitations in the table.
	 * 
	 */
	// Number of invitations in the database
	public int getNumberOfInvitations() {
		String query = "SELECT COUNT(*) AS count FROM InvitationCodes";
		try {
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				return resultSet.getInt("count");
			}
		} catch  (SQLException e) {
	        e.printStackTrace();
	    }
		return 0;
	}

	/*******
	 * <p> Method: String setOneTimePassword(String username) </p>
	 * 
	 * <p> Description: Generates and sets a randomly generated one-time password for the specified user.</p>
	 * 
	 * @param username identifies the user whose one-time password should be set.
	 * 
	 * @return the generated one-time password, or an empty string if the user does not exist.
	 * 
	 */
	// Sets a random one-time password for a user.
	public String setOneTimePassword(String username) {
		if (username == null || username.isEmpty() || username.equals("<Select a User>")) {
			return "";
		}
		
	    String oneTimePassword = UUID.randomUUID().toString().replace("-", "").substring(0, 8); // Generate a random 8-character password
	    String query = "UPDATE userDB SET oneTimePassword = ? WHERE username = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, oneTimePassword);
	        pstmt.setString(2, username);
	       
	        int rowsUpdated = pstmt.executeUpdate();
	        
	        if (rowsUpdated == 1) {
	        	return oneTimePassword;
	        }
	    
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	/*******
	 * <p> Method: boolean isOneTimePassword(String username, String password) </p>
	 * 
	 * <p> Description: Checks if the entered password matches the one-time password
	 * stored for the specified user.</p>
	 * 
	 * @param username identifies the user whose password is being authenticated.
	 * 
	 * @param password is the password supplied by the user.
	 * 
	 * @return true if the password is the user's valid one-time password else false.
	 * 
	 */
	// Checks if the entered password matches a user's stored one-time password.
	public boolean isOneTimePassword(String username, String password) {
		if (username == null || password == null) {
			return false;
		}
		
	    String query = "SELECT oneTimePassword FROM userDB WHERE username = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, username);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	        	if (rs.next()) {
	        		String storedOneTimePassword = rs.getString("oneTimePassword");
	        		
	        		if (storedOneTimePassword != null
	        			&& !storedOneTimePassword.isEmpty()
	        			&& storedOneTimePassword.equals(password)) {
	        			return true;
	        		}
	        	}
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	/*******
	 * <p> Method: boolean clearOneTimePassword(String username) </p>
	 * 
	 * <p> Description: Clears the one-time password stored for the specified user
	 * so that it cannot be used to log in again.</p>
	 * 
	 * @param username identifies the user whose one-time password is being cleared.
	 * 
	 * @return true if the one-time password was cleared successfully.
	 * 
	 */
	// Clear stored one-time password after first use.
	public boolean clearOneTimePassword(String username) {
		if (username == null || username.isEmpty()) {
			return false;
		}
		
	    String query = "UPDATE userDB SET oneTimePassword = NULL WHERE username = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, username);
	        int rowsUpdated = pstmt.executeUpdate();
	        
	        return rowsUpdated == 1;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	/*******
	 * <p> Method: boolean emailaddressHasBeenUsed(String emailAddress) </p>
	 * 
	 * <p> Description: Determine if an email address has been user to establish a user.</p>
	 * 
	 * @param emailAddress is a string that identifies a user in the table
	 *  
	 * @return true if the email address is in the table, else return false.
	 * 
	 */
	// Check to see if an email address is already in the database
	public boolean emailaddressHasBeenUsed(String emailAddress) {
	    String query = "SELECT COUNT(*) AS count FROM InvitationCodes WHERE emailAddress = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, emailAddress);
	        ResultSet rs = pstmt.executeQuery();
	 //     System.out.println(rs);
	        if (rs.next()) {
	            // Mark the code as used
	        	return rs.getInt("count")>0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	
	/*******
	 * <p> Method: String getRoleGivenAnInvitationCode(String code) </p>
	 * 
	 * <p> Description: Get the role associated with an invitation code.</p>
	 * 
	 * @param code is the 6 character String invitation code
	 *  
	 * @return the role for the code or an empty string.
	 * 
	 */
	// Obtain the roles associated with an invitation code.
	public String getRoleGivenAnInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("role");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return "";
	}

	
	/*******
	 * <p> Method: String getEmailAddressUsingCode (String code ) </p>
	 * 
	 * <p> Description: Get the email addressed associated with an invitation code.</p>
	 * 
	 * @param code is the 6 character String invitation code
	 *  
	 * @return the email address for the code or an empty string.
	 * 
	 */
	// For a given invitation code, return the associated email address of an empty string
	public String getEmailAddressUsingCode (String code ) {
	    String query = "SELECT emailAddress FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("emailAddress");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return "";
	}
	
	
	/*******
	 * <p> Method: void removeInvitationAfterUse(String code) </p>
	 * 
	 * <p> Description: Remove an invitation record once it is used.</p>
	 * 
	 * @param code is the 6 character String invitation code
	 *  
	 */
	// Remove an invitation using an email address once the user account has been setup
	public void removeInvitationAfterUse(String code) {
	    String query = "SELECT COUNT(*) AS count FROM InvitationCodes WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	        	int counter = rs.getInt(1);
	            // Only do the remove if the code is still in the invitation table
	        	if (counter > 0) {
        			query = "DELETE FROM InvitationCodes WHERE code = ?";
	        		try (PreparedStatement pstmt2 = connection.prepareStatement(query)) {
	        			pstmt2.setString(1, code);
	        			pstmt2.executeUpdate();
	        		}catch (SQLException e) {
	        	        e.printStackTrace();
	        	    }
	        	}
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return;
	}
	
/*******
	 * <p> Method: String getPassword(String username) </p>
	 * 
	 * <p> Description: Get the password of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the password of a user given that user's username 
	 *  
	 */
	
	// Get the password
		public String getPassword(String username) {
			String query = "SELECT password FROM userDB WHERE userName = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, username);
		        ResultSet rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		            return rs.getString("password"); // Return the password if user exists
		        }
				
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
			return null;
		}
		

	/*******
	 * <p> Method: void updatePassword(String username, String password) </p>		 * 
	 * <p> Description: Update the password of a user given that user's username and the new
	 *		password.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @param password is the new password for the user
	 *  
	 */
	// update the password
	public void updatePassword(String username, String password) {
	    // Check the Password with passwordNameRecognizer
		String passwordError = passwordRecognizer.checkForValidPassword(password);
		
		if (passwordError.isEmpty()) {
			String query = "UPDATE userDB SET password = ? WHERE username = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, password);
				pstmt.setString(2, username);
				pstmt.executeUpdate();
				currentPassword = password;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*******
	 * <p> Method: String getFirstName(String username) </p>
	 * 
	 * <p> Description: Get the first name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the first name of a user given that user's username 
	 *  
	 */
	// Get the First Name
	public String getFirstName(String username) {
		String query = "SELECT firstName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("firstName"); // Return the first name if user exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	

	/*******
	 * <p> Method: void updateFirstName(String username, String firstName) </p>
	 * 
	 * <p> Description: Update the first name of a user given that user's username and the new
	 *		first name.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @param firstName is the new first name for the user
	 *  
	 */
	// update the first name
	public void updateFirstName(String username, String firstName) {
	    String query = "UPDATE userDB SET firstName = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, firstName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentFirstName = firstName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	/*******
	 * <p> Method: String getMiddleName(String username) </p>
	 * 
	 * <p> Description: Get the middle name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the middle name of a user given that user's username 
	 *  
	 */
	// get the middle name
	public String getMiddleName(String username) {
		String query = "SELECT MiddleName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("middleName"); // Return the middle name if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}

	
	/*******
	 * <p> Method: void updateMiddleName(String username, String middleName) </p>
	 * 
	 * <p> Description: Update the middle name of a user given that user's username and the new
	 * 		middle name.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param middleName is the new middle name for the user
	 *  
	 */
	// update the middle name
	public void updateMiddleName(String username, String middleName) {
	    String query = "UPDATE userDB SET middleName = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, middleName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentMiddleName = middleName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: String getLastName(String username) </p>
	 * 
	 * <p> Description: Get the last name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the last name of a user given that user's username 
	 *  
	 */
	// get he last name
	public String getLastName(String username) {
		String query = "SELECT LastName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("lastName"); // Return last name role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	/*******
	 * <p> Method: void updateLastName(String username, String lastName) </p>
	 * 
	 * <p> Description: Update the middle name of a user given that user's username and the new
	 * 		middle name.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param lastName is the new last name for the user
	 *  
	 */
	// update the last name
	public void updateLastName(String username, String lastName) {
	    String query = "UPDATE userDB SET lastName = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, lastName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentLastName = lastName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: String getPreferredFirstName(String username) </p>
	 * 
	 * <p> Description: Get the preferred first name of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the preferred first name of a user given that user's username 
	 *  
	 */
	// get the preferred first name
	public String getPreferredFirstName(String username) {
		String query = "SELECT preferredFirstName FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("firstName"); // Return the preferred first name if user exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	/*******
	 * <p> Method: void updatePreferredFirstName(String username, String preferredFirstName) </p>
	 * 
	 * <p> Description: Update the preferred first name of a user given that user's username and
	 * 		the new preferred first name.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param preferredFirstName is the new preferred first name for the user
	 *  
	 */
	// update the preferred first name of the user
	public void updatePreferredFirstName(String username, String preferredFirstName) {
	    String query = "UPDATE userDB SET preferredFirstName = ? WHERE username = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, preferredFirstName);
	        pstmt.setString(2, username);
	        pstmt.executeUpdate();
	        currentPreferredFirstName = preferredFirstName;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/*******
	 * <p> Method: String getEmailAddress(String username) </p>
	 * 
	 * <p> Description: Get the email address of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return the email address of a user given that user's username 
	 *  
	 */
	// get the email address
	public String getEmailAddress(String username) {
		String query = "SELECT emailAddress FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("emailAddress"); // Return the email address if user exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	/*******
	 * <p> Method: void updateEmailAddress(String username, String emailAddress) </p>
	 * 
	 * <p> Description: Update the email address name of a user given that user's username and
	 * 		the new email address.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param emailAddress is the new preferred first name for the user
	 *  
	 */
	// update the email address
	public void updateEmailAddress(String username, String emailAddress) {
		//Check the email validity against the EmailAddressRecognizer FSM
		String emailError = EmailAddressRecognizer.checkEmailAddress(emailAddress);

		if (emailError.isEmpty()) {
	    	String query = "UPDATE userDB SET emailAddress = ? WHERE username = ?";
	    	try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        	pstmt.setString(1, emailAddress);
	       	 	pstmt.setString(2, username);
	       	 	pstmt.executeUpdate();
	        	currentEmailAddress = emailAddress;
	    	} catch (SQLException e) {
	        	e.printStackTrace();
	    	}
		}
	}
	
	/*******
	 * <p> Method: int getId(String title) </p>
	 * 
	 * <p> Description: Get the id of a lesson given that lesson's title.</p>
	 * 
	 * @param title is the title of the lesson
	 * 
	 * @return the id value of a lesson given that lesson's title 
	 *  
	 */
	// get the id
	public int getId(String title) {
		String query = "SELECT id FROM lessonsLearned WHERE title = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(2, title);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getInt("id"); // Return the id if lesson exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return -1;
	}
	
	/*******
	 * <p> Method: String getTitle(int id) </p>
	 * 
	 * <p> Description: Get the title of a lesson given that lesson's id.</p>
	 * 
	 * @param id is the id of the lesson
	 * 
	 * @return a String title of a lesson given that lesson's id 
	 *  
	 */
	// get the title
	public String getTitle(int id) {
		String query = "SELECT title FROM lessonsLearned WHERE id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, id);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("title"); // Return the title if lesson exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	
	/*******
	 * <p> Method: boolean updateTitle(int id, String title) </p>
	 * 
	 * <p> Description: Update the title of a lesson given the id and new title.</p>
	 * 
	 * @param id is the lesson's id
	 *  
	 * @param title is the new preferred title for the lesson
	 * 
	 * @return true if the update was successful, otherwise false
	 *  
	 */
	// update the title
	public boolean updateTitle(int id, String title) {
		//Check the title validity against the TitleRecognizer FSM
		String titleError = TitleRecognizer.checkForValidTitle(title);

		if (titleError == null || titleError.isEmpty()) {
	    	String query = "UPDATE lessonsLearned SET title = ? WHERE id = ?";
	    	try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	       	 	pstmt.setString(1, title);
	       	 	pstmt.setInt(2, id);
	       	 	int rowsAffected = pstmt.executeUpdate();
	       	 	
	       	 	return rowsAffected > 0;
	    	} catch (SQLException e) {
	        	e.printStackTrace();
	    	}
		}
		return false;
	}
	
	/*******
	 * <p> Method: String getDescription(int id) </p>
	 * 
	 * <p> Description: Get the description of a lesson given that lesson's id.</p>
	 * 
	 * @param id is the id of the lesson
	 * 
	 * @return a String description of a lesson given that lesson's id 
	 *  
	 */
	// get the description
	public String getDescription(int id) {
		String query = "SELECT description FROM lessonsLearned WHERE id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, id);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("description"); // Return the description if lesson exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	/*******
	 * <p> Method: String getDescription(String title) </p>
	 * 
	 * <p> Description: Get the description of a lesson given that lesson's title.</p>
	 * 
	 * @param title is the title of the lesson
	 * 
	 * @return a String description of a lesson given that lesson's title 
	 *  
	 */
	// get the description
	public String getDescription(String title) {
		String query = "SELECT description FROM lessonsLearned WHERE title = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, title);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("description"); // Return the description if lesson exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	/*******
	 * <p> Method: boolean updateDescription(int id, String d) </p>
	 * 
	 * <p> Description: Update the description of a lesson given that lesson's 
	 * 		 id and new description.</p>
	 * 
	 * @param id is the lesson's id
	 * 
	 * @param d is the new preferred description for the lesson
	 *  
	 *  @return true is the description is successfully updated, otherwise false
	 */
	// update the description
	public boolean updateDescription(int id, String d) {
		//Check the description validity against the DescriptionRecognizer FSM
		String descError = DescriptionRecognizer.checkForValidDescription(d);

		if (descError == null || descError.isEmpty()) {
	    	String query = "UPDATE lessonsLearned SET description = ? WHERE id = ?";
	    	try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        	pstmt.setString(1, d);
	        	pstmt.setInt(2, id);
	       	 	int rowsAffected = pstmt.executeUpdate();
	       	 	
	       	 	if (rowsAffected > 0) {
		        	currentDescription = d;
		        	return true;
	       	 	}
	    	} catch (SQLException e) {
	        	e.printStackTrace();
	    	}
		}
		return false;
	}
	
	
	/*******
	 * <p> Method: String getAuthor(int id) </p>
	 * 
	 * <p> Description: Get the author of a lesson given that lesson's id.</p>
	 * 
	 * @param id is the id of the lesson
	 * 
	 * @return a String author of a lesson given that lesson's id, equivalent to a user's username 
	 *  
	 */
	// get the author
	public String getAuthor(int id) {
		String query = "SELECT author FROM lessonsLearned WHERE id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, id);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("author"); // Return the author if lesson exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	/*******
	 * <p> Method: String getAuthor(String title) </p>
	 * 
	 * <p> Description: Get the author of a lesson given that lesson's title.</p>
	 * 
	 * @param title is the title of the lesson
	 * 
	 * @return a String author of a lesson given that lesson's id, equivalent to a user's username 
	 *  
	 */
	// get the author
	public String getAuthor(String title) {
		String query = "SELECT author FROM lessonsLearned WHERE title = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, title);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("author"); // Return the author if lesson exists
	        }
			
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	/*******
	 * <p> Method: boolean getUserAccountDetails(String username) </p>
	 * 
	 * <p> Description: Get all the attributes of a user given that user's username.</p>
	 * 
	 * @param username is the username of the user
	 * 
	 * @return true of the get is successful, else false
	 *  
	 */
	// get the attributes for a specified user
	public boolean getUserAccountDetails(String username) {
		String query = "SELECT * FROM userDB WHERE username = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();			
			rs.next();
	    	currentUsername = rs.getString(2);
	    	currentPassword = rs.getString(3);
			oneTimePassword = rs.getString(4);
	    	currentFirstName = rs.getString(5);
	    	currentMiddleName = rs.getString(6);
	    	currentLastName = rs.getString(7);
	    	currentPreferredFirstName = rs.getString(8);
	    	currentEmailAddress = rs.getString(9);
	    	currentAdminRole = rs.getBoolean(10);
	    	currentNewRole1 = rs.getBoolean(11);
	    	currentNewRole2 = rs.getBoolean(12);
			return true;
	    } catch (SQLException e) {
			return false;
	    }
	}
	
	/*******
	 * <p> Method: Lesson getLessonDetails(String title) </p>
	 * 
	 * <p> Description: Get all the attributes of a lesson given that lesson's title.</p>
	 * 
	 * @param title is the title of the lesson
	 * 
	 * @return true if the get is successful, else false
	 *  
	 */
	// get the attributes for a specified lesson
	public Lesson getLessonDetails(String title) {
		String query = "SELECT * FROM lessonsLearned WHERE title = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, title);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
			    	currentId = rs.getInt(1);
			    	currentTitle = rs.getString(2);
			    	currentDescription = rs.getString(3);
			    	currentAuthor = rs.getString(4);
			    	return new Lesson(currentId, currentTitle,
	    	    			currentDescription, currentAuthor);
				}
			}
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
		
		return null;
	}
	
	/*******
	 * <p> Method: Lesson getLessonDetails(int id) </p>
	 * 
	 * <p> Description: Get all the attributes of a lesson given that lesson's id.</p>
	 * 
	 * @param id is the id of the lesson
	 * 
	 * @return the lesson object identifed by the id
	 *  
	 */
	// get the attributes for a specified lesson
	public Lesson getLessonDetails(int id) {
		String query = "SELECT id, title, description, author FROM lessonsLearned WHERE id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, id);
	        try (ResultSet rs = pstmt.executeQuery()) {
	        	if (rs.next()) {
	        		currentId = rs.getInt(1);
	    	    	currentTitle = rs.getString(2);
	    	    	currentDescription = rs.getString(3);
	    	    	currentAuthor = rs.getString(4);
	    	    	return new Lesson(currentId, currentTitle,
	    	    			currentDescription, currentAuthor);
	        	}
	        }		
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
		
		return null;
	}
	
	
	/*******
	 * <p> Method: boolean updateUserRole(String username, String role, String value) </p>
	 * 
	 * <p> Description: Update a specified role for a specified user's and set and update all the
	 * 		current user attributes.</p>
	 * 
	 * @param username is the username of the user
	 *  
	 * @param role is a string that specifies the role to update
	 * 
	 * @param value is the string that specifies TRUE or FALSE for the role
	 * 
	 * @return true if the update was successful, else false
	 *  
	 */
	// Update a user's role
	public boolean updateUserRole(String username, String role, String value) {
		boolean roleValue;
		if ("true".equalsIgnoreCase(value)) {
			roleValue = true;
		} else if ("false".equalsIgnoreCase(value)) {
			roleValue = false;
		} else {
			return false;
		}
		
		if (role.compareTo("Admin") == 0) {
			String query = "UPDATE userDB SET adminRole = ? WHERE username = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setBoolean(1, roleValue);
				pstmt.setString(2, username);
				pstmt.executeUpdate();
				if (value.compareTo("true") == 0)
					currentAdminRole = true;
				else
					currentAdminRole = false;
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		if (role.compareTo("Role1") == 0) {
			String query = "UPDATE userDB SET newRole1 = ? WHERE username = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setBoolean(1, roleValue);
				pstmt.setString(2, username);
				pstmt.executeUpdate();
				if (value.compareTo("true") == 0)
					currentNewRole1 = true;
				else
					currentNewRole1 = false;
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		if (role.compareTo("Role2") == 0) {
			String query = "UPDATE userDB SET newRole2 = ? WHERE username = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setBoolean(1, roleValue);
				pstmt.setString(2, username);
				pstmt.executeUpdate();
				if (value.compareTo("true") == 0)
					currentNewRole2 = true;
				else
					currentNewRole2 = false;
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;
	}
	
	
	// Attribute getters for the current user
	/*******
	 * <p> Method: String getCurrentUsername() </p>
	 * 
	 * <p> Description: Get the current user's username.</p>
	 * 
	 * @return the username value is returned
	 *  
	 */
	public String getCurrentUsername() { return currentUsername;};

	
	/*******
	 * <p> Method: String getCurrentPassword() </p>
	 * 
	 * <p> Description: Get the current user's password.</p>
	 * 
	 * @return the password value is returned
	 *  
	 */
	public String getCurrentPassword() { return currentPassword;};

	
	/*******
	 * <p> Method: String getCurrentFirstName() </p>
	 * 
	 * <p> Description: Get the current user's first name.</p>
	 * 
	 * @return the first name value is returned
	 *  
	 */
	public String getCurrentFirstName() { return currentFirstName;};

	
	/*******
	 * <p> Method: String getCurrentMiddleName() </p>
	 * 
	 * <p> Description: Get the current user's middle name.</p>
	 * 
	 * @return the middle name value is returned
	 *  
	 */
	public String getCurrentMiddleName() { return currentMiddleName;};

	
	/*******
	 * <p> Method: String getCurrentLastName() </p>
	 * 
	 * <p> Description: Get the current user's last name.</p>
	 * 
	 * @return the last name value is returned
	 *  
	 */
	public String getCurrentLastName() { return currentLastName;};

	
	/*******
	 * <p> Method: String getCurrentPreferredFirstName( </p>
	 * 
	 * <p> Description: Get the current user's preferred first name.</p>
	 * 
	 * @return the preferred first name value is returned
	 *  
	 */
	public String getCurrentPreferredFirstName() { return currentPreferredFirstName;};

	
	/*******
	 * <p> Method: String getCurrentEmailAddress() </p>
	 * 
	 * <p> Description: Get the current user's email address name.</p>
	 * 
	 * @return the email address value is returned
	 *  
	 */
	public String getCurrentEmailAddress() { return currentEmailAddress;};

	
	/*******
	 * <p> Method: boolean getCurrentAdminRole() </p>
	 * 
	 * <p> Description: Get the current user's Admin role attribute.</p>
	 * 
	 * @return true if this user plays an Admin role, else false
	 *  
	 */
	public boolean getCurrentAdminRole() { return currentAdminRole;};

	
	/*******
	 * <p> Method: boolean getCurrentNewRole1() </p>
	 * 
	 * <p> Description: Get the current user's Student role attribute.</p>
	 * 
	 * @return true if this user plays a Student role, else false
	 *  
	 */
	public boolean getCurrentNewRole1() { return currentNewRole1;};

	
	/*******
	 * <p> Method: boolean getCurrentNewRole2() </p>
	 * 
	 * <p> Description: Get the current user's Reviewer role attribute.</p>
	 * 
	 * @return true if this user plays a Reviewer role, else false
	 *  
	 */
	public boolean getCurrentNewRole2() { return currentNewRole2;};

	/*******
	 * <p> Method: int getCurrentLessonId() </p>
	 * 
	 * <p> Description: Get the current lesson's id attribute.</p>
	 * 
	 * @return the value in the id attribute
	 *  
	 */
	
	// Attribute getters for the current lesson
	public int getCurrentLessonId() { return currentId;};

	/*******
	 * <p> Method: String getCurrentLessonTitle() </p>
	 * 
	 * <p> Description: Get the current lesson's title attribute.</p>
	 * 
	 * @return the String in the title attribute
	 *  
	 */
	public String getCurrentLessonTitle() { return currentTitle;};

	/*******
	 * <p> Method: String getCurrentLessonDescription() </p>
	 * 
	 * <p> Description: Get the current lesson's description attribute.</p>
	 * 
	 * @return the String in the description attribute
	 *  
	 */
	public String getCurrentLessonDescription() { return currentDescription;};
	
	/*******
	 * <p> Method: String getCurrentLessonAuthor() </p>
	 * 
	 * <p> Description: Get the current lesson's author attribute.</p>
	 * 
	 * @return the String in the author attribute
	 *  
	 */
	public String getCurrentLessonAuthor() { return currentAuthor;};
	
	/*******
	 * <p> Method: int getNumberOfAdmins() </p>
	 * 
	 * <p> Description: Get the current number of users with the admin role.</p>
	 * 
	 * @return the number of current users with the admin role
	 *  
	 */
	
	public int getNumberOfAdmins() { 
		int adminCount = 0;
		String query = "SELECT * FROM userDB";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					currentAdminRole = rs.getBoolean(9);
					if (currentAdminRole) {
						adminCount++;
					}
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return adminCount;
	}

	/*******
	 * <p> Method: void deleteUser() </p>
	 * 
	 * <p> Description: Delete a given user from the table.</p>
	 * 
	 * @param username specifies the user that we want to delete from the table
	 *  
	 */
	
	public void deleteUser(String username) {
		
		// find user in table
		String query = "SELECT COUNT(*) FROM userDB WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int counter = rs.getInt(1);
				// only remove if the user is still in the user table
				if (counter > 0) {
					query = "DELETE FROM userDB WHERE username = ?";
					try (PreparedStatement pstmt2 = connection.prepareStatement(query)) {
						pstmt2.setString(1, username);
						pstmt2.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	/*******
	 * <p> Method: boolean deleteLesson(String title) </p>
	 * 
	 * <p> Description: Delete a given lesson from the table.</p>
	 * 
	 * @param title specifies the lesson that we want to delete from the table
	 * 
	 * @return true if the lesson is successfully deleted from the database, otherwise false
	 *  
	 */
	
	public boolean deleteLesson(String title) {
		
		// find lesson in table
		String query = "SELECT COUNT(*) FROM lessonsLearned WHERE title = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, title);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int counter = rs.getInt(1);
				// only remove if the lesson is still in the lesson table
				if (counter > 0) {
					query = "DELETE FROM lessonsLearned WHERE title = ?";
					try (PreparedStatement pstmt2 = connection.prepareStatement(query)) {
						pstmt2.setString(1, title);
						int rowsAffected = pstmt2.executeUpdate();
						return rowsAffected > 0;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*******
	 * <p> Method: boolean deleteLesson(int id) </p>
	 * 
	 * <p> Description: Delete a given lesson from the table.</p>
	 * 
	 * @param id specifies the lesson that we want to delete from the table
	 * 
	 * @return true if the lesson is successfully deleted from the database, otherwise false
	 *  
	 */
	
	public boolean deleteLesson(int id) {
		
		// find lesson in table
		String query = "SELECT COUNT(*) FROM lessonsLearned WHERE id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int counter = rs.getInt(1);
				// only remove if the lesson is still in the lesson table
				if (counter > 0) {
					query = "DELETE FROM lessonsLearned WHERE id = ?";
					try (PreparedStatement pstmt2 = connection.prepareStatement(query)) {
						pstmt2.setInt(1, id);
						int rowsAffected = pstmt2.executeUpdate();
						return rowsAffected > 0;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*******
	 * <p> Debugging method</p>
	 * 
	 * <p> Description: Debugging method that dumps the database of the console.</p>
	 * 
	 * @throws SQLException if there is an issues accessing the database.
	 * 
	 */
	// Dumps the database.
	public void dump() throws SQLException {
		String query = "SELECT * FROM userDB";
		ResultSet resultSet = statement.executeQuery(query);
		ResultSetMetaData meta = resultSet.getMetaData();
		while (resultSet.next()) {
		for (int i = 0; i < meta.getColumnCount(); i++) {
		System.out.println(
		meta.getColumnLabel(i + 1) + ": " +
				resultSet.getString(i + 1));
		}
		System.out.println();
		}
		resultSet.close();
	}


	/*******
	 * <p> Method: void closeConnection()</p>
	 * 
	 * <p> Description: Closes the database statement and connection.</p>
	 * 
	 */
	// Closes the database statement and connection.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}
}
