package au.usyd.elec5619.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import au.usyd.elec5619.domain.Notification;
import au.usyd.elec5619.domain.User;

/**
 * Interface to handle business logic of the notification system.
 * 
 * @author Yonghe Tan
 */
public interface NotificationManager extends Serializable {
	
	
	public void sendSystemNotification(int NotifyCaseId, int NewestCaseId) throws Exception;
	
	public int sendBusinessNotification(long bussinessId, Date startDate, Date endDate, String content) throws Exception;
	
	public void sendEmails(List<User> list, String subject, String content);
	
	public void sendMessages(List<Map<String, Object>> list, int nId, long bussinessId, String content, int caseId) throws Exception;
	
	public List<Map<String, Object>> readSendHistory(long bussinessId);
	
	public List<Notification> readDetails(long bussinessId, int userId);
	
	public List<Notification> readUserNotifications(int userId);
	
	public Map<String, Object> readNewestNotification(int userId);
	
	public Long readUnreadCount(int userId, long bussinessId);
	
	public void deleteNotification(int nId);
	
}
