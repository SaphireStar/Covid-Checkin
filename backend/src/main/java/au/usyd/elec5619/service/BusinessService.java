package au.usyd.elec5619.service;

import java.util.List;

import au.usyd.elec5619.domain.Business;

public interface BusinessService {

	 //Get all Businesses
    public List<Business> getBusinesses();
    
    //Get one Business
    public Business getBusinessById(long id);
   
    
}
