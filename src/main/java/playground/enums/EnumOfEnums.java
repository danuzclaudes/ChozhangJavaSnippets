package playground.enums;

public class EnumOfEnums {
    /**
     * 1. group enum classes inside an interface.
     * 2. each nested enum implements the surrounding interface.
     */
    interface Security {
        enum Stock implements Security { SHORT, LONG, MARGIN }
        enum Bond  implements Security { MUNICIPAL, JUNK }
    }

    /**
     * 3. each enum takes class type to store all enum instances.
     */
    public enum SecurityPartition {
        STOCK(Security.Stock.class),
        BOND(Security.Bond.class);
        private Security[] securities;
        SecurityPartition(Class<? extends Security> type) {
            securities = type.getEnumConstants();
        }
        public Security randomSelection() {
            return EnumsUtil.random(securities);
        }
    }

    /*
     * BOND: MUNICIPAL
     * BOND: MUNICIPAL
     * STOCK: MARGIN
     * STOCK: MARGIN
     * BOND: JUNK
     */
    public static void main(String[] args) {
        for(int i = 0; i < 5; i++) {
            SecurityPartition securityPartition = EnumsUtil.random(SecurityPartition.class);
            System.out.println(securityPartition + ": " + securityPartition.randomSelection());
        }
    }
}
