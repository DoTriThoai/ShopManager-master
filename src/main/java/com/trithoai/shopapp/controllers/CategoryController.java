package com.trithoai.shopapp.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trithoai.shopapp.models.Category;
import com.trithoai.shopapp.models.Distributor;
import com.trithoai.shopapp.models.ErrorCode;
import com.trithoai.shopapp.models.ErrorMessage;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by johncarter on 11/04/2016.
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    private final String URL_DISTRIBUTOR = "http://localhost:8080/api/distributor";
    private final String URL_CATEGORY = "http://localhost:8080/api/category";
    private Gson gson = new Gson();

    @RequestMapping("/distributor/{strDistributorId}/list")
    public String listAll(Model model, @PathVariable String strDistributorId){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        try {
            Integer distributorId = Integer.parseInt(strDistributorId);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL_CATEGORY + "/fetch/distributor/" + strDistributorId, HttpMethod.GET, null, String.class);
            strJson = responseEntity.getBody();
            if (responseEntity.getStatusCode() == HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson, ErrorMessage.class);
                if (errorMessage.getErrorCode() == ErrorCode.SUCCESS){
                    TypeToken<List<Category>> typeToken = new TypeToken<List<Category>>(){};
                    List<Category> lstCategories = gson.fromJson(errorMessage.getContent(), typeToken.getType());
                    model.addAttribute("lstCategories", lstCategories);
                    model.addAttribute("errorMessage", "SUCCESS");
                    model.addAttribute("distributorId", strDistributorId);
                }else{
                    model.addAttribute("errorMessage", errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage", ex.getMessage());
        }
        return "category/list";
    }

    @RequestMapping(value = "/distributor/{strDistributorId}/create",method = RequestMethod.GET)
    public String getCreate(@PathVariable String strDistributorId, Model model){
        try {
            Integer distributorId = Integer.parseInt(strDistributorId);
            model.addAttribute("errorMessage", "NONE");
        }catch (Exception ex){
            model.addAttribute("errorMessage", ex.getMessage());
        }
        return "category/create";
    }

    @RequestMapping(value = "/distributor/{strDistributorId}/create",method = RequestMethod.POST)
    public String postCreate(@PathVariable String strDistributorId, @RequestParam("txtName") String strName, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try{
            Integer distributorId = Integer.parseInt(strDistributorId);
            if (strName.isEmpty() || strName == null){
                model.addAttribute("errorMessage", "Category Name is not empty.");
            }else{
                Distributor distributor = new Distributor(distributorId);
                Category category = new Category(strName, distributor);
                HttpEntity entity = new HttpEntity(category, httpHeaders);
                ResponseEntity<String> responseEntity = restTemplate.exchange(URL_CATEGORY + "/save",HttpMethod.POST, entity, String.class);
                strJson = responseEntity.getBody();
                if (responseEntity.getStatusCode()==HttpStatus.OK){
                    ErrorMessage errorMessage = gson.fromJson(strJson, ErrorMessage.class);
                    if (errorMessage.getErrorCode() == ErrorCode.SUCCESS){
                        TypeToken<Category> typeToken = new TypeToken<Category>(){};
                        category = gson.fromJson(errorMessage.getContent(), typeToken.getType());
                        model.addAttribute("errorMessage", "Create Category success with id="+category.getId());
                    }else{
                        model.addAttribute("errorMessage", errorMessage.getContent());
                    }

                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage", ex.getMessage());
        }
        return "category/create";
    }

    @RequestMapping(value="/edit",method=RequestMethod.GET)
    public String getEditRoot(Model model){
        model.addAttribute("errorMessage","Forbiden");
        return "/city/edit";
    }

    @RequestMapping(value = "/edit/{strId}", method = RequestMethod.GET)
    public String getUpdate(Model model, @PathVariable String strId){
        try {
            Integer id = Integer.parseInt(strId);
            RestTemplate restTemplate = new RestTemplate();
            String strJson;
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL_CATEGORY + "/fetch/" + strId, HttpMethod.GET, null, String.class);
            strJson = responseEntity.getBody();
            if (responseEntity.getStatusCode() == HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson, ErrorMessage.class);
                if (errorMessage.getErrorCode() == ErrorCode.SUCCESS){
                    TypeToken<Category> typeToken = new TypeToken<Category>(){};
                    Category category = gson.fromJson(errorMessage.getContent(), typeToken.getType());
                    responseEntity = restTemplate.exchange(URL_DISTRIBUTOR + "/fetch", HttpMethod.GET, null, String.class);
                    strJson = responseEntity.getBody();
                    ErrorMessage distributorError = gson.fromJson(strJson, ErrorMessage.class);
                    if (distributorError.getErrorCode() == ErrorCode.SUCCESS){
                        TypeToken<List<Distributor>> typeTokenDistributors = new TypeToken<List<Distributor>>(){};
                        List<Distributor> lstDistributor = gson.fromJson(distributorError.getContent(), typeTokenDistributors.getType());
                        model.addAttribute("lstDistributors", lstDistributor);
                        model.addAttribute("errorMessage", "NONE");
                        model.addAttribute("distributorId", category.getDistributor().getId());
                        model.addAttribute("category", category);
                    }else {
                        model.addAttribute("errorMessage", distributorError.getContent());
                    }
                    model.addAttribute("errorMessage","NONE");
                    model.addAttribute("distributor",category);
                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "category/edit";
    }

    @RequestMapping(value = "/edit/{strId}", method = RequestMethod.POST)
    public String postUpdate(@PathVariable String strId, @RequestParam("txtName") String strName,@RequestParam("txtDistributor") String strDistributor, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            if (strName.isEmpty() || strName == null){
                model.addAttribute("errorMessage", "Category Name is not empty.");
            }else{
                Integer id = Integer.parseInt(strId);
                Integer did = Integer.parseInt(strDistributor);
                Distributor distributor = new Distributor(did);
                Category category = new Category(id, strName, distributor);
                HttpEntity entity = new HttpEntity(category, httpHeaders);
                ResponseEntity<String> responseEntity = restTemplate.exchange(URL_CATEGORY + "/save", HttpMethod.POST, entity, String.class);
                strJson = responseEntity.getBody();
                if (responseEntity.getStatusCode() == HttpStatus.OK){
                    ErrorMessage errorMessage = gson.fromJson(strJson, ErrorMessage.class);
                    if (errorMessage.getErrorCode() == ErrorCode.SUCCESS){
                        TypeToken<Category> typeToken = new TypeToken<Category>(){};
                        category = gson.fromJson(errorMessage.getContent(), typeToken.getType());
                        responseEntity = restTemplate.exchange(URL_DISTRIBUTOR + "/fetch", HttpMethod.GET, null, String.class);
                        strJson = responseEntity.getBody();
                        ErrorMessage distributorError = gson.fromJson(strJson, ErrorMessage.class);
                        if (distributorError.getErrorCode() == ErrorCode.SUCCESS){
                            TypeToken<List<Distributor>> typeTokenDistributors = new TypeToken<List<Distributor>>(){};
                            List<Distributor> lstDistributor = gson.fromJson(distributorError.getContent(), typeTokenDistributors.getType());
                            model.addAttribute("lstDistributors", lstDistributor);
                            model.addAttribute("errorMessage", "Update Category success");
                            model.addAttribute("distributorId", category.getDistributor().getId());
                            model.addAttribute("category", category);
                        }else {
                            model.addAttribute("errorMessage", distributorError.getContent());
                        }
                    }else{
                        model.addAttribute("errorMessage", errorMessage.getContent());
                    }
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage", ex.getMessage());
        }
        return "category/edit";
    }

    @RequestMapping(value="/delete",method=RequestMethod.GET)
    @ResponseBody
    public String getDeleteRoot(){
        return "Forbiden";
    }

    @RequestMapping(value="/delete/{strId}",method=RequestMethod.GET)
    @ResponseBody
    public String getDeleteCategory(@PathVariable String strId){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            Integer id = Integer.parseInt(strId);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL_CATEGORY + "/delete/" + strId, HttpMethod.GET, null, String.class);
            strJson = responseEntity.getBody();
            if (responseEntity.getStatusCode() == HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson, ErrorMessage.class);
                return errorMessage.getContent();
            }
        }catch (Exception ex){
            return ex.getMessage();
        }
        return "Forbiden";
    }







}
