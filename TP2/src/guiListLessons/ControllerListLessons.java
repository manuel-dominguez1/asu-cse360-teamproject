package guiListLessons;

import guiListLessons.ViewListLessons;
import database.Database;

/*******
 * <p>
 * Title: ControllerListLessons Class.
 * </p>
 * 
 * <p>
 * Description: The Java/FX-based List Lessons Page. This class provides the
 * controller actions basic on the user's use of the JavaFX GUI widgets defined
 * by the View class.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Sierra Trimmer © 2026 </p>
 * 
 * @author Sierra Trimmer
 * 
 * @version 1.00		2026-09-20 Initial version
 * 
 */

public class ControllerListLessons {

	/*-********************************************************************************************
	
	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	/*****
     * <p> Method: ControllerListLessons() </p>
     * 
     * <p> Description: This default constructor is not used in this system. </p>
     */
	public ControllerListLessons() {
	}
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	/**********
	 * <p> Method: performReturn() </p>
	 * 
	 * <p> Description: This method returns the user to their appropriate home page 
	 * based on their active role. </p>
	 */
	protected static void performReturn() {
		if (theDatabase.getCurrentAdminRole()) {
			guiAdminHome.ViewAdminHome.displayAdminHome(ViewListLessons.theStage,
				ViewListLessons.theUser);
		} else if (theDatabase.getCurrentContributorRole()) {
			guiRole1.ViewRole1Home.displayRole1Home(ViewListLessons.theStage,
					ViewListLessons.theUser);
		} else {
			guiRole2.ViewRole2Home.displayRole2Home(ViewListLessons.theStage,
					ViewListLessons.theUser);
		}
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
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewListLessons.theStage);
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
