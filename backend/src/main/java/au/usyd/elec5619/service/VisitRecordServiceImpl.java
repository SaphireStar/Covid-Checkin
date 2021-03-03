package au.usyd.elec5619.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.usyd.elec5619.domain.Business;
import au.usyd.elec5619.domain.User;
import au.usyd.elec5619.domain.VisitRecord;

@Service(value="visitRecordService")
public class VisitRecordServiceImpl implements VisitRecordService {
	
	@Autowired
	private VisitRecordDao visitRecordDao;
	
	@Override
	@Transactional
	public void insertVisitRecord(Business business, User user) {
		visitRecordDao.insertVisitRecord(business, user);
	}
	
	@Override
	@Transactional
	public List<VisitRecord> getAllVisitRecords() {
		return visitRecordDao.getAllVisitRecords();
	}



	


}
