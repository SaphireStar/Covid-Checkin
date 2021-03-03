package au.usyd.elec5619.service.user;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import au.usyd.elec5619.domain.User;

/**
 * User Dao Interface
 * @author Yiqing Yang yyan8151
 *
 */

@Repository
public interface UserDao extends Serializable{
	
	/**
	 * Saves a new user to the table
	 * @param user a new user
	 */
	public int saveUser(User user);
	
	/**
	 * Fetches the user by email
	 * @param email user email
	 * @return the user with the email
	 */
	public List<User> getUserByEmail(String email);
	
	/**
	 * Fetches the user by id
	 * @param id user id
	 * @return the user with this id
	 */
	public User getUserById(int id);
	
	/**
	 * Updates the user info in the database
	 * @param user updated user
	 */
	public void updateUser(User user);
	
}
