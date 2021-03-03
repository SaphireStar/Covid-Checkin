package au.usyd.elec5619.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.usyd.elec5619.domain.Suburb;

@Service(value="SuburbManager")
@Transactional
public class DatabaseSuburbManager implements SuburbManager {
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}
	
	@Override
	public List<Suburb> getSuburbs() {
		return this.sessionFactory.getCurrentSession().createQuery("FROM Suburb").list();
	}

	@Override
	public void addSuburb(Suburb suburb) {
		this.sessionFactory.getCurrentSession().save(suburb);
	}

	@Override
	public Suburb getSuburbByPostcode(int postcode) {
		Session currentSession = this.sessionFactory.getCurrentSession();
		Suburb suburb = (Suburb) currentSession.get(Suburb.class, postcode);
		return suburb;
	}

	@Override
	public void updateSuburb(Suburb suburb) {
		Session currentSession = this.sessionFactory.getCurrentSession();
		currentSession.merge(suburb);
	}

	@Override
	public void deleteSuburb(int postcode) {
		Session currentSession = this.sessionFactory.getCurrentSession();
		Suburb suburb = (Suburb) currentSession.get(Suburb.class, postcode);
		currentSession.delete(suburb);
	}

}
