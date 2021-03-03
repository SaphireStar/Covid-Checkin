package au.usyd.elec5619.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * User Domain
 * @author Yiqing Yang yyan8151
 *
 */
@Entity
@Table(name="users")
public class User implements Serializable{
	
	@Id
	@GeneratedValue
	@Column(name="user_id")
	private int id;
	
	@Column(name="userEmail")
	private String email;
	
	@Column(name="password")
	private String password;
	
	@Column(name="firstName")
	private String firstName;
	
	@Column(name="lastName")
	private String lastName;
	
	@Column(name="address")
	private String address;
	
	@Column(name="mobileNumber")
	private String mobileNumber;
	
	@Column(name="isBusinessRep")
	private boolean isBR;

	public int getId(){
	    return id;
	}
	public String getEmail(){
	    return email;
	}
	public String getPassword(){
	    return password;
	}
	public String getFirstName(){
	    return firstName;
	}
	public String getLastName(){
	    return lastName;
	}
	public String getAddress(){
	    return address;
	}
	public String getMobileNumber(){
	    return mobileNumber;
	}
	public boolean getIsBR(){
	    return isBR;
	}
	public void setId(int id){
	    this.id = id;
	}
	public void setEmail(String email){
	    this.email = email;
	}
	public void setPassword(String password){
	    this.password = password;
	}
	public void setFirstName(String firstName){
	    this.firstName = firstName;
	}
	public void setLastName(String lastName){
	    this.lastName = lastName;
	}
	public void setAddress(String address){
	    this.address = address;
	}
	public void setMobileNumber(String mobileNumber){
	    this.mobileNumber = mobileNumber;
	}
	public void setIsBR(boolean isBR){
	    this.isBR = isBR;
	}

}
