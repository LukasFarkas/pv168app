/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SwingWorkersForUI;

import UI.LessonTableModel;
import cz.muni.fi.pv168.common.DataSourceException;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.pv168project.app.Lesson;
import cz.muni.fi.pv168.pv168project.app.LessonManager;
import cz.muni.fi.pv168.pv168project.app.Student;
import cz.muni.fi.pv168.pv168project.app.StudentManager;
import cz.muni.fi.pv168.pv168project.app.Teacher;
import cz.muni.fi.pv168.pv168project.app.TeacherManager;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

/**
 *
 * @author tomf
 */
public class ShowLessonsSwingWorker extends SwingWorker<Integer, Integer> {
    private LessonManager lm;
    private StudentManager sm;
    private TeacherManager tm;
    private List<Lesson> lessons;
    private List<Student> students;
    private List<Teacher> teachers;
    private JTable tableLessons;
    private JScrollPane scrollPaneLessons;
    private DataSource ds;
    //private Boolean isStudent = null;
    private boolean opSuccess = false;
    private JDialog dialogError;
    private JLabel labelError;
    private ResourceBundle rb;
    
    public ShowLessonsSwingWorker (DataSource ds, JTable tableLessons, JScrollPane scrollPaneLessons, JDialog dialogError, JLabel labelError, ResourceBundle rb) {
        this.lm = new LessonManager(ds);
        //lm.setDataSource(ds);
        this.sm = new StudentManager(ds);
        //sm.setDataSource(ds);
        this.tm = new TeacherManager(ds);
        //tm.setDataSource(ds);
        this.tableLessons = tableLessons;
        this.scrollPaneLessons = scrollPaneLessons;
        this.ds = ds;
        this.dialogError = dialogError;
        this.labelError = labelError;
        this.rb = rb;
    }
    
    @Override
    protected Integer doInBackground() {
        try {
            lessons = lm.findAllLessons();
            students = sm.findAllStudents();
            teachers = tm.findAllTeachers();
            opSuccess = true;
        } catch (DataSourceException | ServiceFailureException e) {
            labelError.setText(e.getMessage());
            dialogError.pack();
            dialogError.setVisible(true);
            opSuccess = false;
        }
        return 0;
    }
    
    @Override
    protected  void done () {
        if (opSuccess) {
            LessonTableModel model = new LessonTableModel(lessons, students, teachers, ds, rb);
            tableLessons.setModel(model);
            tableLessons.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tableLessons.setRowSelectionAllowed(true);
            tableLessons.setCellSelectionEnabled(false);
            scrollPaneLessons.setViewportView(tableLessons);
        }  
    }
    
}
