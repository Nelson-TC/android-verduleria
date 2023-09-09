package com.example.verdumarket.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.verdumarket.model.Product;
import com.example.verdumarket.repository.ProductRepository;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private ProductRepository productRepository;
    private LiveData<List<Product>> productsLiveData;

    public ProductViewModel(ProductRepository productRepository) {
        this.productRepository = productRepository;
        productsLiveData = productRepository.getProducts();
    }

    public LiveData<List<Product>> getProductsLiveData() {
        return productsLiveData;
    }
}
