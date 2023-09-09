package com.example.verdumarket.ui.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.verdumarket.R;
import com.example.verdumarket.model.Category;
import com.example.verdumarket.model.Product;
import com.example.verdumarket.network.ApiClient;
import com.example.verdumarket.network.ApiService;
import com.example.verdumarket.repository.CategoryRepository;
import com.example.verdumarket.utils.MeasurementUtils;
import com.example.verdumarket.viewmodel.CategoryViewModel;
import com.example.verdumarket.viewmodel.factories.CategoryViewModelFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProductActivity extends AppCompatActivity {

    private EditText productNameCreateText;
    private EditText productPriceCreateText;
    private int selectedCategoryId;
    private String selectedUnitMeasurement;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        /* Get the categories and set to the textLayout (select) */
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        CategoryRepository categoryRepository = new CategoryRepository(apiService);
        CategoryViewModelFactory factory = new CategoryViewModelFactory(categoryRepository);
        CategoryViewModel categoryViewModel = new ViewModelProvider(this, factory).get(CategoryViewModel.class);
        categoryViewModel.getCategoriesLiveData().observe(this, categories -> {
            AutoCompleteTextView autoCompleteTextView = findViewById(R.id.auto_complete_category_id_txt);

            ArrayAdapter<Category> autoCompleteAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_dropdown_item_1line, categories) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView textView = (TextView) super.getView(position, convertView, parent);
                    Category category = getItem(position);
                    if (category != null) {
                        textView.setText(category.getName());
                    }
                    return textView;
                }
            };

            autoCompleteTextView.setAdapter(autoCompleteAdapter);

            autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                Category selectedCategory = autoCompleteAdapter.getItem(position);
                if (selectedCategory != null) {
                    selectedCategoryId = selectedCategory.getId();
                    autoCompleteTextView.setText(selectedCategory.getName(), false);
                }
            });
        });

        /* Get the units measurements (local enum) and configure the textLayout (select) */
        AutoCompleteTextView unitMeasurementAutoCompleteTextView = findViewById(R.id.auto_complete_unit_measurement_txt);

        ArrayAdapter<MeasurementUtils.UnitMeasurement> unitMeasurementAdapter = new ArrayAdapter<MeasurementUtils.UnitMeasurement>(this, android.R.layout.simple_dropdown_item_1line, MeasurementUtils.UnitMeasurement.values()) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                MeasurementUtils.UnitMeasurement unitMeasurement = getItem(position);
                if (unitMeasurement != null) {
                    textView.setText(unitMeasurement.getDisplayName());
                }
                return textView;
            }
        };
        unitMeasurementAutoCompleteTextView.setAdapter(unitMeasurementAdapter);

        unitMeasurementAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            selectedUnitMeasurement = unitMeasurementAdapter.getItem(position).getDisplayName();
            unitMeasurementAutoCompleteTextView.setText(selectedUnitMeasurement, false);
        });

        /* Set the data to the activity (text) */

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Return Button
        getSupportActionBar().setTitle("Crear Producto"); // Change Title ActionBar

        /* Declare the create Fields */
        productNameCreateText = findViewById(R.id.productNameCreateText);
        productPriceCreateText = findViewById(R.id.productPriceCreateText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            LayoutInflater inflater = getLayoutInflater();
            String createName = productNameCreateText.getText().toString();

            String priceString = productPriceCreateText.getText().toString();

            double createPrice = -1;
            if (!priceString.isEmpty()) {
                createPrice = Double.parseDouble(priceString);
            }
            if (createName.length() > 0 && createPrice >= 0 && selectedUnitMeasurement != null && selectedCategoryId > 0) {
                Product productToCreate = new Product(-1, createName, 0, createPrice, selectedUnitMeasurement, selectedCategoryId);
                 /* The store function itself */
                retrofit2.Call<Product> call = apiService.createProduct(productToCreate);
                call.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences("productPreferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("updateProducts", true);
                            editor.apply();

                            View layout = inflater.inflate(R.layout.success_toast, findViewById(R.id.success_toast));
                            TextView textView = layout.findViewById(R.id.textView);

                            textView.setText("El producto ha sido añadido");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();

                            setResult(Activity.RESULT_OK);
                            finish();

                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Log.e("EditProductActivity", "Hubo un error al actualizar el producto");
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Log.e("EditProductActivity", "Hubo un error al conectarse con el API");

                    }
                });
            }else{
                View layout = inflater.inflate(R.layout.error_toast, findViewById(R.id.error_toast));
                TextView textView = layout.findViewById(R.id.textView);

                textView.setText("Llena correctamente los datos");
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
