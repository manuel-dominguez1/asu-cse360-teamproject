  package entityClasses;
  
  import java.util.ArrayList;
  import java.util.Collections;
  import java.util.List;
  import java.util.Locale;
  import database.Database;
  
  /*******
   * <p> Title: LessonsLearnedList Class </p>
   * 
   * <p> Description: The LessonsLearnedList class represents a list of LessonsLearned objects
   * retrieved from the persistent H2 database. LessonsLearned handles Create, Update, and Delete
   * for a single lesson; this class handles Read for collections of lessons. One instance holds
   * all lessons in the system; others hold subsets, such as one Contributor's lessons or the
   * results of a keyword search. </p>
   * 
   * 
   * @author Sierra Trimmer
   * 
   * @version 1.0
   * 
   */ 

  public class LessonsLearnedList {
	  /**
	   * <p> Attribute: lessons </p>
	   * 
	   * <p> Description: The in-memory list of LessonsLearned objects held by this list. </p>
	   * 
	   * <p> Source and rationale: The assignment requires a list class supporting CRUD for lists
	   * of lessons learned, including a persistent list of all lessons and subsets of it. The
	   * H2 database is the persistent store, this attribute holds a copy so the GUI can display,
	   * filter, and search lessons without querying the database for every action. </p>
	   */
	  private final List<LessonsLearned> lessons = new ArrayList<>();
	  
	  /*****
	     * <p> Method: LessonsLearnedList() </p>
	     * 
	     * <p> Description: This default constructor establishes an empty list. The list is 
	     * populated from the database by calling refresh(), or by search() when a subset list
	     * is produced. </p>
	     * 
	     */
	  public LessonsLearnedList() {
		  
	  }
	  
	  /*****
	     * <p> Method: void refresh(Database db) </p>
	     * 
	     * <p> Description: Reloads the list with every lesson currently stored in the persistent
	     * database. This must be called after a lesson is created, updated, or deleted so the
	     * list shown to the Contributor matches what is stored. </p>
	     * 
	     * @param db is the existing database connection
	     * 
	     * @throws IllegalArgumentException if db is null
	     * 
	     * @throws IllegalStateException if the database is unable to return the lessons
	     * 
	     */
	  public void refresh(Database db) {
		  // If no database connection
		  if (db == null) {
			  throw new IllegalArgumentException("Database required");
		  }
		  // Create a list of all lessons in the database
		  List<LessonsLearned> databaseLessons = db.getFullLessonList();
		  
		  // A null result means the database query failed
		  if (databaseLessons == null) {
			  throw new IllegalStateException("Unable to load lessons");
		  }
		  
		  // Clear and reload the lessons list
		  lessons.clear();
		  lessons.addAll(databaseLessons);
	  }

	  /*****
	     * <p> Method: {@literal List<LessonsLearned>} all() </p>
	     * 
	     * <p> Description: Returns all lessons currently loaded into this list. </p>
	     *
	     * @return an unmodifiable list of lessons
	     * 
	     */
	  public List<LessonsLearned> all() {
		  return Collections.unmodifiableList(new ArrayList<>(lessons));
	  }
	  
	  /*****
	     * <p> Method: {@literal List<LessonsLearned>} all(String username) </p>
	     * 
	     * <p> Description: Returns all lessons currently loaded into the list
	     * viewable by the current user. </p>
	     * 
	     * @param username is a string representing the username of the user 
	     * currently logged-in
	     * 
	     * @return an unmodifiable list of lessons
	     * 
	     */
	  public List<LessonsLearned> all(String username) {
		  // Create a new list
		  List<LessonsLearned> userLessons = new ArrayList<>();
		  
		  for (LessonsLearned lesson : lessons) {
			  // If the author of the lesson matches the specified username
			  if (lesson.getAuthor().equals(username)) {
				  // Add the lesson to the list
				  userLessons.add(lesson);
			  }
		  }
		  
		  return Collections.unmodifiableList(userLessons);
	  }
	  
	  /*****
	     * <p> Method: LessonsLearned get(int id) </p>
	     * 
	     * <p> Description: Finds a lesson in the list by id. </p>
	     * 
	     * @param id represents the id of the desired lesson
	     * 
	     * @return the LessonsLearned object with a matching id, or null if none is found
	     * 
	     */
	  public LessonsLearned get(int id) {
		  // Search list for lesson with matching id
		  for (LessonsLearned lesson : lessons) {
			  if (lesson.getId() == id) {
				  return lesson;
			  }
		  }
		  
		  return null;
	  }
	  
	  /*****
	     * <p> Method: LessonsLearned get(String title) </p>
	     * 
	     * <p> Description: Finds a lesson in the list by title. </p>
	     * 
	     * @param title represents the title of the desired lesson
	     * 
	     * @return the LessonsLearned object with a matching title, or null if none is found
	     * 
	     */
	  public LessonsLearned get(String title) {
		  // Guard against a null title so the comparison below cannot throw an exception
		  if (title == null) {
			  return null;
		  }
		  
		  // Search list for lesson with matching title
		  for (LessonsLearned lesson : lessons) {
			  if (lesson.getTitle().equals(title)) {
				  return lesson;
			  }
		  }
		  
		  return null;
	  }

	  /*****
	     * <p> Method: LessonsLearnedList search(String query) </p>
	     * 
	     * <p> Description: Produces a new LessonsLearnedList containing the lessons whose title or
	     * description contains every keyword in the query. This is how subsets of all
	     * lessons are created. </p>
	     * 
	     * @param query represents the words or phrase being searched for
	     * 
	     * @return a new LessonsLearnedList of matching lessons, or all lessons if the query is blank
	     * 
	     */
	  public LessonsLearnedList search(String query) {
		  // Construct a new list for results
		  LessonsLearnedList result = new LessonsLearnedList();
		  
		  
		  // If the query is null or blank, return all lessons
		  if (query == null || query.trim().isEmpty()) {
			  result.lessons.addAll(lessons);
			  return result;
		  }
		  
		  // Split the query into individual keywords
		  String[] terms = query.toLowerCase(Locale.ROOT).trim().split("\\s+");
		  for (LessonsLearned lesson : lessons) {
			  String text = (lesson.getTitle() + " " + lesson.getDescription()).toLowerCase(Locale.ROOT);
			  boolean matches = true;
			  
			  // Every keyword must be present (AND search)
			  for (String term : terms) {
				  if (!text.contains(term)) {
					  matches = false;
					  break;
				  }
			  }
			  
			  // Add the lesson only after checking all keywords
			  if (matches) {
				  result.lessons.add(lesson);
			  }
		  }
			  
		  return result;
	  }
	  
	  /*****
	     * <p> Method: int size() </p>
	     * 
	     * <p> Description: Returns the number of loaded lessons. </p>
	     * 
	     * @return the number of lessons
	     * 
	     */
	  public int size() {
		  return lessons.size();
	
	  }
	  
	  /*****
	     * <p> Method: boolean isEmpty() </p>
	     * 
	     * <p> Description: Determines whether this list contains any lessons. </p>
	     * 
	     * @return true if empty, otherwise false
	     * 
	     */
	  public boolean isEmpty() {
		  return lessons.isEmpty();
	  }
	  
 }
