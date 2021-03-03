package au.usyd.elec5619.util;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import au.usyd.elec5619.service.NotificationDAO;
import au.usyd.elec5619.domain.Notification;
import au.usyd.elec5619.domain.User;
import au.usyd.elec5619.domain.VisitRecord;

/**
 * The actual runnable class to handle sendMessage function. 
 * 
 * @author Yonghe Tan
 */
public class MessageRunner implements Runnable {
	
	private NotificationDAO notificationDAO;
	private List<Map<String, Object>> list; 
	private long bussinessId; 
	private String content; 
	private int caseId;
	private int nId;
	
	public MessageRunner(List<Map<String, Object>> list, int nId, long bussinessId, String content, int caseId, NotificationDAO notificationDAO) {
		this.list = list;
		this.bussinessId = bussinessId;
		this.content = content;
		this.caseId = caseId;
		this.notificationDAO = notificationDAO;
		this.nId = nId;
	}

	@Override
	public void run() {
		if(list == null) {
			return;
		}
		Notification notification = new Notification();
		notification.setBussinessId(bussinessId);
		notification.setCaseId(caseId);
		Set<Integer> userSet = new HashSet();
		for(Map<String, Object> map : list) {
			User user =(User) map.get("user");
			if(userSet.contains(user.getId())) {
				continue;
			}
			VisitRecord record = (VisitRecord) map.get("record");
			notification.setRecordId(record.getId());
			notification.setContent(content);
			notification.setUserId(user.getId());
			notification.setCreateTime(new Date());
			notification.setStatus(0);
			notificationDAO.addNotification(notification);
			userSet.add(user.getId());
		}
		// Inform Business success
		if(nId != -1) {
			Notification noti = notificationDAO.getNotificationById(nId);
			noti.setStatus(1);
			notificationDAO.updateNotification(noti);
		}
	}

}
