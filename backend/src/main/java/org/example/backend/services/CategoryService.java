package org.example.backend.services;

import org.example.backend.entities.Category;
import org.example.backend.support.exceptions.*;
import org.example.backend.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Transactional(readOnly = true)
    public List<Category> showAllCategories() {
        return categoryRepository.findAll();
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