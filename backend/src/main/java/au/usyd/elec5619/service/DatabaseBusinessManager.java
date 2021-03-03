package au.usyd.elec5619.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.usyd.elec5619.domain.Business;
import au.usyd.elec5619.domain.Suburb;

@Service(value="BusinessManager")
@Transactional
public class DatabaseBusinessManager implements BusinessManager {
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}
	
	@Override
	public void addBusiness(Business business) {
		this.sessionFactory.getCurrentSession().save(business);
	}
	
	@Override
	public Business getBusinessById(long id) {
		Session currentSession = this.sessionFactory.getCurrentSession();
		Business business = (Business) currentSession.get(Business.class, id);
		return business;
	}
	
	@Override
	public void updateBusiness(Business business) {
		Session currentSession = this.sessionFactory.getCurrentSession();
		Business businessOrginal = (Business) currentSession.get(Business.class, business.getId());
		business.setPhoto(businessOrginal.getPhoto());
		currentSession.merge(business);
	}
	
	
	@Override
	public void deleteBusiness(long id) {
		Session currentSession = this.sessionFactory.getCurrentSession();
		Business business = (Business) currentSession.get(Business.class, id);
		if (business != null) {
			currentSession.delete(business);
		}
	}
	
	@Override
	public void updateBusinessPhoto(long id, String file) {
		Session currentSession = this.sessionFactory.getCurrentSession();
		Business business = (Business) currentSession.get(Business.class, id);
		if (business != null) {
			business.setPhoto(file);
			currentSession.merge(business);
		}
	}
	
	@Override
	public List<Business> getBusinesses() {
		return this.sessionFactory.getCurrentSession().createQuery("FROM Business").list();
	}

	@Override
	public List<Business> getUserBusinesses(int id) {
		return this.sessionFactory.getCurrentSession().createQuery("FROM Business WHERE businessRepId ="+id).list();
	}

}
