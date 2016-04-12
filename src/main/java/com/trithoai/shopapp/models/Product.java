package com.trithoai.shopapp.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by johncarter on 11/04/2016.
 */
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private Integer price;

    @ManyToOne
    private Category category;

    public Product(){}

    public Product(Integer id){this.id = id;}

    public Product(String name, Integer price){this.name = name; this.price = price;}

    public Product(String name,Integer price, Category category){
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
