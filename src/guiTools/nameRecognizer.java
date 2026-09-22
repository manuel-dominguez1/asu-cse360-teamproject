package guiTools;

public class nameRecognizer {
	/**
	 * <p> Title: FSM-translated NameRecognizer. </p>
	 *
	 * <p> Description: </p>
	 * 
	 * <p> copyright:  </p>
	 * 
	 * @author Sam Schafer
	 * 
	 * @version 1.00
	 * 
	 */
	
	
	public static String nameRecognizerErrorMessage = "";		//The error message text
	public static String nameRecognizerInput = "";				// The input being processed
	public static int nameRecognizerIndexofError = -1;			// The index of error location
	private static int state = 0;								// The current state value
	private static int nextState = 0;							// The next state value
	private static boolean finalState = false;					// Is this state a final state?
	private static String inputLine = "";						// The input line
	private static char currentChar;							// The current character in the line
	private static int currentCharNdx;							// The index of the current character
	private static boolean running;								// The flag that specifies if the FSM is running
	private static int nameSize = 0;							// A numeric value may not exceed 32 characters
	
	public static boolean isEmpty = false; 						//boolean for JUnit tests
	public static boolean endsInSymbol = false; 						//boolean for JUnit tests
	public static boolean unknownChar = false; 						//boolean for JUnit tests
	public static boolean tooLong = false; 						//boolean for JUnit tests
	
	// Private method to display debugging data
		private static void displayDebuggingInfo() {
			// Display the current state of the FSM as part of an execution trace
			if (currentCharNdx >= inputLine.length())
				// display the line with the current state numbers aligned
				System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
						((finalState) ? "       F   " : "           ") + "None");
			else
				System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
					((finalState) ? "       F   " : "           ") + "  " + currentChar + " " + 
					((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + 
					nextState + "     " + nameSize);
		}
		
		// Private method to move to the next character within the limits of the input line
		private static void moveToNextCharacter() {
			currentCharNdx++;
			if (currentCharNdx < inputLine.length())
				currentChar = inputLine.charAt(currentCharNdx);
			else {
				currentChar = ' ';
				running = false;
			}
		}
	
	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
	 * method.
	 * 
	 * @param input		The input string for the Finite State Machine
	 * @return			An output string that is empty if every things is okay or it is a String
	 * 					with a helpful description of the error
	 */
	
	public static String checkForValidName(String input) {
		// Check to ensure that there is input to process
		if(input.length() <= 0) {
			nameRecognizerErrorMessage = "\n*** ERROR *** Name cannot be empty";
			isEmpty = true;
			return nameRecognizerErrorMessage;
		}
		
		
		// The local variables used to perform the Finite State Machine simulation
		state = 0;							// This is the FSM state number
		inputLine = input;					// Save the reference to the input line as a global
		currentCharNdx = 0;					// The index of the current character
		currentChar = input.charAt(0);		// The current character from above indexed position
				
		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state

		nameRecognizerInput = input;	// Save a copy of the input
		running = true;						// Start the loop
		nextState = -1;						// There is no next state
		System.out.println("\nCurrent Final Input  Next\nState   State Char  State  Size");
				
		// This is the place where semantic actions for a transition to the initial state occur
		
		nameSize = 0;					// Initialize the name size

		// The Finite State Machines continues until the end of the input is reached or at some 
		// state the current character does not match any valid transition to a next state
		while (running) {
			// The switch statement takes the execution to the code for the current state, where
			// that code sees whether or not the current character is valid to transition to a
			// next state
			switch (state) {
			case 0: 
				// State 0 has 1 valid transition that is addressed by an if statement.
				
				// The current character is checked against A-Z, a-z. If any are matched
				// the FSM goes to state 1
				
				// A-Z, a-z -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z') ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z')) {	// Check for a-z	
					nextState = 1;
					
					// Count the character 
					nameSize++;
					
					// This only occurs once, so there is no need to check for the size getting
					// too large.
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;
				
				// The execution of this state is finished
				break;
			
			case 1: 
				// State 1 has two valid transitions, 
				//	1: a A-Z, a-z that transitions back to state 1
				//  2: a symbol (-,') that transitions to state 2
				
				// A-Z, a-z -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' )){	// Check for a-z
						
					nextState = 1;
					
					// Count the character
					nameSize++;
				}
				// - or ' -> State 2
				if ((currentChar == '-') || 	// check for '-'
						currentChar >= '\'') {	// check for '''
					
					nextState = 2;
				}
				//if it is non of those characters the FSM halts
				else 
					running = false;
				
				// The execution of this state is finished
				// If the size is larger than 32, the loop must stop
				if (nameSize > 32)
					running = false;
				break;
				
			case 2:
				// State 2 has 1 valid transition that is addressed by an if statement.
				
				// The current character is checked against A-Z, a-z. If any are matched
				// the FSM goes to state 1
				
				// A-Z, a-z -> State 1
				if ((currentChar >= 'A' && currentChar <= 'Z') ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z')) {	// Check for a-z	
					nextState = 1;
					
					// Count the character 
					nameSize++;
					
				}
				// If it is none of those characters, the FSM halts
				else 
					running = false;
				
				// The execution of this state is finished
				// If the size is larger than 32, the loop must stop
				if (nameSize > 32)
					running = false;
				break;
			}
			
			if (running) {
				displayDebuggingInfo();
				// When the processing of a state has finished, the FSM proceeds to the next
				// character in the input and if there is one, it fetches that character and
				// updates the currentChar.  If there is no next character the currentChar is
				// set to a blank.
				moveToNextCharacter();

				// Move to the next state
				state = nextState;
				
				// Is the new state a final state?  If so, signal this fact.
				if (state == 1) finalState = true;

				// Ensure that one of the cases sets this to a valid value
				nextState = -1;
			}
			// Should the FSM get here, the loop starts again
		}
		displayDebuggingInfo();
		
		System.out.println("The loop has ended.");
		
		// When the FSM halts, we must determine if the situation is an error or not.  That depends
		// of the current state of the FSM and whether or not the whole string has been consumed.
		// This switch directs the execution to separate code for each of the FSM states and that
		// makes it possible for this code to display a very specific error message to improve the
		// user experience.
		nameRecognizerIndexofError = currentCharNdx;	// Set index of a possible error;
		nameRecognizerErrorMessage = "\n*** ERROR *** ";
		
		// The following code is a slight variation to support just console output.
		switch (state) {
		case 0:
			nameRecognizerErrorMessage += "A name must start with a letter A-Z or a-z.\n";
			unknownChar = true;
			return nameRecognizerErrorMessage;
			
		case 1:
			// State 1 is a final state.  Check to see if the UserName length is valid.  If so we
			// we must ensure the whole string has been consumed.

			if (nameSize > 32) {
				// name is too long
				nameRecognizerErrorMessage += 
						"A name must have no more than 32 characters.\n";
				tooLong = true;
				return nameRecognizerErrorMessage;
			}
			else if (currentCharNdx < input.length()) {
				// There are characters remaining in the input, so the input is not valid
				nameRecognizerErrorMessage += 
					"A name may only contain non consecutive hyphens and apostrophes, or the characters A-Z, a-z.\n";
				unknownChar = true;
				return nameRecognizerErrorMessage;
			}
			else {
				// UserName is valid
				nameRecognizerIndexofError = -1;
				nameRecognizerErrorMessage = "";
				return nameRecognizerErrorMessage;
			}
			
		case 2:
			if (nameSize > 32) {
				// name is too long
				nameRecognizerErrorMessage += 
						"A name must have no more than 32 characters.\n";
				tooLong = true;
				return nameRecognizerErrorMessage;
			}
			else if ((currentChar == '-') ||
				currentChar == '\'') {
				nameRecognizerErrorMessage +=
						"A name must not contain consecutive hyphens or apostrophes";
				unknownChar = true;
				return nameRecognizerErrorMessage;
			}
			else if (!(currentChar >= 'A' && currentChar <= 'Z') ||	
					!(currentChar >= 'a' && currentChar <= 'z')) {
				nameRecognizerErrorMessage +=
						"The last character of a name must be a letter";
				endsInSymbol = true;
				return nameRecognizerErrorMessage;
			}
		default:
			// This is for the case where we have a state that is outside of the valid range.
			// This should not happen
			return "";
		}
	}
}