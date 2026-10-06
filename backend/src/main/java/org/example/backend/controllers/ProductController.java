package org.example.backend.controllers;

import org.example.backend.entities.Product;
import org.example.backend.support.exceptions.*;
import org.example.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping
    public ResponseEntity create(@RequestBody Product product) {
        try {
            productService.addProduct(product);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(
                    "Category not found!",
                    HttpStatus.BAD_REQUEST);
        } catch (ProductAlreadyExistException e) {
            return new ResponseEntity<>(
                    "Product already exists!",
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                "Added successful!",
                HttpStatus.OK);
    }


    @GetMapping
    public List<Product> getAll() {
        return productService.showAllProducts();
    }


    @GetMapping("/paged")
    public ResponseEntity getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        List<Product> result =
                productService.showAllProducts(
                        pageNumber, pageSize, sortBy);

        if (result.size() <= 0) {
            return new ResponseEntity<>(
                    "No results!",
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/search/by_name")
    public ResponseEntity getByName(
            @RequestParam(required = false) String name) {

        List<Product> result =
                productService.showProductsByName(name);

        if (result.size() <= 0) {
            return new ResponseEntity<>(
                    "No results!",
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/category/{categoryId}")
    public ResponseEntity getByCategory(
            @PathVariable Integer categoryId) {

        try {
            List<Product> result =
                    productService.showProductsByCategory(categoryId);

            if (result.size() <= 0) {
                return new ResponseEntity<>(
                        "No results!",
                        HttpStatus.OK);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(
                    "Category not found!",
                    HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/category/{categoryId}/paged")
    public ResponseEntity getByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            List<Product> result =
                    productService.showProductsByCategory(
                            categoryId,
                            pageNumber,
                            pageSize,
                            sortBy);

            if (result.size() <= 0) {
                return new ResponseEntity<>(
                        "No results!",
                        HttpStatus.OK);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(
                    "Category not found!",
                    HttpStatus.BAD_REQUEST);
        }
    }
}