package automationTestbed;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import database.Database;
import entityClasses.LessonsLearned;
import entityClasses.LessonsLearnedList;

/*******
 * <p>Title: ContributorTestingAutomation.</p>
 * <p>Description: Runs Phase Two lesson validation, CRUD, collection, and
 * persistence tests without opening JavaFX. Adapted from the HW2 lesson testbed.</p>
 * <p>Coverage: add lessons with core information; examine lessons; load and
 * select lessons; manage editable core information; delete lessons; persistence.
 * Validation is exercised through create and update scenarios instead of separate
 * recognizer-only tests. Normal execution runs 15 functional cases; later runs
 * add one persistence verification. Setup and cleanup failures are reported.</p>
 * <p>Manual coverage still required: Contributor privacy and experience entry.
 * The manual plan must include at least two meaningful cases for each applicable
 * requirement. This testbed alone does not demonstrate all Contributor stories.
 * GUI feedback, navigation, and confirmation behavior also require manual checks.</p>
 * <p>Normal tests use predictable numbered titles and remove their records.
 * The dedicated persistence record stays saved. Press Run again to verify it.
 * Do not delete the database between persistence runs.</p>
 * <p>Known defects retained for follow-up: maximum description storage is tested
 * and should fail with the supplied 255-character column. Internal punctuation
 * cases are not executed because the recognizers can loop indefinitely in their
 * missing state 2. Locks and Phase Three AI are excluded.</p>
 * @author Manuel A Dominguez
 * @version 1.01 Focused Contributor story coverage
 *
 */
public class ContributorTestingAutomation {
    /*******
     * <p> Attribute: numPassed </p>
     *
     * <p> Description: Number of tests whose actual behavior matches expectations. </p>
     *
     */
    private static int numPassed = 0;
    /*******
     * <p> Attribute: numFailed </p>
     *
     * <p> Description: Number of failed assertions or test exceptions. </p>
     *
     */
    private static int numFailed = 0;
    /*******
     * <p> Attribute: testNumber </p>
     *
     * <p> Description: Sequential console test number. </p>
     *
     */
    private static int testNumber = 0;
    /*******
     * <p> Attribute: theDatabase </p>
     *
     * <p> Description: Local application database, using the team's unchanged connection
     * setup.
     * </p>
     *
     */
    private static final Database theDatabase = new Database();
    /*******
     * <p> Attribute: createdIds </p>
     *
     * <p> Description: IDs of normal-test records, retained for cleanup even after failures.
     * </p>
     *
     */
    private static final List<Integer> createdIds = new ArrayList<>();
    /*******
     * <p> Attribute: prefix </p>
     *
     * <p> Description: Identifies the predictable titles reserved for normal test records.
     * </p>
     *
     */
    private static final String prefix = "TP2 Test ";
    /*******
     * <p> Attribute: author </p>
     *
     * <p> Description: Username recorded as the author; the supplied schema has no author
     * foreign key. </p>
     *
     */
    private static final String author = "TP2TestContributor";
    /*******
     * <p> Attribute: persistenceTitle </p>
     *
     * <p> Description: Stable title used only by the persistence test. </p>
     *
     */
    private static final String persistenceTitle = "TP2 Persistence Dedicated Record";

    /*******
     * <p> Method: ContributorTestingAutomation() </p>
     *
     * <p> Description: This utility testbed is run through main rather than instantiated. </p>
     *
     */
    private ContributorTestingAutomation() { }

    /*******
     * <p> Method: void main(String[] args) </p>
     *
     * <p> Description: Connects to the database, runs the tests, cleans up test records, and
     * closes the connection. </p>
     *
     * @param args command-line arguments; not used
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
            evaluate("SETUP", "Execution completes without an unexpected exception",
                    "No exception", e.toString(), false);
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
     * <p> Method: void runNormalTests() </p>
     *
     * <p> Description: Runs the create, read, update, and delete test groups. </p>
     */
    private static void runNormalTests() {
        testCreateLessons();
        testReadLessons();
        testUpdateLessons();
        testDeleteLessons();
    }

    /*******
     * <p> Method: void testCreateLessons() </p>
     *
     * <p> Description: Creates lessons using valid, missing, duplicate, and maximum-length
     * input, then checks the database results. </p>

     *
     * <p> Requirement: Add a lesson with core information. Pass requires valid data to be
     * saved correctly and missing or duplicate input to be rejected without changing existing
     * data. The maximum-length case checks that accepted input can be stored. </p>
     */
    private static void testCreateLessons() {
        System.out.println("\n=== Create Lessons ===");
        try {
            String title = prefix + "01 Create Lesson";
            String description = "Validate required input before saving";
            boolean created = LessonsLearned.createLesson(title, description, author, theDatabase);
            LessonsLearned saved = track(title);
            
            boolean correct = created && saved != null && saved.getId() > 0 && title.equals(saved.getTitle())
            		&& description.equals(saved.getDescription()) && author.equals(saved.getAuthor());
            
            evaluate("Add a lesson with core information", "Valid lesson is saved correctly",
                    "Matching title, description, author, and positive ID", describe(saved), correct);

            String invalidTitle = prefix + "02 Missing Description";
            boolean invalidCreated = LessonsLearned.createLesson(invalidTitle, "", author, theDatabase);
            LessonsLearned invalidSaved = track(invalidTitle);
            evaluate("Add a lesson with core information", "Missing description is rejected",
                    "false and no record", "Returned " + invalidCreated + "; " + describe(invalidSaved),
                    !invalidCreated && invalidSaved == null);

            
            // Test case 3: Empty-title records should never exist. Track one if this test uncovers a defect.
            invalidCreated = LessonsLearned.createLesson("", description, author, theDatabase);
            invalidSaved = track("");
            evaluate("Add a lesson with core information", "Missing title is rejected",
                    "false and no record", "Returned " + invalidCreated + "; " + describe(invalidSaved),
                    !invalidCreated && invalidSaved == null);
            // Test case 4: Duplicate title preserves original lesson
            if (created && saved != null) {
                boolean duplicateCreated = LessonsLearned.createLesson(title,
                        "Replacement description", author, theDatabase);
                LessonsLearned after = theDatabase.getLessonDetails(saved.getId());
                evaluate("Add a lesson with core information", "Duplicate title preserves original lesson",
                        "false and original unchanged", "Returned " + duplicateCreated + "; " + describe(after),
                        !duplicateCreated && sameValues(saved, after));
            }

            // TODO: Keep this defect-revealing test: validation permits 500 characters,
            // but the supplied database schema stores only 255.
            String longTitle = prefix + "03 Maximum Description";
            String longDescription = "A".repeat(500);
            boolean longCreated = LessonsLearned.createLesson(longTitle, longDescription, author, theDatabase);
            LessonsLearned longSaved = track(longTitle);
            evaluate("Add a lesson with core information", "Maximum valid description saves completely",
                    "Created with all 500 characters", describe(longSaved),
                    longCreated && longSaved != null && longDescription.equals(longSaved.getDescription()));
        } catch (Exception e) {
            evaluate("Create lesson tests", "Execution completes", "No exception", e.toString(), false);
        }
    }

    /*******
     * <p> Method: void testReadLessons() </p>
     *
     * <p> Description: Retrieves lesson details and checks matching and empty lesson-list
     * subsets. </p>

     *
     * <p> Requirement: List lessons and examine a selected lesson. Pass requires matching
     * saved values, no details for a missing record, the correct matching subset, and an empty
     * unmatched subset. Contributor privacy is checked separately in manual GUI tests. </p>
     */
    private static void testReadLessons() {
        System.out.println("\n=== Read and List Lessons ===");
        try {
            String title = prefix + "10 Read Lesson";
            LessonsLearned saved = createTracked(title, "Details available for examination");
            if (saved == null) {
                return;
            }
            LessonsLearned readBack = theDatabase.getLessonDetails(saved.getId());
            evaluate("Examine a lesson", "Saved lesson details can be retrieved",
                    describe(saved), describe(readBack), sameValues(saved, readBack));

            LessonsLearned missing = theDatabase.getLessonDetails(-1);
            evaluate("Examine a lesson", "Nonexistent lesson returns no details",
                    "null", describe(missing), missing == null);

            LessonsLearnedList list = new LessonsLearnedList();
            list.refresh(theDatabase);
            LessonsLearnedList subset = list.search(title);
            evaluate("List and select lessons", "Loaded list and matching subset contain saved lesson",
                    "Correct lesson in list and one matching result", "Matching results: " + subset.size(),
                    sameValues(saved, list.get(saved.getId()))
                    && subset.size() == 1 && sameValues(saved, subset.get(saved.getId())));

            subset = list.search("TP2NoMatchingKeywordZZ");
            evaluate("List and select lessons", "Unmatched search gives an empty subset",
                    "Zero results", "Results: " + subset.size(), subset.size() == 0 && subset.isEmpty());
        } catch (Exception e) {
            evaluate("Read lesson tests", "Execution completes", "No exception", e.toString(), false);
        }
    }

    /*******
     * <p> Method: void testUpdateLessons() </p>
     *
     * <p> Description: Updates the title and description, then checks that empty-field updates
     * leave the saved lesson unchanged. </p>

     *
     * <p> Requirement: Manage editable lesson information. Pass requires valid edits to be
     * saved while preserving the other fields, and invalid edits to leave all saved fields
     * unchanged. Experience entry is checked separately in manual GUI tests. </p>
     */
    private static void testUpdateLessons() {
        System.out.println("\n=== Update Lessons ===");
        try {
            LessonsLearned original = createTracked(prefix + "20 Update Lesson", "Original description");
            if (original == null) {
                return;
            }
            int id = original.getId();
            String newTitle = prefix + "21 Updated Lesson";
            boolean updated = LessonsLearned.updateTitle(id, newTitle, theDatabase);
            LessonsLearned saved = theDatabase.getLessonDetails(id);
            evaluate("Manage editable lesson information", "Valid title update is saved",
                    "New title; original description and author", describe(saved),
                    updated && saved != null && saved.getId() == id
                    && newTitle.equals(saved.getTitle())
                    && original.getDescription().equals(saved.getDescription())
                    && author.equals(saved.getAuthor()));

            String newDescription = "Updated description";
            updated = LessonsLearned.updateDescription(id, newDescription, theDatabase);
            saved = theDatabase.getLessonDetails(id);
            evaluate("Manage editable lesson information", "Valid description update is saved",
                    "New description; title and author preserved", describe(saved),
                    updated && saved != null && saved.getId() == id
                    && newTitle.equals(saved.getTitle())
                    && newDescription.equals(saved.getDescription())
                    && author.equals(saved.getAuthor()));

            LessonsLearned beforeInvalidUpdate = theDatabase.getLessonDetails(id);
            updated = LessonsLearned.updateTitle(id, "", theDatabase);
            saved = theDatabase.getLessonDetails(id);
            evaluate("Manage editable lesson information", "Empty title update preserves saved lesson",
                    "false and unchanged", "Returned " + updated + "; " + describe(saved),
                    !updated && sameValues(beforeInvalidUpdate, saved));

            updated = LessonsLearned.updateDescription(id, "", theDatabase);
            saved = theDatabase.getLessonDetails(id);
            evaluate("Manage editable lesson information", "Empty description update preserves saved lesson",
                    "false and unchanged", "Returned " + updated + "; " + describe(saved),
                    !updated && sameValues(beforeInvalidUpdate, saved));
        } catch (Exception e) {
            evaluate("Update lesson tests", "Execution completes", "No exception", e.toString(), false);
        }
    }

    /*******
     * <p> Method: void testDeleteLessons() </p>
     *
     * <p> Description: Deletes a lesson, checks that it is gone, and tries deleting it again.
     * </p>

     *
     * <p> Requirement: Delete a lesson as part of managing it. Pass requires the lesson to
     * disappear from storage and the refreshed list, and a second deletion to return false.
     * GUI confirmation and permissions are checked separately. </p>
     */
    private static void testDeleteLessons() {
        System.out.println("\n=== Delete Lessons ===");
        try {
            LessonsLearned saved = createTracked(prefix + "30 Delete Lesson", "Lesson to remove");
            if (saved == null) {
                return;
            }
            int id = saved.getId();
            boolean deleted = LessonsLearned.deleteLesson(id, theDatabase);
            LessonsLearned remaining = theDatabase.getLessonDetails(id);
            LessonsLearnedList list = new LessonsLearnedList();
            list.refresh(theDatabase);
            evaluate("Delete a lesson", "Deletion removes lesson from storage and list",
                    "true and absent from storage and list", "Returned " + deleted + "; " + describe(remaining),
                    deleted && remaining == null && list.get(id) == null);

            deleted = LessonsLearned.deleteLesson(id, theDatabase);
            evaluate("Delete a lesson", "Already deleted lesson cannot be deleted again",
                    "false and still absent", "Returned " + deleted,
                    !deleted && theDatabase.getLessonDetails(id) == null);
        } catch (Exception e) {
            evaluate("Delete lesson tests", "Execution completes", "No exception", e.toString(), false);
        }
    }

    /*******
     * <p> Method: LessonsLearned createTracked(String title, String description) </p>
     *
     * <p> Description: Creates a test lesson and records its generated ID for cleanup. </p>
     *
     * @param title unique test title
     *
     * @param description valid description
     *
     * @return stored lesson, or null if creation/readback fails
     *
     */
    private static LessonsLearned createTracked(String title, String description) {
        boolean created = LessonsLearned.createLesson(title, description, author, theDatabase);
        LessonsLearned saved = track(title);
        // Successful preparation is not counted as a separate user-story test.
        if (!created || saved == null) {
            evaluate("Test Setup", "Prepare record: " + title,
                    "Created and readable", describe(saved), false);
            return null;
        }
        return saved;
    }

    /*******
     * <p> Method: LessonsLearned track(String title) </p>
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
     * <p> Method: boolean sameValues(LessonsLearned expected, LessonsLearned actual) </p>
     *
     * <p> Description: Compares the ID, title, description, and author of two lessons. </p>
     *
     * @param expected expected lesson
     *
     * @param actual readback lesson
     *
     * @return true when both exist and all four fields match
     *
     */
    private static boolean sameValues(LessonsLearned expected, LessonsLearned actual) {
        return expected != null && actual != null && expected.getId() == actual.getId()
                && Objects.equals(expected.getTitle(), actual.getTitle())
                && Objects.equals(expected.getDescription(), actual.getDescription())
                && Objects.equals(expected.getAuthor(), actual.getAuthor());
    }

    /*******
     * <p> Method: String describe(LessonsLearned lesson) </p>
     *
     * <p> Description: Returns the lesson information for display in the console. </p>
     *
     * @param lesson retrieved lesson or null
     *
     * @return ID, title, description, and author, or null marker
     *
     */
    private static String describe(LessonsLearned lesson) {
        if (lesson == null) {
            return "null";
        } else {
            return "ID=" + lesson.getId() + ", title=" + lesson.getTitle()
                    + ", description=" + lesson.getDescription()
                    + ", author=" + lesson.getAuthor();
        }
    }

    /*******
     * <p> Method: void evaluate(String requirement, String name, String expected, String
     * actual,
     * boolean passed) </p>
     *
     * <p> Description: Displays one test result and updates the pass or fail count. </p>
     *
     * @param requirement requirement references covered
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
     * <p> Method: void cleanup() </p>
     *
     * <p> Description: Deletes the tracked test records and leaves the persistence record
     * saved. </p>
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

    /*******
     * <p> Method: void testPersistence() </p>
     *
     * <p> Description: Checks for a lesson saved by a previous execution. If it is missing,
     * creates it and asks the user to run the tests again. </p>
     *

     *
     * <p> Requirement: Persistent lesson storage required by TP2. Pass requires a record from
     * a previous execution to retain its title, description, author, and a positive ID.
     * Creating the first record only prepares the check and is not counted as a pass. </p>
     */
    private static void testPersistence() {
        LessonsLearned saved = theDatabase.getLessonDetails(persistenceTitle);
        if (saved != null) {
            boolean matches = saved.getId() > 0
                    && persistenceTitle.equals(saved.getTitle())
                    && "Saved between executions".equals(saved.getDescription())
                    && author.equals(saved.getAuthor());
            evaluate("Persistence", "Lesson persists between executions",
                    "Previously saved title, description, author, and positive ID",
                    describe(saved), matches);
        } else {
            boolean created = LessonsLearned.createLesson(persistenceTitle,
                    "Saved between executions", author, theDatabase);
            LessonsLearned createdLesson = theDatabase.getLessonDetails(persistenceTitle);
            if (created && createdLesson != null
                    && createdLesson.getId() > 0
                    && persistenceTitle.equals(createdLesson.getTitle())
                    && "Saved between executions".equals(createdLesson.getDescription())
                    && author.equals(createdLesson.getAuthor())) {
                System.out.println("\nPersistence lesson saved. Run again to verify it remains.");
                System.out.println("Persistence verification is pending; it is not counted as passed.");
            } else {
                evaluate("Persistence", "Create persistence lesson",
                        "Dedicated record saved correctly", describe(createdLesson), false);
            }
        }
    }
}
