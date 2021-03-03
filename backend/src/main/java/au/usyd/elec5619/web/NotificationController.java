package au.usyd.elec5619.web;

import java.text.ParseException;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import au.usyd.elec5619.service.BusinessDao;
import au.usyd.elec5619.service.BusinessManager;
import au.usyd.elec5619.domain.Business;
import au.usyd.elec5619.domain.Notification;
import au.usyd.elec5619.domain.User;
import au.usyd.elec5619.service.NotificationManager;
import au.usyd.elec5619.service.NotificationService;
import au.usyd.elec5619.service.user.UserManager;
import au.usyd.elec5619.service.user.VisitRecordManager;


/**
 * Notification System Controller, responsible for everything about notification.
 * Businesses can send notifications to customers. 
 * System automatically sends new case messages to users with email.
 * User view their notification.
 * 
 * @author Yonghe Tan
 */
@Controller
@RequestMapping(value = "/notification/**")
public class NotificationController {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	// user manager
	@Resource(name="dbUserManager")
	private UserManager userManager;
	// visit record manager
	@Resource(name="dbVisitRecordManager")
	private VisitRecordManager visitRecordManager;
	
	@Autowired
	private NotificationManager notificationManager;
	
	@Autowired
	private BusinessDao bussinessDAO;
	
	
	/**
	 * business send notification
	 * */
	@RequestMapping(value = "/notify/{bussinessID}", method = RequestMethod.GET)
	public String showNotifyPage(
			@PathVariable("bussinessID") long bussinessID, Model model) {
		Business business = bussinessDAO.getBusinessById(bussinessID);
		if(business == null) {
			return "noexist";
		}
		model.addAttribute("business", business);
		return "notify";
	}
	
	/**
	 * user read notification
	 * */
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public String sendNotification(
			@PathVariable("userId") int userId, Model model) {
		User user = userManager.getUserInformation(userId);
		if(user == null) {
			return "noexist";
		}
		List<Notification> notis = notificationManager.readUserNotifications(userId);
		System.out.println(notis.size());
		
		List<Map<String, Object>> notiList = new ArrayList();
		Set<Long> set = new HashSet();
		int unread = 0;
		for(Notification noti: notis) {
			long bId = noti.getBussinessId();
			if(bId == 0) {
				unread = noti.getStatus() == 0 ? ++unread : unread;
				continue;
			}
			if(set.contains(bId)) {
				continue;
			}
			Business bus = bussinessDAO.getBusinessById(bId);
			if(bus == null) {
				continue;
			}
			set.add(bId);
			Map<String, Object> map = new HashMap();
			map.put("bId", bId);
			map.put("unread", notificationManager.readUnreadCount(userId, bId));
			map.put("businessName", bus.getBusinessName());
			map.put("createTime", noti.getCreateTime().toString().substring(0, 19));
			notiList.add(map);
		}
		
		model.addAttribute("notiList", notiList);
		model.addAttribute("user", user);
		if(unread > 0) {
			model.addAttribute("unread", unread);
		}
		
		return "notifications";
	}
	
	/**
	 * business send notification
	 * */
	@RequestMapping(value = "/notify", method = RequestMethod.POST)
	public String sendNotification(
			@RequestParam(value = "businessId") long businessId,
			@RequestParam(value = "startTime")String startTime, 
			@RequestParam(value = "endTime")String endTime,
			@RequestParam(value = "content")String content, RedirectAttributes model) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date start = simpleDateFormat.parse(startTime);
			Date end = simpleDateFormat.parse(endTime);
			int result = notificationManager.sendBusinessNotification(businessId, start, end, content);
			
			if(result == 0) {
				model.addFlashAttribute("error", 0);
			}else {
				model.addFlashAttribute("error", 2);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("Wrong Format:", e.toString());
			model.addFlashAttribute("error", 1);
		}
		String url = "/notification/notify/" + businessId;
		return "redirect:" + url;
	}
	
	/**
	 * user read notification detail. AJAX
	 * */
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	@ResponseBody
	public String readNotificationDetail(
			@RequestParam(value = "bussinessId") long bussinessId,
			@RequestParam(value = "userId")int userId) {
        
		System.out.println(bussinessId + "|| userId:"+userId);
		List<Notification> notis = notificationManager.readDetails(bussinessId, userId);
		List<Map<String, Object>> contentList = new ArrayList();
		Business business = bussinessDAO.getBusinessById(bussinessId);
		if(business != null) {
			for(Notification noti: notis) {			
				Map<String, Object> map = new HashMap();
				map.put("bId", bussinessId);
				map.put("bName", business.getBusinessName());
				map.put("bAddress", business.getAddress());
				map.put("nId", noti.getId());
				map.put("createTime", noti.getCreateTime().toString().substring(0, 19));
				map.put("content", noti.getContent());
				contentList.add(map);
			}
		}
		
		return getJSONString(0, contentList);
	}
	
	/**
	 * user read system messages detail. AJAX
	 * */
	@RequestMapping(value = "/system", method = RequestMethod.POST)
	@ResponseBody
	public String readSystemNotification(
			@RequestParam(value = "userId")int userId) {
		List<Notification> notis = notificationManager.readDetails(0, userId);
		List<Map<String, Object>> contentList = new ArrayList();
		
		for(Notification noti: notis) {
			Map<String, Object> map = new HashMap();
			map.put("bId", 0);
			map.put("nId", noti.getId());
			map.put("createTime", noti.getCreateTime().toString().substring(0, 19));
			map.put("content", noti.getContent());
			contentList.add(map);
		}
		
		return getJSONString(0, contentList);
	}
	
	/**
	 * business read sent history. AJAX
	 * */
	@RequestMapping(value = "/history", method = RequestMethod.POST)
	@ResponseBody
	public String readSendHistory(
			@RequestParam(value = "bussinessID")long bussinessID) {	
		List<Map<String, Object>> notiList = notificationManager.readSendHistory(bussinessID);
		return getJSONString(0, notiList);
	}
	
	/**
	 * Polling check new Notification.
	 * */
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ResponseBody
	public String checkNewestNotification(
			@RequestParam(value = "userId")int userId) {
		Map<String, Object> map = notificationManager.readNewestNotification(userId);
		return (new JSONObject(map)).toJSONString();
	}
	
	/**
	 * delete Notification
	 * */
	@RequestMapping(value = "/delete/{nId}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteNotification(
			@PathVariable(value = "nId")int nId) {
		
		notificationManager.deleteNotification(nId);
		Map<String, Object> map = new HashMap();
		map.put("code", 0);
		return (new JSONObject(map)).toJSONString();
	}
	
	/**
	 * send business by name. AJAX
	 * */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public String searchBusiness(
			@RequestParam("userId") int userId,
			@RequestParam(value = "str")String str) {

		User user = userManager.getUserInformation(userId);
		List<Notification> notis = notificationManager.readUserNotifications(userId);
		List<Map<String, Object>> notiList = new ArrayList();
		Set<Long> set = new HashSet();
		
		List<Business> busList = bussinessDAO.getBusByString(str);
		System.out.println(busList.size());
		
		Set<Long> searchSet = new HashSet();
		for(Business bus:busList) {
			searchSet.add(bus.getId());
		}
		int unread = 0;
		for(Notification noti: notis) {
			long bId = noti.getBussinessId();
			if(bId == 0) {
				unread = noti.getStatus() == 0 ? ++unread : unread;
				continue;
			}
			if(set.contains(bId) || (!searchSet.contains(bId))) {
				continue;
			}
			set.add(bId);
			Map<String, Object> map = new HashMap();
			map.put("bId", bId);
			map.put("unread", notificationManager.readUnreadCount(userId, bId));
			map.put("businessName", bussinessDAO.getBusinessById(bId).getBusinessName());
			map.put("createTime", noti.getCreateTime().toString().substring(0, 19));
			notiList.add(map);
		}
		
		return getJSONString(0, notiList);
	}
	
	/**
	 * Transform List<Map<String, Object>> to JSON String.
	 * */
	public static String getJSONString(int code, List<Map<String, Object>> list) {
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		if(list!=null) {
			jsonArray.addAll(list);
		}
		json.put("code", code);
		json.put("list", jsonArray);
		String str = json.toJSONString();
        return str;
    }
}
