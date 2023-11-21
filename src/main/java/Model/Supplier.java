package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static DataBaseUtility.DataBaseConnection.getConnection;

public class Supplier {
    private final int supplierId;
    private String supplierName;
    private String contactInfo;
    private String address;

    public Supplier(int supplierId, String supplierName, String contactInfo, String address) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contactInfo = contactInfo;
        this.address = address;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static void deleteSupplier(Supplier supplier) {
        try{
            PreparedStatement statement = getConnection().prepareStatement("delete from supplier where supplier_id = ?");
            int supplierId = supplier.getSupplierId();
            statement.setInt(1, supplierId);
            int deletedRows = statement.executeUpdate();
            if(deletedRows != 0){
                System.out.println("Supplier deleted successfully");
            }else{
                System.out.println("No product found with ID "+supplierId);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete");
        }
    }

    public static void addSupplier(Supplier supplier){
        try{
            PreparedStatement statement = getConnection().prepareStatement("insert into supplier(supplier_name, contact_info, address) values(?,?,?)");
            statement.setString(1,supplier.getSupplierName());
            statement.setString(2,supplier.getContactInfo());
            statement.setString(3,supplier.getAddress());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Supplier added successfully");
            } else {
                System.out.println("Failed to add the supplier");
            }
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add the supplier");
        }
    }

    public static void updateSupplier(Supplier supplier){
        try{
            PreparedStatement statement = getConnection().prepareStatement("update supplier set supplier_name" +
                    " = ?, contact_info = ?, address = ? where customer_id = ?");
            statement.setString(1,supplier.getSupplierName());
            statement.setString(2, supplier.getContactInfo());
            statement.setString(3,supplier.getAddress());
            statement.setInt(4,supplier.getSupplierId());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Supplier updated successfully");
            } else {
                System.out.println("Failed to update the supplier");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Supplier> getAllSuppliersFromDB() {
        List<Supplier> supplierList = new ArrayList<>();
        try {
            ResultSet rs = getConnection().createStatement().executeQuery("select * from supplier");
            while (rs.next()) {
                int supplierId = rs.getInt("supplier_id");
                String supplierName = rs.getString("supplier_name");
                String contactInfo = rs.getString("contact_info");
                String address = rs.getString("address");

                // Create Supplier objects and add them to the list
                Supplier supplier = new Supplier(supplierId, supplierName, contactInfo, address);
                supplierList.add(supplier);
                supplier = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Getting all suppliers failed");
        }

        return supplierList;
    }

}
