/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import cz.muni.fi.pv168.pv168project.app.Lesson;
import cz.muni.fi.pv168.pv168project.app.Student;
import cz.muni.fi.pv168.pv168project.app.Teacher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author tomf
 */
public class LessonTableModel extends AbstractTableModel {
        private List<Lesson> lessons;
        private HashMap<Long, String> students;
        private HashMap<Long, String> teachers;
        private DataSource ds;
        private ResourceBundle rb;
        
        /*
        public LessonTableModel () {    
        }
        
        public LessonTableModel (List<Lesson> lessons, DataSource ds) {
            this.lessons = new ArrayList<Lesson>(lessons);
            this.ds = ds;
            
        }
        */
        
        public LessonTableModel (List<Lesson> lessons, List<Student> students, List<Teacher> teachers, DataSource ds, ResourceBundle rb) {
            this.lessons = new ArrayList<Lesson>(lessons);
            this.rb = rb;
            
            this.students = new HashMap<Long, String>();
            for (Student s : students) {
                this.students.put(s.getId(), s.getFullName());
            }
            
            this.teachers = new HashMap<Long, String>();
            for (Teacher t : teachers) {
                this.teachers.put(t.getId(), t.getFullName());
            }
            this.ds = ds;
        }
        
        Class[] types = new Class [] {
            java.lang.Long.class, Student.class, Teacher.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
        };

        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }

        boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false
        };
        
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }

        @Override
        public int getRowCount() {
            if (lessons != null){
                return lessons.size();
            }
            return 0;
        }

        @Override
        public int getColumnCount() {
            return 6;
        }
        
        @Override
        public String getColumnName(int column) { //need to rewrite to bunde.properties
            //String[] columnNames = {"LessonID", "Student", "Teacher", "Skill", "Price", "Region"};
            String[] columnNames = {rb.getString("PairAppUIv2.jTable_lessons.columnModel.title0"), 
                rb.getString("PairAppUIv2.jTable_lessons.columnModel.title1"), 
                rb.getString("PairAppUIv2.jTable_lessons.columnModel.title2"), 
                rb.getString("PairAppUIv2.jTable_lessons.columnModel.title3"), 
                rb.getString("PairAppUIv2.jTable_lessons.columnModel.title4"), 
                rb.getString("PairAppUIv2.jTable_lessons.columnModel.title5") };
            return columnNames[column];
        }
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) throws IllegalArgumentException {
            /*
            StudentManager sm = new StudentManager();
            sm.setDataSource(ds);
            TeacherManager tm = new TeacherManager();
            tm.setDataSource(ds);
            */
            Lesson lesson = lessons.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return lesson.getId();
                case 1:
                    return students.get(lesson.getStudentId());
                    /*
                    Student student = sm.getStudent(lesson.getStudentId());
                    return student.getFullName();
                    */
                case 2:
                    return teachers.get(lesson.getTeacherId());
                    /*
                    Teacher teacher = tm.getTeacher(lesson.getTeacherId());
                    return teacher.getFullName();
                    */
                case 3:
                    return lesson.getSkill();
                case 4:
                    return lesson.getPrice(); 
                case 5:
                    return lesson.getRegion();
                default:
                    //throw new IllegalArgumentException("Wrong column index");
                    throw new IllegalArgumentException(rb.getString("PairAppUIv2.LessonTableModel.IllegalArgumentException.text"));
            }
        }
    }
    
