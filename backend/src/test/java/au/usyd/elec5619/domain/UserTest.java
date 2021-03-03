package au.usyd.elec5619.domain;

import junit.framework.TestCase;

public class UserTest extends TestCase{
	
	private User user;
	
	protected void setUp() throws Exception{
		user = new User();
	}
	
	public void testSetAndGetId() {
		int id = 1;
		user.setId(id);
		assertEquals(id, user.getId());
	}
	
	public void testSetAndGetEmail() {
		String emailString = "test@gmail.com";
		assertNull(user.getEmail());
		user.setEmail(emailString);
		assertEquals(emailString, user.getEmail());
	}
	
	public void testSetAndGetPassword() {
		String password = "password";
		assertNull(user.getPassword());
		user.setPassword(password);
		assertEquals(password, user.getPassword());
	}
	
	public void testSetAndGetFirstName() {
		String firstName = "firstName";
		assertNull(user.getFirstName());
		user.setFirstName(firstName);
		assertEquals(firstName, user.getFirstName());
	}
	
	public void testSetAndGetLastName() {
		String lastName = "lastName";
		assertNull(user.getLastName());
		user.setLastName(lastName);
		assertEquals(lastName, user.getLastName());
	}
	
	public void testSetAndGetAddress() {
		String address = "address";
		assertNull(user.getAddress());
		user.setAddress(address);
		assertEquals(address, user.getAddress());
	}
	
	public void testSetAndGetMobileNumber() {
		String number = "0123456789";
		assertNull(user.getMobileNumber());
		user.setMobileNumber(number);
		assertEquals(number, user.getMobileNumber());
	}
	
	public void testSetAndGetBR() {
		boolean flag = true;
		user.setIsBR(flag);
		assertEquals(flag, user.getIsBR());
		flag = false;
		user.setIsBR(flag);
		assertEquals(flag, user.getIsBR());
	}
	/**
	 * 
	
	@Column(name="isBusinessRep")
	private boolean isBR;
	 */
}
