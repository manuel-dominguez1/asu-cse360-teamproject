package passwordPopUpWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import passwordEvaluationTestbedMain.PasswordEvaluationGUITestbed;

/*******
 * <p> Title: View Class - establishes the Graphics User interface, presents information to the
 * user, and accept information from the user.</p>
 *
 * <p> Description: This View class is a major component of a Model View Controller (MVC)
 * application design that provides the user with Graphical User Interface with JavaFX
 * widgets as opposed to a command line interface.
 * 
 * In this case the GUI consists of numerous widgets to show the user where to enter the password,
 * where any errors are located, and a set of requirements for a valid password and whether or not
 * they have been satisfied
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 *
 * @version 2.00	2025-07-30 Rewrite of this application for the Fall 2025 offering of CSE 360
 * and other ASU courses.
 */

public class View {


	/*
	 * The following private objects are with GUI widgets that the view manages.
	 */
	
	/*
	 * A Label for the text input field and the text input field
	 * 
	 */
	static private Label label_Password = new Label("Enter the password here");
	static protected TextField text_Password = new TextField();

	/* 
	 * Feedback labels to show the user where the error is located.  For this application, the
	 * errors are ones of omission, so the "error" is always at the end of the input (e.g., a
	 * password must have an upper case letter.  If the current input does not include an upper 
	 * case character, this can be corrected by adding one.  We so this at the end, but in 
	 * reality, the upper case could be anywhere in the password.
	 * 
	 */
	static protected Label label_errPassword = new Label();	// There error labels change based on the
	static protected Label noInputFound = new Label();			// user's input
	static private TextFlow errPassword;
	static protected Text errPasswordPart1 = new Text();		// This contains the user's input
	static protected Text errPasswordPart2 = new Text();		// This is the up arrow
	static protected Label errPasswordPart3 = new Label();		// This is the concatenation of the two

    /* 
	 * Feedback labels with text and color to specify which of the requirements have been satisfied
	 * (using both text and the color green) and which have not (both with text and the color red).
	 * 
	 */
	static protected Label validPassword = new Label();		// This only appears with a valid password
	static protected Label label_Requirements = 
    		new Label("A valid password must satisfy the following requirements:");
	static protected Label label_UpperCase = new Label();		// These empty labels change based on the
	static protected Label label_LowerCase = new Label();		// user's input
	static protected Label label_NumericDigit = new Label();	
	static protected Label label_SpecialChar = new Label();
	static protected Label label_LongEnough = new Label();

    /* 
	 * Button to finish the process.  It only become active when all the requirements have been met
	 * 
	 */
	static protected Button button_Finish = new Button();

	
	/*******
	 * <p> Title: View - Default Constructor </p>
	 * 
	 * <p> Description: This constructor does not perform any special function for this
	 * application. </p>
	 *
	public View() {
		// No special actions required
	}


	/*******
	 * <p> Title: View - Static Public "Constructor" </p>
	 * 
	 * <p> Description: This method is not really a constructor.  That is the reason for the quote
	 * marks.  This method creates three singletons: 1) Model, 2) View, and 3) Controller.
	 * 
	 * This is NOT how one writes a singleton in normal practice.  To see the normal practice, look
	 * at the getModel and getController methods within the Model and Controller classes in this
	 * application.
	 * 
	 * This "constructor" was written this way due to a design requirement that it would be called
	 * before any other constructor in the MVC Display Package.  Unlike the others, this has a
	 * parameter of the Stage onto which these GUI elements are to be placed.  The first thing
	 * this method does is invoke the actual private constructor using the passed-in parameter. It
	 * then invokes the other two singleton "constructors" (i.e.,  Model and the Controller) and
	 * then defines three private static variables for it's own use.
	 * 
	 * This "constructor" does not return a value as the caller will not be using a reference
	 * to this View.  The very next thing the caller does is show the Stage that contains this
	 * Pane and then it stops.</p>
	 * 
	 * @param primaryStage Specifies the Stage on which the GUI should be built.	 
	 *

	static public void setupView(Pane theRoot) {
		theView = new View(theRoot);
	}
	
	/*******
	 * <p> Title: View - Private Constructor </p>
	 * 
	 * <p> Description: This is the actual constructor for this singleton class.  It creates the
	 * various GUI elements, specifies attributes for each (e.g., the location in the window and
	 * a call to a handler method to be used when the GUI element is used), adds these elements to
	 * a window pane, creates a new scene using the list of GUI elements and a specification of the
	 * width and height of the scene, and the sets that scene onto the Stage.
	 * 
	 * Some of these widgets change based on the input the user provides.  In those cases, the text
	 * and the color will be programmatically set within the Model component of the MVC and no text
	 * or color attribute will be set here.
	 * 
	 * @param theRoot Specifies the Pane on which the GUI widgets should be added.	 
	 */
	
	public static void view (Pane theRoot) {
		double windowWidth = PasswordEvaluationGUITestbed.WINDOW_WIDTH;

		// Label the password input field with a title just above it, left aligned
		setupLabelWidget(label_Password, 10, 10, "Arial", 14, windowWidth-10, Pos.BASELINE_LEFT);
		
		// Establish the text input operand field and when anything changes in the password,
		// the code will process the entire input to ensure that it is valid or in error.
		setupTextWidget(text_Password, 10, 30, "Arial", 18, windowWidth-20, Pos.BASELINE_LEFT, 
				true);
		text_Password.textProperty().addListener((observable, oldValue, newValue) 
				-> {Model.updatePassword(); });
		
		// Establish an error message for when there is no input
		setupLabelWidget(noInputFound, 10, 80, "Arial", 14, windowWidth-10, Pos.BASELINE_LEFT);	
		noInputFound.setText("No input text found!");	// This changes based on input, but we 
		noInputFound.setTextFill(Color.RED);			// want this to be shown at startup
		
		// Establish an error message for the password, left aligned
		label_errPassword.setTextFill(Color.RED);
		label_errPassword.setAlignment(Pos.BASELINE_RIGHT);
		setupLabelWidget(label_errPassword, 22, 96, "Arial", 14, windowWidth-150-10, 
				Pos.BASELINE_LEFT);		
				
		// Error Message components for the Password
		errPasswordPart1.setFill(Color.BLACK);		// The user input is copied for this part
	    errPasswordPart1.setFont(Font.font("Arial", FontPosture.REGULAR, 18));
	    
	    errPasswordPart2.setFill(Color.RED);		// A red up arrow is added next
	    errPasswordPart2.setFont(Font.font("Arial", FontPosture.REGULAR, 24));
		
	    errPassword = new TextFlow(errPasswordPart1, errPasswordPart2);
		errPassword.setMinWidth(windowWidth-10);	// The two parts are merged into one
		errPassword.setLayoutX(22);					// and positioned directly below the text
		errPassword.setLayoutY(70);					// input field
		
		setupLabelWidget(errPasswordPart3, 20, 110, "Arial", 14, 200, Pos.BASELINE_LEFT);
											// Position the composition object on the window
		
		// Position the assessment display of the various requirements components
	    setupLabelWidget(label_Requirements, 10, 140, "Arial", 16, windowWidth-10, 
	    		Pos.BASELINE_LEFT);
	    
	    setupLabelWidget(label_UpperCase, 30, 180, "Arial", 14, windowWidth-10, 
				Pos.BASELINE_LEFT);

	    setupLabelWidget(label_LowerCase, 30, 210, "Arial", 14, windowWidth-10, 
				Pos.BASELINE_LEFT);
	    
	    setupLabelWidget(label_NumericDigit, 30, 240, "Arial", 14, windowWidth-10, 
				Pos.BASELINE_LEFT);
	    
	    setupLabelWidget(label_SpecialChar, 30, 270, "Arial", 14, windowWidth-10, 
				Pos.BASELINE_LEFT);
	    
	    setupLabelWidget(label_LongEnough, 30, 300, "Arial", 14, windowWidth-10, 
				Pos.BASELINE_LEFT);
	    
		resetAssessments();	// This method is use after each change to establish an initial state
		
		// Setup the valid Password message, which is used when all the requirements have been met
		validPassword.setTextFill(Color.GREEN);
		validPassword.setAlignment(Pos.BASELINE_RIGHT);
		setupLabelWidget(validPassword, 10, 340, "Arial", 18, windowWidth-150-10, 
				Pos.BASELINE_LEFT);
		
		// Setup the Finish and Save The Password button
		setupButtonWidget(button_Finish, "Finish and Save The Password", (windowWidth-250)/2,
				380, "Arial", 14, 250, Pos.BASELINE_CENTER);
		button_Finish.setDisable(true);						// It is initially disabled
		button_Finish.setOnAction(new EventHandler<>() {	// Specify the event handler to be
        	public void handle(ActionEvent event) {			// called when the button is pressed
        		Controller.handleButtonPress();
        	}
        });

		// Place the just-initialized GUI widgets into the pane, whether they have text or not
		// and or a fixed color
		theRoot.getChildren().addAll(label_Password, text_Password, noInputFound, 
				label_errPassword, errPassword, errPasswordPart3, validPassword,
				label_Requirements, label_UpperCase, label_LowerCase, label_NumericDigit,
				label_SpecialChar, label_LongEnough, button_Finish);
	}
	
	/*******
	 * <p> Title: View - Public Method the returns a reference to the View singleton object</p>
	 * 
	 * <p> Description: This method return a reference the to singleton View.  The design the this
	 * application ensures the singleton has already been created, so there is no need to check to 
	 * see if the object is there. This is in conflict with the general notion of singletons, where
	 * the code must first see if the object has been instantiated and if not, instantiate it.
	 * 
	 * @return The method returns a reference to the View singleton object.
	 *
	
	static public View getView() {
		return theView;
	}
	
	/*******
	 * <p> Title: resetAssessments - Used by all MVC components to reset widgets to a known state
	 * </p>
	 * 
	 * <p> Description: This method resets the five password requirement assessment to their 
	 * initial state of not satisfied.  During the evaluation process in the Model, the changes
	 * the user makes can change one or more of these to satisfied and to the color green.
	 * 
	 */
	
	static protected void resetAssessments() {
	    label_UpperCase.setText("At least one upper case letter - Not yet satisfied");
	    label_UpperCase.setTextFill(Color.RED);
	    
	    label_LowerCase.setText("At least one lower case letter - Not yet satisfied");
	    label_LowerCase.setTextFill(Color.RED);
	    
	    label_NumericDigit.setText("At least one numeric digit - Not yet satisfied");
	    label_NumericDigit.setTextFill(Color.RED);
	    
	    label_SpecialChar.setText("At least one special character - Not yet satisfied");
	    label_SpecialChar.setTextFill(Color.RED);
	    
	    label_LongEnough.setText("At least eight characters - Not yet satisfied");
	    label_LongEnough.setTextFill(Color.RED);
	}

	
	/*
	 * Private local method to initialize the standard attribute fields for a label
	 */
	static private void setupLabelWidget(Label l, double x, double y, String ff, double f, double w, 
			Pos p){
		l.setLayoutX(x);
		l.setLayoutY(y);		
		l.setFont(Font.font(ff, f));	// The font face and the font size
		l.setMinWidth(w);
		l.setAlignment(p);
	}

	
	/*
	 * Private local method to initialize the standard attribute fields for a text field
	 */
	static private void setupTextWidget(TextField t, double x, double y, String ff, double f, double w, 
			Pos p, boolean e){
		t.setFont(Font.font(ff, f));	// The font face and the font size
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}

	
	/*
	 * Private local method to initialize the standard fields for a button
	 */
	static private void setupButtonWidget(Button b, String s, double x, double y, String ff, double f, double w, 
			Pos p){
		b.setText(s);
		b.setFont(Font.font(ff, f));	// The font face and the font size
		b.setMinWidth(w);
		b.setMaxWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
}
