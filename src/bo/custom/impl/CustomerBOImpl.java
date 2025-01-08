package bo.custom.impl;

import bo.custom.CustomerBO;
import dao.custom.CustomerDAO;
import dto.CustomerDTO;
import entity.Customer;
import org.apache.commons.dbcp2.BasicDataSource;
import dao.DaoFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerBOImpl implements CustomerBO {

    private final CustomerDAO customerDAO = (CustomerDAO) DaoFactory.getInstance().getDAO(DaoFactory.DaoType.CUSTOMER);

    @Override
    public boolean saveCustomer(CustomerDTO customerDTO, BasicDataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            return customerDAO.save(
                    new Customer(
                            customerDTO.getCustomerId(),
                            customerDTO.getCustomerName(),
                            customerDTO.getCustomerAddress(),
                            customerDTO.getCustomerEmail(),
                            customerDTO.getCustomerPhone()
                    ),
                    connection
            );
        }
    }
    @Override
    public boolean updateCustomer(CustomerDTO customerDTO, BasicDataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            return customerDAO.update(
                    new Customer(
                            customerDTO.getCustomerId(),
                            customerDTO.getCustomerName(),
                            customerDTO.getCustomerAddress(),
                            customerDTO.getCustomerEmail(),
                            customerDTO.getCustomerPhone()
                    ),
                    connection
            );
        }
    }

    @Override
    public boolean deleteCustomer(String customerId, BasicDataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            return customerDAO.delete(customerId, connection);
        }
    }

    @Override
    public CustomerDTO getCustomer(String customerId, BasicDataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Customer customer = customerDAO.get(customerId, connection);
            if (customer != null) {
                return new CustomerDTO(
                        customer.getCustomerId(),
                        customer.getCustomerName(),
                        customer.getCustomerAddress(),
                        customer.getCustomerEmail(),
                        customer.getCustomerPhone()
                );
            }
            return null;
        }
    }

    @Override
    public List<CustomerDTO> getCustomers(BasicDataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            List<Customer> customers = customerDAO.getAll(connection); // This should fetch data from your database
            return customers.stream()
                    .map(customer -> new CustomerDTO(
                            customer.getCustomerId(),   // customerId should match the backend entity
                            customer.getCustomerName(), // customerName
                            customer.getCustomerAddress(), // customerAddress
                            customer.getCustomerPhone(), // customerPhone
                            customer.getCustomerEmail()  // customerEmail
                    ))
                    .collect(Collectors.toList());
        }
    }

}
