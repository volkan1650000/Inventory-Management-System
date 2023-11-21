package UI;

import Model.Orders;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
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

public class OrdersUI extends Application {

    private final TableView<Orders> ordersTable = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        // Set up columns for the TableView
        TableColumn<Orders, Integer> idColumn = new TableColumn<>("Order ID");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOrderId()).asObject());

        TableColumn<Orders, Integer> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductId()).asObject());

        TableColumn<Orders, Timestamp> orderDateColumn = new TableColumn<>("Order Date");
        orderDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOrderDate()));

        TableColumn<Orders, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        TableColumn<Orders, Double> totalCostColumn = new TableColumn<>("Total Cost");
        totalCostColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalCost()).asObject());

        TableColumn<Orders, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus()));

        // Add columns to the TableView
        ordersTable.getColumns().addAll(idColumn, productIdColumn, orderDateColumn, quantityColumn, totalCostColumn, statusColumn);

        // Fetch orders and populate the table
        List<Orders> orders = Orders.getAllOrdersFromDB();
        ordersTable.getItems().addAll(orders);

        // Define buttons for operations like delete, edit, and add
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            Orders selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                Orders.deleteOrders(selectedOrder);
                ordersTable.getItems().remove(selectedOrder);
            } else {
                System.out.println("No order selected");
            }
        });

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            Orders selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                // Open a dialog to edit order details
                editOrderDetails(selectedOrder);
            } else {
                System.out.println("No order selected");
            }
        });

        Button addButton = new Button("Add Order");
        addButton.setOnAction(e -> addOrder());

        // Handle row selection to enable/disable buttons based on selection

        HBox buttonBar = new HBox(deleteButton, editButton, addButton);

        deleteButton.setDisable(true);
        editButton.setDisable(true);

        ordersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                deleteButton.setDisable(false);
                editButton.setDisable(false);
            } else {
                deleteButton.setDisable(true);
                editButton.setDisable(true);
            }
        });

        VBox root = new VBox(ordersTable, buttonBar);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Orders Table");
        primaryStage.show();
    }

    private void editOrderDetails(Orders order) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Order");

        // Create fields to edit order details
        TextField productIdField = new TextField(String.valueOf(order.getProductId()));
        TextField quantityField = new TextField(String.valueOf(order.getQuantity()));
        TextField totalCostField = new TextField(String.valueOf(order.getTotalCost()));
        TextField statusField = new TextField(order.getStatus());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try{
                // Update order details based on input fields
                order.setProductId(Integer.parseInt(productIdField.getText()));
                order.setQuantity(Integer.parseInt(quantityField.getText()));
                order.setTotalCost(Double.parseDouble(totalCostField.getText()));
                order.setStatus(statusField.getText());

                Orders.updateOrder(order); // Update details in the database

                // Close the edit window
                editStage.close();

                // Refresh table to display updated data
                ordersTable.getItems().clear();
                ordersTable.getItems().addAll(Orders.getAllOrdersFromDB());
            } catch (NumberFormatException ex) {
                // Inform the user about incorrect input
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Please enter valid numbers for product id, quantity and total cost fields.");
                alert.showAndWait();
            } catch (SQLException ex) {
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
                new Label("Total Cost:"), totalCostField,
                new Label("Status:"), statusField,
                saveButton
        );

        Scene editScene = new Scene(editLayout, 300, 200);
        editStage.setScene(editScene);
        editStage.show();
    }


    private void addOrder() {
        Stage addStage = new Stage();
        addStage.setTitle("Add Order");

        // Create fields to add a new order
        TextField productIdField = new TextField();
        TextField quantityField = new TextField();
        TextField totalCostField = new TextField();
        TextField statusField = new TextField();

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String productIdText = productIdField.getText();
            String quantityText = quantityField.getText();
            String totalCostText = totalCostField.getText();
            String status = statusField.getText();
            try{
                int id = Integer.parseInt(productIdText);
                int quantity = Integer.parseInt(quantityText);
                double totalCost =  Double.parseDouble(totalCostText);
                // Create a new order with the entered details
                Orders newOrder = new Orders(0,id,null,quantity,totalCost,status);

                // Add the new order to the database
                Orders.addOrder(newOrder);

                // Close the add window
                addStage.close();

                // Refresh table to display the new data
                ordersTable.getItems().clear();
                ordersTable.getItems().addAll(Orders.getAllOrdersFromDB());
            }catch(NumberFormatException ex){
                // Inform the user about incorrect input
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Please enter valid numbers for product id, quantity and total cost fields.");
                alert.showAndWait();
            } catch (SQLException ex) {
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
                new Label("Total Cost:"), totalCostField,
                new Label("Status:"), statusField,
                addButton
        );

        Scene addScene = new Scene(addLayout, 300, 200);
        addStage.setScene(addScene);
        addStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}

