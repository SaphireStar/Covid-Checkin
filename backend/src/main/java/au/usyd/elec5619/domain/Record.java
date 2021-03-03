package au.usyd.elec5619.domain;

import java.util.Date;
/**
 *  Record domain, the combination of visit records and business
 *  Yiqing Yang yyan8151
 *
 */
public class Record {
	
	private Date checkInTimeDate;
	
	private String businessName;
	
	private boolean risk;
	
	public Date getCheckInTimeDate() {
		return checkInTimeDate;
	}
	public String getBusinessName() {
		return businessName;
	}
	
	public boolean getRisk() {
		return risk;
	}
	
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public void setCheckInTimeDate(Date checkInTimeDate) {
		this.checkInTimeDate = checkInTimeDate;
	}
	public void setRisk(boolean risk) {
		this.risk = risk;
	}

}
