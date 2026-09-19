package guiListUsers;
import entityClasses.User;
import database.Database;
import guiAddRemoveRoles.ViewAddRemoveRoles;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;

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

	// Reference for the in-memory database so this package has access
	protected static void repaintTheWindow() {
		// Clear what had been displayed
		ViewListUsers.theRootPane.getChildren().clear();
		
		// Determine which of the two views to show to the user
		
			// Only show the request to select a user to be updated and the ComboBox
			ViewListUsers.theRootPane.getChildren().addAll(
					ViewListUsers.label_PageTitle, 
					 ViewListUsers.tableView,  ViewListUsers.button_Return,
					ViewListUsers.button_Logout, ViewListUsers.button_Quit);
		
		
		
		// Add the list of widgets to the stage and show it
		
		// Set the title for the window
		ViewListUsers.theStage.setTitle("List Users Page");
		ViewListUsers.theStage.setScene(ViewListUsers.theListUsersScene);
		ViewListUsers.theStage.show();
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