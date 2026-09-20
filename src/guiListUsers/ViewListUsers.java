package guiListUsers;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
/*******
 * <p>
 * Title: GUIListUsersPage Class.
 * </p>
 * 
 * <p>
 * Description: The Java/FX-based page for listing all users.
 * </p>
 * 
 * 
 */

public class ViewListUsers {

	/*-*******************************************************************************************
	
	Attributes
	
	*/
	
	// These are the widget attributes for the GUI. There are 3 areas for this GUI.
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// GUI Area 1: It informs the user about the purpose of this page, whose account
	// is being used,
	// and a button to allow this user to update the account settings.
	protected static Label label_PageTitle = new Label();


	// GUI Area 2: Contains the table of user information
	protected static TableView<User> tableView = new TableView<>();
	protected static TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
	protected static TableColumn<User, String> fnameColumn = new TableColumn<>("First Name");
	protected static TableColumn<User, String> mnameColumn = new TableColumn<>("Middle Name");
	protected static TableColumn<User, String> lnameColumn = new TableColumn<>("Last Name");
	protected static TableColumn<User, Boolean> pnameColumn = new TableColumn<>("Preferred First Name");
	protected static TableColumn<User, String> emailColumn = new TableColumn<>("Email Address");
	protected static TableColumn<User, Boolean> adminColumn = new TableColumn<>("Admin");
	protected static TableColumn<User, Boolean> role1Column = new TableColumn<>("Role1");
	protected static TableColumn<User, Boolean> role2Column = new TableColumn<>("Role2");
	
	

	// GUI Area 3: This is last of the GUI areas. It is used for quitting the
	// application, logging
	// out, and on other pages a return is provided so the user can return to a
	// previous page when
	// the actions on that page are complete. Be advised that in most cases in this
	// code, the
	// return is to a fixed page as opposed to the actual page that invoked the
	// pages.
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
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
	protected static String theSelectedUser = ""; // The user whose roles are being updated

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
	 * to cause the Listusers page to be displayed.
	 * 
	 * It first sets up very shared attributes so we don't have to pass parameters.
	 * 
	 * It then checks to see if the page has been setup. If not, it instantiates the
	 * class, initializes all the static aspects of the GUI widgets (e.g., location
	 * on the page, font, size, and any methods to be performed).
	 * 
	 * After the instantiation, the code then populates the elements that change
	 * based on the user and the system's current state. It then sets the Scene onto
	 * the stage, and makes it visible to the user.
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

		// If not yet established, populate the static aspects of the GUI by creating
		// the
		// singleton instance of this class
		if (theView == null)
			theView = new ViewListUsers();
		List<User> userList = theDatabase.getFullUserList();
	    tableView.setItems(FXCollections.observableArrayList(userList));
		ControllerListUsers.repaintTheWindow();
		

	}

	/**********
	 * <p>
	 * Method: GUIListUsersPage()
	 * </p>
	 * 
	 * <p>
	 * Description: This method initializes all the elements of the graphical user
	 * interface. This method determines the location, size, font, color, and change
	 * and event handlers for each GUI object.
	 * </p>
	 * 
	 * This is a singleton, so this is performed just one. Subsequent uses fill in
	 * the changeable fields using the displayAddRempoveRoles method.
	 * </p>
	 * 
	 */
	public ViewListUsers() {

		// This page is used by all roles, so we do not specify the role being used

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theListUsersScene = new Scene(theRootPane, width, height);

		// Populate the window with the title and other common widgets and set their
		// static state

		// GUI Area 1
		label_PageTitle.setText("User List");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

	
		// GUI Area 2
		
		
		
		usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
		fnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		mnameColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
		lnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		pnameColumn.setCellValueFactory(new PropertyValueFactory<>("preferredFirstName"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
		adminColumn.setCellValueFactory(new PropertyValueFactory<>("adminRole"));
		role1Column.setCellValueFactory(new PropertyValueFactory<>("newRole1"));
		role2Column.setCellValueFactory(new PropertyValueFactory<>("newRole2"));
	    tableView.getColumns().addAll(usernameColumn,fnameColumn,mnameColumn,lnameColumn,pnameColumn,emailColumn,adminColumn,role1Column,role2Column);
	    
		
		tableView.setPrefWidth(width - 40);
		tableView.setPrefHeight(440);
		tableView.setLayoutY(65);
		tableView.setLayoutX(20);
	

		// GUI Area 3
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 530);
		button_Return.setOnAction((_) -> {
			ControllerListUsers.performReturn();
		});

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 530);
		button_Logout.setOnAction((_) -> {
			ControllerListUsers.performLogout();
		});

		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 530);
		button_Quit.setOnAction((_) -> {
			ControllerListUsers.performQuit();
		});

		// This is the end of the GUI Widgets for the page

		// Due to the very dynamic nature of this page, setting the widget into the Root
		// Pane has
		// has been delegated to the repaintTheWindow and doSelectUser controller
		// methods.
		// Don't follow this pattern if formatting of the page does not change
		// dynamically.
	}

	/*-*******************************************************************************************
	
	Helper methods used to minimizes the number of lines of code needed above
	
	*/

	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l  The Label object to be initialized
	 * @param ff The font to be used
	 * @param f  The size of the font to be used
	 * @param w  The width of the Button
	 * @param p  The alignment (e.g. left, centered, or right)
	 * @param x  The location from the left edge (x axis)
	 * @param y  The location from the top (y axis)
	 */

	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);
	}

	/**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b  The Button object to be initialized
	 * @param ff The font to be used
	 * @param f  The size of the font to be used
	 * @param w  The width of the Button
	 * @param p  The alignment (e.g. left, centered, or right)
	 * @param x  The location from the left edge (x axis)
	 * @param y  The location from the top (y axis)
	 */
	protected static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) {
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);
	}

}