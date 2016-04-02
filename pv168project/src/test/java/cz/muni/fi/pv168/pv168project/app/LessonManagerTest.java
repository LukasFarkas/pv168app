/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;


import cz.muni.fi.pv168.common.DBUtils;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.*;
import static java.time.Month.*;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;



public class LessonManagerTest {

    private LessonManager manager;
    private DataSource ds;
    
    private final static ZonedDateTime NOW
            = LocalDateTime.of(2016, MAY, 29, 14, 00).atZone(ZoneId.of("UTC"));

    // ExpectedException is one possible mechanisms for testing if expected
    // exception is thrown. See createGraveWithExistingId() for usage example.
    @Rule
    // attribute annotated with @Rule annotation must be public :-(
    public ExpectedException expectedException = ExpectedException.none();

    //--------------------------------------------------------------------------
    // Test initialization
    //--------------------------------------------------------------------------

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        
        ds.setDatabaseName("memory:lessonmanager-test");
        
        ds.setCreateDatabase("create");
        return ds;
    }

    private static Clock prepareClockMock(ZonedDateTime now) {
        // We don't need to use Mockito, because java already contais
        // implementation of Clock which returns fixed time.
        return Clock.fixed(now.toInstant(), now.getZone());
    }
        
    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,AgencyManager.class.getResource("createTables.sql"));
        manager = new LessonManager(prepareClockMock(NOW));
        manager.setDataSource(ds);
    }

    @After
    public void tearDown() throws SQLException {
        // Drop tables after each test
        DBUtils.executeSqlScript(ds,LessonManager.class.getResource("dropTables.sql"));
    }

   
    
    // creating lesson entities for testing
    
    /**
     * Creates sample lesson with sample data.
     * @return 
     */
    private Lesson newLesson(){
        return newLesson(LocalDateTime.from(NOW), 2, BigDecimal.valueOf(250), 
                "First lesson ever", 1L, 1L, Long.MAX_VALUE);
    }
    /**
     * Creates new lesson with givin prameters.
     * @param start
     * @param duration
     * @param price
     * @param notes
     * @param teacherId
     * @param studentId
     * @param id
     * @return 
     */
    private Lesson newLesson(LocalDateTime start, int duration, BigDecimal price,
                String notes, Long teacherId, Long studentId, Long id) {
        Lesson newLesson = new Lesson();
        newLesson.setDuration(duration);
        newLesson.setId(id);
        newLesson.setNotes(notes);
        newLesson.setPrice(price);
        newLesson.setStart(start);
        newLesson.setStudentId(studentId);
        newLesson.setTeacherId(teacherId);
        return newLesson;
    }
    
    
    
    //Simple tests:


    //--------------------------------------------------------------------------
    // Tests for operations for creating and fetching graves
    //--------------------------------------------------------------------------

    @Test
    public void createLesson() {
        Lesson lesson = newLesson();
        manager.createLesson(lesson);

        Long lessonId = lesson.getId();
        assertThat(lessonId).isNotNull();

        assertThat(manager.getLesson(lessonId))
                .isNotSameAs(lesson)
                .isEqualToComparingFieldByField(lesson);
    }

    @Test
    public void findAllLessons() {

        assertThat(manager.findAllLessons()).isEmpty();

        Lesson l1 = newLesson();
        Lesson l2 = newLesson(LocalDateTime.MAX, 3, BigDecimal.valueOf(200), 
                "Second lesson ever", 2L, 2L, Long.MAX_VALUE-1); //should with some different values

        manager.createLesson(l1);
        manager.createLesson(l2);

        assertThat(manager.findAllLessons())
                .usingFieldByFieldElementComparator()
                .containsOnly(l1,l2);
    }

    // Test exception with expected parameter of @Test annotation
    // it does not allow to specify exact place where the exception
    // is expected, therefor it is suitable only for simple single line tests
    @Test(expected = IllegalArgumentException.class)
    public void createNullGrave() {
        manager.createLesson(null);
    }
/*
    // Test exception with ExpectedException @Rule
    @Test
    public void createGraveWithExistingId() {
        Grave grave = newLesson().id(1L).build();
        expectedException.expect(IllegalEntityException.class);
        manager.createGrave(grave);
    }

    // Test exception using AssertJ assertThatThrownBy() method
    // this requires Java 8 due to using lambda expression
    @Test
    public void createGraveWithNegativeColumn() {
        Grave grave = newLesson().column(-1).build();
        assertThatThrownBy(() -> manager.createGrave(grave))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    public void createGraveWithNegativeRow() {
        Grave grave = newLesson().row(-1).build();
        expectedException.expect(ValidationException.class);
        manager.createGrave(grave);
    }

    @Test
    public void createGraveWithNegativeCapacity() {
        Grave grave = newLesson().capacity(-1).build();
        expectedException.expect(ValidationException.class);
        manager.createGrave(grave);
    }

    @Test
    public void createGraveWithZeroCapacity() {
        Grave grave = newLesson().capacity(0).build();
        expectedException.expect(ValidationException.class);
        manager.createGrave(grave);
    }

    @Test
    public void createGraveWithZeroColumn() {
        Grave grave = newLesson().column(0).build();
        manager.createGrave(grave);

        assertThat(manager.getGrave(grave.getId()))
                .isNotNull()
                .isEqualToComparingFieldByField(grave);
    }

    @Test
    public void createGraveWithZeroRow() {
        Grave grave = newLesson().row(0).build();
        manager.createGrave(grave);

        assertThat(manager.getGrave(grave.getId()))
                .isNotNull()
                .isEqualToComparingFieldByField(grave);
    }

    @Test
    public void createGraveWithNullNote() {
        Grave grave = newLesson().note(null).build();
        manager.createGrave(grave);

        assertThat(manager.getGrave(grave.getId()))
                .isNotNull()
                .isEqualToComparingFieldByField(grave);
    }

    //--------------------------------------------------------------------------
    // Tests for GraveManager.updateGrave(Grave) operation
    //--------------------------------------------------------------------------

    @Test
    public void updateGraveColumn() {
        // Let us create two graves, one will be used for testing the update
        // and another one will be used for verification that other objects are
        // not affected by update operation
        Grave graveForUpdate = newLesson().build();
        Grave anotherGrave = sampleBigGraveBuilder().build();
        manager.createGrave(graveForUpdate);
        manager.createGrave(anotherGrave);

        // Performa the update operation ...
        graveForUpdate.setColumn(1);

        // ... and save updated grave to database
        manager.updateGrave(graveForUpdate);

        // Check if grave was properly updated
        assertThat(manager.getGrave(graveForUpdate.getId()))
                .isEqualToComparingFieldByField(graveForUpdate);
        // Check if updates didn't affected other records
        assertThat(manager.getGrave(anotherGrave.getId()))
                .isEqualToComparingFieldByField(anotherGrave);
    }

    // Now we want to test also other update operations. We could do it the same
    // way as in updateGraveColumn(), but we would get couple of almost the same
    // test methods, which would differ from each other only in single line
    // with update operation. To avoid duplicit code and make the test better
    // maintainable, we need to separate the update operation from the test
    // method and to let us call test method multiple times with differen
    // update operation.

    // Let start with functional interface which will represent update operation
    // BTW, we could use standard Consumer<T> functional interface (and I would
    // probably do it in real test), but I decided to define my own interface to
    // make the test better understandable
    @FunctionalInterface
    private static interface Operation<T> {
        void callOn(T subjectOfOperation);
    }

    // The next step is implementation of generic test method. This method will
    // perform update test with given update operation.
    // The method is almost the same as updateGraveColumn(), the only difference
    // is the line with calling given updateOperation.
    private void testUpdateGrave(Operation<Grave> updateOperation) {
        Grave sourceGrave = newLesson().build();
        Grave anotherGrave = sampleBigGraveBuilder().build();
        manager.createGrave(sourceGrave);
        manager.createGrave(anotherGrave);

        updateOperation.callOn(sourceGrave);

        manager.updateGrave(sourceGrave);
        assertThat(manager.getGrave(sourceGrave.getId()))
                .isEqualToComparingFieldByField(sourceGrave);
        // Check if updates didn't affected other records
        assertThat(manager.getGrave(anotherGrave.getId()))
                .isEqualToComparingFieldByField(anotherGrave);
    }

    // Now we will call testUpdateGrave(...) method with different update
    // operations. Update operation is defined with Lambda expression.

    @Test
    public void updateGraveRow() {
        testUpdateGrave((grave) -> grave.setRow(3));
    }

    @Test
    public void updateGraveCapacity() {
        testUpdateGrave((grave) -> grave.setCapacity(5));
    }

    @Test
    public void updateGraveNote() {
        testUpdateGrave((grave) -> grave.setNote("Not so nice grave"));
    }

    @Test
    public void updateGraveNoteToNull() {
        testUpdateGrave((grave) -> grave.setNote(null));
    }

    // Test also if attemtpt to call update with invalid grave throws
    // the correct exception.

    @Test(expected = IllegalArgumentException.class)
    public void updateNullGrave() {
        manager.updateGrave(null);
    }

    @Test
    public void updateGraveWithNullId() {
        Grave grave = newLesson().id(null).build();
        expectedException.expect(IllegalEntityException.class);
        manager.updateGrave(grave);
    }

    @Test
    public void updateGraveWithNonExistingId() {
        Grave grave = newLesson().id(1L).build();
        expectedException.expect(IllegalEntityException.class);
        manager.updateGrave(grave);
    }

    @Test
    public void updateGraveWithNegativeColumn() {
        Grave grave = newLesson().build();
        manager.createGrave(grave);
        grave.setColumn(-1);
        expectedException.expect(ValidationException.class);
        manager.updateGrave(grave);
    }

    @Test
    public void updateGraveWithNegativeRow() {
        Grave grave = newLesson().build();
        manager.createGrave(grave);
        grave.setRow(-1);
        expectedException.expect(ValidationException.class);
        manager.updateGrave(grave);
    }

    @Test
    public void updateGraveWithZeroCapacity() {
        Grave grave = newLesson().build();
        manager.createGrave(grave);
        grave.setCapacity(0);
        expectedException.expect(ValidationException.class);
        manager.updateGrave(grave);
    }

    @Test
    public void updateGraveWithNegativeCapacity() {
        Grave grave = newLesson().build();
        manager.createGrave(grave);
        grave.setCapacity(-1);
        expectedException.expect(ValidationException.class);
        manager.updateGrave(grave);
    }

    //--------------------------------------------------------------------------
    // Tests for GraveManager.deleteGrave(Grave) operation
    //--------------------------------------------------------------------------

    @Test
    public void deleteGrave() {

        Grave g1 = newLesson().build();
        Grave g2 = sampleBigGraveBuilder().build();
        manager.createGrave(g1);
        manager.createGrave(g2);

        assertThat(manager.getGrave(g1.getId())).isNotNull();
        assertThat(manager.getGrave(g2.getId())).isNotNull();

        manager.deleteGrave(g1);

        assertThat(manager.getGrave(g1.getId())).isNull();
        assertThat(manager.getGrave(g2.getId())).isNotNull();

    }

    // Test also if attemtpt to call delete with invalid parameter throws
    // the correct exception.

    @Test(expected = IllegalArgumentException.class)
    public void deleteNullGrave() {
        manager.deleteGrave(null);
    }

    @Test
    public void deleteGraveWithNullId() {
        Grave grave = newLesson().id(null).build();
        expectedException.expect(IllegalEntityException.class);
        manager.deleteGrave(grave);
    }

    @Test
    public void deleteGraveWithNonExistingId() {
        Grave grave = newLesson().id(1L).build();
        expectedException.expect(IllegalEntityException.class);
        manager.deleteGrave(grave);
    }

    //--------------------------------------------------------------------------
    // Tests if GraveManager methods throws ServiceFailureException in case of
    // DB operation failure
    //--------------------------------------------------------------------------

    @Test
    public void createGraveWithSqlExceptionThrown() throws SQLException {
        // Create sqlException, which will be thrown by our DataSource mock
        // object to simulate DB operation failure
        SQLException sqlException = new SQLException();
        // Create DataSource mock object
        DataSource failingDataSource = mock(DataSource.class);
        // Instruct our DataSource mock object to throw our sqlException when
        // DataSource.getConnection() method is called.
        when(failingDataSource.getConnection()).thenThrow(sqlException);
        // Configure our manager to use DataSource mock object
        manager.setDataSource(failingDataSource);

        // Create Grave instance for our test
        Grave grave = newLesson().build();

        // Try to call Manager.createGrave(Grave) method and expect that
        // exception will be thrown
        assertThatThrownBy(() -> manager.createGrave(grave))
                // Check that thrown exception is ServiceFailureException
                .isInstanceOf(ServiceFailureException.class)
                // Check if cause is properly set
                .hasCause(sqlException);
    }

    // Now we want to test also other methods of GraveManager. To avoid having
    // couple of method with lots of duplicit code, we will use the similar
    // approach as with testUpdateGrave(Operation) method.

    private void testExpectedServiceFailureException(Operation<GraveManager> operation) throws SQLException {
        SQLException sqlException = new SQLException();
        DataSource failingDataSource = mock(DataSource.class);
        when(failingDataSource.getConnection()).thenThrow(sqlException);
        manager.setDataSource(failingDataSource);
        assertThatThrownBy(() -> operation.callOn(manager))
                .isInstanceOf(ServiceFailureException.class)
                .hasCause(sqlException);
    }

    @Test
    public void updateGraveWithSqlExceptionThrown() throws SQLException {
        Grave grave = newLesson().build();
        manager.createGrave(grave);
        testExpectedServiceFailureException((graveManager) -> graveManager.updateGrave(grave));
    }

    @Test
    public void getGraveWithSqlExceptionThrown() throws SQLException {
        Grave grave = newLesson().build();
        manager.createGrave(grave);
        testExpectedServiceFailureException((graveManager) -> graveManager.getGrave(grave.getId()));
    }

    @Test
    public void deleteGraveWithSqlExceptionThrown() throws SQLException {
        Grave grave = newLesson().build();
        manager.createGrave(grave);
        testExpectedServiceFailureException((graveManager) -> graveManager.deleteGrave(grave));
    }

    @Test
    public void findAllGravesWithSqlExceptionThrown() throws SQLException {
        testExpectedServiceFailureException((graveManager) -> graveManager.findAllGraves());
    }
*/
}
