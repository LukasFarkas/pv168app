/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SwingWorkersForUI;

import cz.muni.fi.pv168.common.DataSourceException;
import cz.muni.fi.pv168.common.IllegalEntityException;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.pv168project.app.Lesson;
import cz.muni.fi.pv168.pv168project.app.LessonManager;
import cz.muni.fi.pv168.pv168project.app.Student;
import cz.muni.fi.pv168.pv168project.app.StudentManager;
import cz.muni.fi.pv168.pv168project.app.Teacher;
import cz.muni.fi.pv168.pv168project.app.TeacherManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 *
 * @author tomf
 */
public class DeleteEntitySwingWorker extends SwingWorker<Integer, Integer> {
    // managers
    private StudentManager sm;
    private TeacherManager tm;
    private LessonManager lm;
    // button to start Update on entity (student or teacher) and lessons
    private JButton buttonShowAllEntities;
    private JButton buttonLessonDisplayAll;
    private Long id;
    private JLabel labelEntityIDValue;
    private JDialog dialogInfo;
    private boolean opSuccess = false;
    private Boolean isStudent = null;
    private JDialog dialogError;
    private JLabel labelError;
    private ResourceBundle rb;
    
    public DeleteEntitySwingWorker (DataSource ds, Long id, boolean isStudent, JButton buttonShowAllEntities,
            JButton buttonLessonDisplayAll, JLabel labelEntityIDValue, JDialog dialogInfo,
            JDialog dialogError, JLabel labelError, ResourceBundle rb) {
        if (isStudent) {
            this.sm = new StudentManager(ds);
            //sm.setDataSource(ds);
        } else {
            this.tm = new TeacherManager(ds);
            //tm.setDataSource(ds);
        }
        this.lm = new LessonManager(ds);
        //lm.setDataSource(ds);
        this.buttonShowAllEntities = buttonShowAllEntities;
        this.buttonLessonDisplayAll = buttonLessonDisplayAll;
        this.rb = rb;
        this.id = id;
        this.labelEntityIDValue = labelEntityIDValue;
        this.dialogInfo = dialogInfo;
        this.dialogError = dialogError;
        this.labelError = labelError;
        this.isStudent = isStudent;
    }
    
    @Override
    protected Integer doInBackground() {
        if (Boolean.TRUE.equals(isStudent)) {
            try {
                Student student = sm.getStudent(id);
                List<Lesson> lessons = lm.getLesson(student);
                for (int i = 0; i< lessons.size(); i++) {
                    lm.deleteLesson(lessons.get(i));
                }
                sm.deleteStudent(student);
                opSuccess = true;
            } catch (DataSourceException | IllegalArgumentException | IllegalEntityException | ServiceFailureException e) {
                labelError.setText(e.getMessage());
                dialogError.pack();
                dialogError.setVisible(true);
                opSuccess = false;
            }
            
            
        }
        else if (Boolean.FALSE.equals(isStudent)) {
            try {
                Teacher teacher = tm.getTeacher(id);
                List<Lesson> lessons = lm.getLesson(teacher);
                for (int i = 0; i< lessons.size(); i++) {
                    lm.deleteLesson(lessons.get(i));
                }
                tm.deleteTeacher(teacher);
                opSuccess = true;
            } catch (DataSourceException | IllegalArgumentException | IllegalEntityException | ServiceFailureException e) {
                labelError.setText(e.getMessage());
                dialogError.pack();
                dialogError.setVisible(true);
                opSuccess = false;
            }
        } 
        else {
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
            labelEntityIDValue.setText(rb.getString("PairAppUIv2.UnknownID.text"));

            //aktualizovani listu studentu
            for (ActionListener a : buttonShowAllEntities.getActionListeners()) {
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
                });
            }

            for (ActionListener a : buttonLessonDisplayAll.getActionListeners()) {
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
                });
            }
            dialogInfo.pack();
            dialogInfo.setVisible(true);
        }
        
    }
}
