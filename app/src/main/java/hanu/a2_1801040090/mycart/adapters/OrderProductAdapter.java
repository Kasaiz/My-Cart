package hanu.a2_1801040090.mycart.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import hanu.a2_1801040090.mycart.CartActivity;
import hanu.a2_1801040090.mycart.MainActivity;
import hanu.a2_1801040090.mycart.R;
import hanu.a2_1801040090.mycart.db.ProductManager;
import hanu.a2_1801040090.mycart.models.Product;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderProductHolder> {
    private List<Product> products;

    public OrderProductAdapter(List<Product> products){
        this.products = products;
    }

    @NonNull
    @Override
    public OrderProductAdapter.OrderProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.ordered_product, parent, false);

        return new OrderProductAdapter.OrderProductHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductAdapter.OrderProductHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class OrderProductHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tvD, tvP, tvQ, tvPr;
        private ImageButton btnPlus, btnSubtract;
        private Context context;

        public OrderProductHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;

            iv = itemView.findViewById(R.id.iv);
            tvD = itemView.findViewById(R.id.tvD);
            tvP = itemView.findViewById(R.id.tvP);
            tvQ = itemView.findViewById(R.id.tvQ);
            tvPr = itemView.findViewById(R.id.tvPr);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnSubtract = itemView.findViewById(R.id.btnSubtract);
        }

        public void bind(Product product){

            OrderProductAdapter.OrderProductHolder.ImageLoader imageLoader = new OrderProductAdapter.OrderProductHolder.ImageLoader();
            imageLoader.execute(product.getThumbnail());

            long priceProduct = product.getQuantity()*product.getUnitPrice();

            tvD.setText(product.getName());
            tvP.setText(""+product.getUnitPrice());
            tvPr.setText(""+priceProduct);
            tvQ.setText(""+product.getQuantity());
            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //update Quantity in dbs
                    ProductManager.getInstance(context).updateQ(product);
                    product.setQuantity(product.getQuantity()+1);
                    tvQ.setText(""+product.getQuantity());
                    long newPrice = product.getQuantity()*product.getUnitPrice();
                    tvPr.setText(""+newPrice);
                    ((CartActivity) context).updateTotal(product.getUnitPrice());
                }
            });
            btnSubtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //update Q in db
                    if(product.getQuantity()==1){
                        ProductManager.getInstance(context).delete(product.getId());
                        products.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());

                    }else {
                        ProductManager.getInstance(context).updateQ2(product);
                        product.setQuantity(product.getQuantity()-1);
                        tvQ.setText(""+product.getQuantity());
                        long newPrice = product.getQuantity()*product.getUnitPrice();
                        tvPr.setText(""+newPrice);
                    }
                    ((CartActivity) context).updateTotal(product.getUnitPrice()*(-1));
                }
            });
        }

        class ImageLoader extends AsyncTask<String, Void, Bitmap> {

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
                iv.setImageBitmap(bitmap);
            }
        }
    }
}
