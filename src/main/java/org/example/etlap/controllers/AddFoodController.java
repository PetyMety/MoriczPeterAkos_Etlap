package org.example.etlap.controllers;

import javafx.application.Platform;
import javafx.scene.control.*;
import org.example.etlap.database.DatabaseConnection;
import org.example.etlap.models.Food;

import java.sql.SQLException;

public class AddFoodController {
    private DatabaseConnection databaseConnection;
    @javafx.fxml.FXML
    private TextArea foodDescriptionTextField;
    @javafx.fxml.FXML
    private TextField foodNameTextField;
    @javafx.fxml.FXML
    private ComboBox<String> categoryCBox;
    @javafx.fxml.FXML
    private Spinner<Integer> foodPriceSpinner;

    public void setDatabaseConnection(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        try {
            categoryCBox.setItems(databaseConnection.getCategory());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void initialize(){
        foodPriceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 100, 100));
    }


    @javafx.fxml.FXML
    public void add2FoodButton() {
        String name = foodNameTextField.getText();
        String description = foodDescriptionTextField.getText();
        String category = categoryCBox.getValue();
        Integer price = foodPriceSpinner.getValue();
        Food food = new Food(null, name, description, category, price);
        try {
            databaseConnection.create(food);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Succesful data acquisition!");
            alert.showAndWait();

            if (etlapController != null) {
                etlapController.refreshData();
            }

            foodNameTextField.clear();
            foodDescriptionTextField.clear();
            foodPriceSpinner.getEditor().clear();
            categoryCBox.setValue(null);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("An error occurred while recording the data!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    private EtlapController etlapController;

    public void setEtlapController(EtlapController etlapController) {
        this.etlapController = etlapController;
    }

}