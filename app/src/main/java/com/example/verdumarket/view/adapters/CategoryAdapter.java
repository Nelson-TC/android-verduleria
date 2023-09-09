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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verdumarket.R;
import com.example.verdumarket.model.Category;
import com.example.verdumarket.network.ApiClient;
import com.example.verdumarket.network.ApiService;
import com.example.verdumarket.ui.activities.EditCategoryActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categories;
    private Context context;

    private CategoryAdapterListener listener;


    public CategoryAdapter(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryNameTextView;

        ImageView editCategoryButton;
        ImageView deleteCategoryButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            editCategoryButton = itemView.findViewById(R.id.editCategoryButton);
            editCategoryButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Category category = categories.get(position);
                    Intent intent = new Intent(v.getContext(), EditCategoryActivity.class);
                    intent.putExtra("category", category);
                    v.getContext().startActivity(intent);
                }
            });
            deleteCategoryButton = itemView.findViewById(R.id.deleteCategoryButton);
            deleteCategoryButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    showDeleteConfirmationDialog(position);
                }
            });
        }

        public void bind(Category category) {
            categoryNameTextView.setText(category.getName());
        }
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
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

    private void deleteCategory(Category category) {
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

    public interface CategoryAdapterListener {
        void onCategoryDeleted();
    }

    public void setCategoryAdapterListener(CategoryAdapterListener listener) {
        this.listener = listener;
    }


}

