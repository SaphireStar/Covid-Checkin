package au.usyd.elec5619.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class NotificationTest {

	private Notification notification;
	
	@Before
	public void setUp() throws Exception {
		notification = new Notification();
	}

	@Test
	public void test() {
		String testDescrition = "this is for test";
		assertNull(notification.getContent());
		notification.setContent(testDescrition);
		assertNotNull(notification.getContent());
	}
}
