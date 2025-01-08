package dao;

import dao.custom.impl.CustomerDAOImpl;
import dao.custom.impl.ItemDAOImpl;

public class DaoFactory {
    private static DaoFactory daoFactory;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            daoFactory = new DaoFactory();
        }
        return daoFactory;
    }

    public enum DaoType {
        CUSTOMER,ITEM
    }

    // Updated method to use generics for type safety
    public SuperDao getDAO(DaoType daoType) {
        switch (daoType) {
            case CUSTOMER:
                return  new CustomerDAOImpl();
                case ITEM:
                    return new ItemDAOImpl();
            default:
              return null;
        }
    }
}
