package com.trithoai.shopapp.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trithoai.shopapp.ajaxmodel.AjaxCategory;
import com.trithoai.shopapp.models.Category;
import com.trithoai.shopapp.models.Distributor;
import com.trithoai.shopapp.models.ErrorCode;
import com.trithoai.shopapp.models.ErrorMessage;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by johncarter on 14/04/2016.
 */
@RestController
@RequestMapping("/categoryrest")
public class CategoryRestController {
    private final String URL_DISTRIBUTOR = "http://localhost:8080/api/distributor";
    private final String URL_CATEGORY = "http://localhost:8080/api/category";
    private Gson gson = new Gson();


    @RequestMapping(value = "/ajax/create")
    public String deleteCategory(@RequestBody AjaxCategory category){

        if (isValidCategory(category)){
            RestTemplate restTemplate = new RestTemplate();
            String strJson;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            try {
                Distributor distributor = new Distributor(category.getDistributorId());
                Category categoryControl = new Category(category.getName(), distributor);
                HttpEntity entity = new HttpEntity(categoryControl, httpHeaders);
                ResponseEntity<String> responseEntity = restTemplate.exchange(URL_CATEGORY + "/save", HttpMethod.POST, entity, String.class);
                strJson = responseEntity.getBody();

                if (responseEntity.getStatusCode() == HttpStatus.OK){
                    ErrorMessage errorMessage = gson.fromJson(strJson, ErrorMessage.class);
                    if (errorMessage.getErrorCode() == ErrorCode.SUCCESS){
                        //thuc hien thanh cong
                        responseEntity = restTemplate.exchange(URL_CATEGORY + "/fetch/distributor/" + category.getDistributorIdMaster(), HttpMethod.GET, null, String.class);
                        strJson = responseEntity.getBody();
                        if (responseEntity.getStatusCode() == HttpStatus.OK){
                            ErrorMessage errorMessageCategories = gson.fromJson(strJson, ErrorMessage.class);
                            if (errorMessageCategories.getErrorCode() == ErrorCode.SUCCESS){
                                TypeToken<List<Category>> typeToken = new TypeToken<List<Category>>(){};
                                List<Category> lstCategories = gson.fromJson(errorMessageCategories.getContent(), typeToken.getType());
                                return new Gson().toJson(lstCategories);

                            }else{
                                return null;
                            }
                        }
                    }else{
                        //thuc hien that bai
                        return "ERROR";
                    }
                }

            }catch (Exception ex){
                return null;
            }
        }

        return null;
    }

    private boolean isValidCategory(AjaxCategory category) {

        boolean valid = true;

        if (category == null) {
            valid = false;
        }

        if ((StringUtils.isEmpty(category.getName())) && (StringUtils.isEmpty(category.getDistributorId()))&& (StringUtils.isEmpty(category.getDistributorIdMaster()))) {
            valid = false;
        }
//        Check isInteger
//        try{
//            Integer.parseInt(category.getDistributorId());
//        }catch (NumberFormatException e){
//            valid = false;
//        }catch (NullPointerException e){
//            valid = false;
//        }

        return valid;
    }
}
