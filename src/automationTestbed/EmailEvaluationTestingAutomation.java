package automationTestbed;
import guiTools.EmailAddressRecognizer;

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
public class EmailEvaluationTestingAutomation {
	
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
		performTestCase(1, "user@example.com", true);
		performTestCase(2, "a@b.net", true);
		performTestCase(3, "alpha-bravo@example.com", true);
		
		// Expected to fail:
		//less than one character
		performTestCase(4, "@gmail.com", false);
		//more than 255 characters
		performTestCase(5, "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456@gmail.com", false);

		
		//Local part less than one character
		//performTestCase(6, "@gmail.com", false);
		//Domain part less than one character
		//performTestCase(7, "apples@.com", false);
		//Missing @ symbol between local and domain
		performTestCase(8, "applesgmail.com", false);
		//Local part includes non approved special character
		performTestCase(10, "apple$win@gmail.com", false);
		//back to back special character
		performTestCase(11, "apples&&apples@gmail.com", false);
		//special character begins local
		performTestCase(12, "-apples@gmail.com", false);
		//special character ends local
		performTestCase(13, "apples-@gmail.com", false);
		//Domain has valid TLD(.com, .net, .org, etc)
		performTestCase(14, "apples@gmail.sdh", false);
		
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
		String resultText= EmailAddressRecognizer.checkEmailAddress(inputText);
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The email <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The email <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The email <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The email <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
		displayEvaluation();
	}
	
	private static void displayEvaluation() {

		if (!EmailAddressRecognizer.tooShort)
			System.out.println("At least 1 character in length - Satisfied");
		else
			System.out.println("At least 1 character in length - Not Satisfied");		

		if (!EmailAddressRecognizer.tooLong)
			System.out.println("Less than 255 characters in length - Satisfied");
		else
			System.out.println("Less than 255 characters in length - Not Satisfied");
		
		if (!EmailAddressRecognizer.beginsNonAlphanumeric)
			System.out.println("Begins with alphanumeric character - Satisfied");
		else
			System.out.println("Begins with alphanumeric character - Not Satisfied");

		if (!EmailAddressRecognizer.missingAtSymbol)
			System.out.println("Contains one @ - Satisfied");
		else
			System.out.println("contains one @ - Not Satisfied");
	

		if (!EmailAddressRecognizer.invalidDomain)
			System.out.println("correct domain - Satisfied");
		else
			System.out.println("correct domain - Not Satisfied");

		if (!EmailAddressRecognizer.endsWithSymbol)
			System.out.println("Ends with non special character - Satisfied");
		else
			System.out.println("Ends with non special character - Not Satisfied");
	}
}
