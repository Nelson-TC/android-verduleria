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
import com.example.verdumarket.model.Product;
import com.example.verdumarket.network.ApiClient;
import com.example.verdumarket.network.ApiService;
import com.example.verdumarket.repository.ProductRepository;
import com.example.verdumarket.ui.activities.CreateProductActivity;
import com.example.verdumarket.view.adapters.ProductAdapter;
import com.example.verdumarket.viewmodel.ProductViewModel;
import com.example.verdumarket.viewmodel.factories.ProductViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsFragment extends Fragment {

    private ProductViewModel productViewModel;
    private ProductAdapter productAdapter;

    private void loadProducts() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<List<Product>> call = apiService.getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    if (products != null) {
                        // Actualiza la lista de productos en el adaptador y notifica el cambio
                        productAdapter.setProducts(products);
                    }
                } else {
                    Log.e("ProductsFragment", "Hubo un error al cargar los productos");
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("ProductsFragment", "Hubo un error al conectarse con el API");
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        Button addProductButton = view.findViewById(R.id.addProductButton);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cuando se hace clic en el botón, inicia la actividad CreateCategoryActivity
                Intent intent = new Intent(getActivity(), CreateProductActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        productAdapter = new ProductAdapter(new ArrayList<>());
        recyclerView.setAdapter(productAdapter);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        ProductRepository productRepository = new ProductRepository(apiService);
        ProductViewModelFactory factory = new ProductViewModelFactory(productRepository);

        productViewModel = new ViewModelProvider(this, factory).get(ProductViewModel.class);

        productViewModel.getProductsLiveData().observe(getViewLifecycleOwner(), products -> {
            productAdapter.setProducts(products);
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("productPreferences", 0);//0 represents PRIVATE_MODE
        boolean productUpdated = sharedPreferences.getBoolean("updateProducts", false);
        if (productUpdated) {
            loadProducts();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("updateProducts", false);
            editor.apply();
        }
    }



}
