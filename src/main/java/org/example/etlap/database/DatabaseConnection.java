package org.example.etlap.database;


import org.example.etlap.models.Food;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    private static final String DB_DRIVER = "mysql";
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_DATABASE = "etlap";

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private final Connection connection;

    public DatabaseConnection() throws SQLException {
        String dbUrl = String.format("jdbc:%s://%s:%s/%s", DB_DRIVER, DB_HOST, DB_PORT, DB_DATABASE);
        connection = DriverManager.getConnection(dbUrl, DB_USER, DB_PASSWORD);
    }

    public List<Food> getAll() throws SQLException {
        List<Food> foods = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM etlap";
        ResultSet result = statement.executeQuery(sql);
        while (result.next()) {
            String name = result.getString("nev");
            String description = result.getString("leiras");
            String category = result.getString("kategoria");
            int price = result.getInt("ar");
            foods.add(new Food(name, description, category, price));
        }
        return foods;
    }

    //Tesztelés alatt - átírni a változókat
    public boolean create(Food food) throws SQLException {
        String sql = "INSERT INTO etlap(nev, leiras, kategoria, ar) VALUES(?,?,?);";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, phone.getBrand());
        statement.setString(2, phone.getModel());
        statement.setInt(3, phone.getPrice());
        return statement.executeUpdate() == 1;
    }

}
