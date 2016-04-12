package com.trithoai.shopapp.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
@RequestMapping("/distributor")
public class DistributorController {
    private final String URL = "http://localhost:8080/api/distributor";
    private Gson gson = new Gson();

    @RequestMapping("/list")
    public String listAll(Model model){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/fetch", HttpMethod.GET, null, String.class);
            strJson = responseEntity.getBody();
            if (responseEntity.getStatusCode() == HttpStatus.OK){
                ErrorMessage  errorMessage = gson.fromJson(strJson, ErrorMessage.class);
                if (errorMessage.getErrorCode()== ErrorCode.SUCCESS){
                    TypeToken<List<Distributor>>token = new TypeToken<List<Distributor>>(){};
                    List<Distributor> lstDistributors =gson.fromJson(errorMessage.getContent(), token.getType());
                    model.addAttribute("errorMessage", "SUCCESS");
                    model.addAttribute("lstDistributors", lstDistributors);
                }else{
                    model.addAttribute("errorMessage", errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "distributor/list";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getCreate(Model model){
        model.addAttribute("errorMessage","NONE");
        return "distributor/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String postCreate(@RequestParam("txtName") String strName, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            if (strName.isEmpty() || strName==null){
                model.addAttribute("errorMessage", "Distributor Name is not empty.");
            }else{
                Distributor distributor = new Distributor(strName);
                HttpEntity entity = new HttpEntity(distributor, httpHeaders);
                ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/save", HttpMethod.POST, entity, String.class);
                strJson = responseEntity.getBody();
                if (responseEntity.getStatusCode() == HttpStatus.OK){
                    ErrorMessage errorMessage = gson.fromJson(strJson, ErrorMessage.class);
                    if (errorMessage.getErrorCode() == ErrorCode.SUCCESS){
                        TypeToken<Distributor> typeToken = new TypeToken<Distributor>(){};
                        distributor = gson.fromJson(errorMessage.getContent(), typeToken.getType());
                        model.addAttribute("errorMessage","Create success distributor with id = " + distributor.getId());
                    }else{
                        model.addAttribute("errorMessage", errorMessage.getContent());
                    }
                }
            }

        }catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "distributor/create";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String getUpdateRoot(Model model){
        model.addAttribute("errorMessage","Forbiden");
        return "distributor/edit";
    }

    @RequestMapping(value = "/edit/{strId}", method = RequestMethod.GET)
    public String getUpdate(Model model, @PathVariable String strId){
        try {
            Integer id = Integer.parseInt(strId);
            RestTemplate restTemplate = new RestTemplate();
            String strJson;
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/fetch/" + strId, HttpMethod.GET, null, String.class);
            strJson = responseEntity.getBody();
            if (responseEntity.getStatusCode() == HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson, ErrorMessage.class);
                if (errorMessage.getErrorCode() == ErrorCode.SUCCESS){
                    TypeToken<Distributor> typeToken = new TypeToken<Distributor>(){};
                    Distributor distributor = gson.fromJson(errorMessage.getContent(), typeToken.getType());

                    model.addAttribute("errorMessage","NONE");
                    model.addAttribute("distributor",distributor);
                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "distributor/edit";
    }

    @RequestMapping(value = "edit/{strId}", method = RequestMethod.POST)
    public String postUpdate(@PathVariable String strId,@RequestParam("txtName") String strName, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            if (strName.isEmpty() || strName == null){
                model.addAttribute("errorMessage", "Distributor Name is not empty.");
            }else{
                Integer id = Integer.parseInt(strId);
                Distributor distributor = new Distributor(id, strName);
                HttpEntity entity = new HttpEntity(distributor, httpHeaders);
                ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/save", HttpMethod.POST, entity, String.class);
                strJson = responseEntity.getBody();
                if (responseEntity.getStatusCode() == HttpStatus.OK){
                    ErrorMessage errorMessage = gson.fromJson(strJson, ErrorMessage.class);
                    if (errorMessage.getErrorCode() == ErrorCode.SUCCESS){
                        TypeToken<Distributor> typeToken = new TypeToken<Distributor>(){};
                        distributor = gson.fromJson(errorMessage.getContent(), typeToken.getType());
                        model.addAttribute("distributor", distributor);
                        model.addAttribute("errorMessage", "Update Distributor success");
                    }else{
                        model.addAttribute("errorMessage", errorMessage.getContent());
                    }
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage", ex.getMessage());
        }
        return "distributor/edit";
    }

    @RequestMapping(value="/delete",method=RequestMethod.GET)
    @ResponseBody
    public String getDeleteRoot(){
        return "Forbiden";
    }

    @RequestMapping(value="/delete/{strId}",method=RequestMethod.GET)
    @ResponseBody
    public String getDeleteDistributor(@PathVariable String strId){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            Integer id = Integer.parseInt(strId);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/delete/" + strId, HttpMethod.GET, null, String.class);
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
