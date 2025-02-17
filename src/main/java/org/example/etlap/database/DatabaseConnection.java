package org.example.etlap.database;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.etlap.models.Food;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    private static final String DB_DRIVER = "mysql";
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_DATABASE = "etlapdb";

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
            Integer id = result.getInt("id");
            String name = result.getString("nev");
            String description = result.getString("leiras");
            String category = result.getString("kategoria");
            int price = result.getInt("ar");
            foods.add(new Food(id, name, description, category, price));
        }
        return foods;
    }

    public ObservableList<String> getCategory() throws SQLException {
        ObservableList<String> categories = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        String sql = "SELECT kategoria FROM etlap GROUP BY kategoria";
        ResultSet result = statement.executeQuery(sql);
        while (result.next()) {
            String category = result.getString("kategoria");
            categories.add(category);
        }
        return categories;
    }

    public boolean delete(Food food) throws SQLException{
        String sql = "DELETE FROM etlap WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, food.getId());
        return statement.executeUpdate()==1;
    }

    public boolean updatePrice(Food food) throws SQLException{
        String sql = "UPDATE etlap SET ar = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, food.getPrice());
        statement.setInt(2, food.getId());
        return statement.executeUpdate()==1;
    }

    public boolean updateAllPrices(Integer increase) throws SQLException {
        String sql = "UPDATE etlap SET ar = ar + ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, increase);
        return statement.executeUpdate()==1;
    }

    public boolean updateAllPricesPercent(Integer increase) throws SQLException {
        String sql = "UPDATE etlap SET ar = ar + (ar * ? / 100)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, increase);
        return statement.executeUpdate()==1;
    }

    public boolean create(Food food) throws SQLException {
        String sql = "INSERT INTO etlap(nev,leiras,ar,kategoria) VALUES(?,?,?,?);";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, food.getName());
        statement.setString(2, food.getDescription());
        statement.setInt(3, food.getPrice());
        statement.setString(4, food.getCategory());
        return statement.executeUpdate() == 1;
    }

}
