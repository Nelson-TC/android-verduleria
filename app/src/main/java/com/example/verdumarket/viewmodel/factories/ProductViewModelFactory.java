package com.example.verdumarket.viewmodel.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.verdumarket.repository.ProductRepository;
import com.example.verdumarket.viewmodel.ProductViewModel;

public class ProductViewModelFactory implements ViewModelProvider.Factory {
    private ProductRepository productRepository;

    public ProductViewModelFactory(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductViewModel.class)) {
            return (T) new ProductViewModel(productRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
