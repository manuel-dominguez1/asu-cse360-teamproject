package guiListLessons;
import entityClasses.LessonsLearnedList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;
import entityClasses.LessonsLearned;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import database.Database;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
/*******
 * <p>
 * Title: ViewListLessons Class.
 * </p>
 * 
 * <p> Description: The Java/FX-based page for listing lessons.  This class provides the JavaFX GUI widgets
 * that enable an admin to display all the current lessons and their information. </p>
 * 
 * <p> The class has been written using a singleton design pattern and is the View portion of the 
 * Model, View, Controller pattern.  The pattern is designed that the all accesses to this page and
 * its functions starts by invoking the static method displayListUsers.  No other method should 
 * attempt to instantiate this class as that is controlled by displayListUsers.  It ensure that
 * only one instance of class is instantiated and that one is properly configured for each use. </p>
 * 
 * <p> Please note that this implementation is not appropriate for concurrent systems with multiple
 * users. This Baeldung article provides insight into the issues: 
 *           https://www.baeldung.com/java-singleton</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter, Kristena Kay, Sierra Trimmer
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 2.00		2026-09-20 Modified Code to Create List Users Page
 * @version 3.00		2026-09-22 Modified to create List Lessons Page
 * 
 */

public class ViewListLessons {

	/*-*******************************************************************************************
	
	Attributes
	
	*/
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	
	// These are the widget attributes for the GUI
	
	// GUI Area 1: It informs the user about the purpose of this page and whose account is being used.
	protected static Label label_PageTitle = new Label();
	protected static Label label_TableTitle = new Label();
		
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
	
	// A list of all the current lessons and their information.
	private static LessonsLearnedList lessonList = new LessonsLearnedList();
	protected static ObservableList<LessonsLearned> lessonInfoList = FXCollections.observableArrayList();
	
	// GUI Area 2: A table to display all the lessons and their information.
	protected static TableView<LessonsLearned> lessonInfoTable = new TableView<>();
	protected static TableColumn<LessonsLearned, Integer> idColumn = new TableColumn<>("Id");
	protected static TableColumn<LessonsLearned, String> titleColumn = new TableColumn<>("Title");
	protected static TableColumn<LessonsLearned, String> descriptionColumn = new TableColumn<>("Description");
	protected static TableColumn<LessonsLearned, String> authorColumn = new TableColumn<>("Author");
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
	// GUI Area 3:
	// Button to return to admin homepage.
	protected static Button button_Return = new Button("Return");
	// Button to logout and display user login page
	protected static Button button_Logout = new Button("Logout");
	// Button to quit application.
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.

	
	// These attributes are used to configure the page and populate it with this
	// user's information
	private static ViewListLessons theView; // Used to determine if instantiation of the class
											// is needed
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	protected static Stage theStage; // The Stage that JavaFX has established for us
	protected static Pane theRootPane; // The Pane that holds all the GUI widgets
	protected static User theUser; // The current user of the application

	/**
	 * Sets the scene each invocation populates.
	 */
	public static Scene theListLessonsScene = null; // The Scene each invocation populates

	/*-*******************************************************************************************
	
	Constructors
	
	*/

	/**********
	 * <p>
	 * Method: displayListLessons(Stage ps, User user)
	 * </p>
	 * 
	 * <p>
	 * Description: This method is the single entry point from outside this package
	 * to cause the list lessons page to be displayed.
	 * 
	 * It first sets up very shared attributes so we don't have to pass parameters.
	 * 
	 * Next it updates the list of user information. 
	 * 
	 * It then checks to see if the page has been setup. If not, it instantiates the
	 * class, initializes all the static aspects of the GUI widgets (e.g., location
	 * on the page, font, size, and any methods to be performed).
	 * 
	 * After the instantiation, the code sets the Scene onto the stage, and makes it 
	 * visible to the user.
	 * 
	 * @param ps   specifies the JavaFX Stage to be used for this GUI and it's
	 *             methods
	 * 
	 * @param user specifies the current User
	 *
	 */
	public static void displayListLessons(Stage ps, User user) {

		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// Populate the dynamic aspects of the GUI with the data from all lessons
		lessonList.refresh(theDatabase);
		// If the user is admin, list all lessons
		if(user.getAdminRole()) {
			lessonInfoList.setAll(lessonList.all()); // Fetch all user data
		// else list only lessons belonging to the current user
		} else {
			lessonInfoList.setAll(lessonList.all(user.getUserName())); // Fetch all user data
		}
		
		// If not yet established, populate the static aspects of the GUI by creating
		// the singleton instance of this class
		if (theView == null)
			theView = new ViewListLessons();
		
		// Set the title for the window and display the page
		theStage.setTitle("List Lessons Page");
		theStage.setScene(ViewListLessons.theListLessonsScene); // Set this page onto the stage
		theStage.show();									// Display it to the user
		

	}

	/**********
	 * <p>
	 * Method: ViewListLessonsPage()
	 * </p>
	 * 
	 * <p>
	 * Description: This method initializes all the elements of the graphical user
	 * interface. This method determines the location, size, font, color, and event 
	 * handlers for each GUI object. It also determines the getter function for each
	 * column in the table.
	 * </p>
	 * 
	 * <p> This is a singleton, so this is performed just one. Subsequent uses fill in
	 * the changeable fields using the displayListLessons method.
	 * </p>
	 * 
	 */
	public ViewListLessons() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theListLessonsScene = new Scene(theRootPane, width, height);
		theListLessonsScene.getStylesheets().add(
				getClass().getResource("/applicationMain/application.css").toExternalForm());

		// Populate the window with common widgets and set their static state

		label_PageTitle.setText("List Lessons Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);
		label_PageTitle.setStyle("-fx-font-weight: bold;");

		label_TableTitle.setText("Lesson List");
		setupLabelUI(label_TableTitle, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		label_TableTitle.setStyle("-fx-font-weight: bold;");

		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("Author"));
		
		lessonInfoTable.getColumns().addAll(idColumn, titleColumn, descriptionColumn, authorColumn);
		lessonInfoTable.setItems(lessonInfoList);
		lessonInfoTable.setMinWidth(width - 40);
		lessonInfoTable.setMaxWidth(width - 40);
		lessonInfoTable.setPrefHeight(400);
		lessonInfoTable.setLayoutY(110);
		lessonInfoTable.setLayoutX(20);
	
		
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 530);
		button_Return.setOnAction((_) -> {
			ControllerListLessons.performReturn();
		});

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 530);
		button_Logout.setOnAction((_) -> {
			ControllerListLessons.performLogout();
		});

		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 530);
		button_Quit.setOnAction((_) -> {
			ControllerListLessons.performQuit();
		});
		
		
		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
				label_PageTitle, label_TableTitle,
				line_Separator1,
				lessonInfoTable,
				line_Separator4,
				button_Return, button_Logout, button_Quit);
		
	}

	/*-*******************************************************************************************
	
	Helper methods used to minimizes the number of lines of code needed above
	
	*/
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
}
