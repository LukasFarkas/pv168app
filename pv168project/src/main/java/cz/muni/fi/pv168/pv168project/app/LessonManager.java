/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author L
 */
public class LessonManager {
    
    private DataSource dataSource;
    
    private final Clock clock;

    public LessonManager(Clock clock) {
        this.clock = clock;
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private void validate(Lesson lesson) throws IllegalArgumentException {
        if (lesson == null) {
            throw new IllegalArgumentException("lesson is null");
        }
        if (lesson.getStart() == null) {
            throw new IllegalArgumentException();
        }
        if (lesson.getDuration() <= 0) {
            throw new IllegalArgumentException("duration is negative number");
        }
        if (lesson.getPrice().doubleValue() < 0 ) {
            throw new IllegalArgumentException ("price is negative.");
        }
        if (lesson.getTeacherId() == null) {
            throw new IllegalArgumentException("Teacher is null.");
        }
        if (lesson.getStudentId() == null) {
            throw new IllegalArgumentException("Student is null.");
        }
    }
    
    public void createLesson(Lesson lesson) throws ServiceFailureException {
        if (lesson.getId() != null) {
            throw new IllegalArgumentException("lesson id is already set");
        }
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "INSERT INTO LESSON (start,duration,price,notes,teacher,student) VALUES (?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) 
        {
            st.setTimestamp(1, toSqlTimestamp(lesson.getStart()));
            st.setInt(2, lesson.getDuration());
            st.setString(3, lesson.getPrice().toPlainString());
            st.setString(4, lesson.getNotes());
            st.setLong (5, lesson.getTeacherId());
            st.setLong (6, lesson.getStudentId());
            
            
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows ("
                        + addedRows + ") inserted when trying to insert lesson " + lesson);
            }
            
            ResultSet keyRS = st.getGeneratedKeys();
            lesson.setId(getKey(keyRS, lesson));
            
        } catch (SQLException ex) {
                throw new ServiceFailureException("Error when inserting lesson " + lesson, ex);
            }
    }
    
    private Long getKey(ResultSet keyRS, Lesson lesson) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert student " + lesson
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert student " + lesson
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert student " + lesson
                    + " - no key found");
        }
    }
    
    public Lesson getLesson(Long id) throws ServiceFailureException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,start,duration,price,notes,teacher,student FROM lesson WHERE id = ?")) {

            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Lesson lesson = resultSetToLesson(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + lesson + " and " + resultSetToLesson(rs));
                }

                return lesson;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving lesson with id " + id, ex);
        }
    }

    public List<Lesson> getLesson(Teacher teacher, Student student){
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,start,duration,price,notes,teacher,student FROM student WHERE teacher = ?, student = ?")) {

            st.setLong(1, teacher.getId());
            st.setLong(2, student.getId());
            
            ResultSet rs = st.executeQuery();

            List<Lesson> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToLesson(rs));
            }
            return result;

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving student-teacher lessons", ex);
        }
    }
    
    public List<Lesson> getLesson(Teacher teacher){
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,start,duration,price,notes,teacher,student FROM student WHERE teacher = ?")) {

            st.setLong(1, teacher.getId());
            
            ResultSet rs = st.executeQuery();

            List<Lesson> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToLesson(rs));
            }
            return result;

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving teacher lessons", ex);
        }
    }
    
    public List<Lesson> getLesson(Student student){
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,start,duration,price,notes,teacher,student FROM student WHERE student = ?")) {

            st.setLong(1, student.getId());
            
            ResultSet rs = st.executeQuery();

            List<Lesson> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToLesson(rs));
            }
            return result;

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving student lessons", ex);
        }
    }
    
    private Lesson resultSetToLesson(ResultSet rs) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(rs.getLong("id"));
        lesson.setStart(toLocalDateTime(rs.getTimestamp("start")));
        lesson.setDuration(rs.getInt("duration"));
            // need to check if new object doesn't cause stack overflow 
        lesson.setPrice(new BigDecimal(rs.getString("price")));
        lesson.setNotes(rs.getString("notes"));
        lesson.setTeacherId(rs.getLong("teacher"));
        lesson.setStudentId(rs.getLong("student"));
        return lesson;
    }
    
    public void updateLesson(Lesson lesson) throws ServiceFailureException {
        validate(lesson);
        if (lesson.getId() == null) {
            throw new IllegalArgumentException("lesson id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                    "UPDATE Lesson SET start = ?, duration = ?, price = ?, notes = ?, teacher = ?, student = ? WHERE id = ?")) {

            st.setTimestamp(1, toSqlTimestamp(lesson.getStart()));
            st.setInt(2, lesson.getDuration());
            st.setString(3, lesson.getPrice().toPlainString());
            st.setString(4, lesson.getNotes());
            st.setLong(5, lesson.getTeacherId());
            st.setLong(6, lesson.getStudentId());
                // to look at, not sure if works
            st.setLong(7, lesson.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("Lesson " + lesson + " was not found in database!");
            } else if (count != 1) {
                throw new ServiceFailureException("Invalid updated rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when updating lesson " + lesson, ex);
        }
    }

    public void deleteLesson(Lesson lesson) throws ServiceFailureException {
        if (lesson == null) {
            throw new IllegalArgumentException("lesson is null");
        }
        if (lesson.getId() == null) {
            throw new IllegalArgumentException("lesson id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                    "DELETE FROM lesson WHERE id = ?")) {

            st.setLong(1, lesson.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("Lesson " + lesson + " was not found in database!");
            } else if (count != 1) {
                throw new ServiceFailureException("Invalid deleted rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when updating lesson " + lesson, ex);
        }
    }
    
    public List<Lesson> findAllLesson() throws ServiceFailureException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,start,duration,price,notes,teacher,student FROM student")) {

            ResultSet rs = st.executeQuery();

            List<Lesson> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToLesson(rs));
            }
            return result;

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving all lessons", ex);
        }
    }
    
    private static Timestamp toSqlTimestamp(LocalDateTime localdatetime) {
        return localdatetime == null ? null : Timestamp.valueOf(localdatetime);
    }

    private static LocalDateTime toLocalDateTime (Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
