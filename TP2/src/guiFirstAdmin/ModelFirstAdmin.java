package guiFirstAdmin;

import guiTools.UserNameRecognizer;
import guiTools.passwordRecognizer;

/*******
 * <p> Title: ModelFirstAdmin Class. </p>
 * 
 * <p> Description: The First System Startup Page Model.  This class stores methods for validating the username
 * and password of the first user to login.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 *  
 */

public class ModelFirstAdmin {
	
	/*****
     * <p> Method: ModelFirstAdmin() </p>
     * 
     * <p> Description: This default constructor is not used in this system. </p>
     */
	public ModelFirstAdmin() {
	}

	/**********
	 * <p> Method: public usernameIsValid(String s) </p>
	 * 
	 * <p> Description: This method is called when the first user has clicked on the Login button. This
	 * method checks the username to see if it is valid.
	 * 
	 * @param s specifies the user's username
	 * 
	 * @return an error message to the user if the username is invalid, otherwise return nothing
	 * 
	 */	
	public static String usernameIsValid(String s) {
		return UserNameRecognizer.checkForValidUserName(s);
	}
	
	/**********
	 * <p> Method: public passwordIsValid(String s) </p>
	 * 
	 * <p> Description: This method is called when the first user has clicked on the Login button. This
	 * method checks the password to see if it is valid.
	 * 
	 * @param s specifies the user's password
	 * 
	 * @return an error message to the user if the password is invalid, otherwise return nothing
	 * 
	 */	
	public static String passwordIsValid(String s) {
		return passwordRecognizer.checkForValidPassword(s);
	}

}
