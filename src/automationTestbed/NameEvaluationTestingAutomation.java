package automationTestbed;
import guiTools.nameRecognizer;

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
		performTestCase(1, "Tom", true);
		performTestCase(2, "Xa", true);
		performTestCase(3, "Superfluvolous", true);
		
		// Expected to fail:
		//empty input
		performTestCase(4, "", false);
		//non-alphabetical characters
		performTestCase(5, "M001", false);
		//over 33 characters in length
		performTestCase(6, "ThisisaverylongNamethatIdidnotbothertocount", false);
		
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
		String resultText= nameRecognizer.checkForValidName(inputText);
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The name <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The name <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The name <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The name <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
		displayEvaluation();
	}
	
	private static void displayEvaluation() {
		
		if (nameRecognizer.isEmpty)
			System.out.println("At least one letter - Satisfied");
		else
			System.out.println("At least one letter - Not Satisfied");

		if (!nameRecognizer.unknownChar)
			System.out.println("only approved special characters - Satisfied");
		else
			System.out.println("only approved special characters - Not Satisfied");
	
		if (nameRecognizer.endsInSymbol)
			System.out.println("does not end with a symbol - Satisfied");
		else
			System.out.println("does not end with a symbol - Not Satisfied");
	

		if (!nameRecognizer.tooLong)
			System.out.println("less than 33 characters - Satisfied");
		else
			System.out.println("less than 33 characters - Not Satisfied");

	}
}
