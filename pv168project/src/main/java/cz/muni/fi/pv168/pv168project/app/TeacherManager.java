/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import cz.muni.fi.pv168.common.DBUtils;
import static cz.muni.fi.pv168.common.DBUtils.executeQueryForMultipleTeachers;
import static cz.muni.fi.pv168.common.DBUtils.executeQueryForSingleTeacher;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author L
 */
public class TeacherManager {
    
    private DataSource dataSource;
    
    public TeacherManager () {
    }
    
    public TeacherManager (DataSource ds) {
        this.dataSource = ds;
    }

    private static final Logger logger = Logger.getLogger(
            TeacherManager.class.getName());
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }
    
    private void validate(Teacher teacher) throws IllegalArgumentException, ValidationException {
        if (teacher == null) {
            throw new IllegalArgumentException("teacher is null");
        }
        if (teacher.getFullName() == null) {
            throw new ValidationException("name is null");
        }
        if (teacher.getSkill() <= 0) {
            throw new ValidationException("skill is negative number");
        }
        if (!isMember(teacher.getRegion()))
            throw new ValidationException("region is not permitted");
        if (teacher.getPrice().doubleValue() < 0 )
            throw new ValidationException("price is negative number");
    }
    
    public void createTeacher (Teacher teacher) throws ServiceFailureException {
        checkDataSource();
        validate(teacher);
        
        if (teacher.getId() != null) {
            throw new IllegalEntityException("teacher id is already set");
        }

        Connection conn = null;
        PreparedStatement st = null;
        
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            
            st = conn.prepareStatement(
                    "INSERT INTO Teacher (fullName,skill,region,price) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, teacher.getFullName());
            st.setInt(2, teacher.getSkill());
            st.setString(3, teacher.getRegion().toString());
            st.setBigDecimal(4, teacher.getPrice().setScale(2));
            
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, teacher, true);  

            Long id = DBUtils.getId(st.getGeneratedKeys());
            teacher.setId(id);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when inserting teacher" + teacher.getId() + "into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    public Teacher getTeacher(Long id) throws ServiceFailureException {
        checkDataSource();
        
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,fullName,skill,region,price FROM Teacher WHERE id = ?");
            st.setLong(1, id);
            return executeQueryForSingleTeacher(st);
        } catch (SQLException ex) {
            String msg = "Error when getting teacher " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    public void updateTeacher(Teacher teacher) throws ServiceFailureException {
        checkDataSource();
        validate(teacher);
        
        if (teacher.getId() == null) {
            throw new IllegalEntityException("teacher id is null");
        }        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);            
            st = conn.prepareStatement(
                    "UPDATE Teacher SET fullName = ?, skill = ?, region = ?, price = ? WHERE id = ?");
            st.setString(1, teacher.getFullName());
            st.setInt(2, teacher.getSkill());
            st.setString(3, teacher.getRegion().toString());
            st.setBigDecimal(4, teacher.getPrice().setScale(2));
            
            st.setLong(5, teacher.getId());
            
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, teacher, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when updating teacher" + teacher.getId() + "in the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    public void deleteTeacher(Teacher teacher) throws ServiceFailureException {
        checkDataSource();
        if (teacher == null) {
            throw new IllegalArgumentException("teacher is null");
        }        
        if (teacher.getId() == null) {
            throw new IllegalEntityException("teacher id is null");
        }        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM Teacher WHERE id = ?");
            
            st.setLong(1, teacher.getId());

            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, teacher, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when deleting teacher" + teacher.getId() + "from the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    public List<Teacher> findAllTeachers() throws ServiceFailureException {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,fullName,skill,region,price FROM Teacher");
            return executeQueryForMultipleTeachers(st);
        } catch (SQLException ex) {
            String msg = "Error when getting all teachers from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }          
    }
    
    public List<Teacher> findAllFreeTeachers() {
        checkDataSource();
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // NOT SURE IF WORKS - HAVE NOT TRIED IT YET
            st = conn.prepareStatement(
                    "SELECT id,fullName,skill,region,price FROM Teacher WHERE id NOT IN (SELECT student FROM lesson) ");
            return executeQueryForMultipleTeachers(st);
        } catch (SQLException ex) {
            String msg = "Error when getting all free teachers from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        } 
    }
    
    /*
    public List<Teacher> findMatchForStudent (Student student) {
        //validate();
        checkDataSource();
        
        if (student == null) {
            throw new IllegalArgumentException("student is null");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,fullName,skill,region,price FROM Teacher WHERE skill >= ?, region = ?, price <= ?");
            st.setInt(1, student.getSkill());
            st.setString(2, student.getRegion().toString());
            st.setBigDecimal(3, student.getPrice().setScale(2));
            
            return executeQueryForMultipleTeachers(st);
        } catch (SQLException ex) {
            String msg = "Error when getting match for student " + student.getId() + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    private Long getKey(ResultSet keyRS, Teacher teacher) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert teacher " + teacher
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert teacher " + teacher
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert teacher " + teacher
                    + " - no key found");
        }
    }

    private Teacher resultSetToTeacher(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(rs.getLong("id"));
        teacher.setFullName(rs.getString("fullName"));
        teacher.setLevel(rs.getInt("level"));
        teacher.setDetails(rs.getString("details"));
        return teacher;
    }
    
    */
}
