package au.usyd.elec5619.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import au.usyd.elec5619.domain.Business;

@Transactional
@Repository(value = "businessDao")
public class BusinessDaoImpl implements BusinessDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Business> getBusinesses() {
		return this.sessionFactory.getCurrentSession().createQuery("FROM Business").list();
	}

	@Override
	public Business getBusinessById(long id) {
		Session currentSession = this.sessionFactory.getCurrentSession();
		Business business = (Business) currentSession.get(Business.class, id);
		return business;
	}

	@Override
	public List<Business> getBusByPostcode(int postcode) {
		return this.sessionFactory.getCurrentSession().createQuery("FROM Business where postcode=?").setInteger(0, postcode).list();
	}

	@Override
	public List<Business> getBusByString(String searchString) {
		return this.sessionFactory.getCurrentSession().createQuery("FROM Business where businessName like :name").setString("name", "%"+searchString+"%").list();
		
	}
}
