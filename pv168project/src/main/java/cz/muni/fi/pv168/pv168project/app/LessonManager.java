/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import cz.muni.fi.pv168.common.DBUtils;
import static cz.muni.fi.pv168.common.DBUtils.executeQueryForMultipleLessons;
import static cz.muni.fi.pv168.common.DBUtils.executeQueryForMultipleStudents;
import static cz.muni.fi.pv168.common.DBUtils.executeQueryForMultipleTeachers;
import static cz.muni.fi.pv168.common.DBUtils.executeQueryForSingleLesson;
import static cz.muni.fi.pv168.common.DBUtils.isMember;
import cz.muni.fi.pv168.common.DataSourceException;
import cz.muni.fi.pv168.common.IllegalEntityException;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.common.ValidationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author L
 */
public class LessonManager {
    
    private DataSource dataSource;
    
    public LessonManager () {
    }
    
    public LessonManager (DataSource ds) {
        this.dataSource = ds;
    }
    
    private static final Logger logger = Logger.getLogger(
            LessonManager.class.getName());
    
    private void checkDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not set");
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private void validate(Lesson lesson) throws ValidationException {
        if (lesson == null) {
            throw new ValidationException("lesson is null");
        }
        if (lesson.getSkill() < 0) {
            throw new ValidationException("skill value is not permitted");
        }
        if (!isMember(lesson.getRegion()) ) {
            throw new ValidationException("region is not valid");
        }
        if (lesson.getPrice().setScale(2).signum() == -1 ) {
            throw new ValidationException ("price is negative.");
        }
        if (lesson.getTeacherId() == null) {
            throw new ValidationException("Teacher is null.");
        }
        if (lesson.getStudentId() == null) {
            throw new ValidationException("Student is null.");
        }
    }
    
    public void createLesson(Lesson lesson) throws IllegalEntityException, ServiceFailureException, DataSourceException, ValidationException {
        try {
           checkDataSource();
        } catch (IllegalStateException e) {
            throw new DataSourceException ("There was a problem with datasource." ,e);
        }
        validate(lesson);
        
        if (lesson.getId() != null) {
            throw new IllegalEntityException("lesson id is already set");
        }
        Connection conn = null;
        PreparedStatement st = null;        
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            
            st = conn.prepareStatement(
                    "INSERT INTO Lesson (skill,region,price,teacherid,studentid) VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, lesson.getSkill());
            st.setString(2, lesson.getRegion().toString());
            st.setBigDecimal(3, lesson.getPrice().setScale(2));
            st.setLong(4, lesson.getTeacherId());
            st.setLong(5, lesson.getStudentId());
            
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, lesson, true);  

            Long id = DBUtils.getId(st.getGeneratedKeys());
            lesson.setId(id);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when inserting lesson" + lesson.getId() + "into db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    public Lesson getLesson(Long id) throws IllegalArgumentException, ServiceFailureException, DataSourceException {
        try {
           checkDataSource();
        } catch (IllegalStateException e) {
            throw new DataSourceException ("There was a problem with datasource." ,e);
        }
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,skill,region,price,teacherid,studentid FROM Lesson WHERE id = ?");
            st.setLong(1, id);
            return executeQueryForSingleLesson(st);
        } catch (SQLException ex) {
            String msg = "Error when getting lesson with id = " + id + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }

    public Lesson getLesson(Teacher teacher, Student student) throws IllegalArgumentException, ServiceFailureException, DataSourceException{
        try {
           checkDataSource();
        } catch (IllegalStateException e) {
            throw new DataSourceException ("There was a problem with datasource." ,e);
        }
        if (teacher == null) {
            throw new IllegalArgumentException("teacher is null");
        }
        if (student == null) {
            throw new IllegalArgumentException("student is null");
        }       
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,skill,region,price,teacherid,studentid FROM Lesson WHERE teacherid = ? AND studentid = ?");
            st.setLong(1, teacher.getId());
            st.setLong(2, student.getId());
            return executeQueryForSingleLesson(st);
        } catch (SQLException ex) {
            String msg = "Error when getting lesson with teacher/student id = " + teacher.getId() + "/" + student.getId() + " from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    public List<Lesson> getLesson(Teacher teacher) throws IllegalArgumentException, ServiceFailureException, DataSourceException{
        try {
           checkDataSource();
        } catch (IllegalStateException e) {
            throw new DataSourceException ("There was a problem with datasource." ,e);
        }
        
        if (teacher == null) {
            throw new IllegalArgumentException("teacher is null");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,skill,region,price,teacherid,studentid FROM Lesson WHERE teacherid = ?");
            st.setLong(1, teacher.getId());
            return executeQueryForMultipleLessons(st);
        } catch (SQLException ex) {
            String msg = "Error when getting teacher's" + teacher.getId() + "lessons from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    public List<Lesson> getLesson(Student student) throws IllegalArgumentException, ServiceFailureException, DataSourceException{
        try {
           checkDataSource();
        } catch (IllegalStateException e) {
            throw new DataSourceException ("There was a problem with datasource." ,e);
        }
        
        if (student == null) {
            throw new IllegalArgumentException("student is null");
        }
        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,skill,region,price,teacherid,studentid FROM Lesson WHERE studentid = ?");
            st.setLong(1, student.getId());
            return executeQueryForMultipleLessons(st);
        } catch (SQLException ex) {
            String msg = "Error when getting student's" + student.getId() + "lessons from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    public void updateLesson(Lesson lesson) throws IllegalEntityException, ServiceFailureException, DataSourceException, ValidationException {
        try {
           checkDataSource();
        } catch (IllegalStateException e) {
            throw new DataSourceException ("There was a problem with datasource." ,e);
        }
        validate(lesson);
        
        if (lesson.getId() == null) {
            throw new IllegalEntityException("lesson id is null - it is not created");
        }        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);            
            st = conn.prepareStatement(
                    "UPDATE Lesson SET skill = ?, region = ?, price = ?, teacherid = ?, studentid = ? WHERE id = ?");
            st.setInt(1, lesson.getSkill());
            st.setString(2, lesson.getRegion().toString());
            st.setBigDecimal(3, lesson.getPrice().setScale(2));
            st.setLong(4, lesson.getTeacherId());
            st.setLong(5, lesson.getStudentId());
            st.setLong(6, lesson.getId());
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, lesson, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when updating lesson" + lesson.getId() + "in the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }

    public void deleteLesson(Lesson lesson) throws ServiceFailureException, IllegalArgumentException, IllegalEntityException, DataSourceException {
        try {
           checkDataSource();
        } catch (IllegalStateException e) {
            throw new DataSourceException ("There was a problem with datasource." ,e);
        }
        if (lesson == null) {
            throw new IllegalArgumentException("lesson is null");
        }        
        if (lesson.getId() == null) {
            throw new IllegalEntityException("lesson id is null");
        }        
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            // Temporary turn autocommit mode off. It is turned back on in 
            // method DBUtils.closeQuietly(...) 
            conn.setAutoCommit(false);
            st = conn.prepareStatement(
                    "DELETE FROM Lesson WHERE id = ?");
            st.setLong(1, lesson.getId());
            int count = st.executeUpdate();
            DBUtils.checkUpdatesCount(count, lesson, false);
            conn.commit();
        } catch (SQLException ex) {
            String msg = "Error when deleting lesson" + lesson.getId() + "from the db";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.doRollbackQuietly(conn);
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    public List<Lesson> findAllLessons() throws ServiceFailureException, DataSourceException {
        try {
           checkDataSource();
        } catch (IllegalStateException e) {
            throw new DataSourceException ("There was a problem with datasource." ,e);
        }
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,skill,price,region,teacherid,studentid FROM Lesson");
            return executeQueryForMultipleLessons(st);
        } catch (SQLException ex) {
            String msg = "Error when getting all lessons from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        }
    }
    
    /*
     *  Match between student-teacher: 
     *  one student can have one pairing with one specific teacher (and vice versa) 
     *  one student can have unlimited number of pairings with different teachers (and v.v.)
     */
    
    public List<Teacher> findMatchForStudent (Student student) throws IllegalArgumentException, ServiceFailureException, DataSourceException {
        try {
           checkDataSource();
        } catch (IllegalStateException e) {
            throw new DataSourceException ("There was a problem with datasource." ,e);
        }
        if (student == null)
            throw new IllegalArgumentException("student is null");
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,fullName,skill,region,price FROM Teacher WHERE skill >= ? AND region = ? AND price <= ? AND id NOT IN (SELECT teacherid FROM Lesson WHERE studentid = ?)");
            st.setInt(1,student.getSkill());
            st.setString(2,student.getRegion().toString());
            st.setBigDecimal(3,student.getPrice().setScale(2));
            st.setLong(4, student.getId());
            return executeQueryForMultipleTeachers(st);
        } catch (SQLException ex) {
            String msg = "Error when getting matching teachers from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        } 
    }
    
    public List<Student> findMatchForTeacher (Teacher teacher) throws IllegalArgumentException, ServiceFailureException, DataSourceException {
        try {
           checkDataSource();
        } catch (IllegalStateException e) {
            throw new DataSourceException ("There was a problem with datasource." ,e);
        }
        if (teacher == null)
            throw new IllegalArgumentException(" teacher is null");
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.prepareStatement(
                    "SELECT id,fullName,skill,region,price FROM Student WHERE skill <= ? AND region = ? AND price >= ? AND id NOT IN (SELECT studentid FROM Lesson WHERE teacherid = ?)");
            st.setInt(1,teacher.getSkill());
            st.setString(2,teacher.getRegion().toString());
            st.setBigDecimal(3,teacher.getPrice().setScale(2));
            st.setLong(4, teacher.getId());
            return executeQueryForMultipleStudents(st);
        } catch (SQLException ex) {
            String msg = "Error when getting matching students from DB";
            logger.log(Level.SEVERE, msg, ex);
            throw new ServiceFailureException(msg, ex);
        } finally {
            DBUtils.closeQuietly(conn, st);
        } 
    }
    
    public void matchValidation (Teacher teacher, Student student) throws ValidationException {
        if (teacher.getSkill() < student.getSkill())
            throw new ValidationException ("teacher too stupid");
        if (teacher.getPrice().compareTo(student.getPrice()) > 0)
            throw new ValidationException ("student too broke");
        if (teacher.getRegion() != student.getRegion())
            throw new ValidationException ("teacher is not close enough (location-wise speaking)");
    }
    
    public void makeMatch (Teacher teacher, Student student) throws IllegalArgumentException {
        if (student == null)
            throw new IllegalArgumentException("student is null");
        if (teacher == null)
            throw new IllegalArgumentException("teacher is null");
        matchValidation(teacher, student);
        
        Lesson lesson = new Lesson ();
        // level is set by student (teacher is always >= than student)
        lesson.setSkill(student.getSkill());
        // price is set by teacher ... go figure
        lesson.setPrice(teacher.getPrice());
        lesson.setRegion(teacher.getRegion());
        lesson.setTeacherId(teacher.getId());
        lesson.setStudentId(student.getId());
        
        createLesson(lesson);
    } 
}
