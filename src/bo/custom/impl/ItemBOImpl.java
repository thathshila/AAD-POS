package bo.custom.impl;

import bo.custom.ItemBO;
import dao.DaoFactory;
import dao.custom.ItemDAO;
import dto.ItemDTO;
import entity.Item;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBOImpl implements ItemBO {
   ItemDAO itemDAO = (ItemDAO) DaoFactory.getInstance().getDAO(DaoFactory.DaoType.ITEM);
    @Override
    public List<ItemDTO> getAllItems(BasicDataSource basicDataSource) {
        List<ItemDTO> itemDTOs = new ArrayList<>();

        try (Connection connection = basicDataSource.getConnection()) {
            // Use the DAO to fetch all items
            List<Item> items = itemDAO.getAll(connection);

            // Convert each Item entity to an ItemDTO
            for (Item item : items) {
                itemDTOs.add(new ItemDTO(
                        item.getItemId(),
                        item.getItemName(),
                        item.getItemQuantity(),
                        item.getItemPrice()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }

        return itemDTOs;
    }

}
