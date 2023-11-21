package UI;

import Model.Inventory;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class InventoryUI extends Application {

    private final TableView<Inventory> inventoryTable = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        // Set up columns for the TableView
        TableColumn<Inventory, Integer> idColumn = new TableColumn<>("Inventory ID");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInventoryId()).asObject());

        TableColumn<Inventory, Integer> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductId()).asObject());

        TableColumn<Inventory, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        TableColumn<Inventory, Timestamp> lastUpdatedColumn = new TableColumn<>("Last Updated");
        lastUpdatedColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLastUpdated()));

        // Add columns to the TableView
        inventoryTable.getColumns().addAll(idColumn, productIdColumn, quantityColumn, lastUpdatedColumn);

        // Fetch inventory items and populate the table
        List<Inventory> inventoryItems = Inventory.getAllInventoryFromDB();
        inventoryTable.getItems().addAll(inventoryItems);

        // Define buttons for operations like delete, edit, and add
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            Inventory selectedInventory = inventoryTable.getSelectionModel().getSelectedItem();
            if (selectedInventory != null) {
                Inventory.deleteInventory(selectedInventory);
                inventoryTable.getItems().remove(selectedInventory);
            } else {
                System.out.println("No inventory selected");
            }
        });

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            Inventory selectedInventory = inventoryTable.getSelectionModel().getSelectedItem();
            if (selectedInventory != null) {
                // Open a dialog to edit inventory details
                editInventoryDetails(selectedInventory);
            } else {
                System.out.println("No inventory selected");
            }
        });

        Button addButton = new Button("Add Inventory");
        addButton.setOnAction(e -> addInventory());

        // Handle row selection to enable/disable buttons based on selection

        HBox buttonBar = new HBox(deleteButton, editButton, addButton);

        deleteButton.setDisable(true);
        editButton.setDisable(true);

        inventoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                deleteButton.setDisable(false);
                editButton.setDisable(false);
            } else {
                deleteButton.setDisable(true);
                editButton.setDisable(true);
            }
        });

        VBox root = new VBox(inventoryTable, buttonBar);
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Inventory Table");
        primaryStage.show();
    }

    private void editInventoryDetails(Inventory inventory) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Inventory");

        // Create fields to edit inventory details
        TextField productIdField = new TextField(String.valueOf(inventory.getProductId()));
        TextField quantityField = new TextField(String.valueOf(inventory.getQuantity()));

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Update inventory details based on input fields
            try{
                inventory.setProductId(Integer.parseInt(productIdField.getText()));
                inventory.setQuantity(Integer.parseInt(quantityField.getText()));

                Inventory.updateInventory(inventory); // Update details in the database

                // Close the edit window
                editStage.close();

                // Refresh table to display updated data
                inventoryTable.getItems().clear();
                inventoryTable.getItems().addAll(Inventory.getAllInventoryFromDB());
            }catch(NumberFormatException ex){
                // Inform the user about incorrect input
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Please enter valid numbers for product id and quantity fields.");
                alert.showAndWait();
            }
            catch (SQLException ex) {
                // Inform the user about incorrect product ID
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid product ID");
                alert.setContentText("Please enter valid product ID, if you are unsure, you can check the IDs on the product table");
                alert.showAndWait();
            }

        });

        VBox editLayout = new VBox(
                new Label("Product ID:"), productIdField,
                new Label("Quantity:"), quantityField,
                saveButton
        );

        Scene editScene = new Scene(editLayout, 300, 200);
        editStage.setScene(editScene);
        editStage.show();
    }

    private void addInventory() {
        Stage addStage = new Stage();
        addStage.setTitle("Add Inventory");

        // Create fields to add a new inventory item
        TextField productIdField = new TextField();
        TextField quantityField = new TextField();

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            // Create a new inventory item with the entered details
            String productIdText = productIdField.getText();
            String quantityText = quantityField.getText();
            try{
                int productId = Integer.parseInt(productIdText);
                int quantity = Integer.parseInt(quantityText);
                Inventory newInventory = new Inventory(0, productId, quantity,null); // Assuming last updated time is current time
                // Add the new inventory item to the database
                Inventory.addInventory(newInventory);

                // Close the add window
                addStage.close();

                // Refresh table to display the new data
                inventoryTable.getItems().clear();
                inventoryTable.getItems().addAll(Inventory.getAllInventoryFromDB());
            }catch(NumberFormatException ex){
                // Inform the user about incorrect input
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Please enter valid numbers for product id and quantity fields.");
                alert.showAndWait();
            }catch (SQLException ex){
                // Inform the user about incorrect product ID
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid product ID");
                alert.setContentText("Please enter valid product ID, if you are unsure, you can check the IDs on the product table");
                alert.showAndWait();
            }

        });

        VBox addLayout = new VBox(
                new Label("Product ID:"), productIdField,
                new Label("Quantity:"), quantityField,
                addButton
        );

        Scene addScene = new Scene(addLayout, 300, 200);
        addStage.setScene(addScene);
        addStage.show();
    }



    // Add methods for editInventoryDetails and addInventory here

    public static void main(String[] args) {
        launch(args);
    }
}
