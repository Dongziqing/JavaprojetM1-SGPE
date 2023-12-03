package com.sgms.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.sql.SQLException;
import java.lang.ClassNotFoundException;
import java.util.Properties;

public class BaseDao {

    private static final String PROPERTIES_FILE = "db.properties";

    private static String URL;
    private static String userName;
    private static String password;
    private static String driver;
    static{
        initBaseDao();
    }

    private static void initBaseDao(){
        Properties properties = loadProperties();
        URL = properties.getProperty("jdbc.url");
        userName = properties.getProperty("jdbc.username");
        password = properties.getProperty("jdbc.password");
        driver = properties.getProperty("jdbc.DriverClassName");
    }

    public Connection getCon() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(URL, userName, password);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = BaseDao.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + PROPERTIES_FILE);
                return properties;
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}

