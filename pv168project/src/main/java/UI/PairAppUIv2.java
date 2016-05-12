/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;
import cz.muni.fi.pv168.common.DBUtils;
import cz.muni.fi.pv168.pv168project.app.*;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.table.AbstractTableModel;
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
     
    
    /**
     * List renderer for teacher and student
     */
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

            Color background;
            Color foreground;

//            // check if this cell represents the current DnD drop location
//            if (jlist.getDropLocation() != null
//                && !jlist.getDropLocation().isInsert()
//                && jlist.getDropLocation().getIndex() == index) {
//
//                background = Color.WHITE;
//                foreground = Color.BLUE;
//
//            // check if this cell is selected
//            } else 
            if (isSelected) {
                background = Color.WHITE;
                foreground = Color.RED;

            // unselected, and not the DnD drop location
            } else {
                background = Color.WHITE;
                foreground = Color.BLACK;
            }

            setBackground(background);
            setForeground(foreground);
            
            return this;
        }


    }
    
    /**
     * Table model for lessons
     */
    class LessonTableModel extends AbstractTableModel {
        private List<Lesson> lessons; 
        
        private LessonTableModel (List<Lesson> lessons) {
            this.lessons = new ArrayList<Lesson>(lessons);
        }
        
        boolean[] canEdit = new boolean [] {
            false, false, false, false, false
        };
        
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
        }

        @Override
        public int getRowCount() {
            return lessons.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }
        
        @Override
        public String getColumnName(int column) {
            String[] columnNames = {"Student", "Teacher", "Skill", "Price", "Region"};
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Lesson lesson = lessons.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return lesson.getPrice(); 
                case 1:
                    return lesson.getRegion();
                case 2:
                    return lesson.getSkill();
                case 3:
                    //resit v jinem vlakne!!!!
                    Teacher teacher = tm.getTeacher(lesson.getTeacherId());
                    return teacher.getFullName();
                case 4:
                    Student student = sm.getStudent(lesson.getStudentId());
                    return student.getFullName();
                default:
                    throw new IllegalArgumentException("Wrong column index");
            }
        }
    }
    
    /**
     * Creates new form PairAppUIv2
     */
    public PairAppUIv2() {
        initComponents();
    }
    
    
    
    //nasledujici tri metody reseny pres managery:
    
//    public DefaultListModel populateJList (DataSource dataSource) throws SQLException {
//        DefaultListModel model = new DefaultListModel();
//        Connection conn = null;
//        PreparedStatement st = null;
//        ResultSet rs;
//        
//        try {
//            conn = dataSource.getConnection();
//            st = conn.prepareStatement(
//                    "SELECT fullName FROM Student");
//            rs = st.executeQuery();
//            
//            while (rs.next()) {
//                String name = rs.getString ("fullName");
//                model.addElement(name);
//            }
//            return model;
//            
//        } catch (SQLException ex) {
//            String msg = "Error when getting all students from DB - list";
//            //logger.log(Level.SEVERE, msg, ex);
//            throw new ServiceFailureException(msg, ex);
//        } finally {
//            DBUtils.closeQuietly(conn, st);
//        }
//    }
//    
//    public void pop (DefaultListModel model, JList list) {
//        list.setModel(model);
//    }
//    
//    public DefaultTableModel popjTable () {
//        Connection conn = null;
//        PreparedStatement st = null;
//        try {
//            conn = dataSource.getConnection();
//            st = conn.prepareStatement(
//                    "SELECT id,price,skill,region,teacherid,studentid FROM Lesson");
//            ResultSet rs = st.executeQuery();
//            return null;
//        } catch (SQLException ex) {
//            String msg = "Error when getting all lessons from DB";
//            //logger.log(Level.SEVERE, msg, ex);
//            throw new ServiceFailureException(msg, ex);
//        } finally {
//            DBUtils.closeQuietly(conn, st);
//        }
//    }

    
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog_AddingNewStudent = new javax.swing.JDialog();
        Panel_AddingStudentEditPanel = new javax.swing.JPanel();
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
        SplitPane_StudentSkill1 = new javax.swing.JSplitPane();
        TextField_StudentSkill1 = new javax.swing.JTextField();
        Label_StudentSkill1 = new javax.swing.JLabel();
        SplitPane_StudentRegion1 = new javax.swing.JSplitPane();
        Label_StudentRegion1 = new javax.swing.JLabel();
        ComboBox_StudentRegion1 = new javax.swing.JComboBox();
        Button_AddingStudentSaveNew = new javax.swing.JButton();
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
        Button_StudentAddNew = new javax.swing.JButton();
        Button_StudentUpdate = new javax.swing.JButton();
        Button_StudentDisplayLessons = new javax.swing.JButton();
        Button_StudentAddLesson = new javax.swing.JButton();
        jScrollPane_StudentList = new javax.swing.JScrollPane();
        List_StudentList = new javax.swing.JList();
        Button_StudentShowAllStudents = new javax.swing.JButton();
        Panel_TeachersTab = new javax.swing.JPanel();
        Label_TeachersList = new javax.swing.JLabel();
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
        SplitPane_TeacherSkill = new javax.swing.JSplitPane();
        TextField_TeacherSkill = new javax.swing.JTextField();
        Label_TeacherSkill = new javax.swing.JLabel();
        SplitPane_TeacherRegion = new javax.swing.JSplitPane();
        Label_TeacherRegion = new javax.swing.JLabel();
        ComboBox_TeacherRegion = new javax.swing.JComboBox();
        Button_TeacherAddNew = new javax.swing.JButton();
        Button_TeacherUpdate = new javax.swing.JButton();
        Button_TeacherDisplayLessons = new javax.swing.JButton();
        Button_TeacherAddLesson = new javax.swing.JButton();
        ScrollPane_TeacherList = new javax.swing.JScrollPane();
        List_TeachersList = new javax.swing.JList();
        Button_StudentShowAllTeachers = new javax.swing.JButton();
        Panel_LessonsTab = new javax.swing.JPanel();
        Label_LessonsList = new javax.swing.JLabel();
        ScrollPane_Lessons = new javax.swing.JScrollPane();
        jTable_lessons = new javax.swing.JTable();
        Button_LessonDelete = new javax.swing.JButton();
        Button_LessonDisplayAll = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        Label_Student1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Label_Student1.setText("New student");

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

        SplitPane_StudentSkill1.setDividerLocation(200);

        TextField_StudentSkill1.setText("skill");
        TextField_StudentSkill1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextField_StudentSkill1ActionPerformed(evt);
            }
        });
        SplitPane_StudentSkill1.setRightComponent(TextField_StudentSkill1);

        Label_StudentSkill1.setText("Skill");
        SplitPane_StudentSkill1.setLeftComponent(Label_StudentSkill1);

        SplitPane_StudentRegion1.setDividerLocation(200);

        Label_StudentRegion1.setText("Region");
        SplitPane_StudentRegion1.setLeftComponent(Label_StudentRegion1);

        ComboBox_StudentRegion1.setModel(new javax.swing.DefaultComboBoxModel(Region.values()));
        SplitPane_StudentRegion1.setRightComponent(ComboBox_StudentRegion1);

        Button_AddingStudentSaveNew.setText("Save");
        Button_AddingStudentSaveNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_AddingStudentSaveNewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel_AddingStudentEditPanelLayout = new javax.swing.GroupLayout(Panel_AddingStudentEditPanel);
        Panel_AddingStudentEditPanel.setLayout(Panel_AddingStudentEditPanelLayout);
        Panel_AddingStudentEditPanelLayout.setHorizontalGroup(
            Panel_AddingStudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_AddingStudentEditPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_AddingStudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SplitPane_StudentName1)
                    .addComponent(SplitPane_StudentPrice1)
                    .addComponent(SplitPane_StudentID1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                    .addComponent(SplitPane_StudentSkill1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SplitPane_StudentRegion1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Panel_AddingStudentEditPanelLayout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addComponent(Label_Student1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(Panel_AddingStudentEditPanelLayout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addComponent(Button_AddingStudentSaveNew, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Panel_AddingStudentEditPanelLayout.setVerticalGroup(
            Panel_AddingStudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_AddingStudentEditPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_Student1)
                .addGap(10, 10, 10)
                .addComponent(SplitPane_StudentID1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(SplitPane_StudentName1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(SplitPane_StudentPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(SplitPane_StudentSkill1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(SplitPane_StudentRegion1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Button_AddingStudentSaveNew)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog_AddingNewStudentLayout = new javax.swing.GroupLayout(jDialog_AddingNewStudent.getContentPane());
        jDialog_AddingNewStudent.getContentPane().setLayout(jDialog_AddingNewStudentLayout);
        jDialog_AddingNewStudentLayout.setHorizontalGroup(
            jDialog_AddingNewStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 427, Short.MAX_VALUE)
            .addGroup(jDialog_AddingNewStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialog_AddingNewStudentLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Panel_AddingStudentEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jDialog_AddingNewStudentLayout.setVerticalGroup(
            jDialog_AddingNewStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 416, Short.MAX_VALUE)
            .addGroup(jDialog_AddingNewStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialog_AddingNewStudentLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Panel_AddingStudentEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        Button_StudentAddNew.setText("Add new student");
        Button_StudentAddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_StudentAddNewActionPerformed(evt);
            }
        });

        Button_StudentUpdate.setText("Save edit");

        Button_StudentDisplayLessons.setText("Display lessons");

        Button_StudentAddLesson.setText("Add lesson");
        Button_StudentAddLesson.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_StudentAddLessonActionPerformed(evt);
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
                        .addComponent(Label_Student)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(Panel_StudentEditPanelLayout.createSequentialGroup()
                        .addComponent(Button_StudentAddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                        .addComponent(Button_StudentAddLesson)
                        .addGap(18, 18, 18)
                        .addComponent(Button_StudentDisplayLessons, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_StudentEditPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Button_StudentUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(176, 176, 176))
        );
        Panel_StudentEditPanelLayout.setVerticalGroup(
            Panel_StudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_StudentEditPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(Label_Student)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SplitPane_StudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SplitPane_StudentName, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SplitPane_StudentPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SplitPane_StudentSkill, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SplitPane_StudentRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Button_StudentUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Panel_StudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Button_StudentAddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Panel_StudentEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Button_StudentDisplayLessons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Button_StudentAddLesson)))
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
        jScrollPane_StudentList.setViewportView(List_StudentList);

        Button_StudentShowAllStudents.setText("Show all students");
        Button_StudentShowAllStudents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_StudentShowAllStudentsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel_StudentsTabLayout = new javax.swing.GroupLayout(Panel_StudentsTab);
        Panel_StudentsTab.setLayout(Panel_StudentsTabLayout);
        Panel_StudentsTabLayout.setHorizontalGroup(
            Panel_StudentsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_StudentsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_StudentsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_StudentsTabLayout.createSequentialGroup()
                        .addGroup(Panel_StudentsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane_StudentList, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(Button_StudentShowAllStudents, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addComponent(jScrollPane_StudentList, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_StudentShowAllStudents, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Students", Panel_StudentsTab);

        Label_TeachersList.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Label_TeachersList.setText("Teachers");

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

        SplitPane_TeacherSkill.setDividerLocation(200);

        TextField_TeacherSkill.setText("skill");
        TextField_TeacherSkill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextField_TeacherSkillActionPerformed(evt);
            }
        });
        SplitPane_TeacherSkill.setRightComponent(TextField_TeacherSkill);

        Label_TeacherSkill.setText("Skill");
        SplitPane_TeacherSkill.setLeftComponent(Label_TeacherSkill);

        SplitPane_TeacherRegion.setDividerLocation(200);

        Label_TeacherRegion.setText("Region");
        SplitPane_TeacherRegion.setLeftComponent(Label_TeacherRegion);

        ComboBox_TeacherRegion.setModel(new javax.swing.DefaultComboBoxModel(Region.values()));
        SplitPane_TeacherRegion.setRightComponent(ComboBox_TeacherRegion);

        Button_TeacherAddNew.setText("Add new teacher");
        Button_TeacherAddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_TeacherAddNewActionPerformed(evt);
            }
        });

        Button_TeacherUpdate.setText("Save edit");
        Button_TeacherUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_TeacherUpdateActionPerformed(evt);
            }
        });

        Button_TeacherDisplayLessons.setText("Display lessons");

        Button_TeacherAddLesson.setText("Add lesson");

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
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Panel_TeacherEditPanelLayout.createSequentialGroup()
                        .addComponent(Label_Teacher)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(SplitPane_TeacherSkill, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SplitPane_TeacherRegion, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_TeacherEditPanelLayout.createSequentialGroup()
                        .addComponent(Button_TeacherAddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                        .addComponent(Button_TeacherAddLesson)
                        .addGap(18, 18, 18)
                        .addComponent(Button_TeacherDisplayLessons, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_TeacherEditPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Button_TeacherUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(176, 176, 176))
        );
        Panel_TeacherEditPanelLayout.setVerticalGroup(
            Panel_TeacherEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_TeacherEditPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(Label_Teacher)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SplitPane_TeacherID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SplitPane_TeacherName, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SplitPane_TeacherPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SplitPane_TeacherSkill, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SplitPane_TeacherRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Button_TeacherUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(Panel_TeacherEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_TeacherEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Button_TeacherAddNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Button_TeacherAddLesson))
                    .addComponent(Button_TeacherDisplayLessons, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        List_TeachersList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        ScrollPane_TeacherList.setViewportView(List_TeachersList);

        Button_StudentShowAllTeachers.setText("Show all teachers");
        Button_StudentShowAllTeachers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_StudentShowAllTeachersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel_TeachersTabLayout = new javax.swing.GroupLayout(Panel_TeachersTab);
        Panel_TeachersTab.setLayout(Panel_TeachersTabLayout);
        Panel_TeachersTabLayout.setHorizontalGroup(
            Panel_TeachersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_TeachersTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_TeachersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_TeachersTabLayout.createSequentialGroup()
                        .addGroup(Panel_TeachersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ScrollPane_TeacherList, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(Button_StudentShowAllTeachers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                    .addGroup(Panel_TeachersTabLayout.createSequentialGroup()
                        .addComponent(ScrollPane_TeacherList, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_StudentShowAllTeachers, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                    .addComponent(Panel_TeacherEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Teachers", Panel_TeachersTab);

        Label_LessonsList.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Label_LessonsList.setText("Lessons");

        jTable_lessons.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable_lessons.setColumnSelectionAllowed(true);
        ScrollPane_Lessons.setViewportView(jTable_lessons);
        jTable_lessons.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        Button_LessonDelete.setText("Delete lesson");
        Button_LessonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_LessonDeleteActionPerformed(evt);
            }
        });

        Button_LessonDisplayAll.setText("Display all");
        Button_LessonDisplayAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_LessonDisplayAllActionPerformed(evt);
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
                            .addComponent(ScrollPane_Lessons, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
                            .addGroup(Panel_LessonsTabLayout.createSequentialGroup()
                                .addComponent(Button_LessonDisplayAll)
                                .addGap(155, 155, 155)
                                .addComponent(jLabel1)
                                .addGap(238, 238, 238)
                                .addComponent(Button_LessonDelete)))))
                .addContainerGap())
        );
        Panel_LessonsTabLayout.setVerticalGroup(
            Panel_LessonsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_LessonsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Label_LessonsList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ScrollPane_Lessons, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(Panel_LessonsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_LessonsTabLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(Panel_LessonsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Button_LessonDelete)
                            .addComponent(Button_LessonDisplayAll))
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

    private void Button_StudentAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_StudentAddNewActionPerformed
        // TODO add your handling code here:
        jDialog_AddingNewStudent.setVisible(true);
        jDialog_AddingNewStudent.pack();
    }//GEN-LAST:event_Button_StudentAddNewActionPerformed

    private void TextField_TeacherNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextField_TeacherNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_TeacherNameActionPerformed

    private void TextField_TeacherPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextField_TeacherPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_TeacherPriceActionPerformed

    private void TextField_TeacherSkillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextField_TeacherSkillActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextField_TeacherSkillActionPerformed

    private void Button_StudentAddLessonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_StudentAddLessonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Button_StudentAddLessonActionPerformed

    private void Button_StudentShowAllStudentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_StudentShowAllStudentsActionPerformed
        // TODO add your handling code here:
        List<Student> list = sm.findAllStudents();
        DefaultListModel model = new DefaultListModel();
        for (Student t : list) {
            model.addElement(t);
        }
        List_StudentList.setModel(model);
        List_StudentList.setCellRenderer(new EntityListRenderer());
    }//GEN-LAST:event_Button_StudentShowAllStudentsActionPerformed

    private void Button_StudentShowAllTeachersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_StudentShowAllTeachersActionPerformed
        // TODO add your handling code here:
        List<Teacher> list = tm.findAllTeachers();
        DefaultListModel model = new DefaultListModel();
        for (Teacher t : list) {
            model.addElement(t.getFullName());
        }
        List_TeachersList.setModel(model);
        List_StudentList.setCellRenderer(new EntityListRenderer());
    }//GEN-LAST:event_Button_StudentShowAllTeachersActionPerformed

    private void Button_LessonDisplayAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_LessonDisplayAllActionPerformed
        // TODO add your handling code here:
        
            List<Lesson> ls = lm.findAllLessons();
            LessonTableModel model = new LessonTableModel(ls);
            
            jTable_lessons.setModel(model);
            //nastavime jinej model nez pri inicializaci, nejake nastaveni se muze zmenit (napr column selection)
        
    }//GEN-LAST:event_Button_LessonDisplayAllActionPerformed

    private void Button_TeacherAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_TeacherAddNewActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_Button_TeacherAddNewActionPerformed

    private void Button_LessonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_LessonDeleteActionPerformed
        // TODO add your handling code here:
        
        //lm.deleteLesson(lesson);
    }//GEN-LAST:event_Button_LessonDeleteActionPerformed

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

    private void Button_AddingStudentSaveNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_AddingStudentSaveNewActionPerformed
        
        //need to check valid data!!
        Student student = new Student();
        student.setFullName(TextField_StudentName1.getText());
        student.setPrice(BigDecimal.valueOf(Double.valueOf(TextField_StudentPrice1.getText())).setScale(2));
        student.setRegion((Region) ComboBox_StudentRegion1.getSelectedItem());
        student.setSkill(Integer.parseInt(TextField_StudentSkill1.getText()));
        sm.createStudent(student);
        jDialog_AddingNewStudent.dispose();
        
    }//GEN-LAST:event_Button_AddingStudentSaveNewActionPerformed

    private void Button_TeacherUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_TeacherUpdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Button_TeacherUpdateActionPerformed

    
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
            dataSource = createMemoryDatabase(); // delat v jinym vlakne?
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
    private javax.swing.JButton Button_AddingStudentSaveNew;
    private javax.swing.JButton Button_LessonDelete;
    private javax.swing.JButton Button_LessonDisplayAll;
    private javax.swing.JButton Button_StudentAddLesson;
    private javax.swing.JButton Button_StudentAddNew;
    private javax.swing.JButton Button_StudentDisplayLessons;
    private javax.swing.JButton Button_StudentShowAllStudents;
    private javax.swing.JButton Button_StudentShowAllTeachers;
    private javax.swing.JButton Button_StudentUpdate;
    private javax.swing.JButton Button_TeacherAddLesson;
    private javax.swing.JButton Button_TeacherAddNew;
    private javax.swing.JButton Button_TeacherDisplayLessons;
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
    private javax.swing.JPanel Panel_AddingStudentEditPanel;
    private javax.swing.JPanel Panel_LessonsTab;
    private javax.swing.JPanel Panel_StudentEditPanel;
    private javax.swing.JPanel Panel_StudentsTab;
    private javax.swing.JPanel Panel_TeacherEditPanel;
    private javax.swing.JPanel Panel_TeachersTab;
    private javax.swing.JScrollPane ScrollPane_Lessons;
    private javax.swing.JScrollPane ScrollPane_TeacherList;
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
    private javax.swing.JSplitPane SplitPane_TeacherID;
    private javax.swing.JSplitPane SplitPane_TeacherName;
    private javax.swing.JSplitPane SplitPane_TeacherPrice;
    private javax.swing.JSplitPane SplitPane_TeacherRegion;
    private javax.swing.JSplitPane SplitPane_TeacherSkill;
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
    private javax.swing.JDialog jDialog_AddingNewStudent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane_StudentList;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable_lessons;
    // End of variables declaration//GEN-END:variables
}
