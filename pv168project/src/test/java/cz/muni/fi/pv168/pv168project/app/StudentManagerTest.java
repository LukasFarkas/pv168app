/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import cz.muni.fi.pv168.common.DBUtils;
import cz.muni.fi.pv168.common.EntityNotFoundException;
import cz.muni.fi.pv168.common.IllegalEntityException;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.common.ValidationException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.sql.SQLException;
import java.time.LocalDate;
import static java.time.Month.OCTOBER;
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


public class StudentManagerTest {
    
    private StudentManager manager;
    private DataSource ds;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    //--------------------------------------------------------------------------
    // Test initialization
    //--------------------------------------------------------------------------

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        // we will use in memory database
        ds.setDatabaseName("memory:studentmgr-test");
        // database is created automatically if it does not exist yet
        ds.setCreateDatabase("create");
        return ds;
    }

    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,StudentManager.class.getResource("createTables.sql"));
        manager = new StudentManager();
        manager.setDataSource(ds);
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,StudentManager.class.getResource("dropTables.sql"));
    }
    
    
    //--------------------------------------------------------------------------
    // Preparing test data
    //--------------------------------------------------------------------------

    private Student sampleStudentJozko () {
        Student s = new Student();
        s.setFullName("Jozko Mamradmrkvu");
        s.setSkill(2);
        s.setPrice(BigDecimal.valueOf(500.00).setScale(2));
        s.setRegion(Region.INDIA);
        return s;
    }
    
    private Student sampleStudentPetka () {
        Student s = new Student();
        s.setFullName("Petka Plastova");
        s.setSkill(1);
        s.setPrice(BigDecimal.valueOf(200.00).setScale(2));
        s.setRegion(Region.ENGLAND);
        return s;
    }
    
    //--------------------------------------------------------------------------
    // Testing creating and getting students
    //--------------------------------------------------------------------------
    
    @Test
    public void createStudent() {
        Student body = sampleStudentJozko();
        manager.createStudent(body);

        Long bodyId = body.getId();
        assertThat(bodyId).isNotNull();

        assertThat(manager.getStudent(bodyId))
                .isNotSameAs(body)
                .isEqualToComparingFieldByField(body);
    }
    
    @Test
    public void findAllStudents() {

        assertThat(manager.findAllStudents()).isEmpty();

        Student jozko = sampleStudentJozko();
        Student petka = sampleStudentPetka();

        manager.createStudent(jozko);
        manager.createStudent(petka);

        assertThat(manager.findAllStudents())
                .usingFieldByFieldElementComparator()
                .containsOnly(jozko,petka);
    }

    // Test exception with expected parameter of @Test annotation
    // it does not allow to specify exact place where the exception
    // is expected, therefor it is suitable only for simple single line tests
    @Test(expected = IllegalArgumentException.class)
    public void createNullStudent() {
        manager.createStudent(null);
    }

    // Test exception with ExpectedException @Rule
    @Test
    public void createStudentWithExistingId() {
        Student body = sampleStudentJozko();
        body.setId(1L);
        expectedException.expect(IllegalEntityException.class);
        manager.createStudent(body);
    }

    // Test exception using AssertJ assertThatThrownBy() method
    // this requires Java 8 due to using lambda expression
    @Test
    public void createStudentWithNullName() {
        Student body = sampleStudentJozko();
        body.setFullName(null);
        assertThatThrownBy(() -> manager.createStudent(body))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    public void createStudentWithNullRegion() {
        Student body = sampleStudentJozko();
        body.setRegion(null);
        assertThatThrownBy(() -> manager.createStudent(body))
                .isInstanceOf(ValidationException.class);
    }

    // This and next test are testing special cases with border values
    // Body died one day before born is not allowed ...
    @Test
    public void createStudentWithNegativeLevel() {
        Student body = sampleStudentJozko();
        body.setSkill(-4);
        assertThatThrownBy(() -> manager.createStudent(body))
                .isInstanceOf(ValidationException.class);
    }

    // ... while the body died and born at the same day are allowed
    @Test
    public void createStudentWithNegativePrice() {
        Student body = sampleStudentJozko();
        body.setPrice(BigDecimal.valueOf(10.05).negate().setScale(2));
        expectedException.expect(ValidationException.class);
        manager.createStudent(body);
        //assertThatThrownBy(() -> manager.createStudent(body)).isInstanceOf(ValidationException.class);
    }

    //--------------------------------------------------------------------------
    // Testing update
    //--------------------------------------------------------------------------
    
    
    @Test
    public void updateStudentLevel() {
        // Let us create two graves, one will be used for testing the update
        // and another one will be used for verification that other objects are
        // not affected by update operation
        Student j = sampleStudentJozko();
        Student p = sampleStudentPetka();
        manager.createStudent(j);
        manager.createStudent(p);

        // Performa the update operation ...
        j.setSkill(10);

        // ... and save updated grave to database
        manager.updateStudent(j);

        // Check if grave was properly updated
        assertThat(manager.getStudent(j.getId()))
                .isEqualToComparingFieldByField(j);
        // Check if updates didn't affected other records
        assertThat(manager.getStudent(p.getId()))
                .isEqualToComparingFieldByField(p);
    }
    
    @FunctionalInterface
    private static interface Operation<T> {
        void callOn(T subjectOfOperation);
    }
    
    private void testUpdateStudent(Operation<Student> updateOperation) {
        Student j = sampleStudentJozko();
        Student p = sampleStudentPetka();
        manager.createStudent(j);
        manager.createStudent(p);

        updateOperation.callOn(j);

        manager.updateStudent(j);
        assertThat(manager.getStudent(j.getId()))
                .isEqualToComparingFieldByField(j);
        // Check if updates didn't affected other records
        assertThat(manager.getStudent(p.getId()))
                .isEqualToComparingFieldByField(p);
    }
    
    @Test
    public void updateStudentRegion() {
        testUpdateStudent((grave) -> grave.setRegion(Region.RUSSIAN));
    }

    @Test
    public void updateStudentPrice() {
        testUpdateStudent((grave) -> grave.setPrice(BigDecimal.valueOf(1000.10).setScale(2)));
    }

    @Test
    public void updateStudentName() {
        testUpdateStudent((grave) -> grave.setFullName("Anakondaq"));
    }

    @Test
    public void updateStudentPriceToNegative() {
        expectedException.expect(ValidationException.class);
        testUpdateStudent((grave) -> grave.setPrice(BigDecimal.valueOf(10.00).negate().setScale(2)));
    }

    // Test also if attemtpt to call update with invalid grave throws
    // the correct exception.

    @Test(expected = IllegalArgumentException.class)
    public void updateNullStudent() {
        manager.updateStudent(null);
    }

    @Test
    public void updateStudentWithNullId() {
        Student grave = sampleStudentJozko();
        grave.setId(null);
        expectedException.expect(IllegalEntityException.class);
        manager.updateStudent(grave);
    }
    
    //--------------------------------------------------------------------------
    // Testing deletion
    //--------------------------------------------------------------------------

    @Test
    public void deleteStudent() {

        Student g1 = sampleStudentJozko();
        Student g2 = sampleStudentPetka();
        manager.createStudent(g1);
        manager.createStudent(g2);

        assertThat(manager.getStudent(g1.getId())).isNotNull();
        assertThat(manager.getStudent(g2.getId())).isNotNull();

        manager.deleteStudent(g1);

        assertThat(manager.getStudent(g1.getId())).isNull();
        assertThat(manager.getStudent(g2.getId())).isNotNull();

    }

    // Test also if attemtpt to call delete with invalid parameter throws
    // the correct exception.

    @Test(expected = IllegalArgumentException.class)
    public void deleteNullStudent() {
        manager.deleteStudent(null);
    }

    @Test
    public void deleteStudentWithNullId() {
        Student grave = sampleStudentJozko();
        grave.setId(null);
        expectedException.expect(IllegalEntityException.class);
        manager.deleteStudent(grave);
    }

    @Test
    public void deleteStudentWithNonExistingId() {
        Student grave = sampleStudentJozko();
        grave.setId(1L);
        expectedException.expect(IllegalEntityException.class);
        manager.deleteStudent(grave);
    }

    //--------------------------------------------------------------------------
    // Tests if GraveManager methods throws ServiceFailureException in case of
    // DB operation failure
    //--------------------------------------------------------------------------

    @Test
    public void createStudentWithSqlExceptionThrown() throws SQLException {
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
        Student grave = sampleStudentJozko();

        // Try to call Manager.createGrave(Grave) method and expect that
        // exception will be thrown
        assertThatThrownBy(() -> manager.createStudent(grave))
                // Check that thrown exception is ServiceFailureException
                .isInstanceOf(ServiceFailureException.class)
                // Check if cause is properly set
                .hasCause(sqlException);
    }

    // Now we want to test also other methods of GraveManager. To avoid having
    // couple of method with lots of duplicit code, we will use the similar
    // approach as with testUpdateGrave(Operation) method.

    private void testExpectedServiceFailureException(Operation<StudentManager> operation) throws SQLException {
        SQLException sqlException = new SQLException();
        DataSource failingDataSource = mock(DataSource.class);
        when(failingDataSource.getConnection()).thenThrow(sqlException);
        manager.setDataSource(failingDataSource);
        assertThatThrownBy(() -> operation.callOn(manager))
                .isInstanceOf(ServiceFailureException.class)
                .hasCause(sqlException);
    }

    @Test
    public void updateStudentWithSqlExceptionThrown() throws SQLException {
        Student grave = sampleStudentJozko();
        manager.createStudent(grave);
        testExpectedServiceFailureException((graveManager) -> graveManager.updateStudent(grave));
    }

    @Test
    public void getStudentWithSqlExceptionThrown() throws SQLException {
        Student grave = sampleStudentJozko();
        manager.createStudent(grave);
        testExpectedServiceFailureException((graveManager) -> graveManager.getStudent(grave.getId()));
    }

    @Test
    public void deleteStudentWithSqlExceptionThrown() throws SQLException {
        Student grave = sampleStudentJozko();
        manager.createStudent(grave);
        testExpectedServiceFailureException((graveManager) -> graveManager.deleteStudent(grave));
    }

    @Test
    public void findAllStudentsWithSqlExceptionThrown() throws SQLException {
        testExpectedServiceFailureException((studentManager) -> studentManager.findAllStudents());
    }
    
    
/*
    private StudentManager manager;
    private DataSource datasource;

    @Rule
    // attribute annotated with @Rule annotation must be public :-(
    public ExpectedException expectedException = ExpectedException.none();

    
    @Before
    public void setUp() throws SQLException {
        datasource = prepareDataSource();
        try (Connection connection = datasource.getConnection()) {
            connection.prepareStatement("CREATE TABLE STUDENT ("
                    + "id bigint primary key generated always as identity,"
                    + "fullName varchar(50),"
                    + "level int,"
                    + "details varchar(50))").executeUpdate();
        }
        
        manager = new StudentManager(datasource);
    }
    
    
    @After
    public void tearDown() throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            connection.prepareStatement("DROP TABLE STUDENT").executeUpdate();
        }
    }

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        //we will use in memory database
        ds.setDatabaseName("memory:studentmanager-test");
        ds.setCreateDatabase("create");
        return ds;
    }
    
    
    @Test
    public void createStudent() {
        Student student = newStudent("Mr Peepants", "", 4);
        manager.createStudent(student);

        Long studentId = student.getId();
        assertNotNull(studentId);
        Student result = manager.getStudent(studentId);
        assertEquals(student, result);
    }

    @Test
    public void deleteStudent() {
        Student t1 = newStudent("Mr random", "", 2);
        Student t2 = newStudent("Mrs surely", "is coo-coo", 5);

        manager.createStudent(t1);
        manager.createStudent(t2);

        assertNotNull(manager.getStudent(t1.getId()));
        assertNotNull(manager.getStudent(t2.getId()));

        manager.deleteStudent(manager.getStudent(t1.getId()));

        assertNull(manager.getStudent(t1.getId()));
        assertNotNull(manager.getStudent(t2.getId()));

    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createStudentWithNull() {
            manager.createStudent(null);       
    }
    

    @Test
    public void deleteStudentWithNonExistingId() {
        Student student = newStudent("","",2);
        student.setId(1L);
        expectedException.expect(EntityNotFoundException.class);
        manager.deleteStudent(student);
    }

    private static Student newStudent(String fullName, String details, int level) {
        Student student = new Student();
        student.setFullName(fullName);
        student.setDetails(details);
        student.setLevel(level);
        return student;
    }
*/
}
