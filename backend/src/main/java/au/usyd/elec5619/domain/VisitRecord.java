package au.usyd.elec5619.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;




@Entity
@Table(name="VisitRecord")
public class VisitRecord implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="visit_id")
	private long id;
	

	@OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name ="business_id")
	private Business business;
	
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="customer_id")
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "checkInTime")
	private Date date = new Date();
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Business getBusiness() {
		return business;
	}

	
	public void setBusiness(Business business) {
		this.business = business;
	}
	

	public User getCustomer() {
		return this.user;
	}

	public void setCustomer(User user) {
		this.user = user;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public void setDate(Date date){
	    this.date = date;  
	}

	

	
	
	@Override
	public String toString() {
		return "VisitRecord [id=" + id + ", business=" + business + ", User=" + user + ", date_time=" + date +"]";
	}
	
}

