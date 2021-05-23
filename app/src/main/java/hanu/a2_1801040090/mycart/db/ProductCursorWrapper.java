package hanu.a2_1801040090.mycart.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1801040090.mycart.models.Product;

public class ProductCursorWrapper extends CursorWrapper {

    public ProductCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Product getProduct(){
        int id = getInt(getColumnIndex("id"));
        String thumbnail = getString(getColumnIndex(DbSchema.ProductsTable.Cols.THUMBNAIL));
        String name = getString(getColumnIndex(DbSchema.ProductsTable.Cols.NAME));
        int unitPrice = getInt(getColumnIndex(DbSchema.ProductsTable.Cols.UNITPRICE));
        int quantity = getInt(getColumnIndex(DbSchema.ProductsTable.Cols.QUANTITY));

        return new Product(id, thumbnail, name, unitPrice, quantity);
    }

    public Product getProductByID(){
        Product product = null;

        moveToFirst();
        if(!isAfterLast()){
            product = getProduct();
        }
        return product;
    }

    public List<Product> getProducts(){
        List<Product> products = new ArrayList<>();

        moveToFirst();
        while (!isAfterLast()){
            products.add(getProduct());
            moveToNext();
        }
        return products;
    }

}
