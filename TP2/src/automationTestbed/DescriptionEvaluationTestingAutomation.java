package automationTestbed;
import guiTools.DescriptionRecognizer;

/*******
 * <p> Title: DescriptionEvaluationTestingAutomation </p>
 * 
 * <p> Description: Java semi-automated tests </p>
 * 
 * 
 * 
 * @author Manuel A Dominguez
 * @author Sierra Trimmer
 * 
 * @version 0.50	2026-09-20 A simple set of semi-automated test cases in system output
 * @version 1.00	2026-10-06 Modified for title validation
 * 
 * 
 */
public class DescriptionEvaluationTestingAutomation {
	
	/*****
     * <p> Method: TitleEvaluationTestingAutomation() </p>
     * 
     * <p> Description: This default constructor is not used in this system. </p>
     */
	public DescriptionEvaluationTestingAutomation() {
	} 
	
	
	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests
	
	
	/*******
	 * <p> Title: DescriptionEvaluationTestingAutomation main method</p>
	 * 
	 * <p> Description: This main line displays a header to the console, performs a sequence of
	 * test cases, and then displays a footer with a summary of the results.</p>
	 * 
	 * @param args The array of command lines parameters.
	 */
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\nTesting Automation");

		/************** Start of the test cases **************/
		
		// Expected to pass:
		performTestCase(1, "This is an example description.", true);
		performTestCase(2, "example", true);
		performTestCase(3, "a lesson-example of 1234. ", true);
		
		// Expected to fail:
		// empty
		performTestCase(4, "", false);
		// non-alphabetic start char
		performTestCase(5, ".", false);
		performTestCase(6, "1234", false);
		// more than 500 characters
		performTestCase(7, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
				+ "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
				+ "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
				+ "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
				+ "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
				+ "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", false);
		// contains invalid character
		performTestCase(8, "$Lesson 1 description$", false);
		
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
		String resultText= DescriptionRecognizer.checkForValidDescription(inputText);
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The description <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The description <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The description <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The description <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
		displayEvaluation();
	}
	
	private static void displayEvaluation() {
		
		
		if (!DescriptionRecognizer.tooLong)
			System.out.println("At most 500 characters in length - Satisfied");
		else
			System.out.println("At most 500 characters in length - Not Satisfied");
	

		if (!DescriptionRecognizer.nonAlphabetStart)
			System.out.println("Begins with an alphabetical character - Not Satisfied");
		else
			System.out.println("Begins with an alphabetical character - Satisfied");

		if (!DescriptionRecognizer.unknownChar)
			System.out.println("no unknown characters - Satisfied");
		else
			System.out.println("no unknown characters - Not Satisfied");
	}
		
}
