package guiCreateDeleteLessons;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import database.Database;
import entityClasses.LessonsLearned;
import entityClasses.User;


/*******
 * <p> Title: GUICreateDeleteLessons Class. </p>
 * 
 * <p> Description: The Java/FX-based page for creating and deleting lessons.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-20 Initial version
 *  
 */

public class ViewCreateDeleteLessons {
	
	/*-*******************************************************************************************

	Attributes
	
	*/
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	
	// These are the widget attributes for the GUI. There are 3 areas for this GUI.
	
	// GUI Area 1: It informs the user about the purpose of this page and whose account is being used.
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
	
	// When no operation has been selected, only Area 2a is shown.  If an operation in the ComboBox in Area 1a
	// has been specified, then Area 2b, Area 2c, or Area 2d is made visible.
	
	// Area 2a: This allows the admin to select an operation of the system as the first step in creating,
	// deleting, or updating a lesson.  The act of selecting an operation causes the change is the GUI.  The Admin does
	// not need to push a button to make this happen.
	protected static Label label_SelectOperation = new Label("Select an operation to perform on a lesson:");
	protected static ComboBox <String> combobox_SelectOperation = new ComboBox <String>();
	protected static String [] ops = {"<Select an Operation>", "Create new lesson", "Delete existing lesson", "Update existing lesson"};
	protected static Button button_confirmOperation = new Button("Confirm");
	
	// Area 2b: When the create operation has been selected these widgets are shown and can be used
	protected static Label label_CreateLessonTitle = new Label("Enter the Title of your new lesson:");
	protected static TextField text_CreateLessonTitle = new TextField();
	protected static Label label_CreateLessonDescription = new Label("Enter a Description of your new lesson:");
	protected static TextArea textarea_CreateLessonDescription = new TextArea();
	protected static Button button_SubmitCreate = new Button("Submit");
	protected static Alert alertCreationConfirmation = new Alert(AlertType.INFORMATION);
	protected static Alert alertCreationError = new Alert(AlertType.INFORMATION);
	protected static Label label_CreateTitleError = new Label("");
	protected static Label label_CreateDescriptionError = new Label("");
	
	// Area 2c: When the delete operation has been selected these widgets are shown and can be used
	protected static Label label_LessonToDelete = new Label("Enter the Title or ID of the lesson you would like to delete:");
	protected static TextField text_LessonToDelete = new TextField();
	protected static Button button_LookupLessonToDelete = new Button("Find");
	protected static Label label_DeleteLessonTitle = new Label("Lesson Title:");
	protected static TextField text_DeleteLessonTitle = new TextField();
	protected static Label label_DeleteLessonDescription = new Label("Lesson Description:");
	protected static TextArea textarea_DeleteLessonDescription = new TextArea();
	protected static Button button_SubmitDelete = new Button("Submit");
	protected static Alert alertDeletionConfirmation = new Alert(AlertType.CONFIRMATION);
	protected static Alert alertDeletionDone = new Alert(AlertType.INFORMATION);
	protected static Alert alertDeletionError = new Alert(AlertType.INFORMATION);
	protected static Label label_DeleteLookupError = new Label("");
	protected static Label label_DeletionError = new Label("");
	
	/*
	protected static ObservableList<LessonsLearned> lessonInfoList = FXCollections.observableArrayList();
	protected static TableView<LessonsLearned> lessonInfoTable = new TableView<>();
	protected static TableColumn<LessonsLearned, Integer> idColumn = new TableColumn<>("Id");
	protected static TableColumn<LessonsLearned, String> titleColumn = new TableColumn<>("Title");
	protected static TableColumn<LessonsLearned, String> descriptionColumn = new TableColumn<>("Description");
	protected static TableColumn<LessonsLearned, String> authorColumn = new TableColumn<>("Author");
	*/
	
	// Area 2d: When the update operation has been selected these widgets are shown and can be used
	protected static Label label_LessonToUpdate = new Label("Enter the Title or ID of the lesson you would like to update:");
	protected static TextField text_LessonToUpdate = new TextField();
	protected static Button button_LookupLessonToUpdate = new Button("Find");
	protected static Label label_UpdateLessonTitle = new Label("Confirm the Title of your lesson:");
	protected static TextField text_UpdateLessonTitle = new TextField();
	protected static Label label_UpdateLessonDescription = new Label("Confirm the Description of your lesson:");
	protected static TextArea textarea_UpdateLessonDescription = new TextArea();
	protected static Button button_SubmitUpdate = new Button("Submit");
	protected static Alert alertUpdateConfirmation = new Alert(AlertType.CONFIRMATION);
	protected static Alert alertUpdateDone = new Alert(AlertType.INFORMATION);
	protected static Alert alertUpdateError = new Alert(AlertType.INFORMATION);
	protected static Label label_UpdateError = new Label("");
	protected static Label label_UpdateLookupError = new Label("");
	protected static Label label_UpdateTitleError = new Label("");
	protected static Label label_UpdateDescriptionError = new Label("");

	
	protected static Alert alertNotImplemented = new Alert(AlertType.INFORMATION);
		
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
	// GUI Area 3: This is last of the GUI areas.  It is used for quitting the application, logging
	// out, and on other pages a return is provided so the user can return to a previous page when
	// the actions on that page are complete.  Be advised that in most cases in this code, the 
	// return is to a fixed page as opposed to the actual page that invoked the pages.
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewCreateDeleteLessons theView;	// Used to determine if instantiation of the class
												// is needed
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;		

	protected static Stage theStage;			// The Stage that JavaFX has established for us
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	protected static User theUser;				// The current user of the application
	
	/**
	 * Sets the scene each invocation populates.
	 */
	public static Scene theCreateDeleteLessonsScene = null;
	protected static String theSelectedOperation = "";		// The lesson operation being performed
	protected static String theSelectedLesson = "";			// The lesson being operated on
	protected static String theCreatedLesson = "";			// The lesson being added
	protected static String theDeletedLesson = "";			// The lesson being removed



	/*-*******************************************************************************************

	Constructors
	
	*/

	/**********
	 * <p> Method: displayCreateDeleteLessons(Stage ps, Lesson lesson) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the CreateDeleteLessons page to be displayed.
	 * 
	 * It first sets up very shared attributes so we don't have to pass parameters.
	 * 
	 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
	 * initializes all the static aspects of the GUI widgets (e.g., location on the page, font,
	 * size, and any methods to be performed).
	 * 
	 * After the instantiation, the code then populates the elements that change based on the user
	 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
	 * to the user.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 * @param user specifies the User whose roles will be updated
	 *
	 */
	public static void displayCreateDeleteLessons(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI by creating the 
		// singleton instance of this class
		if (theView == null) theView = new ViewCreateDeleteLessons();
		
		/* Populate Table
		lessonInfoList.setAll(theDatabase.getFullLessonList()); // Fetch all user data
		*/
		
		// Default to no user selected
		combobox_SelectOperation.getSelectionModel().select(0);
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.  This page is different from the others.  Since there are two 
		// modes (1: user has not been selected, and 2: user has been selected) there are two
		// lists of widgets to be displayed.  For this reason, we have implemented the following 
		// two controller methods to deal with this dynamic aspect.
		ControllerCreateDeleteLessons.repaintTheWindow();
	}

	
	/**********
	 * <p> Method: GUICreateDeleteLessonsPage() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object. </p>
	 * 
	 * <p> This is a singleton, so this is performed just one.  Subsequent uses fill in the changeable
	 * fields using the displayCreateDeleteLessons method.</p>
	 * 
	 */
	public ViewCreateDeleteLessons() {
		
		// This page is used by all roles, so we do not specify the role being used		
			
		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theCreateDeleteLessonsScene = new Scene(theRootPane, width, height);
		theCreateDeleteLessonsScene.getStylesheets().add(
				getClass().getResource("/applicationMain/application.css").toExternalForm());
		
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Create/Delete Lessons Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);
		label_PageTitle.setStyle("-fx-font-weight: bold;");

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		label_UserDetails.setStyle("-fx-font-weight: bold;");
		
		// GUI Area 2a
		setupLabelUI(label_SelectOperation, "Arial", 20, 300, Pos.BASELINE_LEFT, 20, 130);
		label_SelectOperation.setStyle("-fx-font-weight: bold;");

		
		setupComboBoxUI(combobox_SelectOperation, "Dialog", 16, 250, 450, 125);
		List<String> operationList = new ArrayList<String>();	// Create a new list of the
		for (int i = 0; i < ops.length; i++) {				// lesson operations this code currently supports
			operationList.add(ops[i]);
		}
		combobox_SelectOperation.setItems(FXCollections.observableArrayList(operationList));
		combobox_SelectOperation.getSelectionModel().select(0);
		theSelectedOperation = combobox_SelectOperation.getSelectionModel().getSelectedItem();
		combobox_SelectOperation.getSelectionModel().selectedItemProperty()
    	.addListener((@SuppressWarnings("unused") ObservableValue<? extends String> observable, 
    		@SuppressWarnings("unused") String oldvalue, 
    		@SuppressWarnings("unused") String newValue) -> {
    			if (newValue != null) {
    				ViewCreateDeleteLessons.theSelectedOperation = newValue;
    				ControllerCreateDeleteLessons.repaintTheWindow();
    			}
    		});
		
		// GUI Area 2b: Create a Lesson
		setupLabelUI(label_CreateLessonTitle, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 165);
		setupTextUI(text_CreateLessonTitle, "Arial", 16, 360, Pos.BASELINE_LEFT,
				20, 195, true);
		setupLabelUI(label_CreateLessonDescription, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 235);
		setupTextAreaUI(textarea_CreateLessonDescription, "Arial", 16, 750, 200, 20, 265, true);
		setupLabelUI(label_CreateTitleError, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 100);
		label_CreateTitleError.setTextFill(Color.RED);
		setupLabelUI(label_CreateDescriptionError, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 100);
		label_CreateDescriptionError.setTextFill(Color.RED);
		setupButtonUI(button_SubmitCreate, "Dialog", 18, 210, Pos.CENTER, 300, 475);			
		button_SubmitCreate.getStyleClass().remove("fb-button");
		button_SubmitCreate.getStyleClass().add("grey-button");
		button_SubmitCreate.setOnAction((_) -> {ControllerCreateDeleteLessons.performCreateLesson(); });
		
		// GUI Area 2c: Delete a Lesson
		setupLabelUI(label_LessonToDelete, "Arial", 16, 400, Pos.BASELINE_LEFT, 20, 165);
		setupTextUI(text_LessonToDelete, "Arial", 16, 410, Pos.BASELINE_LEFT,
				20, 195, true);
		setupButtonUI(button_LookupLessonToDelete, "Dialog", 18, 210, Pos.CENTER, 450, 195);			
		button_LookupLessonToDelete.getStyleClass().remove("fb-button");
		button_LookupLessonToDelete.getStyleClass().add("grey-button");
		button_LookupLessonToDelete.setOnAction((_) -> {ControllerCreateDeleteLessons.performFindLessonToDelete(); });
		setupLabelUI(label_DeletionError, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 100);
		label_DeletionError.setTextFill(Color.RED);
		setupLabelUI(label_DeleteLookupError, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 100);
		label_DeleteLookupError.setTextFill(Color.RED);
		setupLabelUI(label_DeleteLessonTitle, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 235);
		setupTextUI(text_DeleteLessonTitle, "Arial", 16, 410, Pos.BASELINE_LEFT,
				20, 265, false);
		setupLabelUI(label_DeleteLessonDescription, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 305);
		setupTextAreaUI(textarea_DeleteLessonDescription, "Arial", 16, 750, 125, 20, 335, false);
		
		/*
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("Author"));
		lessonInfoTable.getColumns().addAll(idColumn, titleColumn, descriptionColumn, authorColumn);
		lessonInfoTable.setItems(lessonInfoList);
		lessonInfoTable.setMinWidth(width - 40);
		lessonInfoTable.setMaxWidth(width - 40);
		lessonInfoTable.setPrefHeight(200);
		lessonInfoTable.setLayoutY(265);
		lessonInfoTable.setLayoutX(20);
		*/
		
		setupButtonUI(button_SubmitDelete, "Dialog", 18, 210, Pos.CENTER, 300, 475);			
		button_SubmitDelete.getStyleClass().remove("fb-button");
		button_SubmitDelete.getStyleClass().add("grey-button");
		button_SubmitDelete.setOnAction((_) -> {ControllerCreateDeleteLessons.performDeleteLesson(); });
		
		
		// GUI Area 2d: Update a Lesson
		setupLabelUI(label_LessonToUpdate, "Arial", 16, 400, Pos.BASELINE_LEFT, 20, 165);
		setupTextUI(text_LessonToUpdate, "Arial", 16, 410, Pos.BASELINE_LEFT,
				20, 195, true);
		setupButtonUI(button_LookupLessonToUpdate, "Dialog", 18, 210, Pos.CENTER, 450, 195);			
		button_LookupLessonToUpdate.getStyleClass().remove("fb-button");
		button_LookupLessonToUpdate.getStyleClass().add("grey-button");
		button_LookupLessonToUpdate.setOnAction((_) -> {ControllerCreateDeleteLessons.performFindLessonToUpdate(); });
		setupLabelUI(label_UpdateError, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 100);
		label_UpdateError.setTextFill(Color.RED);
		setupLabelUI(label_UpdateLookupError, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 100);
		label_UpdateLookupError.setTextFill(Color.RED);
		setupLabelUI(label_UpdateLessonTitle, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 235);
		setupTextUI(text_UpdateLessonTitle, "Arial", 16, 410, Pos.BASELINE_LEFT,
				20, 265, true);
		setupLabelUI(label_UpdateTitleError, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 100);
		label_UpdateTitleError.setTextFill(Color.RED);
		setupLabelUI(label_UpdateLessonDescription, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 305);
		setupTextAreaUI(textarea_UpdateLessonDescription, "Arial", 16, 750, 125, 20, 335, true);
		setupLabelUI(label_UpdateDescriptionError, "Arial", 16, 300, Pos.BASELINE_LEFT, 20, 100);
		label_UpdateDescriptionError.setTextFill(Color.RED);
		setupButtonUI(button_SubmitUpdate, "Dialog", 18, 210, Pos.CENTER, 300, 475);			
		button_SubmitUpdate.getStyleClass().remove("fb-button");
		button_SubmitUpdate.getStyleClass().add("grey-button");
		button_SubmitUpdate.setOnAction((_) -> {ControllerCreateDeleteLessons.performUpdateLesson(); });
		
		// GUI Area 3
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.setOnAction((_) -> {ControllerCreateDeleteLessons.performReturn(); });

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((_) -> {ControllerCreateDeleteLessons.performLogout(); });
    
		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((_) -> {ControllerCreateDeleteLessons.performQuit(); });
		
		// This is the end of the GUI Widgets for the page
		
		// Due to the very dynamic nature of this page, setting the widget into the Root Pane has 
		// has been delegated to the repaintTheWindow and doSelectUser controller methods.
		// Don't follow this pattern if formatting of the page does not change dynamically.
	}	

	/*-*******************************************************************************************

	Helper methods used to minimizes the number of lines of code needed above
	
	*/

	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x,
			double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	
	/**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	protected static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x,
			double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);
		b.getStyleClass().add("fb-button");

	}

	/**********
	 * Private local method to initialize the standard fields for a ComboBox
	 * 
	 * @param c		The ComboBox object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the ComboBox
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	protected static void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w,
			double x, double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
		c.getStyleClass().add("fb-button");
	}
	
	/**********
	 * Private local method to initialize the standard fields for a text input field
	 * 
	 * @param t		The TextField object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the TextField
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param e		Is this TextField user editable?
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}
	
	/**********
	 * Private local method to initialize the standard fields for a text input area
	 * 
	 * @param t		The TextArea object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the TextArea
	 * @param h		The height of the TextArea
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param e		Is this TextArea user editable?
	 */
	private void setupTextAreaUI(TextArea t, String ff, double f, double w, double h, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setMinHeight(h);
		t.setMaxHeight(h);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
		
		t.setWrapText(true);
	}
	
	
}
