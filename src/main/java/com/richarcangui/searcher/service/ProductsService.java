package com.richarcangui.searcher.service;

import com.richarcangui.searcher.model.db.Product;
import com.richarcangui.searcher.model.response.ProductsQueryResponse;


public interface ProductsService {
    ProductsQueryResponse getProducts(String name, String description, String price, String type, String category);

    Product getProduct(String productId);
}
