package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.demo.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemFormController {

    @FXML
    private TextField itemNameField;

    @FXML
    private TextField itemCodeField;

    @FXML
    private TextField itemNoField;

    @FXML
    private TextField priceField;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    private Connection connection;

    public ItemFormController() {
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSubmit() {
        String itemName = itemNameField.getText();
        String itemCode = itemCodeField.getText();
        String itemNo = itemNoField.getText();
        double price = Double.parseDouble(priceField.getText());

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO item_mst (item_name, item_code, item_no, price) VALUES (?, ?, ?, ?)");
            statement.setString(1, itemName);
            statement.setString(2, itemCode);
            statement.setString(3, itemNo);
            statement.setDouble(4, price);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Item saved successfully!");
            } else {
                showAlert("Failed to save item.");
            }
        } catch (SQLException | NumberFormatException e) {
            showAlert("Error occurred while saving item: " + e.getMessage());
        }
    }

    @FXML
    private void handleReset() {
        itemNameField.clear();
        itemCodeField.clear();
        itemNoField.clear();
        priceField.clear();
    }

    @FXML
    private void handleUpdate() {
        String itemName = itemNameField.getText();
        String itemNo = itemNoField.getText();
        double price = Double.parseDouble(priceField.getText());
        String itemCode = itemCodeField.getText();

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE item_mst SET item_name=?, item_no=?, price=? WHERE item_code=?");
            statement.setString(1, itemName);
            statement.setString(2, itemNo);
            statement.setDouble(3, price);
            statement.setString(4, itemCode);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Item updated successfully!");
            } else {
                showAlert("Failed to update item.");
            }
        } catch (SQLException | NumberFormatException e) {
            showAlert("Error occurred while updating item: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        String itemCode = itemCodeField.getText();

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM item_mst WHERE item_code=?");
            statement.setString(1, itemCode);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Item deleted successfully!");
                handleReset();
            } else {
                showAlert("Failed to delete item.");
            }
        } catch (SQLException e) {
            showAlert("Error occurred while deleting item: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearchByItemCode() {
        String itemCode = itemCodeField.getText();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM item_mst WHERE item_code=?");
            statement.setString(1, itemCode);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                itemNameField.setText(resultSet.getString("item_name"));
                itemNoField.setText(resultSet.getString("item_no"));
                priceField.setText(String.valueOf(resultSet.getDouble("price")));
            } else {
                showAlert("Item not found.");
            }
        } catch (SQLException e) {
            showAlert("Error occurred while searching for item: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
