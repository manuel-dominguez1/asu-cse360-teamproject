package automationTestbed;
import guiTools.UserNameRecognizer;

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
public class UsernameEvaluationTestingAutomation {
	
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
		performTestCase(1, "mo0-mo0", true);
		performTestCase(2, "S0ur-patch", true);
		performTestCase(3, "s0ftware94", true);
		
		// Expected to fail:
		//less than 4 characters
		performTestCase(4, "Moo", false);
		performTestCase(5, "Why", false);
		performTestCase(6, "Eat", false);
		performTestCase(7, "s33", false);

		//more than 32 characters
		performTestCase(8, "m23456789012345678901234567890123", false);

		//begins with non-alphabetical character
		performTestCase(9, "9oo-9oo", false);
		performTestCase(10, "90210", false);
		
		//back-to-back symbols
		performTestCase(11, "apples&&apples", false);
		performTestCase(12, "Bat-_man4", false);
		
		//Ends on a symbol
		performTestCase(13, "Batmann4-", false);
		
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
		String resultText= UserNameRecognizer.checkForValidUserName(inputText);
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The username <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The username <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The username <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The username <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
		displayEvaluation();
	}
	
	private static void displayEvaluation() {
		
		if (!UserNameRecognizer.tooShort)
			System.out.println("At least four characters in length - Satisfied");
		else
			System.out.println("At least four characters in length - Not Satisfied");

		if (!UserNameRecognizer.tooLong)
			System.out.println("At most 32 characters in length - Satisfied");
		else
			System.out.println("At most 32 characters in length - Not Satisfied");
	

		if (!UserNameRecognizer.nonAlphabetStart)
			System.out.println("Begins with an alphabetical character - Satisfied");
		else
			System.out.println("Begins with an alphabetical character - Not Satisfied");

		if (!UserNameRecognizer.unknownChar)
			System.out.println("no unknown characters - Satisfied");
		else
			System.out.println("no unknown characters - Not Satisfied");

		if (!UserNameRecognizer.symbolErr)
			System.out.println("Does not end in a symbol, nor contains a symbol followed by symbol - Satisfied");
		else
			System.out.println("Does not end in a symbol, nor contains a symbol followed by symbol - Not Satisfied");
	}
}
