package hanu.a2_1801040090.mycart.db;

public class DbSchema {
    public final class ProductsTable{
        public static final String NAME = "products";

        public final class Cols{
            public static final String THUMBNAIL = "thumbnail";
            public static final String NAME = "name";
            public static final String UNITPRICE = "unit_price";
            public static final String QUANTITY = "quantity";
        }
    }
}
