package au.usyd.elec5619.service;

import java.io.Serializable;
import java.util.List;

import au.usyd.elec5619.domain.Suburb;

public interface SuburbManager extends Serializable{
    
    public List<Suburb> getSuburbs();
    
    public void addSuburb(Suburb suburb);
    
    public Suburb getSuburbByPostcode(int postcode);
    
    public void updateSuburb(Suburb suburb);
    
    public void deleteSuburb(int postcode);
    
}