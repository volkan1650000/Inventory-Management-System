package UI;
import Model.Supplier;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class SupplierUI extends Application {
    private final TableView<Supplier> supplierTable = new TableView<>();

    @Override
    public void start(Stage primaryStage) {

        // Set up columns for the TableView
        TableColumn<Supplier, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSupplierId()).asObject());

        TableColumn<Supplier, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSupplierName()));

        TableColumn<Supplier, String> contactInfoColumn = new TableColumn<>("Contact Info");
        contactInfoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContactInfo()));

        TableColumn<Supplier, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));

        // Add columns to the TableView
        supplierTable.getColumns().addAll(idColumn, nameColumn, contactInfoColumn, addressColumn);

        // Fetch suppliers and populate the table
        List<Supplier> suppliers = Supplier.getAllSuppliersFromDB();
        supplierTable.getItems().addAll(suppliers);

        // Define buttons for operations like delete, edit, and add
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
            if (selectedSupplier != null) {
                Supplier.deleteSupplier(selectedSupplier);
                supplierTable.getItems().remove(selectedSupplier);
            } else {
                System.out.println("No supplier selected");
            }
        });

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
            if (selectedSupplier != null) {
                // Open a dialog to edit supplier details
                editSupplierDetails(selectedSupplier);
            } else {
                System.out.println("No supplier selected");
            }
        });

        Button addButton = new Button("Add Supplier");
        addButton.setOnAction(e -> addSupplier());

        HBox buttonBar = new HBox();
        buttonBar.getChildren().addAll(deleteButton, editButton, addButton);

        // Initially, disable buttons as no row is selected
        deleteButton.setDisable(true);
        editButton.setDisable(true);

        // Handle row selection to enable/disable buttons based on selection
        supplierTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                deleteButton.setDisable(false);
                editButton.setDisable(false);
            } else {
                deleteButton.setDisable(true);
                editButton.setDisable(true);
            }
        });

        VBox root = new VBox(supplierTable, buttonBar);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Supplier Table");
        primaryStage.show();
    }

    private void editSupplierDetails(Supplier supplier) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Supplier");

        // Create fields to edit supplier details
        TextField nameField = new TextField(supplier.getSupplierName());
        TextField contactField = new TextField(supplier.getContactInfo());
        TextField addressField = new TextField(supplier.getAddress());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Update supplier details based on input fields
            supplier.setSupplierName(nameField.getText());
            supplier.setContactInfo(contactField.getText());
            supplier.setAddress(addressField.getText());

            Supplier.updateSupplier(supplier); // Update details in the database

            // Close the edit window
            editStage.close();

            // Refresh table to display updated data
            supplierTable.getItems().clear();
            supplierTable.getItems().addAll(Supplier.getAllSuppliersFromDB());
        });

        VBox editLayout = new VBox(
                new Label("Name:"), nameField,
                new Label("Contact Info:"), contactField,
                new Label("Address:"), addressField,
                saveButton
        );

        Scene editScene = new Scene(editLayout, 600, 400);
        editStage.setScene(editScene);
        editStage.show();
    }

    private void addSupplier() {
        Stage addStage = new Stage();
        addStage.setTitle("Add Supplier");

        // Create fields to add a new supplier
        TextField nameField = new TextField();
        TextField contactField = new TextField();
        TextField addressField = new TextField();

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            // Create a new supplier with the entered details
            Supplier newSupplier = new Supplier(0, nameField.getText(), contactField.getText(), addressField.getText());

            // Add the new supplier to the database
            Supplier.addSupplier(newSupplier);

            // Close the add window
            addStage.close();

            // Refresh table to display the new data
            supplierTable.getItems().clear();
            supplierTable.getItems().addAll(Supplier.getAllSuppliersFromDB());
        });

        VBox addLayout = new VBox(
                new Label("Name:"), nameField,
                new Label("Contact Info:"), contactField,
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


