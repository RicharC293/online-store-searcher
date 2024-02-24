package com.richarcangui.searcher.service;

import com.richarcangui.searcher.model.response.ProductsQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.richarcangui.searcher.data.DataAccessRepository;
import com.richarcangui.searcher.model.db.Product;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {

    private final DataAccessRepository repository;

    @Override
    public ProductsQueryResponse getProducts(String name, String description, String price, String type, String category) {
        return repository.findProducts(name, description, price, type, category);
    }

    @Override
    public Product getProduct(String productId) {
        return repository.findById(productId).orElse(null);
    }

}
