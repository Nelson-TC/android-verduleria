package com.example.verdumarket.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;

import com.example.verdumarket.model.Category;
import com.example.verdumarket.repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> categoriesLiveData;

    public CategoryViewModel(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        categoriesLiveData = categoryRepository.getCategories();
    }

    public LiveData<List<Category>> getCategoriesLiveData() {
        return categoriesLiveData;
    }
}
