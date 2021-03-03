package au.usyd.elec5619.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.usyd.elec5619.service.BusinessDao;
import au.usyd.elec5619.service.CaseDAO;
import au.usyd.elec5619.service.user.DbUserManager;
import au.usyd.elec5619.service.user.DbVisitRecordManager;
import au.usyd.elec5619.domain.Business;
import au.usyd.elec5619.domain.CovidCases;
import au.usyd.elec5619.domain.Notification;
import au.usyd.elec5619.domain.User;
import au.usyd.elec5619.domain.VisitRecord;

import org.springframework.beans.factory.annotation.Autowired;

/*
* This is a unit test to test NotificationService.
* The name follows the method int NotificationService.
* @author Yonghe Tan
* */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "../../../../servlet-context.xml",
"../../../../persistence-context.xml",
"../../../../spring-mail.xml"})
public class NotificationServiceTest {
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private DbUserManager userDAO;
	
	@Autowired
	private VisitRecordDao visitDAO;
	
	@Autowired
	private CaseDAO caseDAO;
	
	@Autowired
	private BusinessDao businessDAO;
	
	@Autowired
	private BusinessManager businessManager;
	
	private User user;
	
	private Business business;
	
	/*
	 * Setup basis condition to run test.
	 * */
	@Before
	public void setUp() throws Exception {
		user = userDAO.getUserInformation(1);
		if(user == null) {
			user = new User();
			user.setAddress("abc");
			user.setEmail("709387417@qq.com");
			user.setFirstName("FirstName");
			user.setLastName("TestUser");
			user.setMobileNumber("123");
			user.setIsBR(true);
			user.setPassword("123");
			
			userDAO.register(user);
		}
		user =  userDAO.getUserInformation(1);
		
		if(businessManager.getBusinessById(1)==null) {
			business = new Business();
			business.setBusinessName("Apple");
			business.setPostcode(2031);
			business.setAddress("Apple address, Shopping center, NSW 2031");
			businessManager.addBusiness(business);
		}
		business = businessManager.getBusinessById(1);
		
	}
	
	
	/*
	 * This is a unit test to stimulate a new case occur. 
	 * And System is doing schedule task, then notify user.
	 * Please comment out the schedule before run this test. 
	 * */
	@Test
	public void testStimulateGetNewCaseAndSysNotify()
			throws Exception, IOException {
		boolean thrown = true;
		try {
			user = userDAO.getUserInformation(1);
			VisitRecord record = new VisitRecord();
			record.setBusiness(business);
			record.setCustomer(user);
			record.setDate(new Date());
			visitDAO.insertVisitRecord(business, user);
			CovidCases case1 = new CovidCases();
			case1.setAddress("Test Address");
			case1.setCreateTime(new Date());
			case1.setFoundDate("Test FoundDate");
			case1.setPostcode(business.getPostcode());
			String publishDate = caseDAO.getCaseById(caseDAO.getNewestCaseId()).getPublishDate();
			case1.setPublishDate(publishDate);
			case1.setSuburb("Test Suburb");
			case1.setTime("From 9:00 to 15:00");
			case1.setVenue("Test venue");
			caseDAO.addCase(case1);
			notificationService.dataInspectTask();
		} catch (Exception e) {
			thrown = false;
		}
		assertTrue(thrown);
	}
	
	/*
	 * Test update case data.
	 * Please comment out the schedule before run this test. 
	 * */
	@Test
	public void testUpdateCase()
			throws Exception, IOException {
		boolean thrown = true;
		try {
			notificationService.updateCase();
		} catch (Exception e) {
			thrown = false;
		}
		assertTrue(thrown);
	}
	
	/*
	 * Test system checks new case.
	 * Please comment out the schedule before run this test. 
	 * */
	@Test
	public void testInspectNotification()
			throws Exception, IOException {
		boolean thrown = true;
		try {
			notificationService.dataInspectTask();
		} catch (Exception e) {
			thrown = false;
		}
		assertTrue(thrown);
	}
	
	@Test
	public void testSendMessages() {
		user = userDAO.getUserInformation(1);
		List<Map<String, Object>> list = new ArrayList();
		VisitRecord record = new VisitRecord();
		record.setBusiness(business);
		record.setId(1);
		record.setCustomer(user);
		record.setDate(new Date());
		Map<String,Object> map = new HashMap();
		map.put("user", user);
		map.put("record", record);
		boolean thrown = true;
		list.add(map);
		try {
			notificationService.sendMessages(list,-1, 1, "test sendWebNotifications", 0);
		} catch (Exception e) {
			thrown = false;
		}
		assertTrue(thrown);
	}
	
	@Test
	public void testSendBusinessNotification() {
		boolean thrown = true;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start = simpleDateFormat.parse("2000-09-02 17:00:00");
			Date end = simpleDateFormat.parse("2021-01-02 17:00:00");
			notificationService.sendBusinessNotification(1, start, end, "test send business Notification!");
		} catch (Exception e) {
			thrown = false;
		}
		assertTrue(thrown);
	}
	
	@Test
	public void testSendEmails() {
		boolean thrown = true;
		user = userDAO.getUserInformation(1);
		List<User> list = new ArrayList();
		list.add(user);
		try {
			notificationService.sendEmails(list, "test", "This is for test.   --testMonkey.");
		} catch (Exception e) {
			thrown = false;
		}
		assertTrue(thrown);
	}
	
	@Test
	public void testReadDetail() {
		List<Notification> list = notificationService.readDetails(1, 1);
		assertNotNull(list);
	}
	
	@Test
	public void testReadBussinessNotifications() {
		List<Map<String, Object>> list = notificationService.readSendHistory(1);
		assertNotNull(list);
	}
	
	@Test
	public void testReadUserNotifications() {
		List<Notification> list = notificationService.readUserNotifications(1);
		assertNotNull(list);
	}
	
	@Test
	public void testHello() {
		System.out.println("hello world!");
	}
}
