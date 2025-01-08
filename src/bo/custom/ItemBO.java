package bo.custom;

import bo.SuperBo;
import dto.ItemDTO;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;

public interface ItemBO extends SuperBo {
    List<ItemDTO> getAllItems(BasicDataSource basicDataSource);
}
