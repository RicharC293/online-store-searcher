package com.richarcangui.searcher.model.response;

import com.richarcangui.searcher.model.db.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductsQueryResponse {

    private List<Product> products;
    private List<AggregationDetails> aggs;

}
