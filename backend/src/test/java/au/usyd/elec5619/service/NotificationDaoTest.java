package au.usyd.elec5619.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import au.usyd.elec5619.domain.Business;
import au.usyd.elec5619.domain.Notification;
import au.usyd.elec5619.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "../../../../servlet-context.xml",
"../../../../persistence-context.xml",
"../../../../spring-mail.xml"})
public class NotificationDaoTest{
	
	@Autowired
	private NotificationDAO notificationDAO;
	
	
	@Test
	public void testAddNotification() {
		Notification noti = new Notification();
		noti.setBussinessId(1);
		noti.setStatus(0);
		noti.setType(0);
		noti.setUserId(1);
		noti.setContent("test insert");
		noti.setCaseId(1);
		noti.setCreateTime(new Date());
		assertEquals(notificationDAO.addNotification(noti), noti.getId());
		
	}
	
	@Test
	public void testGetNotificationByUserId() {
		List<Notification> notifications = notificationDAO.getNotificationByUserId(1);
		assertNotNull(notifications);
	}
	
	@Test
	public void testGetDetail() {
		List<Notification> notifications = notificationDAO.getNotiDetail(1, 1);
		assertNotNull(notifications);
	}
	
	@Test
	public void testGetNewestNotification() {
		Notification noti = notificationDAO.getNewestNotification(1);
		assertNotNull(noti);
	}
	
	@Test
	public void testGetUnreadCount() {
		Long unread = notificationDAO.getUnreadCount(1, 1);
		assertNotNull(unread);
	}
	
	@Test
	public void testUpdateNotification() {
		boolean thrown = true;
		try {
			List<Notification> notis = notificationDAO.getNotificationByBussinessId(1);
			Notification noti = notis.get(0);
			noti.setContent("test update");
			notificationDAO.updateNotification(noti);
		} catch (Exception e) {
			thrown = false;
		}
		assertTrue(thrown);
	}
	
	@Test
	public void testDeleteNotificationById() {
		notificationDAO.deleteNotificationById(1);
		assertNull(notificationDAO.getNotificationById(1));
	}
}
