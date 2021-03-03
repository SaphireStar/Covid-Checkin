package au.usyd.elec5619.service.user;

import au.usyd.elec5619.domain.User;

/**
 * The interface is for managing the user information. including the way they log in
 * and their privacy.
 * 
 * @author Yiqing Yang yyan8151
 *
 */

public interface UserManager {

	// register the user and return the user id
	public int register(User user);

	// validate the email and password
	public int login(String email, String password);
	
	// fetch the information of a user
	public User getUserInformation(int id);
	
	// update the information of a user
	public void update(User user);
}
