package UI;

import Model.Customer;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class CustomerUI extends Application {

    private final TableView<Customer> customerTable = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        // Set up columns for the TableView
        TableColumn<Customer, Integer> idColumn = new TableColumn<>("Customer ID");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCustomerId()).asObject());

        TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));

        TableColumn<Customer, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        TableColumn<Customer, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));

        // Add columns to the TableView
        customerTable.getColumns().addAll(idColumn, nameColumn, emailColumn, addressColumn);

        // Fetch customers and populate the table
        List<Customer> customers = Customer.getAllCustomersFromDB();
        customerTable.getItems().addAll(customers);

        // Define buttons for operations like delete, edit, and add
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                Customer.deleteCustomer(selectedCustomer);
                customerTable.getItems().remove(selectedCustomer);
            } else {
                System.out.println("No customer selected");
            }
        });

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                // Open a dialog to edit customer details
                editCustomerDetails(selectedCustomer);
            } else {
                System.out.println("No customer selected");
            }
        });

        Button addButton = new Button("Add Customer");
        addButton.setOnAction(e -> addCustomer());

        // Handle row selection to enable/disable buttons based on selection

        HBox buttonBar = new HBox(deleteButton, editButton, addButton);

        deleteButton.setDisable(true);
        editButton.setDisable(true);

        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                deleteButton.setDisable(false);
                editButton.setDisable(false);
            } else {
                deleteButton.setDisable(true);
                editButton.setDisable(true);
            }
        });

        VBox root = new VBox(customerTable, buttonBar);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Customer Table");
        primaryStage.show();
    }

    private void editCustomerDetails(Customer customer) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Customer");

        // Create fields to edit customer details
        TextField nameField = new TextField(customer.getCustomerName());
        TextField emailField = new TextField(customer.getEmail());
        TextField addressField = new TextField(customer.getAddress());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Update customer details based on input fields
            customer.setCustomerName(nameField.getText());
            customer.setEmail(emailField.getText());
            customer.setAddress(addressField.getText());

            Customer.updateCustomer(customer); // Update details in the database

            // Close the edit window
            editStage.close();

            // Refresh table to display updated data
            customerTable.getItems().clear();
            customerTable.getItems().addAll(Customer.getAllCustomersFromDB());
        });

        VBox editLayout = new VBox(
                new Label("Name:"), nameField,
                new Label("Email:"), emailField,
                new Label("Address:"), addressField,
                saveButton
        );

        Scene editScene = new Scene(editLayout, 300, 200);
        editStage.setScene(editScene);
        editStage.show();
    }

    private void addCustomer() {
        Stage addStage = new Stage();
        addStage.setTitle("Add Customer");

        // Create fields to add a new customer
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField addressField = new TextField();

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            // Create a new customer with the entered details
            Customer newCustomer = new Customer(
                    0,
                    nameField.getText(),
                    emailField.getText(),
                    addressField.getText()
            );

            // Add the new customer to the database
            Customer.addCustomer(newCustomer);

            // Close the add window
            addStage.close();

            // Refresh table to display the new data
            customerTable.getItems().clear();
            customerTable.getItems().addAll(Customer.getAllCustomersFromDB());
        });

        VBox addLayout = new VBox(
                new Label("Name:"), nameField,
                new Label("Email:"), emailField,
                new Label("Address:"), addressField,
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
