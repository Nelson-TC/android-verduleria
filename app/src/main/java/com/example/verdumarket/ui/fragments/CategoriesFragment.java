package com.example.verdumarket.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verdumarket.R;
import com.example.verdumarket.model.Category;
import com.example.verdumarket.model.Product;
import com.example.verdumarket.network.ApiClient;
import com.example.verdumarket.network.ApiService;
import com.example.verdumarket.repository.CategoryRepository;
import com.example.verdumarket.ui.activities.CreateCategoryActivity;
import com.example.verdumarket.view.adapters.CategoryAdapter;
import com.example.verdumarket.viewmodel.CategoryViewModel;
import com.example.verdumarket.viewmodel.factories.CategoryViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesFragment extends Fragment implements CategoryAdapter.CategoryAdapterListener {

    @Override
    public void onCategoryDeleted() {
        loadCategories();
    }

    private CategoryAdapter categoryAdapter;

    private void loadCategories() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<Category>> call = apiService.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body();
                    if (categories != null) {
                        categoryAdapter.setCategories(categories);
                    }
                } else {
                    Log.e("CategoriesFragment", "Hubo un error al cargar las categorias");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("CategoriesFragment", "Hubo un error al conectarse con el API");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        Button addCategoryButton = view.findViewById(R.id.addCategoryButton);

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cuando se hace clic en el botón, inicia la actividad CreateCategoryActivity
                Intent intent = new Intent(getActivity(), CreateCategoryActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        categoryAdapter = new CategoryAdapter(new ArrayList<>(), requireContext());
        categoryAdapter.setCategoryAdapterListener(this);
        recyclerView.setAdapter(categoryAdapter);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        CategoryRepository categoryRepository = new CategoryRepository(apiService);
        CategoryViewModelFactory factory = new CategoryViewModelFactory(categoryRepository);

        CategoryViewModel categoryViewModel = new ViewModelProvider(this, factory).get(CategoryViewModel.class);

        categoryViewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.setCategories(categories);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("categoryPreferences", 0);//0 represents PRIVATE_MODE
        boolean categoryUpdated = sharedPreferences.getBoolean("updateCategories", false);
        if (categoryUpdated) {
            loadCategories();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("updateCategories", false);
            editor.apply();
        }
    }
}

