package bo;

import bo.custom.impl.CustomerBOImpl;
import bo.custom.impl.ItemBOImpl;

import static bo.BoFactory.BoType.CUSTOMER;

public class BoFactory {

    private static BoFactory boFactory;

    private BoFactory() {
    }

    public static BoFactory getBoFactory() {
        return boFactory == null ? boFactory = new BoFactory() : boFactory;
    }


    public enum BoType {
        CUSTOMER,ITEM
    }

    public SuperBo getBoType(BoType boType) {
        switch (boType) {
            case CUSTOMER:
                return new CustomerBOImpl();
                case ITEM:
                    return new ItemBOImpl();
            default:
                return null;
        }
    }
}
