package entityClasses;

/*******
 * <p> Title: Lesson Class </p>
 * 
 * <p> Description: The Lesson class represents one persistent lesson object learned. </p>
 */ 

public class LessonsLearned {
	
	/*
	 * These are the private attributes for this entity object
	 */
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
    // Constructor to initialize a new Lesson object with id, title, description and author.
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
    // Constructor to initialize a new Lesson object with title, description and author.
    public LessonsLearned(String title, String dsc, String author) {
        this.title = title;
        this.description = dsc;
        this.author = author;
    }

    /*****
     * <p> Method: int getId() </p>
     * 
     * <p> Description: This getter returns the Id. </p>
     * 
     * @return an int of the Id
     * 
     */
    // Gets the current value of the Id.
    public int getId() { return id; }
    
    /*****
     * <p> Method: String getTitle() </p>
     * 
     * <p> Description: This getter returns the Title. </p>
     * 
     * @return a String of the Title
     * 
     */
    // Gets the current value of the Title.
    public String getTitle() { return title; }

    
    /*****
     * <p> Method: String getDescription() </p>
     * 
     * <p> Description: This getter returns the Description. </p>
     * 
     * @return a String of the Description
	 *
     */
    // Gets the current value of the Description.
    public String getDescription() { return description; }

    
    /*****
     * <p> Method: String getAuthor() </p>
     * 
     * <p> Description: This getter returns the Author. </p>
     * 
     * @return a String of the Author
	 *
     */
    // Gets the current value of the Author.
    public String getAuthor() { return author; }

    /*****
     * <p> Method: void setTitle(String s) </p>
     * 
     * <p> Description: This setter defines the title attribute. </p>
     * 
     * @param s is a string that specifies the lesson's title
	 *
     */
    // Sets the title of the lesson.
    public void setTitle(String s) { title = s; }
    
    /*****
     * <p> Method: void setDescription(String s) </p>
     * 
     * <p> Description: This setter defines the description attribute. </p>
     * 
     * @param s is a string that specifies the lesson's description
	 *
     */
    // Sets the description of the lesson.
    public void setDescription(String s) { description = s; }
    
    
}
