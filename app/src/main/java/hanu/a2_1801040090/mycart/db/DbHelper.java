package hanu.a2_1801040090.mycart.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 2;

    public DbHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DbSchema.ProductsTable.NAME + "( "+
                "id INTEGER PRIMARY KEY, " +
                DbSchema.ProductsTable.Cols.THUMBNAIL + " TEXT, " +
                DbSchema.ProductsTable.Cols.NAME + " TEXT, " +
                DbSchema.ProductsTable.Cols.UNITPRICE + " INTEGER, " +
                DbSchema.ProductsTable.Cols.QUANTITY + " INTEGER" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbSchema.ProductsTable.NAME);
        onCreate(db);
    }
}
