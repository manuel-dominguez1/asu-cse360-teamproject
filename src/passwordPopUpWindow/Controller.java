package passwordPopUpWindow;

public class Controller {

/*******
 * <p> Title: Controller Class - Based on the current state and user input invoke the right action.
 * </p>
 *
 * <p> Description: This Controller class is a major component of a Model View Controller (MVC)
 * application design that provides the user with Graphical User Interface with JavaFX
 * widgets as opposed to a command line interface.
 * 
 * This is a purely static component of the MVC implementation.  There is no need to instantiate
 * the class.
 * 
 * In this case there is not much for the controller to do.  The user enters text into a text field
 * and presses a button.  The in the first case, the observer pattern is used to connect any change
 * in the input field directly to the code in the Model to evaluate the updated input without 
 * needing to involve the controller.  When the GUI's button is pressed, that event invokes the
 * handler in this class.
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 *
 * @version 2.00	2025-07-31 Rewrite of this application for the Fall 2025 offering of CSE 360
 * and other ASU courses.
 */

	/*-********************************************************************************************
	 * The View's protected static objects enable the controller to interact with the other MVC 
	 * objects used in this application.
	 * 
	 * In this case, the data the controller needs is stored in a GUI element and all the actions
	 * that require the invoking methods in the Model are done via the Observability Pattern, so 
	 * there is no need for the controller to access the Model.
	 */
	
	/*******
	 * <p> Title: handleButtonPress - Handle the user action of clicking on the GUI's button</p>
	 * 
	 * <p> Description: This method invoked when the user click on the GUI's button, commands the
	 * Model to increment its counter, and displays status to the console.  Access to this method
	 * is constrained to just those classes in the display package.
	 * 
	 * The design of the application requires that the only time the GUI button that invokes this
	 * method is enabled is when the input has been evaluated and there are no errors (i.e, all of
	 * the requirements have been satisfied.  That is why this method does not need to check to see
	 * that the input is valid.
	 * 
	 * Pressing the button causes the validated password to be made available for the rest of the
	 * application and it hides the password GUI window from the user so no more changes to the
	 * password can be made.</p>
	 */

	static protected void handleButtonPress() {
 		passwordEvaluationTestbedMain.PasswordEvaluationGUITestbed.theStage.hide();
		String thePassword = View.text_Password.getText();
		System.out.println("The password returned was: "+ thePassword);
    }
}
