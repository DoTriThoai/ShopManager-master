package com.trithoai.shopapp.repositories;

import com.trithoai.shopapp.models.Category;
import com.trithoai.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by johncarter on 11/04/2016.
 */
public interface ProductRepository extends JpaRepository<Product, Integer>{
    public List<Product> findByName(String name);
    public List<Product> findByCategory(Category category);
    public Page<Product> findByCategory(Category category, Pageable pageable);
}
