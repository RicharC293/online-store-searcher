package com.richarcangui.searcher.data;


import com.richarcangui.searcher.model.db.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    List<Product> findByName(String name);

    Optional<Product> findById(String id);

    Product save(Product product);

    void delete(Product product);

    List<Product> findAll();
}
