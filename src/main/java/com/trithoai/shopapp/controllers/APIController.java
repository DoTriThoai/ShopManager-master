package com.trithoai.shopapp.controllers;

import com.google.gson.Gson;
import com.trithoai.shopapp.models.*;
import com.trithoai.shopapp.repositories.CategoryRepository;
import com.trithoai.shopapp.repositories.DistributorRepository;
import com.trithoai.shopapp.repositories.ProductRepository;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by johncarter on 11/04/2016.
 */
@RestController
@RequestMapping("/api")
public class APIController {
    @Autowired
    private DistributorRepository distributorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Gson gson = new Gson();

    @RequestMapping("distributor/fetch")
    public ResponseEntity<?> fetchAllDistributor(){
        List<Distributor>  lstDistributors = distributorRepository.findAll();
        return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.SUCCESS, gson.toJson(lstDistributors)), HttpStatus.OK);
    }

//    update method
    @RequestMapping("distributor/fetch/{strId}")
    public ResponseEntity<?> fetchDistrubutorById(@PathVariable String strId){
        try{
            Integer id = Integer.parseInt(strId);
            Distributor distributor = distributorRepository.findOne(id);
            if (distributor == null)
                return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, "Invalid Distributor"), HttpStatus.OK);
            return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.SUCCESS, gson.toJson(distributor)), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, ex.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "distributor/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveDistributor(@RequestBody Distributor distributor){
        try {
            if (distributor.getName()==null)
                return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, "Distributor Name is not null"), HttpStatus.OK);
            if (distributor.getId()!=null){
                List<Distributor> lstDistributors = distributorRepository.findByName(distributor.getName());
                if (lstDistributors.size() > 0 && lstDistributors.get(0).getId()!= distributor.getId())
                    return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, "Distributor Name is not available"), HttpStatus.OK);

            }else{
                List<Distributor> tmp = distributorRepository.findByName(distributor.getName());
                if (tmp.size() > 0)
                    return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, "Distributor Name is not null"), HttpStatus.OK);
            }
            distributorRepository.save(distributor);
            return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.SUCCESS, gson.toJson(distributor)), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, ex.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/distributor/delete/{strId}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteDistributor(@PathVariable String strId){
        try {
            Integer id = Integer.parseInt(strId);
            Distributor distributor = distributorRepository.findOne(id);
            if (distributor == null)
                return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, "Distributor is not valid"), HttpStatus.OK);
            List<Category> lstCategories = categoryRepository.findByDistributor(distributor);
            for (Category category :
                    lstCategories) {
                List<Product> lstProduct = productRepository.findByCategory(category);
                for (Product product:
                     lstProduct) {
                    productRepository.delete(product);
                }
                categoryRepository.delete(category);
            }
            distributorRepository.delete(distributor);
        }catch (Exception ex){
            return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, ex.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.SUCCESS, "Delete Distributor Success."), HttpStatus.OK);
    }

    // API for Category
    @RequestMapping("category/fetch/distributor/{strDistributorId}")
    public ResponseEntity<?> fetchCategoriesByDistributor(@PathVariable String strDistributorId){
        try {
            Integer distributorId = Integer.parseInt(strDistributorId);
            Distributor distributor = distributorRepository.findOne(distributorId);
            if (distributor == null)
                return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR,"Invalide Distributor"), HttpStatus.OK);
            List<Category> lstCategories = categoryRepository.findByDistributor(distributor);
            return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.SUCCESS,gson.toJson(lstCategories)), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, ex.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "category/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveCategory(@RequestBody Category category){
        try {
            if (category.getName()==null || category.getName().isEmpty())
                return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, "Category Name is not null"), HttpStatus.OK);
            if(category.getDistributor()==null)
                return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, "Distributor is not empty."), HttpStatus.OK);
            if (category.getId()!=null){
                List<Category> lstCategories = categoryRepository.findByName(category.getName());
                if (lstCategories.size() > 0 && lstCategories.get(0).getId()!= category.getId())
                    return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, "Category Name is not available"), HttpStatus.OK);
                if (distributorRepository.findOne(category.getDistributor().getId()) == null)
                    return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, "Distributor is not available"), HttpStatus.OK);
            }else{
                List<Category> tmp = categoryRepository.findByName(category.getName());
                if (tmp.size() > 0)
                    return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, "Category Name is not null"), HttpStatus.OK);
            }
            categoryRepository.save(category);
            return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.SUCCESS, gson.toJson(category)), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, ex.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping("category/fetch/{strId}")
    public ResponseEntity<?> fetchCategoryById(@PathVariable String strId){
        try{
            Integer id = Integer.parseInt(strId);
            Category category = categoryRepository.findOne(id);
            if (category == null)
                return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, "Invalid Category"), HttpStatus.OK);
            return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.SUCCESS, gson.toJson(category)), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<Object>(new ErrorMessage(ErrorCode.ERROR, ex.getMessage()), HttpStatus.OK);
        }
    }






}
