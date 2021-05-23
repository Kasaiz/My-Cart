package hanu.a2_1801040090.mycart.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hanu.a2_1801040090.mycart.R;
import hanu.a2_1801040090.mycart.db.ProductManager;
import hanu.a2_1801040090.mycart.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> implements Filterable {
    private List<Product> products;
    private List<Product> fullProducts;

    public ProductAdapter(List<Product> products){
        this.products = products;
        fullProducts = new ArrayList<>(products);
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.product, parent, false);

        return new ProductHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length()==0){
                filteredList.addAll(fullProducts);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Product product:fullProducts){
                    if (product.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(product);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            products.clear();
            products.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ProductHolder extends RecyclerView.ViewHolder {
        private ImageView ivProduct;
        private TextView tvDes, tvPrice;
        private ImageButton btnAdd;
        private Context context;

        public ProductHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;

            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvDes = itemView.findViewById(R.id.tvDes);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }

        public void bind(Product product){
//            Bitmap myBitmap = null;
//            try {
//                URL url = new URL(product.getThumbnail());
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);
//                connection.connect();
//                InputStream input = connection.getInputStream();
//                myBitmap = BitmapFactory.decodeStream(input);
//            }catch (IOException e){
//                e.printStackTrace();
//            }

//            ivProduct.setImageBitmap(myBitmap);

            ImageLoader imageLoader = new ImageLoader();
            imageLoader.execute(product.getThumbnail());

            tvDes.setText(product.getName());
            tvPrice.setText(""+product.getUnitPrice());
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: find if id is exist update Q else add new one
                    Product exist = ProductManager.getInstance(context).findByID(product.getId());
                    Log.i("exist", ""+exist);
                    //Add product to cart
                    if(exist==null){
                        ProductManager.getInstance(context).add(new Product(product.getId(),product.getThumbnail(),product.getName(),product.getUnitPrice()));
                    }else {
                        ProductManager.getInstance(context).updateQ(product);
                        product.setQuantity(product.getQuantity()+1);
                    }
                }
            });
        }

        class ImageLoader extends AsyncTask<String, Void, Bitmap>{

            @Override
            protected Bitmap doInBackground(String... strings) {
                URL url;
                HttpURLConnection urlConnection;
                try {
                    url = new URL(strings[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();

                    InputStream is = urlConnection.getInputStream();
                    Bitmap image = BitmapFactory.decodeStream(is);
                    return image;
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                ivProduct.setImageBitmap(bitmap);
            }
        }
    }


}
