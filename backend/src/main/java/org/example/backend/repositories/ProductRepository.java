package org.example.backend.repositories;

import org.example.backend.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategoryId(Integer categoryId);

    List<Product> findByNameContaining(String name);

    boolean existsByNameAndCategoryId(String name, Integer categoryId);

    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);
}
