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
import cz.muni.fi.pv168.pv168project.app.Lesson;
import cz.muni.fi.pv168.pv168project.app.LessonManager;
import cz.muni.fi.pv168.pv168project.app.Region;
import cz.muni.fi.pv168.pv168project.app.Student;
import cz.muni.fi.pv168.pv168project.app.StudentManager;
import cz.muni.fi.pv168.pv168project.app.Teacher;
import cz.muni.fi.pv168.pv168project.app.TeacherManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/**
 *
 * @author tomf
 */
public class UpdateEntitySwingWorker extends SwingWorker<Integer, Integer> {
    private StudentManager sm;
    private TeacherManager tm;
    private LessonManager lm;
    private JTextField name;
    private JTextField price;
    private JTextField skill;
    private JComboBox region;
    private JButton buttonEntity;
    private JButton buttonLessons;
    private JDialog dialogInfo;
    private Long id;
    private boolean isStudent;
    private boolean opSuccess = false;
    private JDialog dialogError;
    private JLabel labelError;
    private ResourceBundle rb;
    
    public UpdateEntitySwingWorker (DataSource ds, Long id, boolean isStudent, JTextField textName, JTextField textPrice,
            JTextField textSkill, JComboBox region, JButton buttonEntity, JButton buttonLessons, JDialog dialogInfo, 
            JDialog dialogError, JLabel labelError, ResourceBundle rb) {
        sm = new StudentManager(ds);
        //sm.setDataSource(ds);
        tm = new TeacherManager(ds);
        //tm.setDataSource(ds);
        lm = new LessonManager(ds);
        //lm.setDataSource(ds);
        this.isStudent = isStudent;
        this.id = id;
        this.name = textName;
        this.price = textPrice;
        this.skill = textSkill;
        this.region = region;
        this.buttonEntity = buttonEntity;
        this.buttonLessons = buttonLessons;
        this.dialogInfo = dialogInfo;
        this.dialogError = dialogError;
        this.labelError = labelError;
        this.rb = rb;
    }
    
    @Override
    protected Integer doInBackground() {
        if (Boolean.TRUE.equals(isStudent)) {
            try {
                Student st = sm.getStudent(Long.valueOf(id));
                st.setFullName(name.getText());
                try {
                    st.setPrice(BigDecimal.valueOf(Double.valueOf(price.getText())).setScale(2));
                } catch (NumberFormatException numberFormatException) {
                    //price.setText("Wrong format");
                    price.setText(rb.getString("PairAppUIv2.jTextField_Invalid.text"));
                    return 1;
                }
                try {
                    st.setSkill(Integer.parseInt(skill.getText()));
                } catch (NumberFormatException numberFormatException) {
                    //skill.setText("Wrong format");
                    skill.setText(rb.getString("PairAppUIv2.jTextField_Invalid.text"));
                    return 1;
                }
                st.setRegion((Region) region.getSelectedItem());

                List<Lesson> lessons = lm.getLesson(st);
                //  go through all lessons associated with student
                for (int i = 0; i < lessons.size(); i++) {
                    Lesson l = lessons.get(i);
                    try {
                        // get teacher from current lesson
                        Teacher teach = tm.getTeacher(l.getTeacherId());
                        // validate all of them - if fail, go to CATCH block
                        lm.matchValidation(teach, st);
                        // if OK - update lessons
                        l.setSkill(st.getSkill());
                    // price is determined by teacher, else validation fail
                        // region must be the same - else validation fails
                        lm.updateLesson(l);
                    } catch (ValidationException e) {
                    //Lesson lsds = lm.getLesson(l.getId());
                        // if validation fails - delete appropriate lessons
                        lm.deleteLesson(l);
                    }
                }
                sm.updateStudent(st);
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
                Teacher teach = tm.getTeacher(Long.valueOf(id));
                teach.setFullName(name.getText());
                try {
                    teach.setPrice(BigDecimal.valueOf(Double.valueOf(price.getText())).setScale(2));
                } catch (NumberFormatException numberFormatException) {
                    //price.setText("Wrong format");
                    price.setText(rb.getString("PairAppUIv2.jTextField_Invalid.text"));
                    return 1;
                }
                try {
                    teach.setSkill(Integer.parseInt(skill.getText()));
                } catch (NumberFormatException numberFormatException) {
                    //skill.setText("Wrong format");
                    skill.setText(rb.getString("PairAppUIv2.jTextField_Invalid.text"));
                    return 1;
                }
                teach.setRegion((Region) region.getSelectedItem());

                List<Lesson> lessons = lm.getLesson(teach);
                //  go through all lessons associated with student
                for (int i = 0; i < lessons.size(); i++) {
                    Lesson l = lessons.get(i);
                    try {
                        // get teacher from current lesson
                        Student student = sm.getStudent(l.getStudentId());
                        // validate all of them - if fail, go to CATCH block
                        lm.matchValidation(teach, student);
                    // if OK - update lessons
                        // skill is determined by student, else validation fail
                        l.setPrice(teach.getPrice());
                        // region must be the same - else validation fails
                        lm.updateLesson(l);
                    } catch (ValidationException e) {
                    //Lesson lsds = lm.getLesson(l.getId());
                        // if validation fails - delete appropriate lessons
                        lm.deleteLesson(l);
                    }
                }
                tm.updateTeacher(teach);
                opSuccess = true;
            } catch (DataSourceException | IllegalArgumentException | IllegalEntityException | ServiceFailureException e) {
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
            for (ActionListener a : buttonEntity.getActionListeners()) {
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
                });
            }
            for (ActionListener a : buttonLessons.getActionListeners()) {
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
                });
            }
            dialogInfo.pack();
            dialogInfo.setVisible(true);
        }
    }
}
