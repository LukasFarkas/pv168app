/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SwingWorkersForUI;

import cz.muni.fi.pv168.common.DataSourceException;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.pv168project.app.LessonManager;
import cz.muni.fi.pv168.pv168project.app.Student;
import cz.muni.fi.pv168.pv168project.app.StudentManager;
import cz.muni.fi.pv168.pv168project.app.Teacher;
import cz.muni.fi.pv168.pv168project.app.TeacherManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingWorker;

/**
 *
 * @author tomf
 */
public class SaveNewLessonSwingWorker extends SwingWorker<Integer, Integer> {
    private LessonManager lm;
    private StudentManager sm;
    private TeacherManager tm;
    private JLabel teacherOrStudent;
    private JList availableEntities;
    private JLabel selectedEntityId;
    private JDialog addingNewLesson;
    private JDialog dialogInfo;
    private JButton buttonStudentDisplayLesson;
    private JButton buttonTeacherDisplayLesson;
    //private Boolean isStudent = null;
    private boolean opSuccess = false;
    private JDialog dialogError;
    private JLabel labelError;
    private ResourceBundle rb;
    private Boolean isStudent = null;
    
    public SaveNewLessonSwingWorker (DataSource ds, JLabel teacherOrStudent, JList availableEntities, 
            JLabel selectedEntityId, JDialog addingNewLesson, JDialog dialogInfo, JButton buttonStudentDisplayLesson, 
            JButton buttonTeacherDisplayLesson, JDialog dialogError, JLabel labelError, ResourceBundle rb) {
        this.lm = new LessonManager(ds);
        //lm.setDataSource(ds);
        this.sm = new StudentManager(ds);
        //sm.setDataSource(ds);
        this.tm = new TeacherManager(ds);
        //tm.setDataSource(ds);
        this.teacherOrStudent = teacherOrStudent;
        this.availableEntities = availableEntities;
        this.selectedEntityId = selectedEntityId;
        this.addingNewLesson = addingNewLesson;
        this.dialogInfo = dialogInfo;
        this.dialogError = dialogError;
        this.labelError = labelError;
        this.buttonStudentDisplayLesson = buttonStudentDisplayLesson;
        this.buttonTeacherDisplayLesson = buttonTeacherDisplayLesson;
        this.rb = rb;
        if (teacherOrStudent.getText().equals(rb.getString("PairAppUIv2.teachers"))){
            isStudent = Boolean.TRUE;
        } else if (teacherOrStudent.getText().equals(rb.getString("PairAppUIv2.students"))) {
            isStudent = Boolean.FALSE;
        } 
    }
    
    @Override
    protected Integer doInBackground() {
        try {
            /*
            switch (teacherOrStudent.getText()) {
                //case "teachers":
                case rb.getString("PairAppUIv2.teachers"):
                    //searching for teacher for student
                    Teacher teacher = (Teacher) availableEntities.getSelectedValue();
                    if (teacher == null) {
                        return 1;
                    }
                    Student studentToMatch = sm.getStudent(Long.valueOf(selectedEntityId.getText()));
                    lm.makeMatch(teacher, studentToMatch);
                    break;
                case "students":
                    //searching for new student for teacher
                    Student student = (Student) availableEntities.getSelectedValue();
                    if (student == null) {
                        return 1;
                    }
                    Teacher teacherToMatch = tm.getTeacher(Long.valueOf(selectedEntityId.getText()));
                    lm.makeMatch(teacherToMatch, student);
                    break;
            }
            */
            if (isStudent) {
                //searching for teacher for student
                Teacher teacher = (Teacher) availableEntities.getSelectedValue();
                if (teacher == null) {
                    return 1;
                }
                Student studentToMatch = sm.getStudent(Long.valueOf(selectedEntityId.getText()));
                lm.makeMatch(teacher, studentToMatch);
                opSuccess = true;
            } else if (!isStudent) {
                Student student = (Student) availableEntities.getSelectedValue();
                if (student == null) {
                    return 1;
                }
                Teacher teacherToMatch = tm.getTeacher(Long.valueOf(selectedEntityId.getText()));
                lm.makeMatch(teacherToMatch, student);
                opSuccess = true;
            } else {
                opSuccess = false;
                //labelError.setText("Something went wrong. This should not happen.");
                labelError.setText(rb.getString("PairAppUIv2.jLabel_Error_Default.text"));
                dialogError.pack();
                dialogError.setVisible(true);
            }
        } catch (DataSourceException | IllegalArgumentException | ServiceFailureException e) {
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
            if (isStudent) {
               for (ActionListener a : buttonStudentDisplayLesson.getActionListeners()) {
                    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {});
                } 
            } else if (!isStudent) {
                for (ActionListener a : buttonTeacherDisplayLesson.getActionListeners()) {
                    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {});
                }
            }
            addingNewLesson.dispose();
            dialogInfo.pack();
            dialogInfo.setVisible(true);
        }
    }
}
