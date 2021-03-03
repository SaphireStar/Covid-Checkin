package au.usyd.elec5619.service.user;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import au.usyd.elec5619.domain.User;

/**
 * User Dao implementation
 * @author Yiqing Yang yyan8151
 *
 */
@Transactional
@Repository(value = "userDao")
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public int saveUser(User user) {
		Session currentSession = this.sessionFactory.getCurrentSession();
		currentSession.save(user);
		return user.getId();
	}

	@Override
	public List<User> getUserByEmail(String email) {
		Session currentSession = this.sessionFactory.getCurrentSession();
		List<User> users =  currentSession.createQuery("from User where userEmail = '"+email+"'").list();
		return users;
	}

	@Override
	public User getUserById(int id) {
		// session
		Session currentSession = this.sessionFactory.getCurrentSession();
		// fetch the user by id
		User user = (User) currentSession.get(User.class, id);
		return user;
	}

	@Override
	public void updateUser(User user) {
		Session currentSession = this.sessionFactory.getCurrentSession();
		// fetch the user in database by id
		User userDUser = (User) currentSession.load(User.class, user.getId());
		//update user
		userDUser.setAddress(user.getAddress());
		userDUser.setFirstName(user.getFirstName());
		userDUser.setLastName(user.getLastName());
		userDUser.setMobileNumber(user.getMobileNumber());
		currentSession.update(userDUser);
	}
	
	

}
