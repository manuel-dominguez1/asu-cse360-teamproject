  package entityClasses;
  
  import java.util.ArrayList;
  import java.util.Collections;
  import java.util.List;
  import java.util.Locale;
  import database.Database;
  
  /*******
   * <p> Title: LessonsLearnedList Class </p>
   * 
   * <p> Description: The LessonsLearnedList class represents an in-memory  list of lessons learned
   * retrieved from the persistent H2 database. Supports refreshing, retrieving, and searching lessons. </p>
   * 
   */ 

  public class LessonsLearnedList {
	  // In-memory list of lessons retrieved from the database
	  private final List<LessonsLearned> lessons = new ArrayList<>();
	  
	  /*****
	     * <p> Method: LessonsLearnedList() </p>
	     * 
	     * <p> Description: Default Constructor. </p>
	     * 
	     */
	  public LessonsLearnedList() {
		  
	  }
	  
	  /*****
	     * <p> Method: refresh(Database db) </p>
	     * 
	     * <p> Description: Reloads the list of lessons from the persistent database. </p>
	     * 
	     * @param db is the existing databse connection
	     * 
	     */
	  public void refresh(Database db) {
		  // If no database connection
		  if (db == null) {
			  throw new IllegalArgumentException("Database required");
		  }
		  // Create a list of all lessons in the database
		  List<LessonsLearned> databaseLessons = db.getFullLessonList();
		  
		  // If the list is empty
		  if (databaseLessons == null) {
			  throw new IllegalStateException("Unable to load lessons");
		  }
		  
		  // Clear and reload the lessons list
		  lessons.clear();
		  lessons.addAll(databaseLessons);
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
		  
		  // For each lesson in the database
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
	     * @return a Learned Lessons object with a matching id
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
	     * @return a Learned Lessons object with a matching title
	     * 
	     */
	  public LessonsLearned get(String title) {
		  // If title is empty
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
	     * <p> Description: Produces a list of LessonLearned containing a keyword. </p>
	     * 
	     * @param query represents the words or phrase being searched for
	     * 
	     * @return a list of Learned Lessons objects matching the query
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
			  
			  // Every keyword must be present
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
	     * <p> Description: Determines whether the list is empty. </p>
	     * 
	     * @return true if empty, otherwise false
	     * 
	     */
	  public boolean isEmpty() {
		  return lessons.isEmpty();
	  }
	  
 }
