package com.example.verdumarket.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.verdumarket.model.Category;
import com.example.verdumarket.network.ApiClient;
import com.example.verdumarket.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private ApiService apiService;

    public CategoryRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<List<Category>> getCategories() {
        MutableLiveData<List<Category>> categoriesLiveData = new MutableLiveData<>();

        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categoriesLiveData.setValue(response.body());
                } else {
                    Log.e("CategoryRepository", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // Manejo de errores
            }
        });

        return categoriesLiveData;
    }
}


