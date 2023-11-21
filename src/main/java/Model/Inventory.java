package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static DataBaseUtility.DataBaseConnection.closeConnection;
import static DataBaseUtility.DataBaseConnection.getConnection;

public class Inventory {
    private final int inventoryId;
    private int productId;
    private int quantity;
    private final Timestamp lastUpdated;

    public Inventory(int inventoryId, int productId, int quantity, Timestamp lastUpdated) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public static void deleteInventory(Inventory inventory){
        try(Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement("delete from inventory where inventory_id = ?")){
            int inventoryId = inventory.getInventoryId();
            statement.setInt(1, inventoryId);
            int deletedRows = statement.executeUpdate();
            if(deletedRows != 0){
                System.out.println("Inventory deleted successfully");
            }else{
                System.out.println("No inventory found with ID "+inventory);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete");
        }
    }

    public static void deleteInventory(int productId){
        try(Connection conn = getConnection(); Statement statement = conn.createStatement()){
            statement.execute("delete from inventory where product_id = "+productId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete inventory by id");
        }
    }

    public static void addInventory(Inventory inventory) throws SQLException {
        Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement("insert into inventory(product_id, quantity_on_hand) values(?,?)");
        statement.setInt(1, inventory.getProductId());
        statement.setInt(2, inventory.getQuantity());
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Inventory added successfully");
        } else {
            System.out.println("Failed to add the inventory");
        }
        closeConnection();
        statement.close();
    }

    public static void updateInventory(Inventory inventory) throws SQLException {
        Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement("update inventory set product_id" +
                " = ?, quantity_on_hand = ?, last_updated = CURRENT_TIMESTAMP where inventory_id = ?");
        statement.setInt(1,inventory.getProductId());
        statement.setInt(2,inventory.getQuantity());
        statement.setInt(3,inventory.getInventoryId());
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Inventory updated successfully");
        } else {
            System.out.println("Failed to update the inventory");
        }
        closeConnection();
        statement.close();
    }

    public static List<Inventory> getAllInventoryFromDB() {
        List<Inventory> inventoryList = new ArrayList<>();
        try (Connection conn = getConnection(); ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM inventory")){
            while (rs.next()) {
                int inventoryId = rs.getInt("inventory_id");
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity_on_hand");
                Timestamp lastUpdated = rs.getTimestamp("last_updated");

                // Create Inventory objects and add them to the list
                Inventory inventory = new Inventory(inventoryId, productId, quantity, lastUpdated);
                inventoryList.add(inventory);
                inventory = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to get all inventory");
        }
        return inventoryList;
    }

    public static List<Inventory> showInventoryById(int productId){
        List<Inventory> inventoryList = new ArrayList<>();
        try(Connection conn = getConnection(); ResultSet rs = conn.createStatement().executeQuery("select * from inventory where product_id ="+productId)){
            while (rs.next()){
                int inventoryId = rs.getInt(1);
                int quantity = rs.getInt("quantity_on_hand");
                Timestamp lastUpdated = rs.getTimestamp("last_updated");
                Inventory obj = new Inventory(inventoryId,productId,quantity,lastUpdated);
                inventoryList.add(obj);
                obj = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to get the inventory by ID");
        }
        return inventoryList;
    }

}
