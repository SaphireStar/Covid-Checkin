package au.usyd.elec5619.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Notification")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id", nullable = false, unique = true)
	private int id;
	@Column(name = "userId")
	private int userId;
	@Column(name = "bussinessId") // 0: bussinessId == 0 means system
	private long bussinessId;
	@Column(name = "case_Id")
	private int caseId;
	@Column(name = "record_Id")
	private long recordId;
	@Column(name = "content")
	private String content;
	@Column(name = "status") // 0: unread / sending 1: read / send successfully
	private int status;
	@Column(name = "type") // 0: user 1: business send history
	private int type;
	@Column(name = "createTime")
	private Date createTime;

	public Notification() {
		super();
	}

	public Notification(int userId, long bussinessId, long recordId, int caseId, String content, int status, int type,
			Date createTime) {
		super();
		this.userId = userId;
		this.bussinessId = bussinessId;
		this.recordId = recordId;
		this.caseId = caseId;
		this.content = content;
		this.status = status;
		this.type = type;
		this.createTime = createTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCaseId() {
		return caseId;
	}

	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public long getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(long bussinessId) {
		this.bussinessId = bussinessId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String toString() {
		return "id:" + id + ",userId:" + userId + ",bussinessId:" + bussinessId + ",recordId:" + recordId + ",content:"
				+ content + ",status:" + status + ",createTime:" + createTime;
	}
}
