package entityClasses;

import guiTools.TitleRecognizer;
import guiTools.DescriptionRecognizer;


/*******
 * <p> Title: Lesson Class </p>
 * 
 * <p> Description: The Lesson class represents one persistent lesson object learned. </p>
 *
 */ 

public class LessonsLearned {
	
	// These are the private attributes for this entity object
	
    private int id;
    private String title;
    private String description;
    private String author;
    
    
    /*****
     * <p> Method: Lesson () </p>
     * 
     * <p> Description: This default constructor is not used in this system. </p>
     */
    public LessonsLearned() {
    	
    }

    
    /*****
     * <p> Method: Lesson(int id, String title, String dsc, String author) </p>
     * 
     * <p> Description: This constructor is used to establish lesson objects. </p>
     * 
     * @param id specifies the id number for the lesson
     * 
     * @param title specifies the title of the lesson
     * 
     * @param dsc specifies the description for the lesson
     * 
     * @param author specifies which user created the lesson, identified by username
     * 
     */
    public LessonsLearned(int id, String title, String dsc, String author) {
        this.id = id;
        this.title = title;
        this.description = dsc;
        this.author = author;
    }
    
    /*****
     * <p> Method: Lesson(String title, String dsc, String author) </p>
     * 
     * <p> Description: This constructor is used to establish lesson objects
     * missing an id field. </p>
     * 
     * @param title specifies the title of the lesson
     * 
     * @param dsc specifies the description for the lesson
     * 
     * @param author specifies which user created the lesson, identified by username
     * 
     */
    public LessonsLearned(String title, String dsc, String author) {
        this.title = title;
        this.description = dsc;
        this.author = author;
    }

	/*****
     * <p> Method: boolean isValidTitle(String title) </p>
     * 
     * <p> Description: This method verifies that the desired title is valid
     * before the lesson is saved to the database. </p>
     * 
     * @param title specifies the title of the lesson
     * 
     * @return true if valid, otherwise false
     * 
     */
    public static boolean isValidTitle(String title) {
    	String invalidTitleMessage = TitleRecognizer.checkForValidTitle(title);
		if (!invalidTitleMessage.isEmpty()) {
			return true;
		} else {
			return false;
		}
    }

    /*****
     * <p> Method: boolean isValidDescription(String dsc) </p>
     * 
     * <p> Description: This method verifies that the desired description is valid
     * before the lesson is saved to the database. </p>
     * 
     * @param title specifies the title of the lesson
     * 
     * @return true if valid, otherwise false
     * 
     */
    public static boolean isValidDescription(String dsc) {
    	String invalidDescriptionMessage = DescriptionRecognizer.checkForValidDescription(dsc);
		if (!invalidDescriptionMessage.isEmpty()) {
			return true;
		} else {
			return false;
		}
    }

    /*****
     * <p> Method: int getId() </p>
     * 
     * <p> Description: This getter returns the Id. </p>
     * 
     * @return an int of the Id
     * 
     */
    public int getId() { return id; }
    
    /*****
     * <p> Method: String getTitle() </p>
     * 
     * <p> Description: This getter returns the Title. </p>
     * 
     * @return a String of the Title
     * 
     */
    public String getTitle() { return title; }

    
    /*****
     * <p> Method: String getDescription() </p>
     * 
     * <p> Description: This getter returns the Description. </p>
     * 
     * @return a String of the Description
	 *
     */
    public String getDescription() { return description; }

    
    /*****
     * <p> Method: String getAuthor() </p>
     * 
     * <p> Description: This getter returns the Author. </p>
     * 
     * @return a String of the Author
	 *
     */
    public String getAuthor() { return author; }

    /*****
     * <p> Method: void setTitle(String s) </p>
     * 
     * <p> Description: This setter defines the title attribute. </p>
     * 
     * @param s is a string that specifies the lesson's title
	 *
     */
    public void setTitle(String s) { title = s; }
    
    /*****
     * <p> Method: void setDescription(String s) </p>
     * 
     * <p> Description: This setter defines the description attribute. </p>
     * 
     * @param s is a string that specifies the lesson's description
	 *
     */
    public void setDescription(String s) { description = s; }

	
	/*****
     * <p> Method: boolean createLesson(String title, String description, User currentUser) </p>
     * 
     * <p> Description: This method creates a new LessonsLearned object. </p>
     * 
     * @param title is a string that specifies the lesson's title
     * 
     * @param description is a string that specifies the lesson's description
     * 
     * @param author is a string representing the user creating the lesson, matches the user's username
     * 
     * @return true if creation is successful, otherwise false
	 *
     */
    public static boolean createLesson(String title, String description, String author) {
   
    	// If the title or description is invalid
    	if(!isValidTitle(title) || !isValidDescription(description)) {
    		return false;
    	}
    	
    	// Create lesson object and save it to the List
    	LessonsLearned newLesson = new LessonsLearned(title.trim(), description.trim(), author);
		LessonsLearnedList.add(newLesson);
		
		return true;
    }
    
    /*****
     * <p> Method: boolean deleteLesson(int id) </p>
     * 
     * <p> Description: This method deletes an existing LessonsLearned object. </p>
     * 
     * @param id is the id of the lesson to delete
     * 
     * @return true is deletion is successful, otherwise false
	 *
     */
    public static boolean deleteLesson(int id) {
    	// Delete the lesson in the List
    	return LessonsLearnedList.delete(id);
    }
    
    /*****
     * <p> Method: boolean updateLesson(String title, String description, User currentUser) </p>
     * 
     * <p> Description: This method updates an existing LessonsLearned object. </p>
     * 
     * @param title is a string that specifies the lesson's title
     * 
     * @param description is a string that specifies the lesson's description
     * 
     * @param currentUser is the User entity that is currently logged in and creating the lesson
	 *
     */
    public static boolean updateLesson(int id, String newTitle, String newDescription, User currentUser) { 
    	LessonsLearned lesson = LessonsLearnedList.get(id);
    	// If the lesson doesn't exist
    	if (lesson == null) {
    		return false;
    	}
    	
    	// If the title or description modification is invalid
    	if(!isValidTitle(newTitle) || !isValidDescription(newDescription)) {
    		return false;
    	}	
    	// Set the new attributes
    	lesson.setTitle(newTitle.trim());
    	lesson.setDescription(newDescription.trim());
    	
    	// Update lesson in the List
    	return LessonsLearnedList.update(lesson);
	}
    
}
