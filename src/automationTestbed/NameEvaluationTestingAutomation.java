package automationTestbed;
import guiTools.passwordRecognizer;

/*******
 * <p> Title: UsernameEvaluationTestingAutomation </p>
 * 
 * <p> Description: Java semi-automated tests </p>
 * 
 * 
 * 
 * @author Manuel A Dominguez
 * 
 * @version 0.50	2026-09-20 A simple set of semi-automated test cases in system output
 * 
 * 
 */
public class NameEvaluationTestingAutomation {
	
	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests
	/*
	 * This main line displays a header to the console, performs a sequence of
	 * test cases, and then displays a footer with a summary of the results
	 */
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\nTesting Automation");

		/************** Start of the test cases **************/
		
		// Expected to pass:
		performTestCase(1, "Password_123", true);
		
		// Expected to fail:
		//less than 8 characters
		performTestCase(2, "Mouser3", false);
		performTestCase(3, "n0-Time", false);
		performTestCase(4, "hat_Tw2", false);
		//Missing Upper case
		performTestCase(5, "pass-word2", false);
		performTestCase(6, "p@rty-time1", false);
		//Missing Lower case
		performTestCase(7, "RUNNING-MAN2", false);
		performTestCase(8, "P@RTYTIME1", false);
		//Missing numerical digit
		performTestCase(9, "Pass-word", false);
		performTestCase(10, "Party-times", false);

		//Missing special character
		performTestCase(11, "Password123", false);
		performTestCase(12, "Partytime123", false);
		
		// This is an improperly written negative test, because the password
		// is valid, but the second parameter asserts that it is not valid
//		performTestCase(3, "Aa!15678", false);
		
		// These are improperly written positive test, because the password 
		// is not valid, but the second parameter asserts that it is valid
//		performTestCase(4, "A!", true);
//		performTestCase(5, "", true);
		// Add more test cases here
		
		/************** End of the test cases **************/
		
		/************** Test cases semi-automation report footer **************/
		System.out.println("____________________________________________________________________________");
		System.out.println();
		System.out.println("Number of tests passed: "+ numPassed);
		System.out.println("Number of tests failed: "+ numFailed);
	}
	
	/*
	 * This method sets up the input value for the test from the input parameters,
	 * displays test execution information, invokes precisely the same recognizer
	 * that the interactive JavaFX main line uses, interprets the returned value,
	 * and displays the interpreted result.
	 */
	
	private static void performTestCase(int testCase, String inputText, boolean expectedPass) {
				
		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		System.out.println("\nFinite state machine execution trace:");
		
		/************** Call the recognizer to process the input **************/
		String resultText= passwordRecognizer.checkForValidPassword(inputText);
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The password <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The password <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
		displayEvaluation();
	}
	
	private static void displayEvaluation() {
		
		if (passwordRecognizer.hasUpper)
			System.out.println("At least one upper case letter - Satisfied");
		else
			System.out.println("At least one upper case letter - Not Satisfied");

		if (passwordRecognizer.hasLower)
			System.out.println("At least one lower case letter - Satisfied");
		else
			System.out.println("At least one lower case letter - Not Satisfied");
	

		if (passwordRecognizer.hasDigit)
			System.out.println("At least one digit - Satisfied");
		else
			System.out.println("At least one digit - Not Satisfied");

		if (passwordRecognizer.hasSpecialChar)
			System.out.println("At least one special character - Satisfied");
		else
			System.out.println("At least one special character - Not Satisfied");

		if (!passwordRecognizer.tooShort)
			System.out.println("At least 9 characters - Satisfied");
		else
			System.out.println("At least 9 characters - Not Satisfied");

		if (!passwordRecognizer.tooLong)
			System.out.println("At most 33 characters - Satisfied");
		else
			System.out.println("At most 33 characters - Not Satisfied");
	}
}
