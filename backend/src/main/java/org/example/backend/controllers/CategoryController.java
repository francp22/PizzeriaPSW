package org.example.backend.controllers;

import org.example.backend.entities.Category;
import org.example.backend.support.exceptions.*;
import org.example.backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity create(@RequestBody Category category) {
        try {
            categoryService.addCategory(category);
        } catch (CategoryAlreadyExistException e) {
            return new ResponseEntity<>(
                    "Category already exists!",
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                "Added successful!",
                HttpStatus.OK);
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryService.showAllCategories();
    }
}