package org.example.etlap.controllers;


import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.etlap.EtlapApplication;
import org.example.etlap.database.DatabaseConnection;
import org.example.etlap.models.Food;

import java.io.IOException;
import java.sql.SQLException;

public class EtlapController {

    @FXML
    private TableView<Food> etlapTable;
    @FXML
    private TableColumn<Food, Integer> priceTableCol;
    @FXML
    private TableColumn<Food, String> nameTableCol;
    @FXML
    private TableColumn<Food, String> categoryTableCol;
    @FXML
    private TextArea descriptionTextArea;
    private ObservableList<Food> selectedItems;
    @FXML
    private Spinner<Integer> percentIncreaseSpinner;
    @FXML
    private Spinner<Integer> sumPriceIncreaseSpinner;
    private DatabaseConnection databaseConnection;

    public void initialize() {
        nameTableCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryTableCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceTableCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        percentIncreaseSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5,50,5,5));
        sumPriceIncreaseSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(50,3000,50,50));

        selectedItems =
                etlapTable.getSelectionModel().getSelectedItems();
        selectedItems.addListener(new ListChangeListener<Food>() {
            @Override
            public void onChanged(Change<? extends Food> change) {
                descriptionTextArea.setText(selectedItems.get(0).getDescription());
            }
        });
        try {
            databaseConnection = new DatabaseConnection();
            listEtlap(databaseConnection);
        }catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Failed to connect to the database!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            Platform.exit();
        }
    }

    public void listEtlap(DatabaseConnection databaseConnection) throws SQLException {
        etlapTable.getItems().clear();
        etlapTable.getItems().addAll(databaseConnection.getAll());
    }

    @FXML
    public void deleteButtonClick() {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Are you sure to delete this?");
        try {
            alert.setContentText(selectedItems.get(0).getName());
        }catch (Exception e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("Nothing has been chosen!");
        }

        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            try {
                DatabaseConnection databaseConnection = new DatabaseConnection();
                databaseConnection.delete(selectedItems.get(0));
                listEtlap(databaseConnection);
            }catch (SQLException e) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Hiba!");
                alert1.setHeaderText("Unable to connect to the database!");
                alert1.setContentText(e.getMessage());
                alert1.showAndWait();
            }
        }
    }

    @FXML
    public void addFoodButton() {
        try {
            Stage stage= new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(EtlapApplication.class.getResource("add-food-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 340);

            AddFoodController controller = fxmlLoader.getController();
            controller.setDatabaseConnection(databaseConnection);

            controller.setEtlapController(this);

            stage.setTitle("Új étel hozzáadása!");
            stage.setScene(scene);
            stage.setOnHidden(event-> {
                try {
                    listEtlap(databaseConnection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void refreshData() {
        try {
            listEtlap(databaseConnection);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Failed to refresh data!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void priceIncreaseButton() throws SQLException {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Increase");
        alert.setHeaderText("Are you sure about increasing the price?");

        if(selectedItems.size() == 0) {
            alert.setContentText("All food price " + sumPriceIncreaseSpinner.getValue() + " Ft-tal!");
            alert.showAndWait();
            if(alert.getResult() == ButtonType.OK) {
                databaseConnection.updateAllPrices(sumPriceIncreaseSpinner.getValue());
                etlapTable.getItems().clear();
                etlapTable.getItems().addAll(databaseConnection.getAll());
            }
        }
        else {
            alert.setContentText(selectedItems.get(0).getName() + " price " + sumPriceIncreaseSpinner.getValue() + " Ft-tal!");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                selectedItems.get(0).setPrice(selectedItems.get(0).getPrice() + sumPriceIncreaseSpinner.getValue());
                databaseConnection.updatePrice(selectedItems.get(0));
                etlapTable.getItems().clear();
                etlapTable.getItems().addAll(databaseConnection.getAll());
            }
        }

    }

    @FXML
    public void percentIncreaseButton() throws SQLException {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Increase");
        alert.setHeaderText("Are you sure about increasing the price?");
        if(selectedItems.size() == 0) {
            alert.setContentText("All food price " + percentIncreaseSpinner.getValue() + " %-al!");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                databaseConnection.updateAllPricesPercent(percentIncreaseSpinner.getValue());
                etlapTable.getItems().clear();
                etlapTable.getItems().addAll(databaseConnection.getAll());
            }
        }
        else {
            alert.setContentText(selectedItems.get(0).getName() + " price " + percentIncreaseSpinner.getValue() + " %-al!");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK){
                selectedItems.get(0).setPrice(selectedItems.get(0).getPrice() +
                        (selectedItems.get(0).getPrice() * percentIncreaseSpinner.getValue() / 100));
                databaseConnection.updatePrice(selectedItems.get(0));
                etlapTable.getItems().clear();
                etlapTable.getItems().addAll(databaseConnection.getAll());
            }
        }
    }

}