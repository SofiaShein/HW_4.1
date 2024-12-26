package task4_1;


import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private  static final ComboPooledDataSource ds = new ComboPooledDataSource();
    private static boolean inited;

    public static Connection getConnection() throws SQLException {
        initialize();
        return ds.getConnection();
    }

    private static void initialize(){
        if(inited) return;

        Properties prop = new Properties();

        try{
            prop.load(ConnectionFactory.class.getClassLoader().getResourceAsStream("new-database.properties"));

            ds.setDriverClass(prop.getProperty("db.driver"));
            ds.setJdbcUrl(prop.getProperty("db.url"));
            ds.setUser(prop.getProperty("db.user"));
            ds.setPassword(prop.getProperty("db.password"));

            inited = true;

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}