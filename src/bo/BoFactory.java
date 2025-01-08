package bo;

import bo.custom.impl.CustomerBOImpl;

import static bo.BoFactory.BoType.CUSTOMER;

public class BoFactory {

    private static BoFactory boFactory;

    private BoFactory() {
    }

    public static BoFactory getBoFactory() {
        return boFactory == null ? boFactory = new BoFactory() : boFactory;
    }


    public enum BoType {
        CUSTOMER
    }

    public SuperBo getBoType(BoType boType) {
        switch (boType) {
            case CUSTOMER:
                return new CustomerBOImpl();
            default:
                return null;
        }
    }
}
