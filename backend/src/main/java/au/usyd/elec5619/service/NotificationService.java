package au.usyd.elec5619.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import au.usyd.elec5619.service.BusinessDao;
import au.usyd.elec5619.service.CaseDAO;
import au.usyd.elec5619.service.NotificationDAO;
import au.usyd.elec5619.service.user.DbVisitRecordManager;
import au.usyd.elec5619.service.user.UserManager;
import au.usyd.elec5619.service.user.VisitRecordManager;
import au.usyd.elec5619.domain.Notification;
import au.usyd.elec5619.domain.User;
import au.usyd.elec5619.domain.VisitRecord;
import au.usyd.elec5619.domain.CovidCases;
import au.usyd.elec5619.domain.Business;
import au.usyd.elec5619.util.MailClient;
import au.usyd.elec5619.util.MessageThreadPool;

/**
 * Implement NotificationManager. 
 * Include two scheduling task, notification creation, email send, notification read.
 * 
 * @author Yonghe Tan
 */
@Service(value = "notificationManager")
@EnableScheduling
public class NotificationService implements NotificationManager {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	@Autowired
	private MailClient mailClient;
	
	@Autowired
	private MessageThreadPool messageThreadPool;
	
	@Autowired
	private NotificationDAO notificationDAO;
	
	// user manager
	@Resource(name="dbUserManager")
	private UserManager userManager;
	// visit record manager
	@Autowired
	private VisitRecordDao visitRecordDao;
	
	@Autowired
	private CaseDAO caseDAO;
	
	@Autowired
	private BusinessDao bussinessDAO;
	
	/*
	 * Schedule task to update new case.
	 * */
	@Scheduled(fixedDelay = 1000*60*30)
	public void updateCase() throws Exception{
		logger.info("Start inspect new case--");
		RestTemplate template = new RestTemplate();
		String url = "https://data.nsw.gov.au/data/dataset/0a52e6c1-bc0b-48af-8b45-d791a6d8e289/resource/f3a28eed-8c2a-437b-8ac1-2dab3cf760f9/download/venue-data-2020-oct-24.json";
		String json = template.getForObject(url, String.class);
		JSONObject obj = JSONObject.parseObject(json);
		String publishDate = obj.getString("date");
		if(caseDAO.getCaseCount()== 0 || !publishDate.equals(caseDAO.getCaseById(caseDAO.getNewestCaseId()).getPublishDate())) {
			logger.info("New case upload!");
			String records = obj.getJSONObject("data").getString("monitor");
			List<Map<String,Object>> listMap = JSON.parseObject(records,new TypeReference<List<Map<String, Object>>>(){});
			for(Map<String,Object> map: listMap) {
				CovidCases newCase = new CovidCases();
				String address = (String)map.get("Address");
				int postcode = Integer.parseInt(address.substring(address.length()-4));
				newCase.setAddress(address);
				newCase.setPostcode(postcode);
				newCase.setCreateTime(new Date());
				newCase.setFoundDate((String)map.get("Date"));
				newCase.setPublishDate(publishDate);
				newCase.setSuburb((String)map.get("Suburb"));
				newCase.setTime((String)map.get("Time"));
				newCase.setVenue((String)map.get("Venue"));
				caseDAO.addCase(newCase);
			}
			logger.info("Finish download! There are " + listMap.size() + " new cases!");
		}
    }
	
	/*
	 * Schedule task to send system notification with COVID-19 new Cases.
	 * */
	@Scheduled(fixedDelay = 1000*60*30)
	public void dataInspectTask() throws Exception{
		logger.info("Start inspect new notification:");
		long caseCount = caseDAO.getCaseCount();
		long sysCount = notificationDAO.getSysNotificationCount();
		if(caseCount!=0) {
			int NewestCaseId = caseDAO.getNewestCaseId();
		    if(sysCount == 0 || notificationDAO.getNewestCaseId() < NewestCaseId) {
		        sendSystemNotification(sysCount == 0? 0 : notificationDAO.getNewestCaseId(), NewestCaseId);
		    }
		}else {
			logger.info("There is not enough case records in database to inspect!");
		}
    }
	
	
	@Override
	public void sendSystemNotification(int NotifyCaseId, int NewestCaseId) throws Exception {
		while(NotifyCaseId != NewestCaseId) {
			int caseId = ++NotifyCaseId;
			CovidCases case1 = caseDAO.getCaseById(caseId);
			if(case1==null) {
				continue;
			} 

			Date startDate = new Date(case1.getCreateTime().getTime() - 15 * 24 * 60 * 60 * 1000);
			Date endDate = new Date(case1.getCreateTime().getTime() + 60 * 60 * 1000);
	
			List<Business> busList = bussinessDAO.getBusByPostcode(case1.getPostcode());
			if(busList.size()!=0) {
				logger.info("Find Match businesses, Start to send SysNotifications!");
			}
			Set<Integer> userSet = new HashSet();
			for(Business bus: busList) {
				List<VisitRecord> records = visitRecordDao.getRecordBetweenDate(bus.getId(), startDate, endDate);
				List<User> users = new ArrayList();
				List<Map<String, Object>> list = new ArrayList();
				for(VisitRecord record: records) {
					User user = userManager.getUserInformation(record.getCustomer().getId());
					if(userSet.contains(user.getId())) {
						continue;
					}
					Map<String, Object> map = new HashMap();
					map.put("user", user);
					map.put("record", record);
					users.add(user);
					userSet.add(user.getId());
					list.add(map);
				}
				
				StringBuilder helper = new StringBuilder();
				helper.append("Covid-19 Case was found in ")
					.append(case1.getVenue())
					.append(" (" + case1.getAddress() + ") ")
					.append(" during ")
					.append(case1.getTime())
					.append(" in ")
					.append(case1.getFoundDate())
					.append(". You have been visited ")
					.append(bus.getBusinessName()).append(" recently. Please aware of it.");
				String content = helper.toString();
				sendMessages(list, -1, 0, content, case1.getId());
				sendEmails(users,"Covid-19 Potential Contact Alert!", content);
			}
			
		}
	}
	
	
	@Override
	public int sendBusinessNotification(long bussinessId, Date startDate, Date endDate, String content) throws Exception {

		Business business = bussinessDAO.getBusinessById(bussinessId); 

		List<VisitRecord> records = visitRecordDao.getRecordBetweenDate(bussinessId, startDate, endDate);
		if(records.size()==0) {
			logger.info("No Records during that time!");
			return 0;
		}
		List<User> users = new ArrayList();
		List<Map<String, Object>> list = new ArrayList();
		Set<Integer> userSet = new HashSet();
		for(VisitRecord record: records) {
			User user = userManager.getUserInformation(record.getCustomer().getId());
			if(userSet.contains(user.getId())){
				continue;
			}
			Map<String, Object> map = new HashMap();
			map.put("user", user);
			map.put("record", record);
			userSet.add(user.getId());
			list.add(map);
		}
		if(userSet.size()==0) {
			return 0;
		}
		Notification notification = new Notification();
		notification.setBussinessId(bussinessId);
		notification.setStatus(0);
		notification.setType(1);
		notification.setContent(content);
		notification.setCreateTime(new Date());
		int nId = notificationDAO.addNotification(notification);
		sendMessages(list, nId,  bussinessId, content, 0);
		return 1;
	}
	
	@Override
	public void sendEmails(List<User> list, String subject, String content) {
		if(list == null) {
			return;
		}
		for(User user : list) {
			mailClient.sendMail(user.getEmail(), subject
					, "Dear " + user.getLastName() + ", " + content + 
					" See more details: http://localhost:8080/elec5619/notification/user/" + user.getId() + "!");
		}
	}


	@Override
	public void sendMessages(List<Map<String, Object>> list, int nId, long bussinessId, String content, int caseId) throws InterruptedException {
		if(list == null) {
			return;
		}
		messageThreadPool.sendMessages(list, nId,  bussinessId, content, caseId);
	}


	@Override
	public List<Map<String, Object>> readSendHistory(long bussinessId) {
		List<Notification> notis = notificationDAO.getNotificationByBussinessId(bussinessId);
		List<Map<String, Object>> notiList = new ArrayList();
		for(Notification noti: notis) {
			if(noti.getType() != 1) {
				continue;
			}
			Map<String, Object> map = new HashMap();
			map.put("createTime", noti.getCreateTime().toString().substring(0, 19));
			map.put("content", noti.getContent());
			map.put("status", noti.getStatus());
			map.put("nId", noti.getId());
			notiList.add(map);
		}
		return notiList;
	}

	@Override
	public List<Notification> readUserNotifications(int userId) {
		return notificationDAO.getNotificationByUserId(userId);
	}

	@Override
	public List<Notification> readDetails(long businessId, int userId) {
		List<Notification> notis = notificationDAO.getNotiDetail(businessId, userId);
		for(Notification noti : notis) {
			if(noti.getStatus()==0) {
				noti.setStatus(1);
			}
			notificationDAO.updateNotification(noti);
		}
		
		return notis;
	}

	@Override
	public Map<String, Object> readNewestNotification(int userId) {
		List<Notification> notis = notificationDAO.getNotificationByUserId(userId);
		Map<String, Object> map = new HashMap();
		if(notis.size()==0||notis.get(0).getStatus() == 1) {
			map.put("code", 0);
		}else {
			Notification noti = notis.get(0);
			noti.setStatus(1);
			notificationDAO.updateNotification(noti);
			String sysContent = "You may contact with a covid-19 carrier!Click to see more details!";
			map.put("code", 1);
			map.put("name", noti.getBussinessId()==0?"System Alert":bussinessDAO.getBusinessById(noti.getBussinessId()).getBusinessName());
			map.put("content", noti.getBussinessId()==0?sysContent:noti.getContent());
		}
		return map;
	}

	@Override
	public Long readUnreadCount(int userId, long bussinessId) {
		return notificationDAO.getUnreadCount(bussinessId, userId);
	}

	@Override
	public void deleteNotification(int nId) {
		notificationDAO.deleteNotificationById(nId);
		
	}

	
}
