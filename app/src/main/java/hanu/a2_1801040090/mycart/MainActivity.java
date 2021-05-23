package hanu.a2_1801040090.mycart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hanu.a2_1801040090.mycart.adapters.ProductAdapter;
import hanu.a2_1801040090.mycart.db.ProductManager;
import hanu.a2_1801040090.mycart.models.Product;

public class MainActivity extends AppCompatActivity {
    private List<Product> products;
    private ProductAdapter productAdapter;
    private RecyclerView rv;
    private ProductManager productManager;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.getFilter().filter(newText);
                return false;
            }
        });


        productManager = ProductManager.getInstance(MainActivity.this);

        //recycler view
        rv = findViewById(R.id.rv);
        //get data
        products = new ArrayList<>();
        RestLoader loader = new RestLoader();
        loader.execute("https://mpr-cart-api.herokuapp.com/products");
        Log.i("Products: ",""+ products.size());

//        productAdapter = new ProductAdapter(products);
//        rv.setAdapter(productAdapter);
//        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shopping_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnCart:
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    class RestLoader extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                Scanner sc = new Scanner(is);
                StringBuilder result = new StringBuilder();
                String line;
                while (sc.hasNextLine()){
                    line = sc.nextLine();
                    result.append(line);
                }
                return result.toString();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s==null){
                return;
            }

            try{
                JSONArray root = new JSONArray(s);
                Log.i("root", ""+root);

                for(int i=0;i<root.length();i++){
                    JSONObject product = root.getJSONObject(i);
                    products.add(new Product(product.getInt("id"), product.getString("thumbnail"), product.getString("name"), product.getInt("unitPrice")));

                }
                Log.i("size",""+products.size());
                productAdapter = new ProductAdapter(products);
                rv.setAdapter(productAdapter);
//                rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                rv.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}