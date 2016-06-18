/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SwingWorkersForUI;

import UI.EntityListRenderer;
import cz.muni.fi.pv168.common.DataSourceException;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.pv168project.app.LessonManager;
import cz.muni.fi.pv168.pv168project.app.Student;
import cz.muni.fi.pv168.pv168project.app.Teacher;
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
public class AddLessonSwingWorker extends SwingWorker<Integer, Integer> {
    private LessonManager lm;
    private List<Teacher> availableTeachers;
    private List<Student> availableStudents;
    private Student student;
    private Teacher teacher;
    private JLabel entityName;
    private JDialog noEntityToMatch;
    private JList availableEntities;
    private JDialog addingNewLesson;
    private JLabel selectedEntityId;
    private JLabel teacherOrStudent;
    private boolean opSuccess = false;
    private Boolean isStudent = null;
    private JDialog dialogError;
    private JLabel labelError;
    private ResourceBundle rb;
    
    public AddLessonSwingWorker (DataSource ds, Student student,JLabel entityName,
            JDialog noEntityToMatch, JList availableEntities, JDialog addingNewLesson,
            JLabel selectedEntityId, JLabel teacherOrStudent, JDialog dialogError, JLabel labelError, ResourceBundle rb) {
        this.lm = new LessonManager(ds);
        //lm.setDataSource(ds);
        this.teacher = null;
        this.student = student;
        this.entityName = entityName;
        this.noEntityToMatch = noEntityToMatch;
        this.availableEntities = availableEntities;
        this.addingNewLesson = addingNewLesson;
        this.selectedEntityId = selectedEntityId;
        this.teacherOrStudent = teacherOrStudent;
        this.dialogError = dialogError;
        this.labelError = labelError;
        this.isStudent = Boolean.TRUE;
        this.rb = rb;
    }

    public AddLessonSwingWorker (DataSource ds, Teacher teacher,JLabel entityName,
            JDialog noEntityToMatch, JList availableEntities, JDialog addingNewLesson,
            JLabel selectedEntityId, JLabel teacherOrStudent, JDialog dialogError, JLabel labelError, ResourceBundle rb) {
        this.lm = new LessonManager(ds);
        //lm.setDataSource(ds);
        this.teacher = teacher;
        this.student = null;
        this.entityName = entityName;
        this.noEntityToMatch = noEntityToMatch;
        this.availableEntities = availableEntities;
        this.addingNewLesson = addingNewLesson;
        this.selectedEntityId = selectedEntityId;
        this.teacherOrStudent = teacherOrStudent;
        this.dialogError = dialogError;
        this.labelError = labelError;
        this.isStudent = Boolean.FALSE;
        this.rb = rb;
    }
    
    @Override
    protected Integer doInBackground() {
        if (Boolean.TRUE.equals(isStudent)) {
            try {
                availableTeachers = lm.findMatchForStudent(student);
                opSuccess = true;
            } catch (IllegalArgumentException | DataSourceException | ServiceFailureException e) {
                labelError.setText(e.getMessage());
                dialogError.pack();
                dialogError.setVisible(true);
                opSuccess = false;
            }
            
        }
        else if (Boolean.FALSE.equals(isStudent)) {
            try {
                availableStudents = lm.findMatchForTeacher(teacher);
                opSuccess = true;
            } catch (IllegalArgumentException | DataSourceException | ServiceFailureException e) {
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
            if (Boolean.TRUE.equals(isStudent)) {
                if (availableTeachers.isEmpty()) {
                    //entityName.setText("Student " + student.getFullName());
                    entityName.setText(rb.getString("PairAppUIv2.Student") + " " + student.getFullName());
                    noEntityToMatch.setVisible(true);
                    noEntityToMatch.pack();
                    return;
                }
                DefaultListModel model = new DefaultListModel();
                for (Teacher t : availableTeachers) {
                    model.addElement(t);
                }
                availableEntities.setModel(model);
                availableEntities.setCellRenderer(new EntityListRenderer());

                addingNewLesson.setVisible(true);
                addingNewLesson.pack();

                selectedEntityId.setText(student.getId().toString());
                //teacherOrStudent.setText("teachers");
                teacherOrStudent.setText(rb.getString("PairAppUIv2.teachers"));
            } else if (Boolean.FALSE.equals(isStudent)) {
                if (availableStudents.isEmpty()) {
                    //entityName.setText("Teacher " + teacher.getFullName());
                    entityName.setText(rb.getString("PairAppUIv2.Teacher") + " " + teacher.getFullName());
                    noEntityToMatch.setVisible(true);
                    noEntityToMatch.pack();
                    return;
                }
                DefaultListModel model = new DefaultListModel();
                for (Student s : availableStudents) {
                    model.addElement(s);
                }
                availableEntities.setModel(model);
                availableEntities.setCellRenderer(new EntityListRenderer());

                addingNewLesson.setVisible(true);
                addingNewLesson.pack();

                selectedEntityId.setText(teacher.getId().toString());
                //teacherOrStudent.setText("students");
                teacherOrStudent.setText(rb.getString("PairAppUIv2.students"));
            }
        }
    }
}
