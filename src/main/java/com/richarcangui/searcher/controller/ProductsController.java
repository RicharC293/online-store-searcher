package com.richarcangui.searcher.controller;


import java.util.Map;

import com.richarcangui.searcher.model.response.ProductsQueryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.richarcangui.searcher.model.db.Product;
import com.richarcangui.searcher.service.ProductsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductsController {

    private final ProductsService service;


    @GetMapping("/products")
    @Operation(
            operationId = "Obtener productos",
            description = "Operacion de lectura",
            summary = "Se devuelve una lista de todos los productos almacenados en la base de datos.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    public ResponseEntity<ProductsQueryResponse> getProducts(

            @RequestHeader Map<String, String> headers,
            @Parameter(name = "name", description = "Nombre del producto. No tiene por que ser exacto")
            @RequestParam(required = false) String name,
            @Parameter(name = "description", description = "Descripcion del producto. No tiene por que ser exacta")
            @RequestParam(required = false) String description,
            @Parameter(name = "price", description = "Precio del producto. Tiene que ser exacto")
            @RequestParam(required = false) String price,
            @Parameter(name = "type", description = "Tipo del producto. Tiene que ser exacto puede ser snikers, bots o shoes")
            @RequestParam(required = false) String type,
            @Parameter(name = "category", description = "Categoria del producto. Puede ser cualquiera como man, women o child")
            @RequestParam(required = false) String category) {

        log.info("headers: {}", headers);
        ProductsQueryResponse products = service.getProducts(name, description, price, type, category);

        if(products == null) {
            return ResponseEntity.ok(new ProductsQueryResponse());
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{productId}")
    @Operation(
            operationId = "Obtener un producto",
            description = "Operacion de lectura",
            summary = "Se devuelve un producto a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado el producto con el identificador indicado.")
    public ResponseEntity<Product> getProduct(@PathVariable String productId) {

        log.info("Request received for product {}", productId);
        Product product = service.getProduct(productId);

        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
