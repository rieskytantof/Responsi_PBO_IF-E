package com.pbo.responsi.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {

    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DATABASE = "cart_app";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
        + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver tidak ditemukan.", e);
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void initializeTable(Connection connection) throws SQLException {
        String sql =
            "CREATE TABLE IF NOT EXISTS cart_items ("
            + "  id       INT            NOT NULL AUTO_INCREMENT,"
            + "  name     VARCHAR(255)   NOT NULL UNIQUE,"
            + "  price    DOUBLE         NOT NULL,"
            + "  quantity INT            NOT NULL,"
            + "  PRIMARY KEY (id)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
}