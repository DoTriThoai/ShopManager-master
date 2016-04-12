package com.trithoai.shopapp.repositories;

import com.trithoai.shopapp.models.Category;
import com.trithoai.shopapp.models.Distributor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by johncarter on 11/04/2016.
 */
public interface CategoryRepository extends JpaRepository<Category, Integer>{
    public List<Category> findByName(String name);
    public List<Category> findByDistributor(Distributor distributor);
    public Page<Category> findByDistributor(Distributor distributor, Pageable pageable);
}
