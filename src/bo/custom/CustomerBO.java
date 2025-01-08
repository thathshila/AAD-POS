package bo.custom;

import bo.SuperBo;
import dto.CustomerDTO;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBO extends SuperBo {
    boolean saveCustomer(CustomerDTO customerDTO, BasicDataSource dataSource) throws SQLException;
    boolean updateCustomer(CustomerDTO customerDTO, BasicDataSource dataSource) throws SQLException;
    boolean deleteCustomer(String customerId, BasicDataSource dataSource) throws SQLException;
    CustomerDTO getCustomer(String customerId, BasicDataSource dataSource) throws SQLException;
    List<CustomerDTO> getCustomers(BasicDataSource dataSource) throws SQLException;
}
