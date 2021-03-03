package au.usyd.elec5619.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import au.usyd.elec5619.domain.Business;
import au.usyd.elec5619.domain.User;
import au.usyd.elec5619.domain.VisitRecord;

@Repository(value = "visitRecordDao")
@Transactional
public class VisitRecordDaoImpl implements VisitRecordDao  {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void insertVisitRecord(Business business, User user) {
		VisitRecord visit = new VisitRecord();
		visit.setBusiness(business);
		visit.setCustomer(user);
		this.sessionFactory.getCurrentSession().save(visit);
	}

	@Override
	public List<VisitRecord> getAllVisitRecords() {
		return this.sessionFactory.getCurrentSession().createQuery("FROM VisitRecord").list();
	}
	
	@Override
	public List<VisitRecord> getRecordBetweenDate(long businessId, Date startDate, Date endDate) {
		List<VisitRecord> list = this.sessionFactory.getCurrentSession().createQuery("FROM VisitRecord where date>:startDate and date<:endDate")
				.setTimestamp("startDate", startDate).setTimestamp("endDate", endDate).list();
		List<VisitRecord> res = new ArrayList();
		for(VisitRecord record:list) {
			if(record.getBusiness().getId()==businessId) {
				res.add(record);
			}
		}
		return res;
	}

	

}
