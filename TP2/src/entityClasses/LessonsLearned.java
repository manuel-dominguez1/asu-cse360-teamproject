package entityClasses;

import guiTools.TitleRecognizer;
import guiTools.DescriptionRecognizer;
import database.Database;


/*******
 * <p> Title: LessonsLearned Class </p>
 * 
 * <p> Description: The LessonsLearned class represents a single lesson learned,
 * stored persistently in the database. It provides validation for lesson titles
 * and descriptions, along with static methods to create, delete, and update
 * lessons in the database. </p>
 * 
 * <p> This class implements the Create, Update, and Delete operations required by
 * Contributor Story 1 (add lessons with core information and manage them). The id and
 * author attributes support Contributor Stories 2 and 4 (list and examine only the
 * lessons I have added). Read operations on collections of lessons are provided by
 * LessonsLearnedList. </p>
 * 
 * <p> Planned attributes: The Curator lock epic (TP3) will require a locked flag for the
 * whole lesson and per-field locks, so Contributors cannot edit locked fields or delete a
 * locked lesson (Contributor Stories 1-2). Contributor Story 3 and the Curator time and
 * effort epics will require experience data (what was done, how it was done, time taken,
 * and effort by team member), stored as a separate list per lesson since one lesson may
 * have many attempts. </p>
 * 
 * @author Sierra Trimmer
 * 
 * @version 1.0
 * 
 */ 

public class LessonsLearned {
	
	/** Unique id assigned by the database. Source: Contributor Story 2 (select a lesson to
	 *  examine). Rationale: titles may repeat, so update and delete need a key that
	 *  identifies exactly one lesson. */
    private int id;

	/** Short title of the lesson. Source: Contributor Story 1 (core information) and
	 *  Story 2 (list used to pick a lesson). Rationale: identifies the lesson in lists and
	 *  searches. Validated by TitleRecognizer. */
    private String title;

	/** Full text of the lesson learned. Source: Contributor Story 1 (core information).
	 *  Rationale: holds the actual content of the lesson. Validated by DescriptionRecognizer. */
    private String description;

	/** Username of the Contributor who created the lesson. Source: Contributor Story 2 (list
	 *  of lessons I have added) and Story 4 (only see my own content). Rationale: lets the
	 *  system filter lessons by author. */
    private String author;
    
    
    /*****
     * <p> Method: LessonsLearned() </p>
     * 
     * <p> Description: This default constructor is not used in this system. </p>
     */
    public LessonsLearned() {
    	
    }

    
    /*****
     * <p> Method: LessonsLearned(int id, String title, String dsc, String author) </p>
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
     * <p> Method: LessonsLearned(String title, String dsc, String author) </p>
     * 
     * <p> Description: This constructor is used to establish lesson objects
     * that do not yet have an id, such as new lessons before they are saved
     * to the database. </p>
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
			return false;
		} else {
			return true;
		}
    }

    /*****
     * <p> Method: boolean isValidDescription(String dsc) </p>
     * 
     * <p> Description: This method verifies that the desired description is valid
     * before the lesson is saved to the database. </p>
     * 
     * @param dsc specifies the description of the lesson
     * 
     * @return true if valid, otherwise false
     * 
     */
    public static boolean isValidDescription(String dsc) {
    	String invalidDescriptionMessage = DescriptionRecognizer.checkForValidDescription(dsc);
		if (!invalidDescriptionMessage.isEmpty()) {
			return false;
		} else {
			return true;
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
     * @return a String of the Author's username
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
     * <p> Method: boolean createLesson(String title, String description, String author, Database db) </p>
     * 
     * <p> Description: This method validates the title and description, then
     * creates a new LessonsLearned object and saves it to the database. Leading
     * and trailing whitespace is trimmed from the title and description. </p>
     * 
     * @param title is a string that specifies the lesson's title
     * 
     * @param description is a string that specifies the lesson's description
     * 
     * @param author is a string representing the user creating the lesson, matches the user's username
     * 
     * @param db is the database where lessons are stored
     * 
     * @return true if creation is successful, otherwise false
	 *
     */
    public static boolean createLesson(String title, String description, String author, Database db) {
   
    	// Contributor Story 1: core information must be valid before a lesson is added
    	if(!isValidTitle(title) || !isValidDescription(description)) {
    		return false;
    	}
    	
    	// Create the lesson object from the trimmed core information
    	LessonsLearned lesson = new LessonsLearned(title.trim(), description.trim(), author);
		
    	// Save to the database so the Contributor can see and manage it later (Story 1)
		return db.register(lesson);
    }
    
    /*****
     * <p> Method: boolean deleteLesson(int id, Database db) </p>
     * 
     * <p> Description: This method deletes an existing LessonsLearned object
     * from the database, identified by its id. </p>
     * 
     * @param id is the id of the lesson to delete
     * 
     * @param db is the database where lessons are stored
     * 
     * @return true if deletion is successful, otherwise false
	 *
     */
    public static boolean deleteLesson(int id, Database db) {
    	// Contributor Story 1: a Contributor can manage (delete) the lessons they added
    	return db.deleteLesson(id);
    	
    }
    
    /*****
     * <p> Method: boolean deleteLesson(String title, Database db) </p>
     * 
     * <p> Description: This method deletes an existing LessonsLearned object
     * from the database, identified by its title. </p>
     * 
     * @param title is the title of the lesson to delete
     * 
     * @param db is the database where lessons are stored
     * 
     * @return true if deletion is successful, otherwise false
	 *
     */
    public static boolean deleteLesson(String title, Database db) {
    	// Contributor Story 1: a Contributor can manage (delete) the lessons they added
    	return db.deleteLesson(title);
    }
    
    /*****
     * <p> Method: boolean updateTitle(int id, String title, Database db) </p>
     * 
     * <p> Description: This method updates the title of an existing LessonsLearned 
     * object. The update fails if the lesson does not exist or the new title is 
     * invalid. </p>
     * 
     * @param id is the id of the desired lesson
     * 
     * @param title is a string for the new desired title
     * 
     * @param db is the database where lessons are stored
     * 
     * @return true if update is successful, otherwise false
	 *
     */
    public static boolean updateTitle(int id, String title, Database db) { 
    	// Contributor Story 2: a lesson must exist before it can be examined or changed
    	if (!db.doesLessonExist(id)) {
    		return false;
    	}
    	
    	// If the title modification is invalid
    	if(!isValidTitle(title)) {
    		return false;
    	}
    	
    	// Set the new attribute
    	boolean titleUpdated = db.updateTitle(id, title.trim());
    	
    	return titleUpdated;
    }
    
    /*****
     * <p> Method: boolean updateDescription(int id, String description, Database db) </p>
     * 
     * <p> Description: This method updates the description of an existing 
     * LessonsLearned object. The update fails if the lesson does not exist or 
     * the new description is invalid. </p>
     * 
     * @param id is the id of the desired lesson
     * 
     * @param description is a string for the new desired description
     * 
     * @param db is the database where lessons are stored
     * 
     * @return true if update is successful, otherwise false
	 *
     */
    public static boolean updateDescription(int id, String description, Database db) { 
    	// Contributor Story 2: a lesson must exist before it can be examined or changed
    	if (!db.doesLessonExist(id)) {
    		return false;
    	}
    	
    	// If the description modification is invalid
    	if(!isValidDescription(description)) {
    		return false;
    	}
    	
    	// Set the new attribute
    	boolean descriptionUpdated = db.updateDescription(id, description.trim());
    	
    	return descriptionUpdated;
    }
    
}
