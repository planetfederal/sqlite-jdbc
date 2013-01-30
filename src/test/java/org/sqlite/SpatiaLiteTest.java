package org.sqlite;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

public class SpatiaLiteTest {

    @BeforeClass
    public static void forName() throws Exception {
        Class.forName("org.sqlite.JDBC");
    }

    @Test
    public void spatiaLiteTest() throws Exception {
        Properties prop = new Properties();
        prop.setProperty("enable_shared_cache", "true");
        prop.setProperty("enable_load_extension", "true");
        prop.setProperty("enable_spatialite", "true");
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:", prop);
            Statement stat = conn.createStatement();
            stat.execute("SELECT InitSpatialMetaData()");
            stat.close();
            stat = conn.createStatement();
            ResultSet rs = 
                    stat.executeQuery("SELECT * FROM geometry_columns");
            rs.close();
            stat.close();

            stat = conn.createStatement();
            rs = stat.executeQuery("SELECT SpatiaLite_Version()");
            assertTrue(rs.next());

            String ver = rs.getString(1);
            assertTrue(ver.startsWith("3"));
            
            rs.close();
            stat.close();
        }
        finally {
            if (conn != null)
                conn.close();
        }
    }
}
