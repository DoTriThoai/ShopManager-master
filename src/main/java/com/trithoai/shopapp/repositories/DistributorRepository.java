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
public interface DistributorRepository extends JpaRepository<Distributor, Integer> {
    public List<Distributor> findByName(String name);
    public Page<Distributor> findAll(Pageable pageable);
}
