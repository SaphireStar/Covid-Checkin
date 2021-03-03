package au.usyd.elec5619.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import au.usyd.elec5619.domain.Business;
import au.usyd.elec5619.domain.User;
import au.usyd.elec5619.domain.VisitRecord;

public interface VisitRecordDao extends Serializable{
	
	//insert new visit row into table 
	public void insertVisitRecord(Business business, User user);
	
	
	
    //Get all Businesses
	public List<VisitRecord> getAllVisitRecords();
	
	public List<VisitRecord> getRecordBetweenDate(long businessId, Date startDate, Date endDate);
}
