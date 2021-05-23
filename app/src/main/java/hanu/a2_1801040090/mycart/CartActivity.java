package hanu.a2_1801040090.mycart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1801040090.mycart.adapters.OrderProductAdapter;
import hanu.a2_1801040090.mycart.adapters.ProductAdapter;
import hanu.a2_1801040090.mycart.db.ProductManager;
import hanu.a2_1801040090.mycart.models.Product;

public class CartActivity extends AppCompatActivity {
    private List<Product> orderProducts;
    private OrderProductAdapter orderProductAdapter;
    private RecyclerView rv;
    private ProductManager productManager;
    private TextView tvCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tvCost = findViewById(R.id.tvCost);

        productManager = ProductManager.getInstance(this);
        orderProducts = productManager.selectAll();

        int total = 0;
        for(int i=0;i<orderProducts.size();i++){
            total = total + orderProducts.get(i).getQuantity()*orderProducts.get(i).getUnitPrice();
        }
        tvCost.setText(""+total);

        orderProductAdapter = new OrderProductAdapter(orderProducts);

        //recycler view
        rv = findViewById(R.id.recyclerView);
        rv.setAdapter(orderProductAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(resultCode==RESULT_OK && requestCode==1){
//            orderProducts.clear();
//            orderProducts.addAll(productManager.selectAll());
//
//            orderProductAdapter.notifyDataSetChanged();
//        }
    }

    public void updateTotal(int value){
        int newValue = Integer.parseInt(String.valueOf(tvCost.getText())) + value;
        tvCost.setText(""+newValue);
    }
}