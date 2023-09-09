package com.example.verdumarket.view.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.verdumarket.R;
import com.example.verdumarket.model.Category;
import com.example.verdumarket.model.Product;
import com.example.verdumarket.network.ApiClient;
import com.example.verdumarket.network.ApiService;
import com.example.verdumarket.ui.activities.EditProductActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private Context context;
    private ProductAdapterListener listener;
    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView productCategoryTextView;
        ImageView editProductButton;


        ProductViewHolder(@NonNull View itemView) {

            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            productCategoryTextView = itemView.findViewById(R.id.productCategoryTextView);

            editProductButton = itemView.findViewById(R.id.editProductButton);
            editProductButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Product product = products.get(position);
                    Intent intent = new Intent(v.getContext(), EditProductActivity.class);
                    intent.putExtra("product", product);
                    v.getContext().startActivity(intent);
                }
            });
        }

        void bind(Product product) {
            productNameTextView.setText(product.getName());
            productPriceTextView.setText(String.format("Q.%.2f", product.getUnitPrice()));
            productCategoryTextView.setText("Categoria: " + product.getCategory().getName().toUpperCase());
        }
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder.setTitle("Eliminar Categoría");
        builder.setMessage("¿Estás seguro de que deseas eliminar esta categoría? En caso de tener productos esta acción no se completará");
        builder.setPositiveButton("Confirmar", (dialog, which) -> {
            Category categoryToDelete = categories.get(position);
            deleteCategory(categoryToDelete);
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteProduct(Product product) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Category> call = apiService.deleteCategory(category.getId());
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    showToast("La categoría ha sido eliminada", Toast.LENGTH_SHORT);
                    if (listener != null) {
                        listener.onCategoryDeleted();
                    }
                } else {
                    Log.e("CategoryAdapter", "Hubo un error al eliminar la categoría");
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("CategoryAdapter", "Hubo un error al conectarse con el API");
            }
        });
    }

    private void showToast(String message, int duration) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.success_toast, null);
        TextView textView = layout.findViewById(R.id.textView);
        textView.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    public interface ProductAdapterListener {
        void onProductDeleted();
    }

    public void setProductAdapterListener(ProductAdapterListener listener) {
        this.listener = listener;
    }

    public interface OnEditClickListener {
        void onEditClick(Product product);
    }

    private OnEditClickListener onEditClickListener;

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }


}
