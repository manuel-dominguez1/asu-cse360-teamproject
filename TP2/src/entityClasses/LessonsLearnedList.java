  package entityClasses;
  
  import java.util.ArrayList;
  import java.util.Collections;
  import java.util.List;
  import java.util.Locale;
  
  /*******
   * <p> Title: LessonsLearnedList Class </p>
   * 
   * <p> Description: The LessonsLearnedList class represents an in-memory result list of lessons learned. </p>
   * 
   */ 

  public class LessonsLearnedList {
	  private final List<LessonsLearned> lessons = new ArrayList<>();
	  public List<LessonsLearned> all() {
		  return Collections.unmodifiableList(new ArrayList<> (lessons));
	  }
	  
	  /*****
	     * <p> Method: add(LessonLearned lesson) </p>
	     * 
	     * <p> Description: Adds a lesson to the list. </p>
	     * 
	     * @param lesson represents a LessonsLearned object to be added to the list
	     * 
	     */
	  public static void add(LessonsLearned lesson) {
		  if (lesson == null) {
			  throw new IllegalArgumentException("Lesson required");
		  }
		  lessons.add(lesson);
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
	  public static LessonsLearned get(int id) {
		  for (LessonsLearned lesson : lessons) {
			  if (lesson.getId() == id) {
				  return lesson;
			  }
		  }
		  
		  return null;
	  }
	  
	  /*****
	     * <p> Method: boolean update(LessonsLearned lesson) </p>
	     * 
	     * <p> Description: Replaces an existing lesson in the result list. </p>
	     * 
	     * @param lesson represents a LessonsLearned object to be updated in the list
	     * 
	     * @return true if update is successful, otherwise false
	     * 
	     */
	  public static boolean update(LessonsLearned lesson) {
		  if (lesson == null) {
			  return false;
		  }
		  for (int i = 0; i < lessons.size(); i++) {
			  if (lessons.get(i).getId() == lesson.getId()) {
				  lessons.set(i,  lesson);
				  return true;
			  }
		  }
		  
		  return false;
	  }
	  
	  /*****
	     * <p> Method: boolean delete(int id) </p>
	     * 
	     * <p> Description: Deletes an existing lesson from the result list. </p>
	     * 
	     * @param id represents the id of the desired lesson
	     * 
	     * @return true if deletion is successful, otherwise false
	     * 
	     */
	  public static boolean delete(int id) {
		  return lessons.removeIf(l -> l.getId() == id);
	  }

	   /*****
	     * <p> Method: boolean delete(String title) </p>
	     * 
	     * <p> Description: Deletes an existing lesson from the result list. </p>
	     * 
	     * @param title represents the title of the desired lesson
	     * 
	     * @return true if deletion is successful, otherwise false
	     * 
	     */
	  public static boolean delete(String title) {
		  return lessons.removeIf(l -> l.getTitle() == title);
	  }
	  
	  /*****
	     * <p> Method: LessonsLearnedList search(String query) </p>
	     * 
	     * <p> Description: Searches for keywords in core fields. </p>
	     * 
	     * @param query is a keyword
	     * 
	     * @return a list of LessonsLearned containing the keyword
	     * 
	     */
	  public LessonsLearnedList search(String query) {
		  LessonsLearnedList result = new LessonsLearnedList();
		  // If the query is null or blank, return all lessons
		  if (query == null || query.trim().isEmpty()) {
			  for (LessonsLearned l : lessons) {
				  result.add(l);
			  }
			  return result;
		  }
		  
		  // Split the query into individual keywords
		  String[] terms = query.toLowerCase(Locale.ROOT).trim().split("\\s+");
		  for (LessonsLearned l : lessons) {
			  String text = (l.getTitle() + " " + l.getDescription()).toLowerCase(Locale.ROOT);
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
				  result.add(l);
			  }
		  }
			  
		  return result;
	  }
	  
 }
