package hanu.a2_1801040090.mycart.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_1801040090.mycart.models.Product;

public class ProductManager {
    private static ProductManager instance;

    private static final String INSERT = "INSERT INTO " + DbSchema.ProductsTable.NAME + "(" +
            "id" + ", " +
            DbSchema.ProductsTable.Cols.THUMBNAIL + ", " +
            DbSchema.ProductsTable.Cols.NAME + ", " +
            DbSchema.ProductsTable.Cols.UNITPRICE + ", " +   //Maybe should add quantity in here <<==
            DbSchema.ProductsTable.Cols.QUANTITY + ") " +
            "VALUES (?,?,?,?,?)";

    private DbHelper dbHelper;
    private static SQLiteDatabase db;

    public static ProductManager getInstance(Context context){
        if(instance == null){
            instance = new ProductManager(context);
        }

        return instance;
    }

    private ProductManager(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public List<Product> selectAll(){
        String sql = "SELECT * FROM " + DbSchema.ProductsTable.NAME;
        Cursor cursor = db.rawQuery(sql, null);
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);
        return cursorWrapper.getProducts();
    }

    public static boolean add(Product product){
        SQLiteStatement st = db.compileStatement(INSERT);

        st.bindLong(1, product.getId());
        st.bindString(2, product.getThumbnail());
        st.bindString(3, product.getName());
        st.bindLong(4, product.getUnitPrice());
        st.bindLong(5, 1);

        long id = st.executeInsert();
        if(id>0){
//            product.setId((int)id);
            return true;
        }

        return false;
    }

    public static boolean delete(long id){
        int result = db.delete(DbSchema.ProductsTable.NAME, "id = ?", new String[]{id+""});
        return result>0;
    }

    public static Product findByID(long id){
        String sql = "SELECT * FROM " + DbSchema.ProductsTable.NAME + " WHERE id = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);
        return cursorWrapper.getProductByID();
    }

    public static boolean update(Product product){
        ContentValues contentValues = new ContentValues();
        contentValues.put("thumbnail", product.getThumbnail());
        contentValues.put("name", product.getName());
        contentValues.put("unitprice", product.getUnitPrice());
        int res = db.update(DbSchema.ProductsTable.NAME, contentValues, "id = ?", new String[]{product.getId()+""});
        return res>0;
    }

    public static boolean updateQ(Product product){
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", product.getQuantity()+1);
        int res = db.update(DbSchema.ProductsTable.NAME, contentValues, "id = ?", new String[]{product.getId()+""});
        return res>0;
    }

    public static boolean updateQ2(Product product){
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", product.getQuantity()-1);
        int res = db.update(DbSchema.ProductsTable.NAME, contentValues, "id = ?", new String[]{product.getId()+""});
        return res>0;
    }
}
