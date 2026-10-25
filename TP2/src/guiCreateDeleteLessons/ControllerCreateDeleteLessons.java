package guiCreateDeleteLessons;

import database.Database;
import entityClasses.LessonsLearned;
import entityClasses.LessonsLearnedList;

import java.util.Optional;
import javafx.scene.control.ButtonType;
import guiTools.TitleRecognizer;
import guiTools.DescriptionRecognizer;

/*******
 * <p> Title: ControllerCreateDeleteLessons Class. </p>
 * 
 * <p> Description: The Java/FX-based Page for creating, deleting, and updating lessons. 
 * This class provides the controller actions basic on the user's use of the JavaFX GUI 
 * widgets defined by the View class.
 * 
 * This page has one of the more complex Controller Classes due to the fact that the changing the
 * values of widgets changes the layout of the page.  It is up to the Controller to determine what
 * to do and it involves the proper elements from View Class for this GUI page.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter, Sierra Trimer
 * 
 * @version 1.00		2026-09-20 Initial version 
 */

public class ControllerCreateDeleteLessons {
	
	/*********************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	/*****
     * <p> Method: ControllerCreateDeleteLessons() </p>
     * 
     * <p> Description: This default constructor is not used in this system. </p>
     */
	public ControllerCreateDeleteLessons() {
	}
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	private static LessonsLearned lessonBeingDeleted = null;
	private static LessonsLearned lessonBeingUpdated = null;
	
	/**********
	 * <p> Method: repaintTheWindow() </p>
	 * 
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values. </p>
	 * 
	 */
	protected static void repaintTheWindow() {
		// Clear what had been displayed
		ViewCreateDeleteLessons.theRootPane.getChildren().clear();
		
		// Determine which of the four views to show to the user
		if (ViewCreateDeleteLessons.theSelectedOperation.compareTo("<Select an Operation>") == 0) {
			// Only show the request to select a user to be updated and the ComboBox
			ViewCreateDeleteLessons.theRootPane.getChildren().addAll(
					ViewCreateDeleteLessons.label_PageTitle, ViewCreateDeleteLessons.label_UserDetails, 
					ViewCreateDeleteLessons.line_Separator1,
					ViewCreateDeleteLessons.label_SelectOperation, ViewCreateDeleteLessons.combobox_SelectOperation, 
					ViewCreateDeleteLessons.line_Separator4, ViewCreateDeleteLessons.button_Return,
					ViewCreateDeleteLessons.button_Logout, ViewCreateDeleteLessons.button_Quit);
		}
		
		if (ViewCreateDeleteLessons.theSelectedOperation.compareTo("Create new lesson") == 0) {
			// Show Area 2b
			ViewCreateDeleteLessons.theRootPane.getChildren().addAll(
					ViewCreateDeleteLessons.label_PageTitle, ViewCreateDeleteLessons.label_UserDetails, 
					ViewCreateDeleteLessons.line_Separator1,
					ViewCreateDeleteLessons.label_SelectOperation, ViewCreateDeleteLessons.combobox_SelectOperation,
					ViewCreateDeleteLessons.label_CreateLessonTitle, ViewCreateDeleteLessons.text_CreateLessonTitle,
					ViewCreateDeleteLessons.label_CreateLessonDescription, ViewCreateDeleteLessons.textarea_CreateLessonDescription,
					ViewCreateDeleteLessons.button_SubmitCreate,
					ViewCreateDeleteLessons.label_CreateTitleError, ViewCreateDeleteLessons.label_CreateDescriptionError,
					ViewCreateDeleteLessons.line_Separator4, ViewCreateDeleteLessons.button_Return,
					ViewCreateDeleteLessons.button_Logout, ViewCreateDeleteLessons.button_Quit);
		}
		
		if (ViewCreateDeleteLessons.theSelectedOperation.compareTo("Delete existing lesson") == 0) {
			// Show Area 2c
			ViewCreateDeleteLessons.theRootPane.getChildren().addAll(
					ViewCreateDeleteLessons.label_PageTitle, ViewCreateDeleteLessons.label_UserDetails, 
					ViewCreateDeleteLessons.line_Separator1,
					ViewCreateDeleteLessons.label_SelectOperation, ViewCreateDeleteLessons.combobox_SelectOperation,
					ViewCreateDeleteLessons.label_LessonToDelete, ViewCreateDeleteLessons.text_LessonToDelete,
					ViewCreateDeleteLessons.button_LookupLessonToDelete,
					ViewCreateDeleteLessons.label_DeleteLessonTitle, ViewCreateDeleteLessons.text_DeleteLessonTitle,
					ViewCreateDeleteLessons.label_DeleteLessonDescription, ViewCreateDeleteLessons.textarea_DeleteLessonDescription,
					//ViewCreateDeleteLessons.lessonInfoTable,
					ViewCreateDeleteLessons.button_SubmitDelete,
					ViewCreateDeleteLessons.label_DeleteLookupError,ViewCreateDeleteLessons.label_DeletionError,
					ViewCreateDeleteLessons.line_Separator4, ViewCreateDeleteLessons.button_Return,
					ViewCreateDeleteLessons.button_Logout, ViewCreateDeleteLessons.button_Quit);
		}
		
		if (ViewCreateDeleteLessons.theSelectedOperation.compareTo("Update existing lesson") == 0) {
			// Show Area 2d
			ViewCreateDeleteLessons.theRootPane.getChildren().addAll(
					ViewCreateDeleteLessons.label_PageTitle, ViewCreateDeleteLessons.label_UserDetails, 
					ViewCreateDeleteLessons.line_Separator1,
					ViewCreateDeleteLessons.label_SelectOperation, ViewCreateDeleteLessons.combobox_SelectOperation,
					ViewCreateDeleteLessons.label_LessonToUpdate, ViewCreateDeleteLessons.text_LessonToUpdate,
					ViewCreateDeleteLessons.button_LookupLessonToUpdate,
					ViewCreateDeleteLessons.label_UpdateLessonTitle, ViewCreateDeleteLessons.text_UpdateLessonTitle,
					ViewCreateDeleteLessons.label_UpdateLessonDescription, ViewCreateDeleteLessons.textarea_UpdateLessonDescription,
					ViewCreateDeleteLessons.button_SubmitUpdate,
					ViewCreateDeleteLessons.label_UpdateLookupError, ViewCreateDeleteLessons.label_UpdateError, 
					ViewCreateDeleteLessons.label_UpdateTitleError, ViewCreateDeleteLessons.label_UpdateDescriptionError, 
					ViewCreateDeleteLessons.line_Separator4, ViewCreateDeleteLessons.button_Return,
					ViewCreateDeleteLessons.button_Logout, ViewCreateDeleteLessons.button_Quit);
		}
		
		// Add the list of widgets to the stage and show it
		
		// Set the title for the window
		ViewCreateDeleteLessons.theStage.setTitle("CSE 360 Foundation Code: Admin Opertaions Page");
		ViewCreateDeleteLessons.theStage.setScene(ViewCreateDeleteLessons.theCreateDeleteLessonsScene);
		ViewCreateDeleteLessons.theStage.show();
	}
	
	
	/**********
	 * <p> 
	 * 
	 * Title: performCreateLesson () Method. </p>
	 * 
	 * <p> Description: Method for creating a new lesson in the database. 
	 * The input for the new lesson is validated with DescriptionRecognizer
	 * and TItleRecognizer classes. If the given input is valid, a new lesson
	 * is created and added to the database and the lessons list. A success 
	 * message is then displayed. Otherwise, a meaningful error message is 
	 * displayed. </p>
	 */
	protected static void performCreateLesson () {
		// Determine which item in the ComboBox list was selected
		ViewCreateDeleteLessons.theSelectedOperation =
				(String) ViewCreateDeleteLessons.combobox_SelectOperation.getValue();
		
		// Verify an operation is selected
		if (ViewCreateDeleteLessons.theSelectedOperation == null) {
			return;
		}
				
		// If the selection is to create a lesson
		if (ViewCreateDeleteLessons.theSelectedOperation.compareTo("Create new lesson") == 0) {
			
			// Retrieve the lesson information from the GUI
			String lessonTitle = ViewCreateDeleteLessons.text_CreateLessonTitle.getText();
			String lessonDescription = ViewCreateDeleteLessons.textarea_CreateLessonDescription.getText();
			String lessonAuthor = ViewCreateDeleteLessons.theUser.getUserName();
			
			// Validate the input
			String invalidTitleMessage = TitleRecognizer.checkForValidTitle(lessonTitle);
			if (!invalidTitleMessage.isEmpty()) {
				ViewCreateDeleteLessons.label_CreateTitleError.setText(invalidTitleMessage);
				return;
			}
			
			ViewCreateDeleteLessons.label_CreateTitleError.setText("");
			
			String invalidDescripMessage = DescriptionRecognizer.checkForValidDescription(lessonDescription);
			if (!invalidDescripMessage.isEmpty()) {
				ViewCreateDeleteLessons.label_CreateDescriptionError.setText(invalidDescripMessage);
				return;
			}
			
			ViewCreateDeleteLessons.label_CreateDescriptionError.setText("");
			ViewCreateDeleteLessons.label_CreateTitleError.setText("");
		
			// Create the Lesson in the database and list
			boolean success = LessonsLearned.createLesson(lessonTitle, lessonDescription, lessonAuthor, theDatabase);
			
			// Show success or error dialog based on database response	
			if (success) {
				ViewCreateDeleteLessons.alertCreationConfirmation.setHeaderText("Lesson Created");
				ViewCreateDeleteLessons.alertCreationConfirmation.setContentText("Your lesson has been successfully saved to the database.");
				ViewCreateDeleteLessons.alertCreationConfirmation.showAndWait();
			}
			
			else {
				ViewCreateDeleteLessons.alertCreationError.setHeaderText("Creation Error");
				ViewCreateDeleteLessons.alertCreationError.setContentText("Unable to create lesson. Check if Title is already in use.");
				ViewCreateDeleteLessons.alertCreationError.showAndWait();
			}
			
			ViewCreateDeleteLessons.text_CreateLessonTitle.clear();
			ViewCreateDeleteLessons.textarea_CreateLessonDescription.clear();
			
		}
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performFindLessonToDelete () Method. </p>
	 * 
	 * <p> Description: Method to find an existing lesson in the database and display
	 * the lesson's current title and description so that
	 * the user can confirm it is the lesson they would like to delete. </p>
	 *  
	 */
	protected static void performFindLessonToDelete () {
		String rawInput = ViewCreateDeleteLessons.text_LessonToDelete.getText();
		
		// Validate that input was entered
		if (rawInput == null || rawInput.trim().isEmpty()) {
			ViewCreateDeleteLessons.label_DeleteLookupError.setText("Please enter the Title or ID of the lesson you would like to delete.");
			return;
		}
		
		// Clear previous error messages
		ViewCreateDeleteLessons.label_DeleteLookupError.setText("");
		ViewCreateDeleteLessons.label_DeletionError.setText("");

		String cleanInput = rawInput.trim();
		lessonBeingDeleted = null;
		
		// Determine if a lesson title or id was entered
		if (cleanInput.matches("\\d+")) {
			int lessonId = Integer.parseInt(cleanInput);
			if(theDatabase.doesLessonExist(lessonId)) {
				lessonBeingDeleted = theDatabase.getLessonDetails(lessonId);
			}
		}
		else {
			if(theDatabase.doesLessonExist(cleanInput)) {
				lessonBeingDeleted = theDatabase.getLessonDetails(cleanInput);
			}
		}
		
		// Lesson does not exist
		if (lessonBeingDeleted == null) {
			ViewCreateDeleteLessons.label_DeletionError.setText("No lesson found matching: \"" + cleanInput + "\"");
			ViewCreateDeleteLessons.text_DeleteLessonTitle.clear();
			ViewCreateDeleteLessons.textarea_DeleteLessonDescription.clear();
			return;
		}
		
		// Populate fields with lesson data
		ViewCreateDeleteLessons.text_DeleteLessonTitle.setText(lessonBeingDeleted.getTitle());
		ViewCreateDeleteLessons.textarea_DeleteLessonDescription.setText(lessonBeingDeleted.getDescription());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performDeleteLesson () Method. </p>
	 * 
	 * <p> Description: Method for deleting a lesson from the database using a lesson's 
	 * id or title.  If the specified lesson to delete is found, a confirmation window is
	 * displayed. If confirmed, the lesson is then deleted from the database. If the lesson
	 * is not found a meaningful error message is displayed. </p>
	 */
	protected static void performDeleteLesson () {
		
		int lessonId = -1;
		String lessonTitle = null;
		
		// Determine which item in the ComboBox list was selected
		ViewCreateDeleteLessons.theSelectedOperation =
				(String) ViewCreateDeleteLessons.combobox_SelectOperation.getValue();
				
		// Verify an operation is selected
		if (ViewCreateDeleteLessons.theSelectedOperation == null) {
			return;
		}
						
		// If the selection is to delete a lesson
		if (ViewCreateDeleteLessons.theSelectedOperation.compareTo("Delete existing lesson")== 0) {
			
			// Check if lesson was found
			if(lessonBeingDeleted == null) {
				ViewCreateDeleteLessons.label_DeletionError.setText("Please find a lesson before attempting to delete.");
				return;
			}
						
			// Get the id of the lesson being updated
			int targetId = lessonBeingDeleted.getId();
			
			boolean isDeleted = false;
			
			ViewCreateDeleteLessons.alertDeletionConfirmation.setHeaderText("Please Confirm");
			ViewCreateDeleteLessons.alertDeletionConfirmation.setContentText("Are you sure you want to delete this lesson?");
			Optional<ButtonType> result = ViewCreateDeleteLessons.alertDeletionConfirmation.showAndWait();

			if (result.isPresent() && result.get() == ButtonType.OK) {		
				// Delete the Lesson Object
				if (targetId != -1) {
					isDeleted = LessonsLearned.deleteLesson(targetId, theDatabase);
				}
				else {
					isDeleted = LessonsLearned.deleteLesson(lessonTitle, theDatabase);
				}
			}
			else {
				return;
			}
			// Show success or error dialog based on database response	
			if (isDeleted) {
				ViewCreateDeleteLessons.alertDeletionDone.setHeaderText("Lesson Deleted");
				ViewCreateDeleteLessons.alertDeletionDone.setContentText("Your lesson has been successfully deleted from the database.");
				ViewCreateDeleteLessons.alertDeletionDone.showAndWait();
			}
					
			else {
				ViewCreateDeleteLessons.alertDeletionError.setHeaderText("Deletion Error");
				ViewCreateDeleteLessons.alertDeletionError.setContentText("Unable to find or delete lesson.");
				ViewCreateDeleteLessons.alertDeletionError.showAndWait();
			}
		}
		
		ViewCreateDeleteLessons.text_LessonToDelete.clear();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performFindLessonToUpdate () Method. </p>
	 * 
	 * <p> Description: Method to find an existing lesson in the database and display
	 * the lesson's current title and description so that
	 * the user can modify them. If the specified lesson to update does not exist, a 
	 * meaningful error message is displayed. </p>
	 *  
	 */
	protected static void performFindLessonToUpdate () {
		String rawInput = ViewCreateDeleteLessons.text_LessonToUpdate.getText();
		
		// Validate that input was entered
		if (rawInput == null || rawInput.trim().isEmpty()) {
			ViewCreateDeleteLessons.label_UpdateLookupError.setText("Please enter the Title or ID of the lesson you would like to update.");
			return;
		}
		
		// Clear previous error messages
		ViewCreateDeleteLessons.label_UpdateLookupError.setText("");
		ViewCreateDeleteLessons.label_UpdateError.setText("");
		ViewCreateDeleteLessons.label_UpdateTitleError.setText("");
		ViewCreateDeleteLessons.label_UpdateDescriptionError.setText("");

		String cleanInput = rawInput.trim();
		lessonBeingUpdated = null;
		
		// Determine if a lesson title or id was entered
		if (cleanInput.matches("\\d+")) {
			int lessonId = Integer.parseInt(cleanInput);
			if(theDatabase.doesLessonExist(lessonId)) {
				lessonBeingUpdated = theDatabase.getLessonDetails(lessonId);
			}
		}
		else {
			if(theDatabase.doesLessonExist(cleanInput)) {
				lessonBeingUpdated = theDatabase.getLessonDetails(cleanInput);
			}
		}
		
		// Lesson does not exist
		if (lessonBeingUpdated == null) {
			ViewCreateDeleteLessons.label_UpdateError.setText("No lesson found matching: \"" + cleanInput + "\"");
			ViewCreateDeleteLessons.text_UpdateLessonTitle.clear();
			ViewCreateDeleteLessons.textarea_UpdateLessonDescription.clear();
			return;
		}
		
		// Populate fields with lesson data
		ViewCreateDeleteLessons.text_UpdateLessonTitle.setText(lessonBeingUpdated.getTitle());
		ViewCreateDeleteLessons.textarea_UpdateLessonDescription.setText(lessonBeingUpdated.getDescription());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performUpdateLesson () Method. </p>
	 * 
	 * <p> Description: Method to update the title or description of the
	 * lesson found by the performFindLesson() method. The new title and/or description
	 * is validated using the TitleRecognizer and/or DescriptionRecognizer class. A
	 * confirmation window is then displayed before the lesson is updated. If the lesson
	 * is updated, a success message is displayed. If the lesson is not updated a meaningful
	 * error message is displayed. </p>
	 */
	protected static void performUpdateLesson () {
		
		// Determine which item in the ComboBox list was selected
		ViewCreateDeleteLessons.theSelectedOperation =
				(String) ViewCreateDeleteLessons.combobox_SelectOperation.getValue();
				
		// Verify an operation is selected
		if (ViewCreateDeleteLessons.theSelectedOperation == null) {
			return;
		}
						
		// If the selection is to update a lesson
		if (ViewCreateDeleteLessons.theSelectedOperation.compareTo("Update existing lesson")== 0) {
				
			// Check if lesson was found
			if(lessonBeingUpdated == null) {
				ViewCreateDeleteLessons.label_UpdateError.setText("Please find a lesson before attempting to update.");
				return;
			}
			
			// Get the id of the lesson being updated
			int targetId = lessonBeingUpdated.getId();
			
			// Read the edited fields
			String newTitle = ViewCreateDeleteLessons.text_UpdateLessonTitle.getText();
			String newDescrip = ViewCreateDeleteLessons.textarea_UpdateLessonDescription.getText();

	
			newTitle = newTitle.trim();
			newDescrip = newDescrip.trim();
				
			boolean titleUpdated = !newTitle.equals(lessonBeingUpdated.getTitle());
			boolean descriptionUpdated = !newDescrip.equals(lessonBeingUpdated.getDescription());
			
			// Check for modifications
			if (!titleUpdated && !descriptionUpdated) {
				ViewCreateDeleteLessons.label_UpdateError.setText("No changes detected.");
				return;
			}
			
			ViewCreateDeleteLessons.label_UpdateError.setText("");
			
			// Validate text modifications
			String invalidTitleMessage = TitleRecognizer.checkForValidTitle(newTitle);
			if (!invalidTitleMessage.isEmpty()) {
				ViewCreateDeleteLessons.label_UpdateTitleError.setText(invalidTitleMessage);
				return;
			}
						
			String invalidDescripMessage = DescriptionRecognizer.checkForValidDescription(newDescrip);
			if (!invalidDescripMessage.isEmpty()) {
				ViewCreateDeleteLessons.label_UpdateDescriptionError.setText(invalidDescripMessage);
				return;
			}
					
			ViewCreateDeleteLessons.label_UpdateDescriptionError.setText("");
			ViewCreateDeleteLessons.label_UpdateTitleError.setText("");

			// Confirm updates
			ViewCreateDeleteLessons.alertUpdateConfirmation.setHeaderText("Please Confirm");
			ViewCreateDeleteLessons.alertUpdateConfirmation.setContentText("Are you sure you want to update this lesson?");
			Optional<ButtonType> result = ViewCreateDeleteLessons.alertUpdateConfirmation.showAndWait();
			
			boolean isUpdated = false;


			if (!result.isPresent() || result.get() != ButtonType.OK) {
				return;
			}
			
			boolean titleSuccess = true;
			boolean descriptionSuccess = true;
			
			// Update title
			if (titleUpdated) {
				titleSuccess = LessonsLearned.updateTitle(targetId, newTitle, theDatabase);
			}
			
			// Update description
			if (descriptionUpdated) {
				descriptionSuccess = LessonsLearned.updateDescription(targetId, newDescrip, theDatabase);

			}
			
			isUpdated = titleSuccess && descriptionSuccess;
			
			// Show success or error dialog based on database response	
			if (isUpdated) {
				ViewCreateDeleteLessons.alertUpdateConfirmation.setHeaderText("Lesson Updated");
				ViewCreateDeleteLessons.alertUpdateConfirmation.setContentText("Your lesson has been successfully updated in the database.");
				ViewCreateDeleteLessons.alertUpdateConfirmation.showAndWait();
				
				// Clear inputs upon success
				ViewCreateDeleteLessons.text_LessonToUpdate.clear();
				ViewCreateDeleteLessons.text_UpdateLessonTitle.clear();
				ViewCreateDeleteLessons.textarea_UpdateLessonDescription.clear();
				
				// Clear error labels
				ViewCreateDeleteLessons.label_UpdateError.setText("");
				ViewCreateDeleteLessons.label_UpdateLookupError.setText("");
				ViewCreateDeleteLessons.label_UpdateTitleError.setText("");
				ViewCreateDeleteLessons.label_UpdateDescriptionError.setText("");

				lessonBeingUpdated = null;

			}
					
			else {
				ViewCreateDeleteLessons.alertUpdateError.setHeaderText("Update Error");
				ViewCreateDeleteLessons.alertUpdateError.setContentText("Unable to find or update lesson.");
				ViewCreateDeleteLessons.alertUpdateError.showAndWait();
			}
		}
	}
	
	
	/**********
	 * <p> Method: performReturn() </p>
	 * 
	 * <p> Description: This method returns the user (who must be an Admin as only admins are the
	 * only users who have access to this page) to the Admin Home page. </p>
	 */
	protected static void performReturn() {
		if (theDatabase.getCurrentAdminRole()) {
			guiAdminHome.ViewAdminHome.displayAdminHome(ViewCreateDeleteLessons.theStage,
				ViewCreateDeleteLessons.theUser);
		} else if (theDatabase.getCurrentContributorRole()) {
			guiRole1.ViewRole1Home.displayRole1Home(ViewCreateDeleteLessons.theStage,
					ViewCreateDeleteLessons.theUser);
		} else {
			guiRole2.ViewRole2Home.displayRole2Home(ViewCreateDeleteLessons.theStage,
					ViewCreateDeleteLessons.theUser);
		}
	}
	
	
	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewCreateDeleteLessons.theStage);
	}
	
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}
