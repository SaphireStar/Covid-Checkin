package au.usyd.elec5619.web;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeMap;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;  

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.usyd.elec5619.domain.Business;
import au.usyd.elec5619.domain.Suburb;
import au.usyd.elec5619.domain.VisitRecord;
import au.usyd.elec5619.domain.User;
import au.usyd.elec5619.service.BusinessManager;
import au.usyd.elec5619.service.user.VisitRecordManager;
import au.usyd.elec5619.service.BusinessService;
import au.usyd.elec5619.service.SuburbManager;
import au.usyd.elec5619.service.VisitRecordDao;
import au.usyd.elec5619.service.VisitRecordService;
import au.usyd.elec5619.service.security.TokenManager;
import au.usyd.elec5619.service.user.DbUserManager;
import au.usyd.elec5619.service.user.DbVisitRecordManager;

import com.google.gson.*;

@Controller
@RequestMapping(value="/business/")
public class BusinessController {

	@Resource(name="businessManager")
	private BusinessManager businessManager;
	
	@Resource(name="suburbManager")
	private SuburbManager suburbManager;
	
	@Autowired
	private VisitRecordDao visitRecordDao;
	@Autowired
	private VisitRecordService visitRecordService;
	
	@Autowired
	private BusinessService businessService;
	
	@Resource(name = "tokenManager")
	private TokenManager tokenManager;
	
	
	@Autowired
	private DbUserManager 	dbUserManager;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	/***
	 * Gets a list of all businesses (currently unused).
	 * @param uid
	 * @param token
	 * @param request
	 * @return list of business objects.
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	@ResponseBody
	public List<Business> getBusinesses(@RequestParam String uid, @RequestParam String token, HttpServletRequest request) {
		List<Business> businesses = this.businessManager.getBusinesses();
		logger.info("Received form request from "+uid +" with token" + token, "");
		boolean flag = tokenManager.verifyToken(token);
		if(!flag) {
			logger.info("invalid token", "");
			return null;
		}
		logger.info("valid token", "");
		for (Business temp : businesses) {
			// Update all the photo paths to relative URIs instead of server file paths
			temp.setPhoto(request.getRequestURL().toString()+temp.getId()+"/photo");
		}
		return businesses;
	}
	
	/**
	 *  Get all businesses associated with a user
	 * @param uid
	 * @param token
	 * @param id
	 * @param request
	 * @return returns a JSON list of businesses associated with a valid user.
	 */
	@RequestMapping(value="/{id}/all", method=RequestMethod.GET)
	@ResponseBody
	public String  getUserBusinesses(@RequestParam String uid, @RequestParam String token, @PathVariable("id") int id, HttpServletRequest request) {
		logger.info("Received form request from "+id +" with token" + token, "");
		boolean flag = tokenManager.verifyToken(token);
		if(!flag) {
			logger.info("invalid token", "");
			return null;
		}
		logger.info("valid token", "");
		if(Integer.parseInt(uid) != id) {
			logger.info("incorrect user", "");
			return null;
		}
		List<Business> businesses = this.businessManager.getUserBusinesses(id);
		// Update all the photo paths to relative URIs instead of server file paths
		for (Business temp : businesses) {
			temp.setPhoto(request.getRequestURL().toString().replace(id+"/all", temp.getId()+"/photo"));
		}
		return new Gson().toJson(businesses);
	}
	/**
	 * Get the details of a particular business
	 * @param uid
	 * @param token
	 * @param id
	 * @param request
	 * @return returns a JSON response of all the business details matching provided ID.
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public String  getBusiness(@RequestParam String uid, @RequestParam String token, @PathVariable("id") long id, HttpServletRequest request) {
		logger.info("Received form request from "+id +" with token" + token, "");
		boolean flag = tokenManager.verifyToken(token);
		if(!flag) {
			logger.info("invalid token", "");
			return null;
		}
		logger.info("valid token", "");		
		JSONParser parser = new JSONParser();
		String json = "";
		try {
			// Combine Business and Relevant Objects into Single JSON representation
			// Business
			Business business = this.businessManager.getBusinessById(id);
			
			// Update photo to relative URI
	        String photoPath = request.getRequestURL().toString()+"/photo";
	        business.setPhoto(photoPath);
	        
			String jsonInString = new Gson().toJson(business);
			JSONObject object1 = (JSONObject) parser.parse(jsonInString);
			// Convert Suburb to Json
			jsonInString = new Gson().toJson(this.suburbManager.getSuburbByPostcode(business.getPostcode()));
			JSONObject object2 = (JSONObject) parser.parse(jsonInString);
			object1.putAll(object2);
		
			// convert to json
			json = object1.toJSONString();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return json;
	}
	
	/**
	 * Check in a user for a specific business
	 * @param id
	 * @param userId
	 * @return Update the checkin records for a specific business, with the user id
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/{userId}", method=RequestMethod.POST)
	@ResponseBody
	public String Checkin(@PathVariable long id, @PathVariable int userId) throws IOException {
		Business curBusi = businessService.getBusinessById(id);
		User user = dbUserManager.getUserInformation(userId);
		visitRecordService.insertVisitRecord(curBusi,user);
		return "Checkin Successful";
	}
	
	/**
	 * Get the view to add a new business
	 * @param uiModel
	 * @return redirect to the add jsp view
	 */
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String addBusiness(@RequestParam String uid, @RequestParam String token, Model uiModel) {
		logger.info("Received form request from "+uid +" with token" + token, "");
		boolean flag = tokenManager.verifyToken(token);
		if(!flag) {
			logger.info("invalid token", "");
			return null;
		}
		logger.info("valid token", "");		
		return "add";
	}
	
	/**
	 * Register a new business for the signed in user
	 * @param uid
	 * @param token
	 * @param session
	 * @param httpServletRequest
	 * @return returns a JSON representation of the business on successful creation
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public String addBusiness(@RequestParam String uid, @RequestParam String token, HttpSession session, HttpServletRequest  httpServletRequest) {
		logger.info("Received form request from "+ uid +" with token" + token, "");
		boolean flag = tokenManager.verifyToken(token);
		if(!flag) {
			logger.info("invalid token", "");
			return null;
		}
		logger.info("valid token", "");
		int userId = Integer.parseInt(uid);

			Business business = new Business();
			business.setBusinessName(httpServletRequest.getParameter("businessName"));	
			business.setPhoneNumber(httpServletRequest.getParameter("phoneNumber"));
			business.setBusinessEmail(httpServletRequest.getParameter("businessEmail"));
			business.setAddress(httpServletRequest.getParameter("address"));
			business.setPostcode(Integer.parseInt(httpServletRequest.getParameter("postcode")));	
			business.setCapacity(Integer.parseInt(httpServletRequest.getParameter("capacity")));

			// need to handle logic for getting user id
			// and make sure isBusinessRep is set to true if not already
			business.setBusinessRepId(userId);
		
			
			//ensure suburb exists in database
			if (this.suburbManager.getSuburbByPostcode(business.getPostcode()) == null) 
			{
				Suburb suburb = new Suburb();
				suburb.setPostcode(business.getPostcode());
				suburb.setSuburb(httpServletRequest.getParameter("suburb"));
				this.suburbManager.addSuburb(suburb);
			}
			this.businessManager.addBusiness(business);
			
			return new Gson().toJson(business);

		
	}
	
	/**
	 * Edit a specific business details
	 * @param uid
	 * @param token
	 * @param id
	 * @param uiModel
	 * @return returns a redirect to the edit JSP view for the business, if request from business owner
	 */
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public String editBusiness(@RequestParam String uid, @RequestParam String token, @PathVariable("id") Long id, Model uiModel) {
		logger.info("Received form request from "+id +" with token" + token, "");
		boolean flag = tokenManager.verifyToken(token);
		if(!flag) {
			logger.info("invalid token", "");
			return null;
		}
		logger.info("valid token", "");
		if(Integer.parseInt(uid) != id) {
			logger.info("incorrect user", "");
			return null;
		}
		Business business = this.businessManager.getBusinessById(id);
		uiModel.addAttribute("business", business);
		
		return "edit";
	}
	
	/**
	 * Update the details of the current business, if verified user & business owner
	 */
	@RequestMapping(value="/edit/**", method=RequestMethod.POST)
	public String editBusiness(@RequestParam String uid, @RequestParam String token, @Valid Business business) {
		logger.info("Received form request from "+uid +" with token" + token, "");
		boolean flag = tokenManager.verifyToken(token);
		if(!flag) {
			logger.info("invalid token", "");
			return null;
		}
		logger.info("valid token", "");				
		business.setBusinessRepId(Integer.parseInt(uid));
		this.businessManager.updateBusiness(business);

		return "redirect:/hello.htm";
	}
	
	/**
	 * Delete a business
	 * @param id
	 * @return returns a redirect on success
	 */
	@RequestMapping(value="/delete/{id}", method=RequestMethod.GET)
	public String deleteBusiness(@RequestParam String uid, @RequestParam String token, @PathVariable("id") Long id) {
		logger.info("Received form request from "+uid +" with token" + token, "");
		boolean flag = tokenManager.verifyToken(token);
		if(!flag) {
			logger.info("invalid token", "");
			return null;
		}
		logger.info("valid token", "");		
		this.businessManager.deleteBusiness(id);
		
		return "redirect:/hello.htm";
	}
	
	/**
	 * Upload a photo for the provided business id. If unsuccessful, a placeholder is uploaded.
	 * @param id
	 * @param session
	 * @param httpServletRequest
	 * @return returns a redirect after processing upload.
	 */
	@RequestMapping(value="/{id}/photo", method=RequestMethod.POST)
	public String uploadPhoto(@PathVariable("id") long id, HttpSession session, MultipartHttpServletRequest  httpServletRequest) {

			Business business = this.businessManager.getBusinessById(id);
	        String path=session.getServletContext().getRealPath("/");
			try {
				// Get photo from request and save on server
				MultipartFile multipartFile = httpServletRequest.getFile("photo");
				String name = multipartFile.getOriginalFilename();
				Path rootPath = Paths.get(path);
		        Path filePath = Paths.get(rootPath.getParent()+"/uploads/"+business.getId()+"-"+name);
		        Files.createDirectories(filePath.getParent());
		        BufferedOutputStream bout=new BufferedOutputStream(  
		                 new FileOutputStream(filePath.toString()));
		        byte barr[]=multipartFile.getBytes();  
		        bout.write(barr);  
		        bout.flush();  
		        bout.close(); 
		        this.businessManager.updateBusinessPhoto(id, filePath.toString());
			} catch(Exception e){
				FileInputStream fis;
				try {
					// Failed to upload, use a default instead.
					Path rootPath = Paths.get(path);
					File sourceimage = new File(rootPath+"/resources/image/img.jpg");
			        Path filePath = Paths.get(rootPath.getParent()+"/uploads/img.jpg");
			        File dest = new File(rootPath.getParent()+"/uploads/img.jpg");
			        FileUtils.copyFile(sourceimage, dest);

			        this.businessManager.updateBusinessPhoto(id, filePath.toString());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}  
		
		return "redirect:/hello.htm";
	}
	
	/**
	 * Get the photo for a particular business
	 * @param id
	 * @return returns a JPEG image associated with the business entity.
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/photo",
			produces = {MediaType.IMAGE_JPEG_VALUE},
			method=RequestMethod.GET)
    public @ResponseBody byte[] getImage(@PathVariable("id") long id) throws IOException {
		logger.info("Received photo get request");		
		try {
			// Combine Business and Relevant Objects into Single JSON representation
			// Business
			Business business = this.businessManager.getBusinessById(id);
			InputStream is = new FileInputStream(business.getPhoto());
            BufferedImage img = ImageIO.read(is);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", bos);
            return bos.toByteArray();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("No photo available");		
		}
		return null;
	}
	
	/**
	 * Get all the check in details for the last week associated with a business.
	 * @param id
	 * @param request
	 * @return returns a JSON object including the count of check-ins per hour for 7 days.
	 */
	@RequestMapping(value="/{id}/checkins", method=RequestMethod.GET)
	@ResponseBody
	public String  getBusinessCheckins(@PathVariable("id") int id, HttpServletRequest request) {
		List<VisitRecord> allList = this.visitRecordDao.getAllVisitRecords();
		List<VisitRecord> checkins = new ArrayList<VisitRecord>();
		for(VisitRecord visitRecord:allList) {
			if(visitRecord.getBusiness().getId() == id) {
				checkins.add(visitRecord);
			}
		}
		HashMap<String, HashMap<Integer, Integer>> dates = new HashMap<String, HashMap<Integer, Integer>>();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		// populate default dates
		LocalDateTime now = LocalDateTime.now();  
		for(int i = 0; i<6; i++) {
			String day = dtf.format(now.minusDays(i));
			dates.put(day, new HashMap<Integer, Integer>());
			for (int j = 0; j<24; j++) {
				dates.get(day).put(j, 0);
			}
		}
	
		for(VisitRecord visit : checkins) {
			Date temp = visit.getDate();
			
			cal.setTime(temp);
			String day = sdf.format(temp);
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			// update day / hour count
			if (dates.containsKey(day)) {
				int curr = dates.get(day).get(hour);
				dates.get(day).put(hour, curr + 1);

			}			
		}
		
		TreeMap<String, HashMap<Integer, Integer>> sorted = new TreeMap<String, HashMap<Integer, Integer>>(Collections.reverseOrder()); 
        sorted.putAll(dates); 

		return new Gson().toJson(sorted);
	}
	
}
