/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SwingWorkersForUI;

import cz.muni.fi.pv168.common.DataSourceException;
import cz.muni.fi.pv168.common.IllegalEntityException;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.common.ValidationException;
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
import javax.swing.SwingWorker;

/**
 *
 * @author tomf
 */
public class AddEntitySwingWorker extends SwingWorker<Integer, Integer> {
    
    private StudentManager sm;
    private TeacherManager tm;
    private Student student;
    private Teacher teacher;
    private JDialog dialogAddingNewStudent;
    private JButton buttonShowAllStudents;
    private JDialog dialogInfo;
    private boolean opSuccess = false;
    private Boolean isStudent = null;
    private JDialog dialogError;
    private JLabel labelError;
    private ResourceBundle rb;
    
    public AddEntitySwingWorker (DataSource ds, Student student, JDialog dialogAddingNewStudent,
            JButton buttonShowAllStudents, JDialog dialogInfo, JDialog dialogError, JLabel labelError, ResourceBundle rb) {
        this.sm = new StudentManager(ds);
        //sm.setDataSource(ds);
        this.student = student;
        this.dialogAddingNewStudent = dialogAddingNewStudent;
        this.buttonShowAllStudents = buttonShowAllStudents;
        this.dialogInfo = dialogInfo;
        this.isStudent = Boolean.TRUE;
        this.dialogError = dialogError;
        this.labelError = labelError;
        this.rb = rb;
    }
    
    public AddEntitySwingWorker (DataSource ds, Teacher teacher, JDialog dialogAddingNewStudent,
            JButton buttonShowAllStudents, JDialog dialogInfo, JDialog dialogError, JLabel labelError, ResourceBundle rb) {
        this.tm = new TeacherManager(ds);
        //tm.setDataSource(ds);
        this.dialogAddingNewStudent = dialogAddingNewStudent;
        this.buttonShowAllStudents = buttonShowAllStudents;
        this.teacher = teacher;
        this.dialogInfo = dialogInfo;
        this.isStudent = Boolean.FALSE;
        this.dialogError = dialogError;
        this.labelError = labelError;
        this.rb = rb;
    }
    
    @Override
    protected Integer doInBackground() {
        if (Boolean.TRUE.equals(isStudent)) {
            try {
                sm.createStudent(student);
                opSuccess = true;
                //throw new ValidationException("chybaaa");
            } catch (ServiceFailureException | IllegalEntityException | ValidationException | DataSourceException e) {
                labelError.setText(e.getMessage());
                dialogError.pack();
                dialogError.setVisible(true);
                opSuccess = false;
            }  
        }
        else if (Boolean.FALSE.equals(isStudent)) {
            try {
                tm.createTeacher(teacher);
                opSuccess = true;
            } catch (ServiceFailureException | IllegalEntityException | ValidationException | DataSourceException e) {
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
            dialogAddingNewStudent.dispose();
            for(ActionListener a : buttonShowAllStudents.getActionListeners()){
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {});
            }
            dialogInfo.pack();
            dialogInfo.setVisible(true);
        }
    }
}
