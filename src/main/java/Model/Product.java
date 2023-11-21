package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static DataBaseUtility.DataBaseConnection.closeConnection;
import static DataBaseUtility.DataBaseConnection.getConnection;

public class Product {

    private final int productId;
    private String productName;
    private String description;
    private double price;
    private int quantity;

    public Product(int productId, String productName, String description, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static void deleteProduct(Product product) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("delete from product where product_id = ?");
        int productId = product.getProductId();
        statement.setInt(1, productId);
        int deletedRows = statement.executeUpdate();
        if(deletedRows != 0){
            System.out.println("Product deleted successfully");
        }else{
            System.out.println("No product found with ID "+productId);
        }
        closeConnection();
        statement.close();
    }

    public static void addProduct(Product product) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("insert into product(product_name, description, price, quantity_available) values(?,?,?,?)");
        statement.setString(1, product.getProductName());
        statement.setString(2, product.getDescription());
        statement.setDouble(3,product.getPrice());
        statement.setInt(4, product.getQuantity());
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Product added successfully");
        } else {
            System.out.println("Failed to add the product");
        }
        closeConnection();
        statement.close();
    }



    public static void updateProduct(Product product) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("update product set product_name" +
                " = ?, description = ?, price = ?, quantity_available = ? where product_id = ?");
        statement.setString(1,product.getProductName());
        statement.setString(2,product.getDescription());
        statement.setDouble(3,product.getPrice());
        statement.setDouble(4,product.getQuantity());
        statement.setInt(5,product.getProductId());
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Product updated successfully");
        } else {
            System.out.println("Failed to update the product");
        }
        closeConnection();
        statement.close();
    }

    public static List<Product> getAllProductsFromDB() {
        List<Product> productList = new ArrayList<>();
        try (Connection conn = getConnection(); ResultSet rs = conn.createStatement().executeQuery("select * from product")){
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity_available");

                // Create Product objects and add them to the list
                Product product = new Product(productId, productName, description, price, quantity);
                productList.add(product);
                product = null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Getting all products failed");
        }

        return productList;
    }

}
