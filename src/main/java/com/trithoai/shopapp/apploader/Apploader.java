package com.trithoai.shopapp.apploader;

import com.trithoai.shopapp.models.Category;
import com.trithoai.shopapp.models.Distributor;
import com.trithoai.shopapp.models.Product;
import com.trithoai.shopapp.repositories.CategoryRepository;
import com.trithoai.shopapp.repositories.DistributorRepository;
import com.trithoai.shopapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by johncarter on 11/04/2016.
 */
@Component
public class Apploader implements ApplicationListener<ContextRefreshedEvent>{
    private DistributorRepository distributorRepository;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Distributor apple = new Distributor("Apple");
        distributorRepository.save(apple);
        Distributor samsung = new Distributor("Samsung");
        distributorRepository.save(samsung);
        Category laptopApple = new Category("Laptop-Apple", apple);
        categoryRepository.save(laptopApple);
        Category tabletApple = new Category("Tablet-Apple", apple);
        categoryRepository.save(tabletApple);
        Category tabletSamsung = new Category("Tablet-Samsung", samsung);
        categoryRepository.save(tabletSamsung);
        Category smartPhoneSamsung = new Category("SmartPhone-Samsung",samsung);
        categoryRepository.save(smartPhoneSamsung);
        Product pro15 = new Product("Macbook Pro Retina 15.4'' -MJLQ2ZP/A (2015)",47000000, laptopApple);
        productRepository.save(pro15);
        Product pro13 = new Product("Macbook Pro Retina 13.3'' - MF841ZP/A (2015)", 45000000, laptopApple);
        productRepository.save(pro13);
        Product ssgalaxys6Egde = new Product("Điện thoại Samsung Galaxy S6 Edge Plus", 20000000, smartPhoneSamsung);
        productRepository.save(ssgalaxys6Egde);
    }

    @Autowired
    public void setDistributorRepository(DistributorRepository distributorRepository){this.distributorRepository = distributorRepository;}
    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository){this.categoryRepository = categoryRepository;}
    @Autowired
    public void setProductRepository(ProductRepository productRepository){this.productRepository = productRepository;}
}
