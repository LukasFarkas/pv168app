/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.sql.SQLException;
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

    @Rule
    // attribute annotated with @Rule annotation must be public :-(
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        StudentManager manager = new StudentManager();
    }

    @Test
    public void createStudent() {
        Student student = newStudent("Mr Peepants", "", 4, 5);
        manager.createStudent(student);

        Long studentId = student.getStudentId();
        assertNotNull(studentId);
        Student result = manager.getStudent(studentId);
        assertEquals(student, result);
    }

    @Test
    public void deleteStudent() {
        Student t1 = newStudent("Mr random", "", 2, (long) 3);
        Student t2 = newStudent("Mrs surely", "is coo-coo", 5, (long) 2);

        manager.createStudent(t1);
        manager.createStudent(t2);

        assertNotNull(manager.getStudent(t1.getStudentId()));
        assertNotNull(manager.getStudent(t2.getStudentId()));

        manager.deleteStudent(t1.studentId);

        assertNull(manager.getStudent(t1.getStudentId()));
        assertNotNull(manager.getStudent(t2.getStudentId()));

    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createStudentWithNull() {
            manager.createStudent(null);       
    }
    

    @Test
    public void deleteStudentWithNonExistingId() {
        Student student = newStudent("","",2,10);
        student.setStudentId(1L);
        expectedException.expect(EntityNotFoundException.class);
        manager.deleteStudent(student.getStudentId());
    }

    private static Student newStudent(String fullName, String details, int level, long studentId) {
        Student student = new Student();
        student.setFullName(fullName);
        student.setDetails(details);
        student.setLevel(level);
        student.setStudentId(studentId);
        return student;
    }

}
