package UI;

import Model.Inventory;
import Model.Orders;
import Model.Product;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductUI extends Application {

    private final TableView<Product> productTable = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        // Set up columns for the TableView
        TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductId()).asObject());

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProductName()));

        TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        // Add columns to the TableView
        productTable.getColumns().addAll(idColumn, nameColumn, descriptionColumn, priceColumn, quantityColumn);

        // Fetch products and populate the table
        List<Product> products = Product.getAllProductsFromDB();
        productTable.getItems().addAll(products);

        // Define buttons for operations like delete, edit, and add
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                List<Inventory> relatedInventory = Inventory.showInventoryById(selectedProduct.getProductId());
                List<Orders> relatedOrders = Orders.showOrdersById(selectedProduct.getProductId());

                if (!relatedInventory.isEmpty() || !relatedOrders.isEmpty()) {
                    // Construct message detailing related entries
                    StringBuilder message = new StringBuilder("Deleting this product will also remove associated entries in:\n");

                    if (!relatedInventory.isEmpty()) {
                        message.append("Inventory:\n");
                        for (Inventory inventory : relatedInventory) {
                            message.append("Inventory ID: ").append(inventory.getInventoryId())
                                    .append(", Quantity: ").append(inventory.getQuantity())
                                    .append("\n");
                        }
                    }

                    if (!relatedOrders.isEmpty()) {
                        message.append("Orders:\n");
                        for (Orders order : relatedOrders) {
                            message.append("Order ID: ").append(order.getOrderId())
                                    .append(", Quantity: ").append(order.getQuantity())
                                    .append("\n");
                        }
                    }

                    // Show confirmation dialog to the user
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Confirm Deletion");
                    alert.setHeaderText("This product is referenced in other tables:");
                    alert.setContentText(message.toString());

                    // Add buttons to confirm or cancel the deletion
                    ButtonType confirmButtonType = new ButtonType("Delete");
                    ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(confirmButtonType, cancelButtonType);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == confirmButtonType) {
                        // User confirmed deletion, proceed with deleteProduct method
                        try {
                            Inventory.deleteInventory(selectedProduct.getProductId());
                            Orders.deleteOrders(selectedProduct.getProductId());
                            Product.deleteProduct(selectedProduct);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        productTable.getItems().remove(selectedProduct);
                    }
                } else {
                    // No related entries found, proceed with deletion
                    try {
                        Product.deleteProduct(selectedProduct);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    productTable.getItems().remove(selectedProduct);
                }
            } else {
                System.out.println("No product selected");
            }
        });


        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                // Open a dialog to edit product details
                editProductDetails(selectedProduct);
            } else {
                System.out.println("No product selected");
            }
        });


        Button addButton = new Button("Add Product");
        addButton.setOnAction(e -> addProduct());
        // Add button actions

        // Handle row selection to enable/disable buttons based on selection
        HBox buttonBar = new HBox(deleteButton, editButton, addButton);

        deleteButton.setDisable(true);
        editButton.setDisable(true);

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                deleteButton.setDisable(false);
                editButton.setDisable(false);
            } else {
                deleteButton.setDisable(true);
                editButton.setDisable(true);
            }
        });

        VBox root = new VBox(productTable, buttonBar);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Product Table");
        primaryStage.show();
    }

    private void editProductDetails(Product product) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Product");

        // Create fields to edit product details
        TextField nameField = new TextField(product.getProductName());
        TextField descriptionField = new TextField(product.getDescription());
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        TextField quantityField = new TextField(String.valueOf(product.getQuantity()));

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Update product details based on input fields

            try {
                product.setProductName(nameField.getText());
                product.setDescription(descriptionField.getText());
                product.setPrice(Double.parseDouble(priceField.getText()));
                product.setQuantity(Integer.parseInt(quantityField.getText()));

                // Update details in the database
               Product.updateProduct(product);

                // Refresh table to display updated data
                productTable.getItems().clear();
                productTable.getItems().addAll(Product.getAllProductsFromDB());
               editStage.close();
            } catch (NumberFormatException ex) {
                // Inform the user about incorrect input
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Please enter valid numbers for price and quantity fields.");
                alert.showAndWait();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        VBox editLayout = new VBox(
                new Label("Name:"), nameField,
                new Label("Description:"), descriptionField,
                new Label("Price:"), priceField,
                new Label("Quantity:"), quantityField,
                saveButton
        );

        Scene editScene = new Scene(editLayout, 300, 200);
        editStage.setScene(editScene);
        editStage.show();
    }

    private void addProduct() {
        Stage addStage = new Stage();
        addStage.setTitle("Add Product");

        // Create fields to add a new product
        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        TextField priceField = new TextField();
        TextField quantityField = new TextField();

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String description = descriptionField.getText();
            String priceText = priceField.getText();
            String quantityText = quantityField.getText();

            // Validate and parse the text to numbers
            try {
                double price = Double.parseDouble(priceText);
                int quantity = Integer.parseInt(quantityText);

                // Create a new product with the entered details
                Product newProduct = new Product(0, name, description, price, quantity);

                // Add the new product to the database
                Product.addProduct(newProduct);

                // Close the add window
                addStage.close();

                // Refresh table to display the new data
                productTable.getItems().clear();
                productTable.getItems().addAll(Product.getAllProductsFromDB());
            } catch (NumberFormatException ex) {
                // Inform the user about incorrect input
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setHeaderText("Invalid input");
                alert.setContentText("Please enter valid numbers for price and quantity fields.");
                alert.showAndWait();
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Handle the SQL exception
            }
        });

        VBox addLayout = new VBox(
                new Label("Name:"), nameField,
                new Label("Description:"), descriptionField,
                new Label("Price:"), priceField,
                new Label("Quantity:"), quantityField,
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
