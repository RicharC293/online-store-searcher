package com.richarcangui.searcher.data;

import java.util.*;

import com.richarcangui.searcher.model.db.Product;
import com.richarcangui.searcher.model.response.AggregationDetails;
import com.richarcangui.searcher.model.response.ProductsQueryResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DataAccessRepository {

    @Value("${server.fullAddress}")
    private String serverFullAddress;

    // Esta clase (y bean) es la unica que usan directamente los servicios para
    // acceder a los datos.
    private final ProductRepository productRepository;
    private final ElasticsearchOperations elasticClient;

    private final String[] nameSearchFields = {"name", "name._2gram", "name._3gram"};

    private final String[] descriptionSearchFields = {"description", "description._2gram", "description._3gram"};

    private final String[] priceSearchFields = {"price", "price._2gram", "price._3gram"};

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Boolean delete(Product product) {
        productRepository.delete(product);
        return Boolean.TRUE;
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    @SneakyThrows
    public ProductsQueryResponse findProducts(String name, String description, String price, String type, String category) {

        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

        // Filter by category
        if (!StringUtils.isEmpty(category)) {
            querySpec.must(QueryBuilders.termQuery("category", category));
        }

        /// Filter by type
        if (!StringUtils.isEmpty(type)) {
            querySpec.must(QueryBuilders.termQuery("type", type));
        }


        if (!StringUtils.isEmpty(name)) {
            querySpec.must(QueryBuilders.multiMatchQuery(name, nameSearchFields).type(Type.BOOL_PREFIX));
        }

        if (!StringUtils.isEmpty(description)) {
            querySpec.must(QueryBuilders.multiMatchQuery(description, descriptionSearchFields).type(Type.BOOL_PREFIX));
        }

        if (!StringUtils.isEmpty(price)) {
            if(price.equals("DESC")) {
                // sort query by price DESC
                NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                        .withQuery(matchAllQuery())
                        .withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC))
                        .build();
                SearchHits<Product> result = elasticClient.search(searchQuery, Product.class);
                return new ProductsQueryResponse(result.getSearchHits().stream().map(SearchHit::getContent).toList(), new LinkedList<>());
            }
            else if(price.equals("ASC")) {
                // sort by price ASC
                NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                        .withQuery(matchAllQuery())
                        .withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC))
                        .build();
                SearchHits<Product> result = elasticClient.search(searchQuery, Product.class);
                return new ProductsQueryResponse(result.getSearchHits().stream().map(SearchHit::getContent).toList(), new LinkedList<>());
            }
            else {
                querySpec.must(QueryBuilders.multiMatchQuery(price, priceSearchFields).type(Type.BOOL_PREFIX));
            }
        }


        //Si no he recibido ningun parametro, busco todos los elementos.
        if (!querySpec.hasClauses()) {
            querySpec.must(matchAllQuery());
        }

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec);


        Query query = nativeSearchQueryBuilder.build();
        SearchHits<Product> result = elasticClient.search(query, Product.class);

        List<AggregationDetails> responseAggs = new LinkedList<>();


        return new ProductsQueryResponse(result.getSearchHits().stream().map(SearchHit::getContent).toList(), responseAggs);
    }
}
