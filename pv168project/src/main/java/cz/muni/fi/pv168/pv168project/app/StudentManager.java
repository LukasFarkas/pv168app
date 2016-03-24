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
public class StudentManager {
    
    private final DataSource dataSource;
    
    
    public StudentManager (DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private void validate(Student student) throws IllegalArgumentException {
        if (student == null) {
            throw new IllegalArgumentException("student is null");
        }
        if (student.getFullName() == null) {
            throw new IllegalArgumentException();
        }
        if (student.getLevel() <= 0) {
            throw new IllegalArgumentException("level is negative number");
        }
    }
    
    public void createStudent (Student student) throws ServiceFailureException {
        validate(student);
        if (student.getId() != null) {
            throw new IllegalArgumentException("student id is already set");
        }

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "INSERT INTO STUDENT (fullName,level,details) VALUES (?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, student.getFullName());
            st.setInt(2, student.getLevel());
            st.setString(3, student.getDetails());
            int addedRows = st.executeUpdate();
            if (addedRows != 1) {
                throw new ServiceFailureException("Internal Error: More rows ("
                        + addedRows + ") inserted when trying to insert student " + student);
            }

            ResultSet keyRS = st.getGeneratedKeys();
            student.setId(getKey(keyRS, student));

        } catch (SQLException ex) {
            throw new ServiceFailureException("Error when inserting grave " + student, ex);
        }
    }
    
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

    public Student getStudent(Long id) throws ServiceFailureException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,fullName,level,details FROM student WHERE id = ?")) {

            st.setLong(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Student student = resultSetToStudent(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + student + " and " + resultSetToStudent(rs));
                }

                return student;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving student with id " + id, ex);
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

    public void updateStudent(Student student) throws ServiceFailureException {
        validate(student);
        if (student.getId() == null) {
            throw new IllegalArgumentException("student id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                    "UPDATE Student SET fullName = ?, level = ?, details = ? WHERE id = ?")) {

            st.setString(1, student.getFullName());
            st.setInt(2, student.getLevel());
            st.setString(3, student.getDetails());
                // param should be = 4 ???
            st.setLong(5, student.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("Student " + student + " was not found in database!");
            } else if (count != 1) {
                throw new ServiceFailureException("Invalid updated rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when updating student " + student, ex);
        }
    }

    public void deleteStudent(Student student) throws ServiceFailureException {
        if (student == null) {
            throw new IllegalArgumentException("student is null");
        }
        if (student.getId() == null) {
            throw new IllegalArgumentException("student id is null");
        }
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                    "DELETE FROM student WHERE id = ?")) {

            st.setLong(1, student.getId());

            int count = st.executeUpdate();
            if (count == 0) {
                throw new EntityNotFoundException("Student " + student + " was not found in database!");
            } else if (count != 1) {
                throw new ServiceFailureException("Invalid deleted rows count detected (one row should be updated): " + count);
            }
        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when updating student " + student, ex);
        }
    }
    
    public List<Student> findAllStudent() throws ServiceFailureException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                        "SELECT id,fullName,level,details FROM student")) {

            ResultSet rs = st.executeQuery();

            List<Student> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToStudent(rs));
            }
            return result;

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving all students", ex);
        }
    }
    
}
