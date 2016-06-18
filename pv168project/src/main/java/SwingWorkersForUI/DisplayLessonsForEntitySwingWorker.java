/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SwingWorkersForUI;

import UI.LessonTableModel;
import cz.muni.fi.pv168.common.DataSourceException;
import cz.muni.fi.pv168.common.IllegalEntityException;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.pv168project.app.Lesson;
import cz.muni.fi.pv168.pv168project.app.LessonManager;
import cz.muni.fi.pv168.pv168project.app.Student;
import cz.muni.fi.pv168.pv168project.app.StudentManager;
import cz.muni.fi.pv168.pv168project.app.Teacher;
import cz.muni.fi.pv168.pv168project.app.TeacherManager;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

/**
 *
 * @author tomf
 */
public class DisplayLessonsForEntitySwingWorker extends SwingWorker<Integer, Integer>  {
    private LessonManager lm;
    private StudentManager sm;
    private TeacherManager tm;
    
    private List<Student> students;
    private List<Teacher> teachers;
    
    private DataSource ds;
    private List<Lesson> lessons;
    private JTabbedPane pane;
    private JPanel tabLessons;
    private JTable tableLessons;
    private JScrollPane scrollPaneLessons;
    private Long id;
    private Boolean isStudent = null;
    private boolean opSuccess = false;
    private JDialog dialogError;
    private JLabel labelError;
    private ResourceBundle rb;
    
    public DisplayLessonsForEntitySwingWorker (DataSource ds, Long id, boolean isStudent, JTabbedPane pane,
            JPanel tabLessons, JTable tableLessons, JScrollPane scrollPaneLessons, JDialog dialogError, JLabel labelError, ResourceBundle rb) {
        sm = new StudentManager(ds);
        //sm.setDataSource(ds);
        tm = new TeacherManager(ds);
        //tm.setDataSource(ds);
        lm = new LessonManager(ds);
        //lm.setDataSource(ds);
        this.ds = ds;
        this.pane = pane;
        this.tabLessons = tabLessons;
        this.tableLessons = tableLessons;
        this.scrollPaneLessons = scrollPaneLessons;
        this.id = id;
        this.isStudent = isStudent;
        this.dialogError = dialogError;
        this.labelError = labelError;
        this.rb = rb;
    }
    
    @Override
    protected Integer doInBackground() {
        if (Boolean.TRUE.equals(isStudent)) {
            try {
                Student st = sm.getStudent(Long.valueOf(id));
                lessons = lm.getLesson(st);
                students = new ArrayList<Student>();
                students.add(st);
                teachers = new ArrayList<Teacher>();
                for (Lesson l : lessons) {
                    teachers.add(tm.getTeacher(l.getTeacherId()));
                }
                opSuccess = true;
            } catch (DataSourceException | IllegalArgumentException | ServiceFailureException e) {
                labelError.setText(e.getMessage());
                dialogError.pack();
                dialogError.setVisible(true);
                opSuccess = false;
            }
        } else if (Boolean.FALSE.equals(isStudent)) {
            try {
                Teacher teach = tm.getTeacher(Long.valueOf(id));
                lessons = lm.getLesson(teach);
                teachers = new ArrayList<Teacher>();
                teachers.add(teach);
                students = new ArrayList<Student>();
                for (Lesson l : lessons) {
                    students.add(sm.getStudent(l.getStudentId()));
                }
                opSuccess = true;
            } catch (DataSourceException | IllegalArgumentException | ServiceFailureException e) {
                labelError.setText(e.getMessage());
                dialogError.pack();
                dialogError.setVisible(true);
                opSuccess = false;
            }
        } else {
            opSuccess = false;
            //labelError.setText("Something went wrong. This should not happen.");
            labelError.setText(rb.getString("PairAppUIv2.jLabel_Error_Default.text"));
            dialogError.pack();
            dialogError.setVisible(true);
        }
        return 0;
    }
    
    @Override
    protected  void done () {
        if (opSuccess) {
            LessonTableModel model = new LessonTableModel(lessons, students, teachers, ds, rb);
            pane.setSelectedComponent(tabLessons);
            tableLessons.setModel(model);
            tableLessons.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tableLessons.setRowSelectionAllowed(true);
            tableLessons.setCellSelectionEnabled(false);
            scrollPaneLessons.setViewportView(tableLessons);
        }
    }
}
