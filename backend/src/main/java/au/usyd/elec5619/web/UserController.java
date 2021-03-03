package au.usyd.elec5619.web;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import au.usyd.elec5619.domain.Record;
import au.usyd.elec5619.domain.User;
import au.usyd.elec5619.service.security.TokenManager;
import au.usyd.elec5619.service.user.UserManager;
import au.usyd.elec5619.service.user.VisitRecordManager;

/**
 * User information system controller, which is responsible for all functions related to query and update user information,
 * including logging, privacy and visit records.
 * 
 * @author Yiqing Yang yyan8151
 *
 */
@Controller
public class UserController {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	// user manager
	@Resource(name="dbUserManager")
	private UserManager userManager;
	// visit record manager
	@Resource(name="dbVisitRecordManager")
	private VisitRecordManager visitRecordManager;

	@Resource(name = "tokenManager")
	private TokenManager tokenManager;
	
	/**
	 * Validates the email and password to decide whether the user can log in
	 */
    
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(HttpServletResponse response, @RequestParam String username, 
			@RequestParam String password) throws Exception {
		// logger
		logger.info("Login called. Received login request from "+username, "");
		// response header
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		// validate username and password
		int id = this.userManager.login(username, password);
		
		/**
		 * ªÒ»°token
		 */
		String tokenString = tokenManager.generateToken(""+id);
		
		// response with the user id
		String resultString = "{\"id\":\""+ id +"\",\"token\":\""+tokenString+"\"}";
		
		// response
        try {
            response.setContentType("application/json");
            response.getWriter().write(resultString);
        } catch (IOException e) {
            e.printStackTrace();
        }    
	}
	
    /**
     * Signs up for a new account and registers the user information
     */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void singUp(HttpServletResponse response, @RequestParam String email, @RequestParam String password,
			@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String address, @RequestParam String phone) throws IOException {
		// logger 
		logger.info("Received signup request from "+ email, "");
		
		// header
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		// create user
		User newUser = new User();
		newUser.setEmail(email);
		newUser.setPassword(password);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setAddress(address);
		newUser.setMobileNumber(phone);
		newUser.setIsBR(false);
		
		// fetch the user id
		int id = this.userManager.register(newUser);
		
		// response with the user id
		String tokenString = tokenManager.generateToken(""+id);
		
		// response with the user id
		String resultString = "{\"id\":\""+ id +"\",\"token\":\""+tokenString+"\"}";
        try {
            response.setContentType("application/json");
            response.getWriter().write(resultString);
            //response.getWriter().write(resultString1);
        } catch (IOException e) {
            e.printStackTrace();
        }      
	}
	
	/**
	 * Fetches the information stored in the database
	 */
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public void fetchForm(@RequestParam String id, @RequestParam String token, HttpServletResponse response) throws IOException {
		// logger
		
		boolean flag = tokenManager.verifyToken(token);
		if(!flag) {
			logger.info("invalid token," + "Received form request from "+id , "");
			return;
		}
		logger.info("valid token," + "Received form request from "+id , "");
		
		
		// header
		response.setHeader("Access-Control-Allow-Origin", "*");
	
		// fetch user information 
		User user = this.userManager.getUserInformation(Integer.parseInt(id));
		
		// response
		String resultString = "{\"firstName\":\""+ user.getFirstName() +"\","
				+ "\"lastName\":\""+ user.getLastName() +"\","
				+ "\"address\":\""+ user.getAddress() +"\","
				+ "\"phone\":\""+user.getMobileNumber()+"\"}";
        try {
            response.setContentType("application/json");
            response.getWriter().write(resultString);
        } catch (IOException e) {
            e.printStackTrace();
        }      
	}

	/**
	 * Updates the information stored on the server
	 */
	@RequestMapping(value = "/form/update", method = RequestMethod.POST)
	public void updateForm(@RequestParam String id,@RequestParam String token,@RequestParam String firstName,
			@RequestParam String lastName,@RequestParam String address,
			@RequestParam String phone, HttpServletResponse response) throws IOException {
		// logger 
		boolean flag = tokenManager.verifyToken(token);
		if(!flag) {
			logger.info("invalid token," + "Update form request from "+id , "");
			return;
		}
		logger.info("valid token," + "Update form request from "+id , "");
		//  header
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		// Encapsulate the updated information
		User user = new User();
		user.setId(Integer.parseInt(id));
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setAddress(address);
		user.setMobileNumber(phone);
		
		// update information
		this.userManager.update(user);
		
		// response to tell the client that the update has been done
		String resultString = "{\"response\":\""+ "response" + "\"}";
        try {
            response.setContentType("application/json");
            response.getWriter().write(resultString);
        } catch (IOException e) {
            e.printStackTrace();
        }      
	}
	
	/**
	 * Fetches the visit history of the user
	 */
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public void getHistory(@RequestParam String id, @RequestParam String token,HttpServletResponse response) throws IOException {
		// logger 
		boolean flag = tokenManager.verifyToken(token);
		if(!flag) {
			logger.info("invalid token," + "Received history request from "+id , "");
			return;
		}
		logger.info("valid token," + "Received history request from "+id , "");
		// header
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		// Fetches the visit history 
		List<Record> records = this.visitRecordManager.getVisitRecord(Integer.parseInt(id));
		
		// response
		String resultString = "{";
		
		int num = records.size();
		resultString += "\"num\":\"" + num +"\",";
		resultString += "\"records\": [";
		
		// data body
		if(num>=1) {
			Record record = records.get(0);
			resultString +="    {\"date\":\""+record.getCheckInTimeDate().toString()+"\", \"location\":\""+ record.getBusinessName()+"\", \"risk\":\""+record.getRisk() +"\"}";
		}
		for(int i=1;i<num;i++) {
			Record record = records.get(i);
			resultString +=",    {\"date\":\""+record.getCheckInTimeDate().toString()+"\", \"location\":\""+ record.getBusinessName()+"\", \"risk\":\""+record.getRisk() +"\"}";
		}
		
		resultString +="  ]";
		resultString+= "}";
		
        try {
            response.setContentType("application/array");
            response.getWriter().write(resultString);
            //response.getWriter().write(resultString1);
        } catch (IOException e) {
            e.printStackTrace();
        }      
	}
}