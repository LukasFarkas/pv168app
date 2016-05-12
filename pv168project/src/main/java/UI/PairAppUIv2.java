/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;
import cz.muni.fi.pv168.common.DBUtils;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.pv168project.app.*;
import java.awt.Component;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.derby.jdbc.EmbeddedDriver;

/**
 *
 * @author L
 */



public class PairAppUIv2 extends javax.swing.JFrame { 
    private static DataSource dataSource;
    private static LessonManager lm;
    private static TeacherManager tm;
    private static StudentManager sm;
     
    
    public class EntityListRenderer extends JLabel implements ListCellRenderer {
        
        @Override
        public Component getListCellRendererComponent(JList jlist, Object e, int index, boolean isSelected, boolean cellHasFocus) {
            
            if (e.getClass().equals(Student.class) ) {
                Student student = (Student) e;
                setText(student.getFullName());
            }
            else if (e.getClass().equals(Teacher.class)) {
                Teacher teacher = (Teacher) e;
                setText(teacher.getFullName());   
            }
            else  {
                throw new IllegalArgumentException();
            }
            return this;
        }


    }
    
    
    class LessonTableModel extends AbstractTableModel {
        private List<Lesson> cars; 
        
    private LessonTableModel (List<Lesson> lessons) {
        this.cars = new ArrayList<Lesson>(lessons);
    }
        
    @Override
    public int getRowCount() {
        return cars.size();
    }
 
    @Override
    public int getColumnCount() {
        return 5;
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Lesson car = cars.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return car.getPrice(); 
            case 1:
                return car.getRegion();
            case 2:
                return car.getSkill();
            case 3:
                //return car.getTeacherId();
                return "";
            case 4:
                //return car.getStudentId();
                return "";
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
}
    
    /**
     * Creates new form PairAppUIv2
     */
    public PairAppUIv2() {
        initComponents();
    }
    
    public DefaultListModel populateJList (DataSource dataSource) throws SQLException {
        DefaultListModel model = new DefaultListModel();
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs;
        
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT fullName FROM Student");
            rs = st.executeQuery();
            
            while (rs.next()) {
                String name = rs.getString ("fullName");
                model.addElement(name);
            }
            return model;
            
        } catch (SQLException ex) {
            String msg = "Error when getting all students from DB - list";
            //logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    public void pop (DefaultListModel model, JList list) {
        list.setModel(model);
    }
    
    public DefaultTableModel popjTable () {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,price,skill,region,teacherid,studentid FROM Lesson");
            ResultSet rs = st.executeQuery();
            return null;
        } catch (SQLException ex) {
            String msg = "Error when getting all lessons from DB";
            //logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        Panel_StudentEditPanel1 = new javax.swing.JPanel();
        Label_Student1 = new javax.swing.JLabel();
        SplitPane_StudentID1 = new javax.swing.JSplitPane();
        StudentID1 = new javax.swing.JLabel();
        Label_StudentID1 = new javax.swing.JLabel();
        SplitPane_StudentName1 = new javax.swing.JSplitPane();
        TextField_StudentName1 = new javax.swing.JTextField();
        Label_StudentName1 = new javax.swing.JLabel();
        SplitPane_StudentPrice1 = new javax.swing.JSplitPane();
        TextField_StudentPrice1 = new javax.swing.JTextField();
        Label_StudentPrice1 = new javax.swing.JLabel();
        SplitPane_StudentSkill2 = new javax.swing.JSplitPane();
        TextField_StudentSkill1 = new javax.swing.JTextField();
        Label_StudentSkill1 = new javax.swing.JLabel();
        SplitPane_StudentRegion1 = new javax.swing.JSplitPane();
        Label_StudentRegion1 = new javax.swing.JLabel();
        ComboBox_StudentRegion1 = new javax.swing.JComboBox();
        Button_StudentUpdate1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        Panel_StudentsTab = new javax.swing.JPanel();
        Label_StudentsList = new javax.swing.JLabel();
        Panel_StudentEditPanel = new javax.swing.JPanel();
        Label_Student = new javax.swing.JLabel();
        SplitPane_StudentID = new javax.swing.JSplitPane();
        StudentID = new javax.swing.JLabel();
        Label_StudentID = new javax.swing.JLabel();
        SplitPane_StudentName = new javax.swing.JSplitPane();
        TextField_StudentName = new javax.swing.JTextField();
        Label_StudentName = new javax.swing.JLabel();
        SplitPane_StudentPrice = new javax.swing.JSplitPane();
        TextField_StudentPrice = new javax.swing.JTextField();
        Label_StudentPrice = new javax.swing.JLabel();
        SplitPane_StudentSkill = new javax.swing.JSplitPane();
        TextField_StudentSkill = new javax.swing.JTextField();
        Label_StudentSkill = new javax.swing.JLabel();
        SplitPane_StudentRegion = new javax.swing.JSplitPane();
        Label_StudentRegion = new javax.swing.JLabel();
        ComboBox_StudentRegion = new javax.swing.JComboBox();
        Button_StudentAdd = new javax.swing.JButton();
        Button_StudentUpdate = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        List_StudentList = new javax.swing.JList();
        Panel_TeachersTab = new javax.swing.JPanel();
        Label_TeachersList = new javax.swing.JLabel();
        ScrollPane_TeachersList = new javax.swing.JScrollPane();
        List_TeachersList = new javax.swing.JList();
        Panel_TeacherEditPanel = new javax.swing.JPanel();
        Label_Teacher = new javax.swing.JLabel();
        SplitPane_TeacherID = new javax.swing.JSplitPane();
        Label_TeacherIDValue = new javax.swing.JLabel();
        Label_TeacherID = new javax.swing.JLabel();
        SplitPane_TeacherName = new javax.swing.JSplitPane();
        TextField_TeacherName = new javax.swing.JTextField();
        Label_TeacherName = new javax.swing.JLabel();
        SplitPane_TeacherPrice = new javax.swing.JSplitPane();
        TextField_TeacherPrice = new javax.swing.JTextField();
        Label_TeacherPrice = new javax.swing.JLabel();
        SplitPane_StudentSkill1 = new javax.swing.JSplitPane();
        TextField_TeacherSkill = new javax.swing.JTextField();
        Label_TeacherSkill = new javax.swing.JLabel();
        SplitPane_TeacherRegion = new javax.swing.JSplitPane();
        Label_TeacherRegion = new javax.swing.JLabel();
        ComboBox_TeacherRegion = new javax.swing.JComboBox();
        Button_TeacherAdd = new javax.swing.JButton();
        Button_TeacherUpdate = new javax.swing.JButton();
        Button_TeacherLessons = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        Panel_LessonsTab = new javax.swing.JPanel();
        Label_LessonsList = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        Label_Student1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Label_Student1.setText("Student");

        SplitPane_StudentID1.setDividerLocation(200);

        StudentID1.setText("IDValue_UNKNOWN");
        SplitPane_StudentID1.setRightComponent(StudentID1);

        Label_StudentID1.setText("ID");
        SplitPane_StudentID1.setLeftComponent(Label_StudentID1);

        SplitPane_StudentName1.setDividerLocation(200);

        TextField_StudentName1.setText("fullName");
        TextField_StudentName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextField_StudentName1ActionPerformed(evt);
            }
        });
        SplitPane_StudentName1.setRightComponent(TextField_StudentName1);

        Label_StudentName1.setText("Name");
        SplitPane_StudentName1.setLeftComponent(Label_StudentName1);

        SplitPane_StudentPrice1.setDividerLocation(200);

        TextField_StudentPrice1.setText("price");
        TextField_StudentPrice1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextField_StudentPrice1ActionPerformed(evt);
            }
        });
        SplitPane_StudentPrice1.setRightComponent(TextField_StudentPrice1);

        Label_StudentPrice1.setText("Price");
        SplitPane_StudentPrice1.setLeftComponent(Label_StudentPrice1);

        SplitPane_StudentSkill2.setDividerLocation(200);

        TextField_StudentSkill1.setText("skill");
        TextField_StudentSkill1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextField_StudentSkill1ActionPerformed(evt);
            }
        });
        SplitPane_StudentSkill2.setRightComponent(TextField_StudentSkill1);

        Label_StudentSkill1.setText("Skill");
        SplitPane_StudentSkill2.setLeftComponent(Label_StudentSkill1);

        SplitPane_StudentRegion1.setDividerLocation(200);

        Label_StudentRegion1.setText("Region");
        SplitPane_StudentRegion1.setLeftComponent(Label_StudentRegion1);

        ComboBox_StudentRegion1.setModel(new javax.swing.DefaultComboBoxModel(Region.values()));
        SplitPane_StudentRegion1.setRightComponent(ComboBox_StudentRegion1);

        Button_StudentUpdate1.setText("Save");
        Button_StudentUpdate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_StudentUpdate1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel_StudentEditPanel1Layout = new javax.swing.GroupLayout(Panel_StudentEditPanel1);
        Panel_StudentEditPanel1.setLayout(Panel_StudentEditPanel1Layout);
        Panel_StudentEditPanel1Layout.setHorizontalGroup(
            Panel_StudentEditPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_StudentEditPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_StudentEditPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SplitPane_StudentName1)
                    .addComponent(SplitPane_StudentPrice1)
                    .addComponent(SplitPane_StudentID1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addComponent(SplitPane_StudentSkill2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SplitPane_StudentRegion1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Panel_StudentEditPanel1Layout.createSequentialGroup()
                        .addComponent(Label_Student1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(Panel_StudentEditPanel1Layout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(Button_StudentUpdate1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Panel_StudentEditPanel1Layout.setVerticalGroup(
            Panel_StudentEditPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_StudentEditPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(Label_Student1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SplitPane_StudentID1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(SplitPane_StudentName1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 47, Short.MAX_VALUE)
                .addComponent(SplitPane_StudentPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(SplitPane_StudentSkill2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SplitPane_StudentRegion1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Button_StudentUpdate1)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 427, Short.MAX_VALUE)
            .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialog1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Panel_StudentEditPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 416, Short.MAX_VALUE)
            .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialog1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Panel_StudentEditPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Label_StudentsList.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Label_StudentsList.setText("Students");

        Label_Student.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Label_Student.setText("Student");

        SplitPane_StudentID.setDividerLocation(200);

        StudentID.setText("IDValue_UNKNOWN");
        SplitPane_StudentID.setRightComponent(StudentID);

        Label_StudentID.setText("ID");
        SplitPane_StudentID.setLeftComponent(Label_StudentID);

        SplitPane_StudentName.setDividerLocation(200);

        TextField_StudentName.setText("fullName");
        TextField_StudentName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextField_StudentNameActionPerformed(evt);
            }
        });
        SplitPane_StudentName.setRightComponent(TextField_StudentName);

        Label_StudentName.setText("Name");
        SplitPane_StudentName.setLeftComponent(Label_StudentName);

        SplitPane_StudentPrice.setDividerLocation(200);

        TextField_StudentPrice.setText("price");
        TextField_StudentPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextField_StudentPriceActionPerformed(evt);
            }
        });
        SplitPane_StudentPrice.setRightComponent(TextField_StudentPrice);

        Label_StudentPrice.setText("Price");
        SplitPane_StudentPrice.setLeftComponent(Label_StudentPrice);

        SplitPane_StudentSkill.setDividerLocation(200);

        TextField_StudentSkill.setText("skill");
        TextField_StudentSkill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextField_StudentSkillActionPerformed(evt);
            }
        });
        SplitPane_StudentSkill.setRightComponent(TextField_StudentSkill);

        Label_StudentSkill.setText("Skill");
        SplitPane_StudentSkill.setLeftComponent(Label_StudentSkill);

        SplitPane_StudentRegion.setDividerLocation(200);

        Label_StudentRegion.setText("Region");
        SplitPane_StudentRegion.setLeftComponent(Label_StudentRegion);

        ComboBox_StudentRegion.setModel(new javax.swing.DefaultComboBoxModel(Region.values()));
        SplitPane_StudentRegion.setRightComponent(ComboBox_StudentRegion);

        Button_StudentAdd.setText("Add student");
        Button_StudentAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_StudentAddActionPerformed(evt);
            }
        });

        Button_StudentUpdate.setText("Save");

        jButton1.setText("Display lessons");

        jButton2.setText("Add lesson");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton6.setText("Show all students");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel_StudentEditPanelLayout = new javax.swing.GroupLayout(Panel_StudentEditPanel);
        Panel_StudentEditPanel.setLayout(Panel_StudentEditPanelLayout);
        Panel_StudentEditPanelLayout.setHorizontalGroup(
            Panel_StudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_StudentEditPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_StudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SplitPane_StudentName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SplitPane_StudentPrice, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SplitPane_StudentID)
                    .addComponent(SplitPane_StudentSkill)
                    .addComponent(SplitPane_StudentRegion)
                    .addGroup(Panel_StudentEditPanelLayout.createSequentialGroup()
                        .addGroup(Panel_StudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Button_StudentAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label_Student))
                        .addGap(0, 398, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_StudentEditPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_StudentEditPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                .addComponent(Button_StudentUpdate)
                .addGap(90, 90, 90)
                .addComponent(jButton2)
                .addGap(33, 33, 33))
        );
        Panel_StudentEditPanelLayout.setVerticalGroup(
            Panel_StudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_StudentEditPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(Label_Student)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SplitPane_StudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(SplitPane_StudentName, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(SplitPane_StudentPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(SplitPane_StudentSkill, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SplitPane_StudentRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(Panel_StudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_StudentEditPanelLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(Panel_StudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(Button_StudentUpdate)))
                    .addGroup(Panel_StudentEditPanelLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jButton6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Panel_StudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_StudentAdd)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        List_StudentList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        List_StudentList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                List_StudentListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(List_StudentList);

        javax.swing.GroupLayout Panel_StudentsTabLayout = new javax.swing.GroupLayout(Panel_StudentsTab);
        Panel_StudentsTab.setLayout(Panel_StudentsTabLayout);
        Panel_StudentsTabLayout.setHorizontalGroup(
            Panel_StudentsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_StudentsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_StudentsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_StudentsTabLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Panel_StudentEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(Label_StudentsList))
                .addGap(11, 11, 11))
        );
        Panel_StudentsTabLayout.setVerticalGroup(
            Panel_StudentsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_StudentsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_StudentsList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_StudentsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Panel_StudentEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Panel_StudentsTabLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 12, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Students", Panel_StudentsTab);

        Label_TeachersList.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Label_TeachersList.setText("Teachers");

        List_TeachersList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        ScrollPane_TeachersList.setViewportView(List_TeachersList);

        Label_Teacher.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Label_Teacher.setText("Teacher");

        SplitPane_TeacherID.setDividerLocation(200);

        Label_TeacherIDValue.setText("IDValue_UNKNOWN");
        SplitPane_TeacherID.setRightComponent(Label_TeacherIDValue);

        Label_TeacherID.setText("ID");
        SplitPane_TeacherID.setLeftComponent(Label_TeacherID);

        SplitPane_TeacherName.setDividerLocation(200);

        TextField_TeacherName.setText("fullName");
        TextField_TeacherName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextField_TeacherNameActionPerformed(evt);
            }
        });
        SplitPane_TeacherName.setRightComponent(TextField_TeacherName);

        Label_TeacherName.setText("Name");
        SplitPane_TeacherName.setLeftComponent(Label_TeacherName);

        SplitPane_TeacherPrice.setDividerLocation(200);

        TextField_TeacherPrice.setText("price");
        TextField_TeacherPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextField_TeacherPriceActionPerformed(evt);
            }
        });
        SplitPane_TeacherPrice.setRightComponent(TextField_TeacherPrice);

        Label_TeacherPrice.setText("Price");
        SplitPane_TeacherPrice.setLeftComponent(Label_TeacherPrice);

        SplitPane_StudentSkill1.setDividerLocation(200);

        TextField_TeacherSkill.setText("skill");
        TextField_TeacherSkill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextField_TeacherSkillActionPerformed(evt);
            }
        });
        SplitPane_StudentSkill1.setRightComponent(TextField_TeacherSkill);

        Label_TeacherSkill.setText("Skill");
        SplitPane_StudentSkill1.setLeftComponent(Label_TeacherSkill);

        SplitPane_TeacherRegion.setDividerLocation(200);

        Label_TeacherRegion.setText("Region");
        SplitPane_TeacherRegion.setLeftComponent(Label_TeacherRegion);

        ComboBox_TeacherRegion.setModel(new javax.swing.DefaultComboBoxModel(Region.values()));
        SplitPane_TeacherRegion.setRightComponent(ComboBox_TeacherRegion);

        Button_TeacherAdd.setText("Add teacher");
        Button_TeacherAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_TeacherAddActionPerformed(evt);
            }
        });

        Button_TeacherUpdate.setText("Save");

        Button_TeacherLessons.setText("Display lessons");

        jButton3.setText("Add lesson");

        jButton7.setText("Show all teachers");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel_TeacherEditPanelLayout = new javax.swing.GroupLayout(Panel_TeacherEditPanel);
        Panel_TeacherEditPanel.setLayout(Panel_TeacherEditPanelLayout);
        Panel_TeacherEditPanelLayout.setHorizontalGroup(
            Panel_TeacherEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_TeacherEditPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_TeacherEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SplitPane_TeacherName)
                    .addComponent(SplitPane_TeacherPrice)
                    .addComponent(SplitPane_TeacherID, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label_Teacher, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SplitPane_StudentSkill1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SplitPane_TeacherRegion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Panel_TeacherEditPanelLayout.createSequentialGroup()
                        .addComponent(Button_TeacherAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Button_TeacherLessons, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Panel_TeacherEditPanelLayout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Button_TeacherUpdate)
                        .addGap(64, 64, 64)
                        .addComponent(jButton3)
                        .addGap(25, 25, 25)))
                .addContainerGap())
        );
        Panel_TeacherEditPanelLayout.setVerticalGroup(
            Panel_TeacherEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_TeacherEditPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(Label_Teacher)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SplitPane_TeacherID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(SplitPane_TeacherName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(SplitPane_TeacherPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(SplitPane_StudentSkill1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SplitPane_TeacherRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(Panel_TeacherEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(Button_TeacherUpdate)
                    .addComponent(jButton7))
                .addGap(35, 35, 35)
                .addGroup(Panel_TeacherEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Button_TeacherLessons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Button_TeacherAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout Panel_TeachersTabLayout = new javax.swing.GroupLayout(Panel_TeachersTab);
        Panel_TeachersTab.setLayout(Panel_TeachersTabLayout);
        Panel_TeachersTabLayout.setHorizontalGroup(
            Panel_TeachersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_TeachersTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_TeachersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_TeachersTabLayout.createSequentialGroup()
                        .addComponent(ScrollPane_TeachersList, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(Panel_TeacherEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(Label_TeachersList))
                .addGap(11, 11, 11))
        );
        Panel_TeachersTabLayout.setVerticalGroup(
            Panel_TeachersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_TeachersTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_TeachersList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_TeachersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Panel_TeacherEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Panel_TeachersTabLayout.createSequentialGroup()
                        .addComponent(ScrollPane_TeachersList, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 9, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Teachers", Panel_TeachersTab);

        Label_LessonsList.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Label_LessonsList.setText("Lessons");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Student", "Teacher", "Skill", "Price", "Region"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton4.setText("Delete lesson");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Display all");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel1.setText("Progress");

        javax.swing.GroupLayout Panel_LessonsTabLayout = new javax.swing.GroupLayout(Panel_LessonsTab);
        Panel_LessonsTab.setLayout(Panel_LessonsTabLayout);
        Panel_LessonsTabLayout.setHorizontalGroup(
            Panel_LessonsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_LessonsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_LessonsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Label_LessonsList)
                    .addGroup(Panel_LessonsTabLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(Panel_LessonsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Panel_LessonsTabLayout.createSequentialGroup()
                                .addComponent(jButton5)
                                .addGap(155, 155, 155)
                                .addComponent(jLabel1)
                                .addGap(238, 238, 238)
                                .addComponent(jButton4))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(195, Short.MAX_VALUE))
        );
        Panel_LessonsTabLayout.setVerticalGroup(
            Panel_LessonsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_LessonsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_LessonsList)
                .addGroup(Panel_LessonsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_LessonsTabLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 172, Short.MAX_VALUE)
                        .addGroup(Panel_LessonsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jButton5))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_LessonsTabLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(26, 26, 26))))
        );

        jTabbedPane1.addTab("Lessons", Panel_LessonsTab);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TextField_StudentNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextField_StudentNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_StudentNameActionPerformed

    private void TextField_StudentPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextField_StudentPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_StudentPriceActionPerformed

    private void TextField_StudentSkillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextField_StudentSkillActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_StudentSkillActionPerformed

    private void Button_StudentAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_StudentAddActionPerformed
        // TODO add your handling code here:
        jDialog1.setVisible(true);
        jDialog1.pack();
    }//GEN-LAST:event_Button_StudentAddActionPerformed

    private void TextField_TeacherNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextField_TeacherNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_TeacherNameActionPerformed

    private void TextField_TeacherPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextField_TeacherPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_TeacherPriceActionPerformed

    private void TextField_TeacherSkillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextField_TeacherSkillActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_TeacherSkillActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        List<Student> list = sm.findAllStudents();
        DefaultListModel model = new DefaultListModel();
        for (Student t : list) {
            model.addElement(t);
        }
        List_StudentList.setModel(model);
        List_StudentList.setCellRenderer(new EntityListRenderer());
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        List<Teacher> list = tm.findAllTeachers();
        DefaultListModel model = new DefaultListModel();
        for (Teacher t : list) {
            model.addElement(t.getFullName());
        }
        List_TeachersList.setModel(model);
        List_StudentList.setCellRenderer(new EntityListRenderer());
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        
            List<Lesson> ls = lm.findAllLessons();
            LessonTableModel model = new LessonTableModel(ls);
            
            /*
            Object[] columnNames = {"Student", "Teacher", "Skill", "Price", "Region"};
            DefaultTableModel model = new DefaultTableModel(new Object[0][0], columnNames);
            for (Lesson adv : ls) {
                Object[] o = new Object[5];
                o[0] = adv.getStudentId();
                if (o[2] == null)
                    o[0] = 
                o[1] = adv.getTeacherId();
                o[2] = adv.getSkill();
                o[3] = adv.getPrice();
                o[4] = adv.getRegion();
                model.addRow(o);
            }
            */
            jTable1.setModel(model);
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void Button_TeacherAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_TeacherAddActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_Button_TeacherAddActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        
        //lm.deleteLesson(lesson);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void List_StudentListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_List_StudentListValueChanged
        // TODO add your handling code here:
        Student st = (Student) List_StudentList.getSelectedValue();
        TextField_StudentName.setText(st.getFullName());
        StudentID.setText(st.getId().toString());
        
        ComboBox_StudentRegion.setSelectedItem(st.getRegion());
        
        
    }//GEN-LAST:event_List_StudentListValueChanged

    private void TextField_StudentName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextField_StudentName1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_StudentName1ActionPerformed

    private void TextField_StudentPrice1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextField_StudentPrice1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_StudentPrice1ActionPerformed

    private void TextField_StudentSkill1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextField_StudentSkill1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_StudentSkill1ActionPerformed

    private void Button_StudentUpdate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_StudentUpdate1ActionPerformed
        // TODO add your handling code here:
        Student student = new Student();
        student.setFullName(TextField_StudentName1.getText());
        student.setPrice(BigDecimal.valueOf(Double.valueOf(TextField_StudentPrice1.getText())).setScale(2));
        student.setRegion((Region) ComboBox_StudentRegion1.getSelectedItem());
        student.setSkill(Integer.parseInt(TextField_StudentSkill1.getText()));
        sm.createStudent(student);
        jDialog1.dispose();
        
    }//GEN-LAST:event_Button_StudentUpdate1ActionPerformed

    
    public static DataSource createMemoryDatabase() throws SQLException {
        BasicDataSource bds = new BasicDataSource();
        //set JDBC driver and URL
        bds.setDriverClassName(EmbeddedDriver.class.getName());
        bds.setUrl("jdbc:derby:memory:studentsDB;create=true");
        //populate db with tables and data
        
        DBUtils.executeSqlScript(bds,StudentManager.class.getResource("createTables.sql"));
        DBUtils.executeSqlScript(bds,StudentManager.class.getResource("test-data.sql"));
        return bds;
    }
    
    public static void main(String args[]) {
        
        try {
            dataSource = createMemoryDatabase();
            lm = new LessonManager();
            lm.setDataSource(dataSource);
            tm = new TeacherManager();
            tm.setDataSource(dataSource);
            sm = new StudentManager();
            sm.setDataSource(dataSource);
        } catch (SQLException ex) {
            Logger.getLogger(PairAppUIv2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PairAppUIv2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PairAppUIv2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PairAppUIv2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PairAppUIv2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PairAppUIv2().setVisible(true);
            }
        });
       
        
        

        //List<Book> allBooks = bookManager.getAllBooks();
        //System.out.println("allBooks = " + allBooks);
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Button_StudentAdd;
    private javax.swing.JButton Button_StudentUpdate;
    private javax.swing.JButton Button_StudentUpdate1;
    private javax.swing.JButton Button_TeacherAdd;
    private javax.swing.JButton Button_TeacherLessons;
    private javax.swing.JButton Button_TeacherUpdate;
    private javax.swing.JComboBox ComboBox_StudentRegion;
    private javax.swing.JComboBox ComboBox_StudentRegion1;
    private javax.swing.JComboBox ComboBox_TeacherRegion;
    private javax.swing.JLabel Label_LessonsList;
    private javax.swing.JLabel Label_Student;
    private javax.swing.JLabel Label_Student1;
    private javax.swing.JLabel Label_StudentID;
    private javax.swing.JLabel Label_StudentID1;
    private javax.swing.JLabel Label_StudentName;
    private javax.swing.JLabel Label_StudentName1;
    private javax.swing.JLabel Label_StudentPrice;
    private javax.swing.JLabel Label_StudentPrice1;
    private javax.swing.JLabel Label_StudentRegion;
    private javax.swing.JLabel Label_StudentRegion1;
    private javax.swing.JLabel Label_StudentSkill;
    private javax.swing.JLabel Label_StudentSkill1;
    private javax.swing.JLabel Label_StudentsList;
    private javax.swing.JLabel Label_Teacher;
    private javax.swing.JLabel Label_TeacherID;
    private javax.swing.JLabel Label_TeacherIDValue;
    private javax.swing.JLabel Label_TeacherName;
    private javax.swing.JLabel Label_TeacherPrice;
    private javax.swing.JLabel Label_TeacherRegion;
    private javax.swing.JLabel Label_TeacherSkill;
    private javax.swing.JLabel Label_TeachersList;
    private javax.swing.JList List_StudentList;
    private javax.swing.JList List_TeachersList;
    private javax.swing.JPanel Panel_LessonsTab;
    private javax.swing.JPanel Panel_StudentEditPanel;
    private javax.swing.JPanel Panel_StudentEditPanel1;
    private javax.swing.JPanel Panel_StudentsTab;
    private javax.swing.JPanel Panel_TeacherEditPanel;
    private javax.swing.JPanel Panel_TeachersTab;
    private javax.swing.JScrollPane ScrollPane_TeachersList;
    private javax.swing.JSplitPane SplitPane_StudentID;
    private javax.swing.JSplitPane SplitPane_StudentID1;
    private javax.swing.JSplitPane SplitPane_StudentName;
    private javax.swing.JSplitPane SplitPane_StudentName1;
    private javax.swing.JSplitPane SplitPane_StudentPrice;
    private javax.swing.JSplitPane SplitPane_StudentPrice1;
    private javax.swing.JSplitPane SplitPane_StudentRegion;
    private javax.swing.JSplitPane SplitPane_StudentRegion1;
    private javax.swing.JSplitPane SplitPane_StudentSkill;
    private javax.swing.JSplitPane SplitPane_StudentSkill1;
    private javax.swing.JSplitPane SplitPane_StudentSkill2;
    private javax.swing.JSplitPane SplitPane_TeacherID;
    private javax.swing.JSplitPane SplitPane_TeacherName;
    private javax.swing.JSplitPane SplitPane_TeacherPrice;
    private javax.swing.JSplitPane SplitPane_TeacherRegion;
    private javax.swing.JLabel StudentID;
    private javax.swing.JLabel StudentID1;
    private javax.swing.JTextField TextField_StudentName;
    private javax.swing.JTextField TextField_StudentName1;
    private javax.swing.JTextField TextField_StudentPrice;
    private javax.swing.JTextField TextField_StudentPrice1;
    private javax.swing.JTextField TextField_StudentSkill;
    private javax.swing.JTextField TextField_StudentSkill1;
    private javax.swing.JTextField TextField_TeacherName;
    private javax.swing.JTextField TextField_TeacherPrice;
    private javax.swing.JTextField TextField_TeacherSkill;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
