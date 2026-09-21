package guiAdminHome;

import java.util.List;
import java.util.Optional;

import database.Database;
import entityClasses.User;
import guiFirstAdmin.ViewFirstAdmin;
import guiUserLogin.ViewUserLogin;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */

public class ControllerAdminHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	private static String userToDelete = "";
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerAdminHome() {
	}
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> 
	 * 
	 * Title: performInvitation () Method. </p>
	 * 
	 * <p> Description: Protected method to send an email inviting a potential user to establish
	 * an account and a specific role. </p>
	 */
	protected static void performInvitation () {
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewAdminHome.text_InvitationEmailAddress.getText();
		if (invalidEmailAddress(emailAddress)) {
			return;
		}
		
		// Check to ensure that we are not sending a second message with a new invitation code to
		// the same email address.  
		if (theDatabase.emailaddressHasBeenUsed(emailAddress)) {
			ViewAdminHome.alertEmailError.setContentText(
					"An invitation has already been sent to this email address.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}
		
		// Inform the user that the invitation has been sent and display the invitation code
		String theSelectedRole = (String) ViewAdminHome.combobox_SelectRole.getValue();
		String invitationCode = theDatabase.generateInvitationCode(emailAddress,
				theSelectedRole);
		String msg = "Code: " + invitationCode + " for role " + theSelectedRole + 
				" was sent to: " + emailAddress;
		System.out.println(msg);
		ViewAdminHome.alertEmailSent.setContentText(msg);
		ViewAdminHome.alertEmailSent.showAndWait();
		
		// Update the Admin Home pages status
		ViewAdminHome.text_InvitationEmailAddress.setText("");
		ViewAdminHome.label_NumberOfInvitations.setText("Number of outstanding invitations: " + 
				theDatabase.getNumberOfInvitations());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: manageInvitations () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void manageInvitations () {
		System.out.println("\n*** WARNING ***: Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("Manage Invitations Issue");
		ViewAdminHome.alertNotImplemented.setContentText("Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: setOnetimePassword () Method. </p>
	 * 
	 * <p> Description: Protected method that generates a new one-time password
	 * for a selected user and displays the password to the administrator. </p>
	 */
	protected static void setOneTimePassword () {
		String selectedUser = (String) ViewAdminHome.combobox_SelectUser.getValue();
		
		// Make sure a user has been selected
		if (selectedUser == null || selectedUser.isEmpty() || selectedUser.equals("<Select a User>")) {
			ViewAdminHome.alertNotImplemented.setTitle("One-Time Password");
			ViewAdminHome.alertNotImplemented.setHeaderText("No User Selected");
			ViewAdminHome.alertNotImplemented.setContentText("Please select a user in order to set a one-time password.");
			ViewAdminHome.alertNotImplemented.showAndWait();
			return;
			
		}
		
		// Generate and set the one-time password
		String oneTimePassword = theDatabase.setOneTimePassword(selectedUser);
		
		// Check whether the operation was successful
		if (oneTimePassword.isEmpty()) {
			ViewAdminHome.alertNotImplemented.setTitle("One-Time Password");
			ViewAdminHome.alertNotImplemented.setHeaderText("Password Reset Failed");
			ViewAdminHome.alertNotImplemented.setContentText("Unable to set a one-time password for user: " + selectedUser);
			ViewAdminHome.alertNotImplemented.showAndWait();
			return;
		}
		
		// Display the new password to the administrator
		ViewAdminHome.alertNotImplemented.setTitle("One-Time Password");
		ViewAdminHome.alertNotImplemented.setHeaderText("One-Time Password Created");
		ViewAdminHome.alertNotImplemented.setContentText(
				"User:" + selectedUser +
				"\n\nOne-time password:" + oneTimePassword);
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: deleteUser () Method. </p>
	 * 
	 * <p> Description: Protected method that deletes a user from the table, given
	 * 					that user exists and is not the only admin user. </p>
	 */
	protected static void deleteUser() {
		
		// handle delete user button click
		ViewAdminHome.button_DoDeleteUser.setOnAction((_) -> {
			
			// check if user exists
			if (theDatabase.doesUserExist(userToDelete)) {
				String currentUsername = theDatabase.getCurrentUsername();
				
				// confirm there is more than one admin user or that user to delete is not self
				if (theDatabase.getNumberOfAdmins() > 1 || !(userToDelete.equals(currentUsername))) {
					
					// show confirmation alert
					ViewAdminHome.alertDeleteUserConfirmation.setTitle("Delete User Confirmation");
					ViewAdminHome.alertDeleteUserConfirmation.setHeaderText("You are about to permanently delete user " + userToDelete);
					ViewAdminHome.alertDeleteUserConfirmation.setContentText("Are you sure you want to delete this user?");
					Optional<ButtonType> confirmationResult = ViewAdminHome.alertDeleteUserConfirmation.showAndWait();
					
					// if user confirms, delete the user
					if (confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK) {
						
						theDatabase.deleteUser(userToDelete);
						
						// show success message
						ViewAdminHome.alertDeleteUser.setHeaderText("User Deleted Successfully");
						ViewAdminHome.alertDeleteUser.setContentText("The user " + userToDelete + " was deleted");
						ViewAdminHome.alertDeleteUser.showAndWait();
						ViewAdminHome.text_UserToDelete.clear();
						
					}
					
					// if user cancels, reset text field
					else {
						ViewAdminHome.text_UserToDelete.clear();
						return;
					}
				} 
				
				// show alert if user to delete is self
				else {
					ViewAdminHome.alertDeleteUser.setContentText(userToDelete + " is your admin account. This account must"
							+ " deleted by a different admin account to ensure there is at least one admin user at all times.");
					ViewAdminHome.alertDeleteUser.showAndWait();
					ViewAdminHome.text_UserToDelete.clear();
				}
				
			}
			
			// show alert if user does not exist
			else {
				ViewAdminHome.alertDeleteUser.setContentText("The entered user does not exist");
				ViewAdminHome.alertDeleteUser.showAndWait();
				ViewAdminHome.text_UserToDelete.clear();
			}
		});	
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: listUsers () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void listUsers() {
		guiListUsers.ViewListUsers.displayListUsers(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: addRemoveRoles () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to add and remove roles for any of
	 * the users currently in the system.  This is done by invoking the AddRemoveRoles Page. There
	 * is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.</p>
	 */
	protected static void addRemoveRoles() {
		guiAddRemoveRoles.ViewAddRemoveRoles.displayAddRemoveRoles(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: invalidEmailAddress () Method. </p>
	 * 
	 * <p> Description: Protected method that is intended to check an email address before it is
	 * used to reduce errors.  The code currently only checks to see that the email address is not
	 * empty.  In the future, a syntactic check must be performed and maybe there is a way to check
	 * if a properly email address is active.</p>
	 * 
	 * @param emailAddress	This String holds what is expected to be an email address
	 */
	protected static boolean invalidEmailAddress(String emailAddress) {
		if (emailAddress.length() == 0) {
			ViewAdminHome.alertEmailError.setContentText(
					"Correct the email address and try again.");
			ViewAdminHome.alertEmailError.showAndWait();
			return true;
		}
		return false;
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminHome.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
	
	/**********
	 * <p> Method: setUserToDelete() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the delete user field in the
	 * View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setUserToDelete() {
		userToDelete = ViewAdminHome.text_UserToDelete.getText();
		
	}
}
