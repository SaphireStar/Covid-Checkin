package au.usyd.elec5619.service;


import java.util.List;

import au.usyd.elec5619.domain.Business;
import au.usyd.elec5619.domain.User;
import au.usyd.elec5619.domain.VisitRecord;

public interface VisitRecordService{
	
	//insert new visit row into table 
	public void insertVisitRecord(Business business, User user);

	//Get all Businesses
	public List<VisitRecord> getAllVisitRecords();
}
