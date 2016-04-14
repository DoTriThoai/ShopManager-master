package com.trithoai.shopapp.ajaxmodel;

/**
 * Created by johncarter on 13/04/2016.
 */
public class AjaxCategory {
    private String name;
    private Integer distributorId;
    private Integer distributorIdMaster;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Integer getDistributorIdMaster() {
        return distributorIdMaster;
    }

    public void setDistributorIdMaster(Integer distributorIdMaster) {
        this.distributorIdMaster = distributorIdMaster;
    }
}
