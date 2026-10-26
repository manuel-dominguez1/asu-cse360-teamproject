package automationTestbed;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import database.Database;
import entityClasses.LessonsLearned;
import entityClasses.LessonsLearnedList;

/*******
 * <p> Title: ContributorTestingAutomation </p>
 *
 * <p> Description: Tests creating, reading, updating, deleting, and listing lessons learned as per our user stories
 * Also removes test records after running, except for one kept to test persistence between runs. </p>
 * 
 * @author Manuel Dominguez
 * @version 1.0
 */

public class ContributorTestingAutomation {
	
    /*******
     * <p> Attribute: numPassed </p>
     *
     * <p> Description: Number of tests that match expectations</p>
     *
     */
    private static int numPassed = 0;
    /*******
     * <p> Attribute: numFailed </p>
     *
     * <p> Description: Number of failed test exceptions</p>
     *
     */
    private static int numFailed = 0;
    /*******
     * <p> Attribute: testNumber </p>
     *
     * <p> Description: Test number for organization</p>
     *
     */
    private static int testNumber = 0;
    /*******
     * <p> Attribute: theDatabase </p>
     *
     * <p> Description: connection to our dataabase, so database tests can be automated</p>
     *
     */
    private static final Database theDatabase = new Database();
    /*******
     * <p> Attribute: createdIds </p>
     *
     * <p> Description: IDs of tests, for cleanup of the database</p>
     *
     */
    private static final List<Integer> createdIds = new ArrayList<>();
    /*******
     * <p> Attribute: author </p>
     *
     * <p> Description: author of normal testing lessons</p>
     *
     */
    private static final String author = "TP2TestContributor";
    /*******
     * <p> Attribute: persistenceTitle </p>
     *
     * <p> Description: set title of our Persistence test</p>
     *
     */
    private static final String persistenceTitle = "TP2 Persistence Test";

    /*******
     * <p> Method: ContributorTestingAutomation() </p>
     *
     * <p> constructor not necessary for this testbed. </p>
     *
     */
    private ContributorTestingAutomation() { }

    /*******
     * <p> Method: main(String[] args) </p>
     *
     * <p> Description: in order: Connects to the database, runs the tests, cleans up test records, and
     * closes the connection. </p>
     *
     * @param args command line arguments not used
     *
     */
    public static void main(String[] args) {
        System.out.println("________________________________________");
        System.out.println("Contributor Testing Automation");
        try {
            theDatabase.connectToDatabase();
            runNormalTests();
            testPersistence();
        } catch (Exception e) {
            evaluate("SETUP", "Execution completes without an unexpected exception", "No exception", e.toString(), false);
            e.printStackTrace();
        } finally {
            cleanup();
            theDatabase.closeConnection();
        }
        System.out.println("________________________________________");
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    /*******
     * <p> Method: runNormalTests() </p>
     *
     * <p> Description: Runs the create, read, update, delete, and Author privacy test groups. </p>
     */
    private static void runNormalTests() {
        testCreateLessons();
        testReadLessons();
        testUpdateLessons();
        testDeleteLessons();
        testAuthorLessonList();
    }

    /*******
     * <p> Method: testCreateLessons() </p>
     *
     * <p> Description: Creates lessons. tests involve valid, missing, duplicate, and max length
     * input, then checks against the database results </p>

     *
     * <p> Requirement: Add a lesson with core information(title, description) to the database. Pass requires valid data to be
     * saved correctly and missing or duplicate input to be rejected without changing existing data. The max length case checks that only accepted length input can be stored. </p>
     */
    private static void testCreateLessons() {
        System.out.println("\n=== Create Lessons ===");
        try {
            // Test case 1: Create a valid lesson and verify all saved core information.
            String title = "Create Test Lesson";
            String description = "Validate required input before saving";
            boolean created = LessonsLearned.createLesson(title, description, author, theDatabase);
            LessonsLearned saved = track(title);
            boolean correct = created && saved != null && saved.getId() > 0
                    && title.equals(saved.getTitle())
                    && description.equals(saved.getDescription())
                    && author.equals(saved.getAuthor());
            evaluate("Add a lesson with core information", "Valid lesson is saved correctly", "Matching title, description, author, and positive ID", describe(saved), correct);

            // Test case 2: Reject creation when the required description is empty.
            String invalidTitle = "Missing Description Test Lesson";
            boolean invalidCreated = LessonsLearned.createLesson(invalidTitle, "", author, theDatabase);
            LessonsLearned invalidSaved = track(invalidTitle);
            evaluate("Add a lesson with core information", "Missing description is rejected", "false and no record", "Returned " + invalidCreated + "; " + describe(invalidSaved), !invalidCreated && invalidSaved == null);

            // Test case 3: Reject creation when the required title is empty.
            invalidCreated = LessonsLearned.createLesson("", description, author, theDatabase);
            invalidSaved = track("");
            evaluate("Add a lesson with core information", "Missing title is rejected", "false and no record", "Returned " + invalidCreated + "; " + describe(invalidSaved), !invalidCreated && invalidSaved == null);

            // Test case 4: Reject a duplicate title, preserve the original lesson.
            if (created && saved != null) {
                boolean duplicateCreated = LessonsLearned.createLesson(title, "Replacement description", author, theDatabase);
                LessonsLearned after = theDatabase.getLessonDetails(saved.getId());
                evaluate("Add a lesson with core information", "Duplicate title preserves original lesson", "false and original unchanged", "Returned " + duplicateCreated + "; " + describe(after), !duplicateCreated && sameValues(saved, after));
            }

            // Test case 5: Save and read back a maxed out 1600 description.
            // This test is checking both the recognizer and actual database allow for 1600 description.
            String longTitle = "Max Description Test Lesson";
            String longDescription = "A".repeat(1600);
            boolean longCreated = LessonsLearned.createLesson(longTitle, longDescription, author, theDatabase);
            LessonsLearned longSaved = track(longTitle);
            evaluate("Add a lesson with core information", "Maximum valid description saves completely", "Created with all 1600 characters", describe(longSaved), longCreated && longSaved != null && longDescription.equals(longSaved.getDescription()));

            // Test case 6: Reject a 1601 character description without saving a lesson.
            String tooLongTitle = "Over Limit Description Test Lesson";
            boolean tooLongCreated = LessonsLearned.createLesson(tooLongTitle, "A".repeat(1601), author, theDatabase);
            LessonsLearned tooLongSaved = track(tooLongTitle);
            evaluate("Add a lesson with core information", "Description above 1600 characters is rejected", "false and no record", "Returned " + tooLongCreated + "; " + describe(tooLongSaved), !tooLongCreated && tooLongSaved == null);
        } catch (Exception e) {
            evaluate("Create lesson tests", "Execution completes", "No exception", e.toString(), false);
        }
    }

    /*******
     * <p> Method: testReadLessons() </p>
     *
     * <p> Description: tests the reading of lesson details and checks both matching and empty lesson list
     * subsets. </p>

     *
     * <p> Requirement: List lessons and examine a selected lesson. Pass requires matching
     * saved values, no details for a missing record, the correct matching subset, and an empty
     * unmatched subset. </p>
     */
    private static void testReadLessons() {
        System.out.println("\n=== Read and List Lessons ===");
        // setup for the lesson used by the read and list tests. if it didnt save into database, none of these tests will pass and so return early.
        try {
           
            String title = "Read Test Lesson";
            LessonsLearned saved = createTracked(title, "Details available for examination");
            if (saved == null) {
                return;
            }
            // Test case 7: Retrieve the prepared lesson and compare every field.
            LessonsLearned readBack = theDatabase.getLessonDetails(saved.getId());
            evaluate("Examine a lesson", "Saved lesson details can be retrieved", describe(saved), describe(readBack), sameValues(saved, readBack));

            // Test case 8: Return no details for a nonexistent lesson ID.
            LessonsLearned missing = theDatabase.getLessonDetails(-1);
            evaluate("Examine a lesson", "Nonexistent lesson returns no details", "null", describe(missing), missing == null);

            // Test case 9: Find the correct lesson in the loaded list and matching subset.
            LessonsLearnedList list = new LessonsLearnedList();
            list.refresh(theDatabase);
            LessonsLearnedList subset = list.search(title);
            evaluate("List and select lessons", "Loaded list and matching subset contain saved lesson", "Correct lesson in list and one matching result", "Matching results: " + subset.size(), sameValues(saved, list.get(saved.getId())) && subset.size() == 1 && sameValues(saved, subset.get(saved.getId())));

            // Test case 10: Return an empty subset when no lesson matches.
            subset = list.search("TP2DoesNotExistZZZ");
            evaluate("List and select lessons", "Unmatched search gives an empty subset", "Zero results", "Results: " + subset.size(), subset.size() == 0 && subset.isEmpty());
        } catch (Exception e) {
            evaluate("Read lesson tests", "Execution completes", "No exception", e.toString(), false);
        }
    }

    /*******
     * <p> Method: testUpdateLessons() </p>
     *
     * <p> Description: testing updates to the title and description, then checks that empty field updates
     * leave the saved lesson unchanged. </p>

     *
     * <p> Requirement: Manage editable lesson information. Pass requires valid edits to be
     * saved while preserving the other fields, while invalid edits to leave all saved fields
     * unchanged </p>
     */
    private static void testUpdateLessons() {
        System.out.println("\n=== Update Lessons ===");
        try {
            // setup for the lesson used by the update tests
            LessonsLearned original = createTracked("Update Test Lesson", "Original description");
            if (original == null) {
                return;
            }
            int id = original.getId();
            // Test case 11: Save a valid title change while preserving the other fields.
            String newTitle = "Newly Updated Test Lesson";
            boolean updated = LessonsLearned.updateTitle(id, newTitle, theDatabase);
            LessonsLearned saved = theDatabase.getLessonDetails(id);
            evaluate("Manage editable lesson information", "Valid title update is saved", "New title; original description and author", describe(saved), updated && saved != null && saved.getId() == id && newTitle.equals(saved.getTitle()) && original.getDescription().equals(saved.getDescription()) && author.equals(saved.getAuthor()));

            // Test case 12: Save a valid description change while preserving the other fields.
            String newDescription = "Updated description";
            updated = LessonsLearned.updateDescription(id, newDescription, theDatabase);
            saved = theDatabase.getLessonDetails(id);
            evaluate("Manage editable lesson information", "Valid description update is saved", "New description; title and author preserved", describe(saved), updated && saved != null && saved.getId() == id && newTitle.equals(saved.getTitle()) && newDescription.equals(saved.getDescription()) && author.equals(saved.getAuthor()));

            // Test case 13: Reject an empty title update and preserve the saved lesson.
            LessonsLearned beforeInvalidUpdate = theDatabase.getLessonDetails(id);
            updated = LessonsLearned.updateTitle(id, "", theDatabase);
            saved = theDatabase.getLessonDetails(id);
            evaluate("Manage editable lesson information", "Empty title update preserves saved lesson", "false and unchanged", "Returned " + updated + "; " + describe(saved), !updated && sameValues(beforeInvalidUpdate, saved));

            // Test case 14: Reject an empty description update and preserve the saved lesson.
            updated = LessonsLearned.updateDescription(id, "", theDatabase);
            saved = theDatabase.getLessonDetails(id);
            evaluate("Manage editable lesson information", "Empty description update preserves saved lesson", "false and unchanged", "Returned " + updated + "; " + describe(saved), !updated && sameValues(beforeInvalidUpdate, saved));
        } catch (Exception e) {
            evaluate("Update lesson tests", "Execution completes", "No exception", e.toString(), false);
        }
    }

    /*******
     * <p> Method: testDeleteLessons() </p>
     *
     * <p> Description: Deletes a lesson, checks that it is gone, and tries deleting it again</p>

     *
     * <p> Requirement: Delete a lesson. Pass requires the lesson to disappear from storage and the refreshed list, and a second deletion to return false</p>
     */
    private static void testDeleteLessons() {
        System.out.println("\n=== Delete Lessons ===");
        try {
            // Prepare the lesson used by the delete tests.
            LessonsLearned saved = createTracked("Delete Test Lesson", "Lesson to remove");
            if (saved == null) {
                return;
            }
            int id = saved.getId();
            // Test case 15: Delete a lesson and verify its absence from storage and the list.
            boolean deleted = LessonsLearned.deleteLesson(id, theDatabase);
            LessonsLearned remaining = theDatabase.getLessonDetails(id);
            LessonsLearnedList list = new LessonsLearnedList();
            list.refresh(theDatabase);
            evaluate("Delete a lesson", "Deletion removes lesson from storage and list", "true and absent from storage and list", "Returned " + deleted + "; " + describe(remaining), deleted && remaining == null && list.get(id) == null);

            // Test case 16: Attempt to delete test again. Reject deletion of the already deleted lesson.
            deleted = LessonsLearned.deleteLesson(id, theDatabase);
            evaluate("Delete a lesson", "Already deleted lesson cannot be deleted again", "false and still absent", "Returned " + deleted, !deleted && theDatabase.getLessonDetails(id) == null);
        } catch (Exception e) {
            evaluate("Delete lesson tests", "Execution completes", "No exception", e.toString(), false);
        }
    }

    /*******
     * <p> Method: testAuthorLessonList() </p>
     *
     * <p> Description: Checks the database can lists lesson titles for an author, ordered by lesson ID. </p>
     *
     * <p> Requirement: Support listing a Contributor's own lessons. The first case checks that both owned lessons appear in ID order and another author's lesson
     * is excluded. Titles are deliberately opposite alphabetical order. The second case expects an empty list for an author with no lessons. </p>
     */
    private static void testAuthorLessonList() {
        System.out.println("\n=== Lessons By Author ===");
        // Prepare two owned lessons and one lesson belonging to another author.
        String firstTitle = "Zulu Author List Test Lesson";
        String secondTitle = "Alpha Author List Test Lesson";
        String otherTitle = "Other Author Test Lesson";
        String listAuthor = "TP2AuthorListContributor";
        String otherAuthor = "TP2OtherListContributor";
        String emptyAuthor = "TP2ContributorWithNoLessons";
        try {
            boolean firstCreated = LessonsLearned.createLesson(firstTitle, "First lesson by ID", listAuthor, theDatabase);
            LessonsLearned first = track(firstTitle);
            boolean secondCreated = LessonsLearned.createLesson(secondTitle, "Second lesson by ID", listAuthor, theDatabase);
            LessonsLearned second = track(secondTitle);
            boolean otherCreated = LessonsLearned.createLesson(otherTitle, "Lesson belonging to another contributor", otherAuthor, theDatabase);
            LessonsLearned other = track(otherTitle);

            if (!firstCreated || first == null || !secondCreated || second == null || !otherCreated || other == null) {
                evaluate("Test Setup", "Prepare author list lessons", "Three new lessons", "One or more lessons could not be prepared", false);
                return;
            }

            // Test case 17: List only the selected author's lessons in ID order.
            List<String> titles = theDatabase.getLessonList(listAuthor);
            boolean correctList = titles != null && titles.size() == 2 && first.getId() < second.getId() && firstTitle.equals(titles.get(0)) && secondTitle.equals(titles.get(1)) && !titles.contains(otherTitle);
            evaluate("List only the author's lessons", "Owned lessons appear in ID order without another author's lesson", "[" + firstTitle + ", " + secondTitle + "]", String.valueOf(titles), correctList);

            // Test case 18: Return an empty list for an author with no lessons.
            List<String> emptyTitles = theDatabase.getLessonList(emptyAuthor);
            evaluate("List only the author's lessons", "Author with no lessons receives an empty list", "[]", String.valueOf(emptyTitles), emptyTitles != null && emptyTitles.isEmpty());
        } catch (Exception e) {
            evaluate("List only the author's lessons", "Author list execution completes", "No exception", e.toString(), false);
        }
    }

    /*******
     * <p> Method: testPersistence() </p>
     *
     * <p> Description: Checks for the persistence lesson saved by a previous execution. If not exists,
     * attempts to create it and asks the user to run the tests again for the persistence check. </p>
     *

     *
     * <p> Requirement: Persistent lesson storage between database connections. Pass requires a record from
     * a previous execution to retain its title, description, author, and a positive ID.
     * Creating the first record only prepares the check and is not counted as a pass. </p>
     */
    private static void testPersistence() {
        // Test case 19 on 2nd+ runs: Verify the lesson retained from a previous execution.
        LessonsLearned saved = theDatabase.getLessonDetails(persistenceTitle);
        if (saved != null) {
            boolean matches = saved.getId() > 0 && persistenceTitle.equals(saved.getTitle()) && "Saved between executions".equals(saved.getDescription()) && author.equals(saved.getAuthor());
            evaluate("Persistence", "Lesson persists between executions", "Previously saved title, description, author, and positive ID", describe(saved), matches);
        } else {
            boolean created = LessonsLearned.createLesson(persistenceTitle, "Saved between executions", author, theDatabase);
            LessonsLearned createdLesson = theDatabase.getLessonDetails(persistenceTitle);
            if (created && createdLesson != null && createdLesson.getId() > 0 && persistenceTitle.equals(createdLesson.getTitle()) && "Saved between executions".equals(createdLesson.getDescription()) && author.equals(createdLesson.getAuthor())) {
                System.out.println("\nPersistence lesson saved. Run again to verify it remains.");
                System.out.println("Persistence verification is pending; it is not counted as passed.");
            } else {
                evaluate("Persistence", "Create persistence lesson", "Dedicated record saved correctly", describe(createdLesson), false);
            }
        }
    }
    
    /*******
     * <p> Method: createTracked(String title, String description) </p>
     *
     * <p> Description: Creates a test lesson and records its generated ID for cleanup, used in the lessons used on multiple tests</p>
     *
     * @param title unique test title of lesson
     *
     * @param description valid description of lesson
     *
     * @return stored lesson, or null if creation/readback fails
     *
     */
    private static LessonsLearned createTracked(String title, String description) {
        boolean created = LessonsLearned.createLesson(title, description, author, theDatabase);
        LessonsLearned saved = track(title);
        if (!created || saved == null) {
            evaluate("Test Setup", "Prepare record: " + title, "Created and readable", describe(saved), false);
            return null;
        }
        return saved;
    }

    /*******
     * <p> Method: track(String title) </p>
     *
     * <p> Description: Finds a test lesson by title and records its ID for cleanup. </p>
     *
     * @param title test title to retrieve
     *
     * @return stored lesson or null
     *
     */
    private static LessonsLearned track(String title) {
        LessonsLearned saved = theDatabase.getLessonDetails(title);
        if (saved != null && !createdIds.contains(saved.getId())) {
            createdIds.add(saved.getId());
        }
        return saved;
    }

    /*******
     * <p> Method: sameValues(LessonsLearned expected, LessonsLearned actual) </p>
     *
     * <p> Description: simple comparison of the ID, title, description, and author of two lessons. </p>
     *
     * @param expected what we expect lesson to readback
     *
     * @param actual what actually came back from the database for the lesson
     *
     * @return true when both exist and all four fields match
     *
     */
    private static boolean sameValues(LessonsLearned expected, LessonsLearned actual) {
        return expected != null && actual != null && expected.getId() == actual.getId() && Objects.equals(expected.getTitle(), actual.getTitle()) && Objects.equals(expected.getDescription(), actual.getDescription()) && Objects.equals(expected.getAuthor(), actual.getAuthor());
    }

    /*******
     * <p> Method: describe(LessonsLearned lesson) </p>
     *
     * <p> Description: Returns the lesson information for display in the console for testing. </p>
     *
     * @param lesson retrieved lesson or null
     *
     * @return String containing ID, title, description, and author, or null marker
     *
     */
    private static String describe(LessonsLearned lesson) {
        if (lesson == null) {
            return "null";
        } else {
            return "ID=" + lesson.getId() + ", title=" + lesson.getTitle()+ ", description=" + lesson.getDescription() + ", author=" + lesson.getAuthor();
        }
    }

    /*******
     * <p> Method: evaluate(String requirement, String name, String expected, String actual, boolean passed) </p>
     *
     * <p> Description: Evaluates and displays one test result and updates the pass or fail count. </p>
     *
     * @param requirement requirement references covered for readability
     *
     * @param name test behavior
     *
     * @param expected expected result
     *
     * @param actual observed result
     *
     * @param passed whether actual behavior matched expectations
     *
     */
    private static void evaluate(String requirement, String name, String expected, String actual, boolean passed) {
        System.out.println("\nTest case " + (++testNumber) + " [" + requirement + "]: " + name);
        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);
        if (passed) {
            System.out.println("***Success***");
            numPassed++;
        } else {
            System.out.println("***Failure***");
            numFailed++;
        }
    }

    /*******
     * <p> Method: cleanup() </p>
     *
     * <p> Description: Deletes the tracked test records. The persistence record remains saved. </p>
     *
     */
    private static void cleanup() {
        for (int id : createdIds) {
            try {
                theDatabase.deleteLesson(id);
                if (theDatabase.getLessonDetails(id) != null) {
                    evaluate("CLEANUP", "Remove test record " + id, "Absent", "Still present", false);
                }
            } catch (Exception e) {
                evaluate("CLEANUP", "Remove test record " + id, "No exception", e.toString(), false);
            }
        }
    }

}
