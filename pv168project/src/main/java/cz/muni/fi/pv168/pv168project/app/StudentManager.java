/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import cz.muni.fi.pv168.common.DBUtils;
import static cz.muni.fi.pv168.common.DBUtils.executeQueryForMultipleStudents;
import static cz.muni.fi.pv168.common.DBUtils.executeQueryForSingleStudent;
import static cz.muni.fi.pv168.common.DBUtils.isMember;
import cz.muni.fi.pv168.common.IllegalEntityException;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.common.ValidationException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author L
 */
public class StudentManager {

    private DataSource dataSource;
    
    private static final Logger logger = Logger.getLogger(
            StudentManager.class.getName());

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    private void validate(Student student) throws IllegalArgumentException, ValidationException {
        if (student == null) {
            throw new IllegalArgumentException("student is null");
        }
        if (student.getFullName() == null) {
            throw new ValidationException("name is null");
        }
        if (student.getSkill() <= 0) {
            throw new ValidationException("skill is negative number");
        }
        if (!isMember(student.getRegion()))
            throw new ValidationException("region is not permitted");
        if (student.getPrice().setScale(2).signum() == -1 )
            throw new ValidationException("price is negative number");
    }
    
    public void createStudent(Student student) throws ServiceFailureException {
        checkDataSource();
        validate(student);
        
        if (student.getId() != null) {
            throw new IllegalEntityException("student id is already set");
        }

        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            
            st = conn.prepareStatement(
                    "INSERT INTO Student (fullName,skill,region,price) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, student.getFullName());
            st.setInt(2, student.getSkill());
            st.setString(3, student.getRegion().toString());
            st.setBigDecimal(4, student.getPrice().setScale(2));
            
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, student, true);  

            Long id = DBUtils.getId(st.getGeneratedKeys());
            student.setId(id);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when inserting student" + student.getId() + "into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    public Student getStudent(Long id) throws ServiceFailureException {
        checkDataSource();
        
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,fullName,skill,region,price FROM Student WHERE id = ?");
            st.setLong(1, id);
            return executeQueryForSingleStudent(st);
        } catch (SQLException ex) {
            String msg = "Error when getting student " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    public List<Student> findMatchForTeacher (Teacher teacher) {
        // validate in separate class ??
        //validate();
        checkDataSource();
        
        if (teacher == null) {
            throw new IllegalArgumentException("teacher is null");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,fullName,skill,region,price FROM Student WHERE skill <= ?, region = ?, price >= ?");
            st.setInt(1, teacher.getSkill());
            st.setString (2, teacher.getRegion().toString());
            st.setBigDecimal(3, teacher.getPrice().setScale(2));
            
            return executeQueryForMultipleStudents(st);
        } catch (SQLException ex) {
            String msg = "Error when getting match for teacher with id = " + teacher.getId() + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    public void updateStudent(Student student) throws ServiceFailureException {
        checkDataSource();
        validate(student);
        
        if (student.getId() == null) {
            throw new IllegalEntityException("student id is null");
        }        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);            
            st = conn.prepareStatement(
                    "UPDATE Student SET fullName = ?, skill = ?, region = ?, price = ? WHERE id = ?");
            st.setString(1, student.getFullName());
            st.setInt(2, student.getSkill());
            st.setString(3, student.getRegion().toString());
            st.setBigDecimal(4, student.getPrice().setScale(2));
            
            st.setLong(5, student.getId());
            
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, student, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when updating student" + student.getId() + "in the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    public void deleteStudent(Student student) throws ServiceFailureException {
        checkDataSource();
        if (student == null) {
            throw new IllegalArgumentException("student is null");
        }        
        if (student.getId() == null) {
            throw new IllegalEntityException("student id is null");
        }        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM Student WHERE id = ?");
            
            st.setLong(1, student.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, student, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when deleting student" + student.getId() + "from the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    public List<Student> findAllStudents() throws ServiceFailureException {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,fullName,skill,region,price FROM Student");
            return executeQueryForMultipleStudents(st);
        } catch (SQLException ex) {
            String msg = "Error when getting all students from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }          
    }
    
    public List<Student> findAllFreeStudents() {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // NOT SURE IF WORKS - HAVE NOT TRIED IT YET
            st = conn.prepareStatement(
                    "SELECT id,fullName,skill,region,price FROM Student WHERE id NOT IN (SELECT student FROM lesson) ");
            return executeQueryForMultipleStudents(st);
        } catch (SQLException ex) {
            String msg = "Error when getting all free students from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        } 
    }

    
    
    /*
    private Long getKey(ResultSet keyRS, Student student) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert student " + student
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert student " + student
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert student " + student
                    + " - no key found");
        }
    }
    
    private Student resultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setFullName(rs.getString("fullName"));
        student.setLevel(rs.getInt("level"));
        student.setDetails(rs.getString("details"));
        return student;
    }   
*/
}

