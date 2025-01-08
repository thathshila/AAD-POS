package dao;

import entity.Customer;

import java.sql.Connection;
import java.util.List;

public interface CrudDao<T> extends SuperDao {
    public boolean save(T object, Connection connection);
    public boolean update(T object, Connection connection);
    public boolean delete(String object, Connection connection);
    public T get(String customerId, Connection connection);
    List<T> getAll(Connection connection);
}
