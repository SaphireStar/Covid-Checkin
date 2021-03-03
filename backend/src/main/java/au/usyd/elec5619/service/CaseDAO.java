package au.usyd.elec5619.service;


import java.util.Date;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import au.usyd.elec5619.domain.CovidCases;
import au.usyd.elec5619.domain.Notification;

@Repository
@Transactional
public class CaseDAO {

	
	@Resource
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	// Insert
	public int addCase(CovidCases case1) {
		return (Integer) this.getSession().save(case1);
	}	
	
	// Select
	public CovidCases getCaseById(int id){
		return (CovidCases) this.sessionFactory.getCurrentSession().createQuery("FROM CovidCases where id=?").setInteger(0, id).uniqueResult();
	}
	
	public long getCaseCount(){
		return (Long) this.sessionFactory.getCurrentSession()
				.createQuery("SELECT count(*) FROM CovidCases").uniqueResult();
	}
	
	public int getNewestCaseId(){
		return (Integer) this.sessionFactory.getCurrentSession()
				.createQuery("SELECT max(c.id) FROM CovidCases c").uniqueResult();
	}
	
}
