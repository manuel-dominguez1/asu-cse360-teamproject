package guiListUsers;

/*******
 * <p>
 * Title: ControllerListUsersRoles Class.
 * </p>
 * 
 * <p>
 * Description: The Java/FX-based List Users Page. This class provides the
 * controller actions basic on the user's use of the JavaFX GUI widgets defined
 * by the View class.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * @author Kristena Kay
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 * @version 2.00		2026-09-20 Modified Code to Create List Users Page
 * 
 */

public class ControllerListUsers {

	/*-********************************************************************************************
	
	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerListUsers() {
	}
	
	/**********
	 * <p>
	 * Method: performReturn()
	 * </p>
	 * 
	 * <p>
	 * Description: This method returns the user (who must be an Admin as only
	 * admins are the only users who have access to this page) to the Admin Home
	 * page.
	 * </p>
	 * 
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewListUsers.theStage, ViewListUsers.theUser);
	}

	/**********
	 * <p>
	 * Method: performLogout()
	 * </p>
	 * 
	 * <p>
	 * Description: This method logs out the current user and proceeds to the normal
	 * login page where existing users can log in or potential new users with a
	 * invitation code can start the process of setting up an account.
	 * </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewListUsers.theStage);
	}

	/**********
	 * <p>
	 * Method: performQuit()
	 * </p>
	 * 
	 * <p>
	 * Description: This method terminates the execution of the program. It leaves
	 * the database in a state where the normal login page will be displayed when
	 * the application is restarted.
	 * </p>
	 * 
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}