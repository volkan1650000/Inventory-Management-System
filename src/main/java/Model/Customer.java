package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static DataBaseUtility.DataBaseConnection.getConnection;

public class Customer {
    private final int customerId;
    private String customerName;
    private String email;
    private String address;

    public Customer(int customerId, String customerName, String email, String address) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.address = address;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static void deleteCustomer(Customer customer){
        try(Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement("delete from customer where customer_id = ?")){
            int customerId = customer.getCustomerId();
            statement.setInt(1, customerId);
            int deletedRows = statement.executeUpdate();
            if(deletedRows != 0){
                System.out.println("Customer deleted successfully");
            }else{
                System.out.println("No customer found with ID "+customerId);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete");
        }
    }

    public static void addCustomer(Customer customer){
        try(Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement("insert into customer(customer_name, email, address) values(?,?,?)")){
            statement.setString(1,customer.getCustomerName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getAddress());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Customer added successfully");
            } else {
                System.out.println("Failed to add the customer");
            }

        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the customer");
        }
    }

    public static void updateCustomer(Customer customer) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("update customer set customer_name = ?, email = ?, address = ? where customer_id = ?")) {

            // Set values to the prepared statement
            statement.setString(1, customer.getCustomerName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getAddress());
            statement.setInt(4, customer.getCustomerId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Customer updated successfully");
            } else {
                System.out.println("Failed to update the customer");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<Customer> getAllCustomersFromDB() {
        List<Customer> customerList = new ArrayList<>();
        try (Connection conn = getConnection(); ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM customer")){
            while (rs.next()) {
                int customerId = rs.getInt("customer_id");
                String customerName = rs.getString("customer_name");
                String email = rs.getString("email");
                String address = rs.getString("address");

                // Create Customer objects and add them to the list
                Customer customer = new Customer(customerId, customerName, email, address);
                customerList.add(customer);
                customer = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to get all customers");
        }
        return customerList;
    }
}
