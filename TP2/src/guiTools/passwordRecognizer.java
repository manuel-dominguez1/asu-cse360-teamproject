package guiTools;


import java.util.Set;


/**
 * <p> Title: FSM-translated PasswordRecognizer. </p>
 * 
 * <p> Description: A demonstration of the mechanical translation of Finite State Machine 
 * diagram into an executable Java program using the Password Recognizer. The code 
 * detailed design is based on a while loop with a select list</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2024 </p>
 * 
 * @author Lynn Robert Carter, Sierra Trimmer
 * 
 * @version 1.00		2024-09-13	Initial baseline derived from the Even Recognizer
 * @version 1.01		2024-09-17	Correction to address UNChar coding error, improper error
 * 									message, and improve internal documentation
 * @version 4.00        2026-09-29  Rewritten for implementation into TP1
 */
public class passwordRecognizer {
	
	/*****
     * <p> Method: passwordRecognizer() </p>
     * 
     * <p> Description: This default constructor is not used in this system. </p>
     */
	public passwordRecognizer() {
	}
	
	/**********************************************************************************************
	 * 
	 * Result attributes to be used for GUI applications where a detailed error message and a 
	 * pointer to the character of the error will enhance the user experience.
	 * 
	 */
	
	// Define which special characters are valid
	private static final Set<Character> SPECIAL_CHARS = Set.of('~', '`', '!', '@', '#',  '$', '%', '^', '&', '*', '(', ')', '-', '_', '+', '{', '}', '[', ']', '|', ':', ';', ',', '.', '/', '?');
	
	/**
	 * Error message to be displayed to user.
	 */
	public static String passwordRecognizerErrorMessage = "";

	/**
	 * Indicates if the input contains an uppercase character
	 */
	public static boolean hasUpper = false;
	
	/**
	 * Indicates if the input contains an lowercase character
	 */
	public static boolean hasLower = false;
	
	/**
	 * Indicates if the input contains a numeric character
	 */
	public static boolean hasDigit = false;
	
	/**
	 * Indicates if the input contains a valid special character
	 */
	public static boolean hasSpecialChar = false;
	
	/**
	 * Indicates if the input meets length requirements
	 */
	public static boolean tooShort = false;
	
	/**
	 * Indicates if the input exceeds length requirements
	 */
	public static boolean tooLong = false;
	
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
			passwordRecognizerErrorMessage = "\n*** ERROR *** Password too short, must exceed 8 characters";
			tooShort = true;
			return passwordRecognizerErrorMessage;
		}
		if (input.length() > 33) {
			passwordRecognizerErrorMessage = "\n*** ERROR *** Password too long, must not exceed 33 characters";
			tooLong = true;
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
