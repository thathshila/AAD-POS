package dao.custom.impl;

import dao.custom.CustomerDAO;
import entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public boolean save(Customer customer, Connection connection) {
            String query = "INSERT INTO customer (id, name, address, email, contact) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setString(1, customer.getCustomerId());
                pst.setString(2, customer.getCustomerName());
                pst.setString(3, customer.getCustomerAddress());
                pst.setString(4, customer.getCustomerEmail());
                pst.setString(5, customer.getCustomerPhone());
                return pst.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public boolean update(Customer customer, Connection connection) throws SQLException {
        String sql = "UPDATE customer SET name = ?, address = ?, email = ?, contact = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getCustomerName());
            stmt.setString(2, customer.getCustomerAddress());
            stmt.setString(3, customer.getCustomerEmail());
            stmt.setString(4, customer.getCustomerPhone());
            stmt.setString(5, customer.getCustomerId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if the update was successful (i.e., at least one row was updated)
        }
    }


    @Override
    public boolean delete(String customerId, Connection connection) throws SQLException {
        String sql = "DELETE FROM customer WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if a customer was deleted, false otherwise
        }
    }


    @Override
    public List<Customer> getAll(Connection connection) {
        List<Customer> customerList = new ArrayList<>();
        String query = "SELECT * FROM customer";  // Your SQL query here

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(((ResultSet) rs).getString("id"));
                customer.setCustomerName(rs.getString("name"));
                customer.setCustomerAddress(rs.getString("address"));
                customer.setCustomerEmail(rs.getString("email"));
                customer.setCustomerPhone(rs.getString("contact"));
                customerList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle your error appropriately
        }

        return customerList;
    }

    @Override
    public Customer get(String customerId, Connection connection) {
        return null;
    }
}
