package com.example.verdumarket.repository;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.verdumarket.model.Product;
import com.example.verdumarket.network.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private ApiService apiService;

    public ProductRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<List<Product>> getProducts() {
        MutableLiveData<List<Product>> productsLiveData = new MutableLiveData<>();

        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    productsLiveData.setValue(response.body());
                } else {
                    Log.e("ProductRepository", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Manejo de errores
            }
        });

        return productsLiveData;
    }
}
