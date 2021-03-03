package au.usyd.elec5619.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Suburb")
public class Suburb implements Serializable {

	@Id
	@Column(name="postcode")
	private int postcode;
	
	@Column(name="suburb", length=20)
    private String suburb;

	
    
    public String getSuburb() {
		return suburb;
	}

    public void setSuburb(String suburb) {
		this.suburb = suburb;
	}
	
	public int getPostcode() {
		return postcode;
	}
	
	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}
	

	@Override
	public String toString() {
		return "Suburb [postcode=" + postcode + ", suburb=" + suburb + "]";
	}

}