package au.usyd.elec5619.service.user;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.usyd.elec5619.domain.User;
import au.usyd.elec5619.service.security.EncryptionManager;

/**
 * An implementation of UserManager by using Hiberante as the data persistence layer
 * 
 * @author Yiqing Yang yyan8151
 *
 */

@Service(value="dbUserManager")
@Transactional
public class DbUserManager implements UserManager{

	// Hibernate session factory
	private SessionFactory sessionFactory;
	
	@Resource(name = "encryptionManager")
	EncryptionManager encryptionManager;
	
	@Autowired
	private UserDao userDao;
	
	// logger
	private static final Logger logger = LoggerFactory.getLogger(DbUserManager.class);
	
	// inject dependency
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}
	
	/** 
	 * register the user and return the user id
	 * @param user the user who requires to sign up
	 */
	public int register(User user) {
		// session
		if(userDao.getUserByEmail(user.getEmail()).size()!=0) return -2; // email occupied
		
		// encrypt password
		try {
			user.setPassword(encryptionManager.decrypt(user.getPassword()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setPassword(encryptionManager.md5(user.getPassword()));
		return userDao.saveUser(user);
	}

	/**
	 *  validate the email and password
	 */
	public int login(String email, String password) {
		// find the user
		List<User> users =  userDao.getUserByEmail(email);
		if(users.size()==0) return -1; // no this user
		User user = users.get(0);
		
		// validate the password
		String decodeString;
		try {
			decodeString = encryptionManager.decrypt(password);
			String passwordSalted = encryptionManager.md5(decodeString);
			return (user.getPassword().equals(passwordSalted))? user.getId():-2;  // log in : wrong password
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -3;  // other issues
	}
	
	
	/**
	 *  fetch the information of a user
	 */
	public User getUserInformation(int userId) {
		return userDao.getUserById(userId);
	}
	
	/**
	 *  update the information of a user
	 *  @param user the updated user information
	 */
	public void update(User user) {
		userDao.updateUser(user);
	}


}
