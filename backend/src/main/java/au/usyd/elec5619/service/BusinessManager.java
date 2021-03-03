package au.usyd.elec5619.service;

import java.io.Serializable;
import java.util.List;

import au.usyd.elec5619.domain.Business;

public interface BusinessManager extends Serializable{
    
    public List<Business> getBusinesses();
    
    public List<Business> getUserBusinesses(int id);
    
    public void addBusiness(Business business);
    
    public Business getBusinessById(long id);
    
    public void updateBusiness(Business business);
    
    public void deleteBusiness(long id);

	void updateBusinessPhoto(long id, String file);
    
}