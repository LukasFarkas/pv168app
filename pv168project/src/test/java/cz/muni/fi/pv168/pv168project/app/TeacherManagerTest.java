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
public class TeacherManagerTest {
    private TeacherManager manager;

    @Rule
    // attribute annotated with @Rule annotation must be public :-(
    public ExpectedException expectedException = ExpectedException.none();
    
    @Before
    public void setUp() throws Exception {
        TeacherManager manager = new TeacherManager();
    }

    @Test
    public void createTeacher() {
        Teacher teacher = newTeacher("Mr Peepants", "", 4, (long) 5);
        manager.createTeacher(teacher);

        Long teacherId = teacher.getTeacherId();
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

        assertNotNull(manager.getTeacher(t1.getTeacherId()));
        assertNotNull(manager.getTeacher(t2.getTeacherId()));

        manager.deleteTeacher(t1.teacherId);

        assertNull(manager.getTeacher(t1.getTeacherId()));
        assertNotNull(manager.getTeacher(t2.getTeacherId()));

        
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void createTeacherWithNull() {
            manager.createTeacher(null);       
    }
    

    @Test
    public void deleteTeacherWithNonExistingId() {
        Teacher teacher = newTeacher("","",2,(long) 10);
        teacher.setTeacherId(1L);
        expectedException.expect(EntityNotFoundException.class);
        manager.deleteTeacher(teacher.getTeacherId());
    }
    
    
    private static Teacher newTeacher(String fullName, String details, int level, long teacherId) {
        Teacher teacher = new Teacher();
        teacher.setFullName(fullName);
        teacher.setDetails(details);
        teacher.setLevel(level);
        teacher.setTeacherId(teacherId);
        return teacher;
    }
    
}
