package com.richarcangui.searcher.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "products", createIndex = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Product {
    @Id
    private String id;

    @Field(type = FieldType.Text, name = "image")
    private String image;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Search_As_You_Type, name = "description")
    private String description;

    @Field(type = FieldType.Search_As_You_Type, name = "price")
    private String price;

    @Field(type = FieldType.Text, name = "type")
    private String type;

    @Field(type = FieldType.Text, name = "category")
    private String category;

}