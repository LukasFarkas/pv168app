/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 *
 * @author L
 */
public class StudentManagerTest {

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

}
