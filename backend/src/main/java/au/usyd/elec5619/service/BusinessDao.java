package au.usyd.elec5619.service;

import java.io.Serializable;

import java.util.List;

import au.usyd.elec5619.domain.Business;

public interface BusinessDao extends Serializable{

    //Get all Businesses
    public List<Business> getBusinesses();
    
    //Get one Business
    public Business getBusinessById(long id);
   
    //Get by PostCode
    public List<Business> getBusByPostcode(int postcode);
    
    //Get by String
    public List<Business> getBusByString(String searchString);
}