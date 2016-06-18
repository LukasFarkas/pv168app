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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingWorker;

/**
 *
 * @author tomf
 */
public class DeleteLessonSwingWorker extends SwingWorker<Integer, Integer> {
    
    private LessonManager lm;
    private JTable tableLessons;
    private JButton buttonLessonDisplayAll;
    private JDialog dialogInfo;
    private JDialog dialogError;
    private JLabel labelError;
    private boolean opSuccess = false;
    private ResourceBundle rb;
    
    public DeleteLessonSwingWorker (DataSource ds, JTable tableLessons, JButton buttonLessonDisplayAll,
            JDialog dialogInfo, JDialog dialogError, JLabel labelError, ResourceBundle rb) {
        this.lm = new LessonManager(ds);
        //lm.setDataSource(ds);
        this.tableLessons = tableLessons;
        this.buttonLessonDisplayAll = buttonLessonDisplayAll;
        this.dialogInfo = dialogInfo;
        this.dialogError = dialogError;
        this.labelError = labelError;
        this.rb = rb;
    }
    
    @Override
    protected Integer doInBackground() {
        try {
            int row = tableLessons.getSelectedRow();
            if (row == -1) { //pokud neni vybrany zadny radek
                return 1;
            }
            Long lessonID = (Long) tableLessons.getValueAt(row, 0);
            Lesson lesson = lm.getLesson(lessonID);
            lm.deleteLesson(lesson);
            opSuccess = true;
        } catch (DataSourceException | IllegalArgumentException | IllegalEntityException | ServiceFailureException e) {
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
            for (ActionListener a : buttonLessonDisplayAll.getActionListeners()) {
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {
                });
            }
            dialogInfo.pack();
            dialogInfo.setVisible(true);
        }
    }  
}
