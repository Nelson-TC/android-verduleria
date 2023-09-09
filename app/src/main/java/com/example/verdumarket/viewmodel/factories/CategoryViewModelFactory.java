package com.example.verdumarket.viewmodel.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.verdumarket.repository.CategoryRepository;
import com.example.verdumarket.viewmodel.CategoryViewModel;

public class CategoryViewModelFactory implements ViewModelProvider.Factory {

    private CategoryRepository categoryRepository;

    public CategoryViewModelFactory(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoryViewModel.class)) {
            return (T) new CategoryViewModel(categoryRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
