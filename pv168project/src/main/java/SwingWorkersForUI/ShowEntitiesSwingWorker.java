/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SwingWorkersForUI;

import UI.EntityListRenderer;
import cz.muni.fi.pv168.common.DataSourceException;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.pv168project.app.Student;
import cz.muni.fi.pv168.pv168project.app.StudentManager;
import cz.muni.fi.pv168.pv168project.app.Teacher;
import cz.muni.fi.pv168.pv168project.app.TeacherManager;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingWorker;

/**
 *
 * @author tomf
 */
public class ShowEntitiesSwingWorker extends SwingWorker<Integer, Integer> {
    
    private StudentManager sm;
    private TeacherManager tm;
    private List<Student> students;
    private List<Teacher> teachers;
    private DefaultListModel model;
    private JList list;
    private JDialog dialogInfo;
    private Boolean isStudent = null;
    private boolean opSuccess = false;
    private JDialog dialogError;
    private JLabel labelError;
    private ResourceBundle rb;
    
    public ShowEntitiesSwingWorker (DataSource ds, JList list, boolean isStudent, JDialog dialogError, JLabel labelError, ResourceBundle rb) {
        if (isStudent) {
            this.sm = new StudentManager(ds);
            //sm.setDataSource(ds);
        } else {
            this.tm = new TeacherManager(ds);
            //tm.setDataSource(ds);
        }       
        this.list = list;
        this.dialogInfo = dialogInfo;
        this.dialogError = dialogError;
        this.labelError = labelError;
        this.isStudent = isStudent;
        this.rb = rb;
    }
    
    @Override
    protected Integer doInBackground() {
        if (Boolean.TRUE.equals(isStudent)) {
            try {
                students = sm.findAllStudents();
                opSuccess = true;
            } catch (DataSourceException | ServiceFailureException e) {
                labelError.setText(e.getMessage());
                dialogError.pack();
                dialogError.setVisible(true);
                opSuccess = false;
            }

        } else if (Boolean.FALSE.equals(isStudent)) {
            try {
                teachers = tm.findAllTeachers();
                opSuccess = true;
            } catch (DataSourceException | ServiceFailureException e) {
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
            model = new DefaultListModel();
            if (tm == null) {
                for (Student st : students) {
                    model.addElement(st);
                }
            } else if (sm == null) {
                for (Teacher teach : teachers) {
                    model.addElement(teach);
                }
            }
            list.setModel(model);
            list.setCellRenderer(new EntityListRenderer());
        }
    }
    
    
}
