package com.trithoai.shopapp.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by johncarter on 11/04/2016.
 */
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String name;

    @ManyToOne
    private Distributor distributor;

    @OneToMany(targetEntity = Product.class)
    private List<Product> products;

    public Category(){}

    public Category(Integer id){
        super();
        this.id = id;
    }

    public Category(String name){
        super();
        this.name = name;
    }

    public Category(Integer id, String name){
        super();
        this.id = id;
        this.name = name;
    }

    public Category(Integer id, String name, Distributor distributor){
        super();
        this.id = id;
        this.name = name;
        this.distributor = distributor;

    }

    public Category(String name, Distributor distributor){
        super();
        this.name = name;
        this.distributor = distributor;

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

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
