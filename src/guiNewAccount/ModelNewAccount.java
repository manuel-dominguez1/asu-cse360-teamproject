package guiNewAccount;

import guiTools.UserNameRecognizer;
import guiTools.passwordRecognizer;

/*******
 * <p> Title: ModelNewAccount Class. </p>
 * 
 * <p> Description: The NewAccount Page Model.  This class is not used as there is no
 * data manipulated by this MVC beyond accepting role information and saving it in the
 * database.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 *  
 */
public class ModelNewAccount {
	public static String usernameIsValid(String username) {
		return UserNameRecognizer.checkForValidUserName(username);
	}
	
	public static String passwordIsValid(String password) {
		return passwordRecognizer.checkForValidPassword(password);
	}
}
