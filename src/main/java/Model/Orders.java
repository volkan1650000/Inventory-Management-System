package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static DataBaseUtility.DataBaseConnection.closeConnection;
import static DataBaseUtility.DataBaseConnection.getConnection;

public class Orders {
    private final int orderId;
    private int productId;
    private final Timestamp orderDate;
    private int quantity;
    private double totalCost;
    private String status;

    public Orders(int orderId, int productId, Timestamp orderDate, int quantity, double totalCost, String status) {
        this.orderId = orderId;
        this.productId = productId;
        this.orderDate = orderDate;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getStatus() {
        return status;
    }


    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static void deleteOrders(Orders orders){
        try(Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement("delete from orders where order_id = ?")){
            int orderId = orders.getOrderId();
            statement.setInt(1, orderId);
            int deletedRows = statement.executeUpdate();
            if(deletedRows != 0){
                System.out.println("Order deleted successfully");
            }else{
                System.out.println("No orders found with ID "+orderId);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete");
        }
    }

    public static void deleteOrders(int productId){
        try(Connection conn = getConnection(); Statement statement = conn.createStatement()){
            statement.execute("delete from orders where product_id = "+productId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete orders by id");
        }
    }

    public static void addOrder(Orders orders) throws SQLException {
        Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement("insert into orders(product_id, quantity_ordered, total_cost, status) values(?,?,?,?)");
        statement.setInt(1, orders.getProductId());
        statement.setInt(2, orders.getQuantity());
        statement.setDouble(3, orders.getTotalCost());
        statement.setString(4, orders.getStatus());
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Order added successfully");
        } else {
            System.out.println("Failed to add the order");
        }
        closeConnection();
        statement.close();
    }

    public static void updateOrder(Orders orders) throws SQLException {
        Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement("update orders set product_id" +
                " = ?, order_date = ?, quantity_ordered = ?, total_cost = ?, status = ? where order_id = ?");
        statement.setInt(1,orders.getProductId());
        statement.setTimestamp(2,orders.getOrderDate());
        statement.setInt(3,orders.getQuantity());
        statement.setDouble(4,orders.getTotalCost());
        statement.setString(5,orders.getStatus());
        statement.setInt(6,orders.getOrderId());
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Order updated successfully");
        } else {
            System.out.println("Failed to update the order");
        }
        closeConnection();
        statement.close();
    }

    public static List<Orders> getAllOrdersFromDB() {
        List<Orders> orderList = new ArrayList<>();
        try (Connection conn = getConnection(); ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM orders")) {
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int productId = rs.getInt("product_id");
                Timestamp orderDate = rs.getTimestamp("order_date");
                int quantity = rs.getInt("quantity_ordered");
                double totalCost = rs.getDouble("total_cost");
                String status = rs.getString("status");

                // Create Order objects and add them to the list
                Orders order = new Orders(orderId, productId, orderDate, quantity, totalCost, status);
                orderList.add(order);
                order = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Getting all orders failed");
        }
        return orderList;
    }

    public static List<Orders> showOrdersById(int productId){
        List<Orders> orderList = new ArrayList<>();
        try(Connection conn = getConnection(); ResultSet rs = conn.createStatement().executeQuery("select * from orders where product_id ="+productId)){
            while (rs.next()){
                int orderId = rs.getInt("order_id");
                Timestamp orderDate = rs.getTimestamp("order_date");
                int quantity = rs.getInt("quantity_ordered");
                double totalCost = rs.getDouble("total_cost");
                String status = rs.getString("status");

                // Create Order objects and add them to the list
                Orders order = new Orders(orderId, productId, orderDate, quantity, totalCost, status);
                orderList.add(order);
                order = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to get the order by ID");
        }
        return orderList;
    }
}
