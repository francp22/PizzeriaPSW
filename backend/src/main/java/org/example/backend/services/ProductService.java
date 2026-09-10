package org.example.backend.services;

import org.example.backend.entities.Category;
import org.example.backend.entities.Product;
import org.example.backend.repositories.CategoryRepository;
import org.example.backend.repositories.ProductRepository;
import org.example.backend.support.exceptions.CategoryAlreadyExistException;
import org.example.backend.support.exceptions.CategoryNotFoundException;
import org.example.backend.support.exceptions.ProductAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Product> showAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = false)
    public void addProduct(Product product)
            throws CategoryNotFoundException, ProductAlreadyExistException {

        if (product.getCategory() == null ||
                product.getCategory().getId() == null ||
                !categoryRepository.existsById(product.getCategory().getId())) {

            throw new CategoryNotFoundException();
        }

        if (productRepository.existsByNameAndCategoryId(
                product.getName(),
                product.getCategory().getId())) {

            throw new ProductAlreadyExistException();
        }

        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> showProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    @Transactional(readOnly = true)
    public List<Product> showAllProducts(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findAll(paging);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Category> showAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> showProductsByCategory(Integer categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Transactional(readOnly = true)
    public List<Product> showProductsByCategory(Integer categoryId, int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));

        Page<Product> pagedResult = productRepository.findByCategoryId(categoryId, paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = false)
    public void addCategory(Category category)
            throws CategoryAlreadyExistException {

        if (categoryRepository.existsByName(category.getName())) {
            throw new CategoryAlreadyExistException();
        }

        categoryRepository.save(category);
    }
}