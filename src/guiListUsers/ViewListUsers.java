package guiListUsers;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import database.Database;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
/*******
 * <p>
 * Title: ViewListUsers Class.
 * </p>
 * 
 * <p> Description: The Java/FX-based page for listing users.  This class provides the JavaFX GUI widgets
 * that enable an admin to display all the current users and their information.  
 * 
 * The class has been written using a singleton design pattern and is the View portion of the 
 * Model, View, Controller pattern.  The pattern is designed that the all accesses to this page and
 * its functions starts by invoking the static method displayListUsers.  No other method should 
 * attempt to instantiate this class as that is controlled by displayListUsers.  It ensure that
 * only one instance of class is instantiated and that one is properly configured for each use.  
 * 
 * Please note that this implementation is not appropriate for concurrent systems with multiple
 * users. This Baeldung article provides insight into the issues: 
 *           https://www.baeldung.com/java-singleton</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * @author Kristena Kay
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 2.00		2026-09-20 Modified Code to Create List Users Page
 * 
 */

public class ViewListUsers {

	/*-*******************************************************************************************
	
	Attributes
	
	*/
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	
	// These are the widget attributes for the GUI
	
	// A list of all the current users and their account information, excluding their password.
	protected static ObservableList<User> userInfoList = FXCollections.observableArrayList();
	
	// A table to display all the users and their information.
	protected static TableView<User> userInfoTable = new TableView<>();
	protected static TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
	protected static TableColumn<User, String> fnameColumn = new TableColumn<>("First Name");
	protected static TableColumn<User, String> mnameColumn = new TableColumn<>("Middle Name");
	protected static TableColumn<User, String> lnameColumn = new TableColumn<>("Last Name");
	protected static TableColumn<User, Boolean> pnameColumn = new TableColumn<>("Preferred First Name");
	protected static TableColumn<User, String> emailColumn = new TableColumn<>("Email Address");
	protected static TableColumn<User, Boolean> adminColumn = new TableColumn<>("Admin");
	protected static TableColumn<User, Boolean> role1Column = new TableColumn<>("Role1");
	protected static TableColumn<User, Boolean> role2Column = new TableColumn<>("Role2");
	
	// A label for the table of user information. 
	protected static Label label_userInfoTable = new Label();
	
	// Button to return to admin homepage.
	protected static Button button_Return = new Button("Return");
	// Button to logout and display user login page
	protected static Button button_Logout = new Button("Logout");
	// Button to quit application.
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.

	
	// These attributes are used to configure the page and populate it with this
	// user's information
	private static ViewListUsers theView; // Used to determine if instantiation of the class
											// is needed
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	protected static Stage theStage; // The Stage that JavaFX has established for us
	protected static Pane theRootPane; // The Pane that holds all the GUI widgets
	protected static User theUser; // The current user of the application

	public static Scene theListUsersScene = null; // The Scene each invocation populates

	/*-*******************************************************************************************
	
	Constructors
	
	*/

	/**********
	 * <p>
	 * Method: displayListUsers(Stage ps, User user)
	 * </p>
	 * 
	 * <p>
	 * Description: This method is the single entry point from outside this package
	 * to cause the list users page to be displayed.
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
	 * @param user specifies the User whose roles will be updated
	 *
	 */
	public static void displayListUsers(Stage ps, User user) {

		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// Populate the dynamic aspects of the GUI with the data from all users
		userInfoList.setAll(theDatabase.getFullUserList()); // Fetch all user data
		
		// If not yet established, populate the static aspects of the GUI by creating
		// the singleton instance of this class
		if (theView == null)
			theView = new ViewListUsers();
		
		// Set the title for the window and display the page
		theStage.setTitle("List Users Page");
		theStage.setScene(ViewListUsers.theListUsersScene); // Set this page onto the stage
		theStage.show();									// Display it to the user
		

	}

	/**********
	 * <p>
	 * Method: GUIListUsersPage()
	 * </p>
	 * 
	 * <p>
	 * Description: This method initializes all the elements of the graphical user
	 * interface. This method determines the location, size, font, color, and event 
	 * handlers for each GUI object. It also determines the getter function for each
	 * column in the table.
	 * </p>
	 * 
	 * This is a singleton, so this is performed just one. Subsequent uses fill in
	 * the changeable fields using the displayListUsers method.
	 * </p>
	 * 
	 */
	public ViewListUsers() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theListUsersScene = new Scene(theRootPane, width, height);

		// Populate the window with common widgets and set their static state

		label_userInfoTable.setText("User List");
		setupUI(label_userInfoTable, "Arial", 28, 210, Pos.CENTER, 0, 5);

		usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
		fnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		mnameColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
		lnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		pnameColumn.setCellValueFactory(new PropertyValueFactory<>("preferredFirstName"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
		adminColumn.setCellValueFactory(new PropertyValueFactory<>("adminRole"));
		role1Column.setCellValueFactory(new PropertyValueFactory<>("newRole1"));
		role2Column.setCellValueFactory(new PropertyValueFactory<>("newRole2"));
		
		
		userInfoTable.getColumns().addAll(usernameColumn, fnameColumn, mnameColumn,
	    		lnameColumn, pnameColumn, emailColumn, adminColumn, role1Column, 
	    		role2Column);
		userInfoTable.setItems(userInfoList);
		userInfoTable.setPrefWidth(width - 40);
		userInfoTable.setPrefHeight(440);
		userInfoTable.setLayoutY(65);
		userInfoTable.setLayoutX(20);
	
		
		setupUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 530);
		button_Return.setOnAction((_) -> {
			ControllerListUsers.performReturn();
		});

		setupUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 530);
		button_Logout.setOnAction((_) -> {
			ControllerListUsers.performLogout();
		});

		setupUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 530);
		button_Quit.setOnAction((_) -> {
			ControllerListUsers.performQuit();
		});
		
		
		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
				label_userInfoTable, 
				userInfoTable,  button_Return,
				button_Logout, button_Quit);
		
	}

	/*-*******************************************************************************************
	
	Helper methods used to minimizes the number of lines of code needed above
	
	*/
	/**********
	 * Private local method to initialize the standard fields for a label or button
	 * 
	 * @param l		The object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	
	private static void setupUI(Labeled l, String ff, double f, double w, Pos p, double x,
			double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	

}