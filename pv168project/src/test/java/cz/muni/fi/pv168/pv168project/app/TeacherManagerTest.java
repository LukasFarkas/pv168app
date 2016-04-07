/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import cz.muni.fi.pv168.common.EntityNotFoundException;
import cz.muni.fi.pv168.common.DBUtils;
import cz.muni.fi.pv168.common.IllegalEntityException;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.common.ValidationException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeacherManagerTest {
    
    private TeacherManager manager;
    private DataSource ds;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    //--------------------------------------------------------------------------
    // Test initialization
    //--------------------------------------------------------------------------

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        // we will use in memory database
        ds.setDatabaseName("memory:teachermgr-test");
        // database is created automatically if it does not exist yet
        ds.setCreateDatabase("create");
        return ds;
    }

    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,TeacherManager.class.getResource("createTables.sql"));
        manager = new TeacherManager();
        manager.setDataSource(ds);
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,TeacherManager.class.getResource("dropTables.sql"));
    }
    
    
    //--------------------------------------------------------------------------
    // Preparing test data
    //--------------------------------------------------------------------------

    private Teacher sampleTeacherBozkov () {
        Teacher s = new Teacher();
        s.setFullName("Bozkov Bolehlav");
        s.setLevel(5);
        s.setPrice(BigDecimal.valueOf(900.00).setScale(2));
        s.setRegion(Region.INDIA);
        return s;
    }
    
    private Teacher sampleTeacherTequila () {
        Teacher s = new Teacher();
        s.setFullName("Tequila Blanco");
        s.setLevel(7);
        s.setPrice(BigDecimal.valueOf(100.00).setScale(2));
        s.setRegion(Region.NORTH_AMERICA);
        return s;
    }
    
    //--------------------------------------------------------------------------
    // Testing creating and getting Teachers
    //--------------------------------------------------------------------------
    
    @Test
    public void createTeacher() {
        Teacher body = sampleTeacherBozkov();
        manager.createTeacher(body);

        Long bodyId = body.getId();
        assertThat(bodyId).isNotNull();

        assertThat(manager.getTeacher(bodyId))
                .isNotSameAs(body)
                .isEqualToComparingFieldByField(body);
    }
    
    @Test
    public void findAllTeachers() {

        assertThat(manager.findAllTeachers()).isEmpty();

        Teacher Bozkov = sampleTeacherBozkov();
        Teacher Tequila = sampleTeacherTequila();

        manager.createTeacher(Bozkov);
        manager.createTeacher(Tequila);

        assertThat(manager.findAllTeachers())
                .usingFieldByFieldElementComparator()
                .containsOnly(Bozkov,Tequila);
    }

    // Test exception with expected parameter of @Test annotation
    // it does not allow to specify exact place where the exception
    // is expected, therefor it is suitable only for simple single line tests
    @Test(expected = IllegalArgumentException.class)
    public void createNullTeacher() {
        manager.createTeacher(null);
    }

    // Test exception with ExpectedException @Rule
    @Test
    public void createTeacherWithExistingId() {
        Teacher body = sampleTeacherBozkov();
        body.setId(1L);
        expectedException.expect(IllegalEntityException.class);
        manager.createTeacher(body);
    }

    // Test exception using AssertJ assertThatThrownBy() method
    // this requires Java 8 due to using lambda expression
    @Test
    public void createTeacherWithNullName() {
        Teacher body = sampleTeacherBozkov();
        body.setFullName(null);
        assertThatThrownBy(() -> manager.createTeacher(body))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    public void createTeacherWithNullRegion() {
        Teacher body = sampleTeacherBozkov();
        body.setRegion(null);
        assertThatThrownBy(() -> manager.createTeacher(body))
                .isInstanceOf(ValidationException.class);
    }

    // This and next test are testing special cases with border values
    // Body died one day before born is not allowed ...
    @Test
    public void createTeacherWithNegativeLevel() {
        Teacher body = sampleTeacherBozkov();
        body.setLevel(-4);
        assertThatThrownBy(() -> manager.createTeacher(body))
                .isInstanceOf(ValidationException.class);
    }

    // ... while the body died and born at the same day are allowed
    @Test
    public void createTeacherWithNegativePrice() {
        Teacher body = sampleTeacherBozkov();
        body.setPrice(BigDecimal.valueOf(10.05).negate().setScale(2));
        expectedException.expect(ValidationException.class);
        manager.createTeacher(body);
        //assertThatThrownBy(() -> manager.createTeacher(body)).isInstanceOf(ValidationException.class);
    }

    //--------------------------------------------------------------------------
    // Testing update
    //--------------------------------------------------------------------------
    
    
    @Test
    public void updateTeacherColumn() {
        // Let us create two graves, one will be used for testing the update
        // and another one will be used for verification that other objects are
        // not affected by update operation
        Teacher j = sampleTeacherBozkov();
        Teacher p = sampleTeacherTequila();
        manager.createTeacher(j);
        manager.createTeacher(p);

        // Performa the update operation ...
        j.setLevel(10);

        // ... and save updated grave to database
        manager.updateTeacher(j);

        // Check if grave was properly updated
        assertThat(manager.getTeacher(j.getId()))
                .isEqualToComparingFieldByField(j);
        // Check if updates didn't affected other records
        assertThat(manager.getTeacher(p.getId()))
                .isEqualToComparingFieldByField(p);
    }
    
    @FunctionalInterface
    private static interface Operation<T> {
        void callOn(T subjectOfOperation);
    }
    
    private void testUpdateTeacher(Operation<Teacher> updateOperation) {
        Teacher j = sampleTeacherBozkov();
        Teacher p = sampleTeacherTequila();
        manager.createTeacher(j);
        manager.createTeacher(p);

        updateOperation.callOn(j);

        manager.updateTeacher(j);
        assertThat(manager.getTeacher(j.getId()))
                .isEqualToComparingFieldByField(j);
        // Check if updates didn't affected other records
        assertThat(manager.getTeacher(p.getId()))
                .isEqualToComparingFieldByField(p);
    }
    
    @Test
    public void updateTeacherRegion() {
        testUpdateTeacher((grave) -> grave.setRegion(Region.RUSSIAN));
    }

    @Test
    public void updateTeacherPrice() {
        testUpdateTeacher((grave) -> grave.setPrice(BigDecimal.valueOf(1000.10).setScale(2)));
    }

    @Test
    public void updateTeacherName() {
        testUpdateTeacher((grave) -> grave.setFullName("Blabaah"));
    }

    @Test
    public void updateTeacherPriceToNegative() {
        expectedException.expect(ValidationException.class);
        testUpdateTeacher((grave) -> grave.setPrice(BigDecimal.valueOf(10.00).negate().setScale(2)));
    }

    // Test also if attemtpt to call update with invalid grave throws
    // the correct exception.

    @Test(expected = IllegalArgumentException.class)
    public void updateNullTeacher() {
        manager.updateTeacher(null);
    }

    @Test
    public void updateTeacherWithNullId() {
        Teacher grave = sampleTeacherBozkov();
        grave.setId(null);
        expectedException.expect(IllegalEntityException.class);
        manager.updateTeacher(grave);
    }
    
    //--------------------------------------------------------------------------
    // Testing deletion
    //--------------------------------------------------------------------------

    @Test
    public void deleteTeacher() {

        Teacher g1 = sampleTeacherBozkov();
        Teacher g2 = sampleTeacherTequila();
        manager.createTeacher(g1);
        manager.createTeacher(g2);

        assertThat(manager.getTeacher(g1.getId())).isNotNull();
        assertThat(manager.getTeacher(g2.getId())).isNotNull();

        manager.deleteTeacher(g1);

        assertThat(manager.getTeacher(g1.getId())).isNull();
        assertThat(manager.getTeacher(g2.getId())).isNotNull();

    }

    // Test also if attemtpt to call delete with invalid parameter throws
    // the correct exception.

    @Test(expected = IllegalArgumentException.class)
    public void deleteNullTeacher() {
        manager.deleteTeacher(null);
    }

    @Test
    public void deleteTeacherWithNullId() {
        Teacher grave = sampleTeacherBozkov();
        grave.setId(null);
        expectedException.expect(IllegalEntityException.class);
        manager.deleteTeacher(grave);
    }

    @Test
    public void deleteTeacherWithNonExistingId() {
        Teacher grave = sampleTeacherBozkov();
        grave.setId(1L);
        expectedException.expect(IllegalEntityException.class);
        manager.deleteTeacher(grave);
    }

    //--------------------------------------------------------------------------
    // Tests if GraveManager methods throws ServiceFailureException in case of
    // DB operation failure
    //--------------------------------------------------------------------------

    @Test
    public void createTeacherWithSqlExceptionThrown() throws SQLException {
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
        Teacher grave = sampleTeacherBozkov();

        // Try to call Manager.createGrave(Grave) method and expect that
        // exception will be thrown
        assertThatThrownBy(() -> manager.createTeacher(grave))
                // Check that thrown exception is ServiceFailureException
                .isInstanceOf(ServiceFailureException.class)
                // Check if cause is properly set
                .hasCause(sqlException);
    }

    // Now we want to test also other methods of GraveManager. To avoid having
    // couple of method with lots of duplicit code, we will use the similar
    // approach as with testUpdateGrave(Operation) method.

    private void testExpectedServiceFailureException(Operation<TeacherManager> operation) throws SQLException {
        SQLException sqlException = new SQLException();
        DataSource failingDataSource = mock(DataSource.class);
        when(failingDataSource.getConnection()).thenThrow(sqlException);
        manager.setDataSource(failingDataSource);
        assertThatThrownBy(() -> operation.callOn(manager))
                .isInstanceOf(ServiceFailureException.class)
                .hasCause(sqlException);
    }

    @Test
    public void updateTeacherWithSqlExceptionThrown() throws SQLException {
        Teacher grave = sampleTeacherBozkov();
        manager.createTeacher(grave);
        testExpectedServiceFailureException((graveManager) -> graveManager.updateTeacher(grave));
    }

    @Test
    public void getTeacherWithSqlExceptionThrown() throws SQLException {
        Teacher grave = sampleTeacherBozkov();
        manager.createTeacher(grave);
        testExpectedServiceFailureException((graveManager) -> graveManager.getTeacher(grave.getId()));
    }

    @Test
    public void deleteTeacherWithSqlExceptionThrown() throws SQLException {
        Teacher grave = sampleTeacherBozkov();
        manager.createTeacher(grave);
        testExpectedServiceFailureException((graveManager) -> graveManager.deleteTeacher(grave));
    }

    @Test
    public void findAllTeachersWithSqlExceptionThrown() throws SQLException {
        testExpectedServiceFailureException((TeacherManager) -> TeacherManager.findAllTeachers());
    }
    
   /* 
    private TeacherManager manager;
    
    private DataSource ds;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        
        ds.setDatabaseName("memory:lessonmanager-test");
        
        ds.setCreateDatabase("create");
        return ds;
    }

    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,LessonManager.class.getResource("createTables.sql"));
        manager = new TeacherManager();
        manager.setDataSource(ds);
    }

    @After
    public void tearDown() throws SQLException {
        // Drop tables after each test
        DBUtils.executeSqlScript(ds,LessonManager.class.getResource("dropTables.sql"));
    }
    
    //Simple tests:

    @Test
    public void createTeacher() {
        Teacher teacher = newTeacher("Mr Peepants", "", 4, (long) 5);
        manager.createTeacher(teacher);

        Long teacherId = teacher.getId();
        assertNotNull(teacherId);
        Teacher result = manager.getTeacher(teacherId);
        assertEquals(teacher, result);
    }
    
    @Test
    public void deleteTeacher() {
        Teacher t1 = newTeacher("Mr random", "", 2, (long) 3);
        Teacher t2 = newTeacher("Mrs surely", "is coo-coo", 5, (long) 2);
        
        manager.createTeacher(t1);
        manager.createTeacher(t2);

        assertNotNull(manager.getTeacher(t1.getId()));
        assertNotNull(manager.getTeacher(t2.getId()));

        manager.deleteTeacher(t1);

        assertNull(manager.getTeacher(t1.getId()));
        assertNotNull(manager.getTeacher(t2.getId()));

        
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createTeacherWithNull() {
            manager.createTeacher(null);       
    }
    

    @Test
    public void deleteTeacherWithNonExistingId() {
        Teacher teacher = newTeacher("","",2,(long) 10);
        teacher.setId(1L);
        expectedException.expect(EntityNotFoundException.class);
        manager.deleteTeacher(teacher);
    }
    
    
    private static Teacher newTeacher(String fullName, String details, int level, long teacherId) {
        Teacher teacher = new Teacher();
        teacher.setFullName(fullName);
        teacher.setDetails(details);
        teacher.setLevel(level);
        teacher.setId(teacherId);
        return teacher;
    }
    */
}
