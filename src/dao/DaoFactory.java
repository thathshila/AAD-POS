package dao;

import dao.custom.impl.CustomerDAOImpl;

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
        CUSTOMER
    }

    // Updated method to use generics for type safety
    public <T extends SuperDao> T getDAO(DaoType daoType) {
        switch (daoType) {
            case CUSTOMER:
                return (T) new CustomerDAOImpl();
            default:
                throw new IllegalArgumentException("Invalid DAO type");
        }
    }
}
