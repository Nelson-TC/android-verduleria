package com.example.verdumarket.network;

import com.example.verdumarket.model.Category;
import com.example.verdumarket.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    /* ---- Products ----- */
    @GET("products")
    Call<List<Product>> getProducts();

    @POST("products")
    Call<Product> createProduct(@Body Product createdProduct);
    @PUT("products/{id}")
    Call<Product> updateProduct(@Path("id") int productId, @Body Product updatedProduct);

    /* ---- Categories ----- */

    @GET("categories")
    Call<List<Category>> getCategories();
    @POST("categories")
    Call<Category>createCategory(@Body Category createdCategory);
    @PUT("categories/{id}")
    Call<Category> updateCategory(@Path("id") int categoryId, @Body Category updatedCategory);
    @DELETE("categories/{id}")
    Call<Category> deleteCategory(@Path("id") int categoryId);

}
