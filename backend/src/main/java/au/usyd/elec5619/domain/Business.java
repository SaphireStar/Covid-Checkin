package au.usyd.elec5619.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Business")
public class Business implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="business_id")
	private long id;
	
	@Column(name="businessName", length=20)
    private String businessName;
	
	@Column(name="phoneNumber", length=20)
    private String phoneNumber;
    
	@Column(name="businessEmail", length=50)
    private String businessEmail;
	
	@Column(name="address", length=50)
    private String address;
	
	@Column(name="postcode")
    private int postcode;
	
	@Column(name="capacity")
    private int capacity;
	
	@Column(name="photo", length=900)
    private String photo;

	@Column(name="businessRepId")
	private int businessRepId;
    
    public long getId() {
		return id;
	}

    public void setId(long id) {
		this.id = id;
	}

	public String getBusinessName() {
		return businessName;
	}
	
	public void setBusinessName(String name) {
		this.businessName = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getBusinessEmail() {
		return businessEmail;
	}

	public void setBusinessEmail(String email) {
		this.businessEmail = email;
	}

	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getPostcode() {
		return postcode;
	}
	
	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}
	
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
    public long getBusinessRepId() {
		return businessRepId;
	}

    public void setBusinessRepId(int id) {
		this.businessRepId = id;
	}	

	@Override
	public String toString() {
		return "Business [id=" + id + ", businessName=" + businessName + ", phoneNumber=" + phoneNumber + ", businessEmail=" + businessEmail
				+ ", address=" + address + ", postcode=" + postcode + ", capacity=" + capacity + ", businessRepId=" + businessRepId +"]";
	}

}
