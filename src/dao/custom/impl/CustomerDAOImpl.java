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
    public boolean update(Customer object, Connection connection) {
        return false;
    }

    @Override
    public boolean delete(String object, Connection connection) {
        return false;
    }

    @Override
    public List<Customer> getAll(Connection connection) {
        List<Customer> customerList = new ArrayList<>();
        String query = "SELECT * FROM customers";  // Your SQL query here

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(((ResultSet) rs).getString("id"));
                customer.setCustomerName(rs.getString("name"));
                customer.setCustomerAddress(rs.getString("address"));
                customer.setCustomerEmail(rs.getString("email"));
                customer.setCustomerPhone(rs.getString("phone"));
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
