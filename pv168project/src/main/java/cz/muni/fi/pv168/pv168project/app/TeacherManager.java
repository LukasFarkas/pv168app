/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author L
 */
public class TeacherManager {
    
    private final DataSource dataSource;
    
    /*
    public StudentManager () {
        this.dataSource = null;
    }
    */
    
    public TeacherManager (DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private void validate(Teacher teacher) throws IllegalArgumentException {
        if (teacher == null) {
            throw new IllegalArgumentException("teacher is null");
        }
        if (teacher.getFullName() == null) {
            throw new IllegalArgumentException();
        }
        if (teacher.getLevel() <= 0) {
            throw new IllegalArgumentException("level is negative number");
        }
    }
    
    public void createTeacher (Teacher teacher) throws ServiceFailureException {
        validate(teacher);
        if (teacher.getId()!= null) {
            throw new IllegalArgumentException("teacher id is already set");
        }

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "INSERT INTO TEACHER (fullName,level,details) VALUES (?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, teacher.getFullName());
            st.setInt(2, teacher.getLevel());
            st.setString(3, teacher.getDetails());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows ("
                        + addedRows + ") inserted when trying to insert teacher " + teacher);
            }

            ResultSet keyRS = st.getGeneratedKeys();
            teacher.setId(getKey(keyRS, teacher));

        } catch (SQLException ex) {
            throw new ServiceFailureException("Error when inserting teacher " + teacher, ex);
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

    public Teacher getTeacher(Long id) throws ServiceFailureException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,fullName,level,details FROM teacher WHERE id = ?")) {

            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Teacher teacher = resultSetToTeacher(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + teacher + " and " + resultSetToTeacher(rs));
                }

                return teacher;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving teacher with id " + id, ex);
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

    public void updateTeacher(Teacher teacher) throws ServiceFailureException {
        validate(teacher);
        if (teacher.getId() == null) {
            throw new IllegalArgumentException("teacher id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                    "UPDATE Teacher SET fullName = ?, level = ?, details = ? WHERE id = ?")) {

            st.setString(1, teacher.getFullName());
            st.setInt(2, teacher.getLevel());
            st.setString(3, teacher.getDetails());
            st.setLong(5, teacher.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("Teacher " + teacher + " was not found in database!");
            } else if (count != 1) {
                throw new ServiceFailureException("Invalid updated rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when updating teacher " + teacher, ex);
        }
    }

    public void deleteTeacher(Teacher teacher) throws ServiceFailureException {
        if (teacher == null) {
            throw new IllegalArgumentException("teacher is null");
        }
        if (teacher.getId() == null) {
            throw new IllegalArgumentException("teacher id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                    "DELETE FROM teacher WHERE id = ?")) {

            st.setLong(1, teacher.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("Teacher " + teacher + " was not found in database!");
            } else if (count != 1) {
                throw new ServiceFailureException("Invalid deleted rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when updating teacher " + teacher, ex);
        }
    }
    
    public List<Teacher> findAllTeachers() throws ServiceFailureException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,fullName,level,details FROM teacher")) {

            ResultSet rs = st.executeQuery();

            List<Teacher> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToTeacher(rs));
            }
            return result;

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving all teachers", ex);
        }
    }
    
}
