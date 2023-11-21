package UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

public class Dashboard extends Application {

    public void start(Stage primaryStage) {
        primaryStage.setResizable(false); // Set stage to not resizable
        primaryStage.setTitle("Inventory Management System");

        VBox root = new VBox(); // Use VBox for vertical alignment
        root.setAlignment(Pos.CENTER); // Center align the VBox

        // Create an HBox to hold the buttons and set its alignment
        HBox buttonBox = new HBox(40); // Add some spacing between buttons
        buttonBox.setAlignment(Pos.CENTER); // Center align the buttons

        // Create buttons for navigation to different sections
        Button productsButton = new Button("Products");
        productsButton.setPrefSize(120, 100); // Adjust size here

        Button suppliersButton = new Button("Suppliers");
        suppliersButton.setPrefSize(120, 100);

        Button ordersButton = new Button("Orders");
        ordersButton.setPrefSize(120, 100);

        Button inventoryButton = new Button("Inventory");
        inventoryButton.setPrefSize(120, 100);

        Button customerButton = new Button("Customer");
        customerButton.setPrefSize(120, 100);

        productsButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-size: 18;");
        suppliersButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-size: 18;");
        ordersButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-size: 18;");
        inventoryButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-size: 18;");
        customerButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-size: 18;");

        productsButton.setOnMousePressed(event -> productsButton.setStyle("-fx-background-color: #7C7C7C; -fx-text-fill: white; -fx-font-size: 18;")); // Lighter shade of gray
        productsButton.setOnMouseReleased(event -> productsButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-size: 18;"));
        // Original color
        suppliersButton.setOnMousePressed(event -> suppliersButton.setStyle("-fx-background-color: #7C7C7C; -fx-text-fill: white; -fx-font-size: 18;"));
        suppliersButton.setOnMouseReleased(event -> suppliersButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-size: 18;"));

        ordersButton.setOnMousePressed(event -> ordersButton.setStyle("-fx-background-color: #7C7C7C; -fx-text-fill: white; -fx-font-size: 18;"));
        ordersButton.setOnMouseReleased(event -> ordersButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-size: 18;"));

        inventoryButton.setOnMousePressed(event -> inventoryButton.setStyle("-fx-background-color: #7C7C7C; -fx-text-fill: white; -fx-font-size: 18;"));
        inventoryButton.setOnMouseReleased(event -> inventoryButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-size: 18;"));

        customerButton.setOnMousePressed(event -> customerButton.setStyle("-fx-background-color: #7C7C7C; -fx-text-fill: white; -fx-font-size: 18;"));
        customerButton.setOnMouseReleased(event -> customerButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-font-size: 18;"));




        // Set action handlers for navigation
        productsButton.setOnAction(e -> showProductsSection());
        suppliersButton.setOnAction(e -> showSuppliersSection());
        ordersButton.setOnAction(e -> showOrdersSection());
        inventoryButton.setOnAction(e -> showInventorySection());
        customerButton.setOnAction(e -> showCustomerSection());

        // Add buttons to the HBox
        buttonBox.getChildren().addAll(productsButton, suppliersButton, ordersButton, inventoryButton, customerButton);

        // Add the HBox to the VBox
        root.getChildren().add(buttonBox);

        // Add some padding to create space between buttons and top
        VBox.setMargin(buttonBox, new Insets(-400, 0, 0, 0));

        Scene scene = new Scene(root,1000, 750);
        BackgroundFill backgroundFill = new BackgroundFill(Color.LIGHTGRAY, null, null);
        Background background = new Background(backgroundFill);
        root.setBackground(background);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void showProductsSection() {
        Stage productsStage = new Stage();
        productsStage.setTitle("Products Section");

        // Instantiate the UI class for Products (Replace 'ProductsUI' with your actual Products UI class name)
        ProductUI productsUI = new ProductUI();

        // Start the ProductsUI on the new stage
        productsUI.start(productsStage);
    }


    private void showSuppliersSection() {
        Stage suppliersStage = new Stage();
        suppliersStage.setTitle("Suppliers Section");

        // Instantiate the UI class for Suppliers (Replace 'SuppliersUI' with your actual Suppliers UI class name)
        SupplierUI suppliersUI = new SupplierUI();

        // Start the SuppliersUI on the new stage
        suppliersUI.start(suppliersStage);
    }

    private void showOrdersSection() {
        Stage ordersStage = new Stage();
        ordersStage.setTitle("Orders Section");

        // Instantiate the UI class for Orders (Replace 'OrdersUI' with your actual Orders UI class name)
        OrdersUI ordersUI = new OrdersUI();

        // Start the OrdersUI on the new stage
        ordersUI.start(ordersStage);
    }

    private void showCustomerSection() {
        Stage customerStage = new Stage();
        customerStage.setTitle("Customer Section");

        // Instantiate the UI class for Customers (Replace 'CustomerUI' with your actual Customer UI class name)
        CustomerUI customerUI = new CustomerUI();

        // Start the CustomerUI on the new stage
        customerUI.start(customerStage);
    }

    private void showInventorySection() {
        Stage inventoryStage = new Stage();
        inventoryStage.setTitle("Inventory Section");

        // Instantiate the UI class for Inventory (Replace 'InventoryUI' with your actual Inventory UI class name)
        InventoryUI inventoryUI = new InventoryUI();

        // Start the InventoryUI on the new stage
        inventoryUI.start(inventoryStage);
    }



    public static void main(String[] args) {
        launch(args);
    }
}

