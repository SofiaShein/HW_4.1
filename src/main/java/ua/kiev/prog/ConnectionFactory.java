package ua.kiev.prog;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static final ComboPooledDataSource ds = new ComboPooledDataSource();
    private static boolean inited;

    public static Connection getConnection() throws SQLException {
        initialize();
        return ds.getConnection();
    }


    private static void initialize() {
        if (inited) return;

        Properties properties = new Properties();
        try {
            properties.load(ConnectionFactory.class.getClassLoader().getResourceAsStream("database.properties"));

            ds.setDriverClass(properties.getProperty("db.driver"));
            ds.setJdbcUrl(properties.getProperty("db.url"));
            ds.setUser(properties.getProperty("db.user"));
            ds.setPassword(properties.getProperty("db.password"));

            inited = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}