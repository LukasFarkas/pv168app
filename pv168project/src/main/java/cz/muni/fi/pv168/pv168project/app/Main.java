/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;

import cz.muni.fi.pv168.common.DBUtils;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.List;
import java.util.logging.Level;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static DataSource createMemoryDatabase() throws SQLException {
        BasicDataSource bds = new BasicDataSource();
        //set JDBC driver and URL
        bds.setDriverClassName(EmbeddedDriver.class.getName());
        bds.setUrl("jdbc:derby:memory:studentsDB;create=true");
        //populate db with tables and data
        
        DBUtils.executeSqlScript(bds,StudentManager.class.getResource("createTables.sql"));
        DBUtils.executeSqlScript(bds,StudentManager.class.getResource("test-data.sql"));
        /*
        new ResourceDatabasePopulator(
                new ClassPathResource("createTables.sql"),
                new ClassPathResource("test-data.sql"))
                .execute(bds);*/
        return bds;
    }

    public static void main(String[] args) {

        log.info("zaciname");

        DataSource dataSource;
        try {
            dataSource = createMemoryDatabase();
            StudentManager studentManager = new StudentManager(dataSource);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        //List<Book> allBooks = bookManager.getAllBooks();
        //System.out.println("allBooks = " + allBooks);

    }

}
