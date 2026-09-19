package passwordRecognizer;


import java.util.Set;


public class passwordRecognizer {
	/**
	 * <p> Title: FSM-translated PasswordRecognizer. </p>
	 * 
	 * <p> Description: A demonstration of the mechanical translation of Finite State Machine 
	 * diagram into an executable Java program using the Password Recognizer. The code 
	 * detailed design is based on a while loop with a select list</p>
	 * 
	 * <p> Copyright: Lynn Robert Carter © 2024 </p>
	 * 
	 * @author Lynn Robert Carter
	 * 
	 * @version 1.00		2024-09-13	Initial baseline derived from the Even Recognizer
	 * @version 1.01		2024-09-17	Correction to address UNChar coding error, improper error
	 * 									message, and improve internal documentation
	 * 
	 */

	/**********************************************************************************************
	 * 
	 * Result attributes to be used for GUI applications where a detailed error message and a 
	 * pointer to the character of the error will enhance the user experience.
	 * 
	 */
	
	// State transition labels
	private enum State {
		START,
		UPPERCASE_FOUND,
		LOWERCASE_FOUND,
		DIGIT_FOUND,
		VALID
	}
	
	// Define which special characters are valid
	private static final Set<Character> SPECIAL_CHARS = Set.of('!', '@', '$', '&', '?', '.');
	
	public static String passwordRecognizerErrorMessage = "";		//The error message text

	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for the Finite State Machine
	 * @return			An output string that is empty if every things is okay or it is a String
	 * 						with a helpful description of the error
	 */
	public static String checkForValidPassword(String input) {
		// Check to ensure that there is input to process
		if(input.length() <= 0) {
			passwordRecognizerErrorMessage = "\n*** ERROR *** Password cannot be empty";
			return passwordRecognizerErrorMessage;
		}
		
		//Check to ensure that the input meets length requirements;
		if (input.length() < 8) {
			passwordRecognizerErrorMessage = "\n*** ERROR *** Password too short, must exceed 7 characters";
			return passwordRecognizerErrorMessage;
		}
		if (input.length() > 33) {
			passwordRecognizerErrorMessage = "\n*** ERROR *** Password too long, must not exceed 33 characters";
			return passwordRecognizerErrorMessage;
		}
		
		
		// Attributes to be tracked
		boolean hasUpper = false;
		boolean hasLower = false;
		boolean hasDigit = false;
		boolean hasSpecialChar = false;
		
		
		
		// Process the input character by character
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			
			if (Character.isUpperCase(ch)) {
				hasUpper = true;
			}
			
			if (Character.isLowerCase(ch)) {
				hasLower = true;
			}
			
			if (Character.isDigit(ch)) {
				hasDigit = true;
			}
			
			if (SPECIAL_CHARS.contains(ch)) {
				hasSpecialChar = true;
			}
		}
			
		//State Transition Logic
		if (!hasUpper) {
			passwordRecognizerErrorMessage = "\n*** ERROR *** Password must contain an uppercase letter";
			return passwordRecognizerErrorMessage;
		}
			
		if (!hasLower) {
			passwordRecognizerErrorMessage = "\n*** ERROR *** Password must contain a lowercase letter";
			return passwordRecognizerErrorMessage;
		}
				
		if (!hasDigit) {
			passwordRecognizerErrorMessage = "\n*** ERROR *** Password must contain a numeric digit";
			return passwordRecognizerErrorMessage;
		}
									
		if (!hasSpecialChar) {
			passwordRecognizerErrorMessage = "\n*** ERROR *** Password must contain a special character (!,@,$,&,?,.)";
			return passwordRecognizerErrorMessage;
		}
		
		return "";
		
	}
	
}
