package cz.muni.fi.pv168.common;

import cz.muni.fi.pv168.pv168project.app.Lesson;
import cz.muni.fi.pv168.pv168project.app.Region;
import cz.muni.fi.pv168.pv168project.app.Student;
import cz.muni.fi.pv168.pv168project.app.Teacher;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * Some DB tools.
 * 
 * @author Petr Adamek 
 */
public class DBUtils {

    private static final Logger logger = Logger.getLogger(
            DBUtils.class.getName());

    /**
     * Closes connection and logs possible error.
     * 
     * @param conn connection to close
     * @param statements  statements to close
     */
    public static void closeQuietly(Connection conn, Statement ... statements) {
        for (Statement st : statements) {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Error when closing statement", ex);
                }                
            }
        }        
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when switching autocommit mode back to true", ex);
            }
            try {
                conn.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when closing connection", ex);
            }
        }
    }

    /**
     * Rolls back transaction and logs possible error.
     * 
     * @param conn connection
     */
    public static void doRollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                if (conn.getAutoCommit()) {
                    throw new IllegalStateException("Connection is in the autocommit mode!");
                }
                conn.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when doing rollback", ex);
            }
        }
    }

    /**
     * Extract key from given ResultSet.
     * 
     * @param key resultSet with key
     * @return key from given result set
     * @throws SQLException when operation fails
     */
    public static Long getId(ResultSet key) throws SQLException {
        if (key.getMetaData().getColumnCount() != 1) {
            throw new IllegalArgumentException("Given ResultSet contains more columns");
        }
        if (key.next()) {
            Long result = key.getLong(1);
            if (key.next()) {
                throw new IllegalArgumentException("Given ResultSet contains more rows");
            }
            return result;
        } else {
            throw new IllegalArgumentException("Given ResultSet contain no rows");
        }
    }

    /**
     * Reads SQL statements from file. SQL commands in file must be separated by 
     * a semicolon.
     * 
     * @param url url of the file
     * @return array of command  strings
     */
    private static String[] readSqlStatements(URL url) {
        try {
            char buffer[] = new char[256];
            StringBuilder result = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
            while (true) {
                int count = reader.read(buffer);
                if (count < 0) {
                    break;
                }
                result.append(buffer, 0, count);
            }
            return result.toString().split(";");
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read " + url, ex);
        }
    }
    
    /**
     * Try to execute script for creating tables. If tables already exist, 
     * appropriate exception is catched and ignored.
     * 
     * @param ds dataSource 
     * @param scriptUrl url of script for creating tables
     * @throws SQLException when operation fails
     */
    public static void tryCreateTables(DataSource ds, URL scriptUrl) throws SQLException {
        try {
            executeSqlScript(ds, scriptUrl);
            logger.warning("Tables created");
        } catch (SQLException ex) {
            if ("X0Y32".equals(ex.getSQLState())) {
                // This code represents "Table/View/... already exists"
                // This code is Derby specific!
                return;
            } else {
                throw ex;
            }
        }
    }
    
    /**
     * Executes SQL script.
     * 
     * @param ds datasource
     * @param scriptUrl url of sql script to be executed
     * @throws SQLException when operation fails
     */
    public static void executeSqlScript(DataSource ds, URL scriptUrl) throws SQLException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            for (String sqlStatement : readSqlStatements(scriptUrl)) {
                if (!sqlStatement.trim().isEmpty()) {
                    conn.prepareStatement(sqlStatement).executeUpdate();
                }
            }
        } finally {
            closeQuietly(conn);
        }
    }

    /**
     * Check if updates count is one. Otherwise appropriate exception is thrown.
     * 
     * @param count updates count.
     * @param entity updated entity (for includig to error message)
     * @param insert flag if performed operation was insert
     * @throws IllegalEntityException when updates count is zero, so updated entity does not exist
     * @throws ServiceFailureException when updates count is unexpected number
     */
    public static void checkUpdatesCount(int count, Object entity, 
            boolean insert) throws IllegalEntityException, ServiceFailureException {
        
        if (!insert && count == 0) {
            throw new IllegalEntityException("Entity " + entity + " does not exist in the db");
        }
        if (count != 1) {
            throw new ServiceFailureException("Internal integrity error: Unexpected rows count in database affected: " + count);
        }
    }
    
    public static Lesson executeQueryForSingleLesson(PreparedStatement st) throws SQLException, ServiceFailureException {
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Lesson result = rowToLesson(rs);
            if (rs.next()) {
                throw new ServiceFailureException(
                        "Internal integrity error: more lessons with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }

    public static List<Lesson> executeQueryForMultipleLessons(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        List<Lesson> result = new ArrayList<Lesson>();
        while (rs.next()) {
            result.add(rowToLesson(rs));
        }
        return result;
    }
    
    public static  Lesson rowToLesson(ResultSet rs) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(rs.getLong("id"));
        //lesson.setStart(toLocalDateTime(rs.getTimestamp("start")));
            // need to check if new object doesn't cause stack overflow or smt
        lesson.setPrice(rs.getBigDecimal("price").setScale(2));
        lesson.setRegion(Region.valueOf(rs.getString("region")));
        lesson.setSkill(rs.getInt("skill"));
        lesson.setTeacherId(rs.getLong("teacherid"));
        lesson.setStudentId(rs.getLong("studentid"));
        return lesson;
    }
    
    public static Student executeQueryForSingleStudent(PreparedStatement st) throws SQLException, ServiceFailureException {
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Student result = rowToStudent(rs);
            if (rs.next()) {
                throw new ServiceFailureException(
                        "Internal integrity error: more students with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }

    public static List<Student> executeQueryForMultipleStudents(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        List<Student> result = new ArrayList<Student>();
        while (rs.next()) {
            result.add(rowToStudent(rs));
        }
        return result;
    }

    public static  Student rowToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setFullName(rs.getString("fullName"));
        student.setSkill(rs.getInt("skill"));
        student.setRegion(Region.valueOf(rs.getString("region")));
        student.setPrice(rs.getBigDecimal("price").setScale(2));
        return student;
    }
    
    public static Teacher executeQueryForSingleTeacher(PreparedStatement st) throws SQLException, ServiceFailureException {
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Teacher result = rowToTeacher(rs);
            if (rs.next()) {
                throw new ServiceFailureException(
                        "Internal integrity error: more students with the same id found!");
            }
            return result;
        } else {
            return null;
        }
    }

    public static List<Teacher> executeQueryForMultipleTeachers(PreparedStatement st) throws SQLException {
        ResultSet rs = st.executeQuery();
        List<Teacher> result = new ArrayList<Teacher>();
        while (rs.next()) {
            result.add(rowToTeacher(rs));
        }
        return result;
    }

    public static  Teacher rowToTeacher(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(rs.getLong("id"));
        teacher.setFullName(rs.getString("fullName"));
        teacher.setSkill(rs.getInt("skill"));
        teacher.setRegion(Region.valueOf(rs.getString("region")));
        teacher.setPrice(rs.getBigDecimal("price").setScale(2));
        return teacher;
    }
    
    public static Timestamp toSqlTimestamp(LocalDateTime localdatetime) {
        return localdatetime == null ? null : Timestamp.valueOf(localdatetime);
    }

    public static LocalDateTime toLocalDateTime (Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
    
    public static boolean isMember(Region reg) {
       Region[] regs = Region.values();
       for (Region r : regs)
           if (r.equals(reg))
               return true;
       return false;
   }
   
}
