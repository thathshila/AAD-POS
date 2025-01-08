package dao.custom.impl;

import dao.custom.ItemDAO;
import entity.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean save(Item object, Connection connection) {
        return false;
    }

    @Override
    public boolean update(Item object, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(String object, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public Item get(String customerId, Connection connection) {
        return null;
    }

    @Override
    public List<Item> getAll(Connection connection) {
        List<Item> items = new ArrayList<>();
        String query = "SELECT *FROM item";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Create an Item object and add it to the list
                items.add(new Item(
                        resultSet.getString("item_code"),
                        resultSet.getString("item_name"),
                        String.valueOf(resultSet.getInt("quantity")),
                        String.valueOf(resultSet.getBigDecimal("unit_price"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }

        return items;
    }

}
