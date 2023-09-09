package com.example.verdumarket.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.verdumarket.R;
import com.example.verdumarket.model.Category;
import com.example.verdumarket.network.ApiClient;
import com.example.verdumarket.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCategoryActivity extends AppCompatActivity {
    private EditText categoryNameCreateText;
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        /* Set the data to the activity (text) */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Return Button
        getSupportActionBar().setTitle("Crear Categoría"); // Change Title ActionBar

        categoryNameCreateText = findViewById(R.id.categoryNameCreateText);
        saveButton = findViewById(R.id.saveButton);


        saveButton.setOnClickListener(v -> {
            String newName = categoryNameCreateText.getText().toString().trim();
            LayoutInflater inflater = getLayoutInflater();
            if(newName.length() > 0) {

                Category newCategory = Category.createNewCategory(newName);

                /* The update function itself */
                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                retrofit2.Call<Category> call = apiService.createCategory(newCategory);
                call.enqueue(new Callback<Category>() {
                    @Override
                    public void onResponse(Call<Category> call, Response<Category> response) {
                        if (response.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("categoryPreferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("updateCategories", true);
                            editor.apply();

                            View layout = inflater.inflate(R.layout.success_toast, findViewById(R.id.success_toast));
                            TextView textView = layout.findViewById(R.id.textView);

                            textView.setText("La categoria ha sido añadida");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();

                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Log.e("CreateCategoryActivity", "Hubo un error al crear la categoria: ");
                        }
                    }

                    @Override
                    public void onFailure(Call<Category> call, Throwable t) {
                        Log.e("CreateCategoryActivity", "Hubo un error al conectarse con el API");

                    }
                });
            }else{
                View layout = inflater.inflate(R.layout.error_toast, findViewById(R.id.error_toast));
                TextView textView = layout.findViewById(R.id.textView);

                textView.setText("El nombre no puede estar vacío");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }
        });
    }

    /* Return to the fragment */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}