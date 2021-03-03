package au.usyd.elec5619.service.user;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.usyd.elec5619.domain.Business;
import au.usyd.elec5619.domain.CovidCases;
import au.usyd.elec5619.domain.Notification;
import au.usyd.elec5619.domain.Record;
import au.usyd.elec5619.domain.Suburb;
import au.usyd.elec5619.domain.User;
import au.usyd.elec5619.domain.VisitRecord;
import au.usyd.elec5619.service.BusinessDao;
import au.usyd.elec5619.service.NotificationDAO;
import au.usyd.elec5619.service.VisitRecordDao;

/**
 * Gets the visit history and detects risks
 * @author Yiqing Yang yyan8151
 *
 */
@Service(value="dbVisitRecordManager")
@Transactional
public class DbVisitRecordManager implements VisitRecordManager{
	
	// Daos
	@Autowired
	private BusinessDao businessDao;
	
	@Autowired
	private VisitRecordDao visitRecordDao;
	
	@Autowired
	private NotificationDAO notificationDAO;
	
	
	//TODO
	public List<Record> getVisitRecord(int userId) {
		// fetch required information
		List<VisitRecord> recordsLists = visitRecordDao.getAllVisitRecords();
		List<Business> businessesList = businessDao.getBusinesses();
		List<Notification> notificationList = notificationDAO.getNotification();
		
		// return value
		List<Record> records = new ArrayList<Record>();
		
		// find all records belong to the user 
		List<VisitRecord> userVisitRecords  = new ArrayList<VisitRecord>();
		for(VisitRecord visitRecord : recordsLists) {
			if(visitRecord.getCustomer().getId() == userId) {
				userVisitRecords.add(visitRecord);
			}
		}
		
		// find all infected visitrecord
		List<VisitRecord> infectedVisitRecords  = new ArrayList<VisitRecord>();
		for(VisitRecord visitRecord : recordsLists) {
			for(Notification notification:notificationList) {
				if(notification.getRecordId() == visitRecord.getId()) {
					infectedVisitRecords.add(visitRecord);
				}
			}
		}
		
		for(VisitRecord userVR: userVisitRecords) {
			Record record = new Record();
			// set the check-in time
			record.setCheckInTimeDate(userVR.getDate());
			
			// set the risk
			long  busId= userVR.getBusiness().getId();
			Date timeDate = userVR.getDate();
			record.setRisk(false);
			for(VisitRecord visitRecord: infectedVisitRecords) {
				if(visitRecord.getBusiness().getId() == userVR.getBusiness().getId()) {
					Date caseTimeDate = visitRecord.getDate();
					long duration = Math.abs(caseTimeDate.getTime() - timeDate.getTime());
					if(duration<=3600000) {// 1 hour
						record.setRisk(true);
						break;
					}
				}
			}
			
			// set business name
			for(Business business:businessesList) {
				if(business.getId() == userVR.getBusiness().getId()) {
					record.setBusinessName(business.getBusinessName());
					break;
				}
			}
			// add to the form
			records.add(record);
		}
		
		return records;
	}
}
