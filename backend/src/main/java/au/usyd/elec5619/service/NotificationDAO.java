package au.usyd.elec5619.service;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import au.usyd.elec5619.domain.Notification;
import au.usyd.elec5619.domain.User;

@Repository
@Transactional
public class NotificationDAO {
	
	@Resource
	private SessionFactory sessionFactory;

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	// Insert
	public int addNotification(Notification notification) {
		return (Integer) this.getSession().save(notification);
	}
	
	// Select
	
	public List<Notification> getNotification(){
		return this.sessionFactory.getCurrentSession().createQuery("FROM Notification").list();
	}
	public Notification getNotificationById(int id){
		return (Notification) this.sessionFactory.getCurrentSession().createQuery("FROM Notification where Id=?").setInteger(0, id).uniqueResult();
	}
	
	public List<Notification> getNotificationByUserId(int userId){
		return this.sessionFactory.getCurrentSession().createQuery("FROM Notification where userId=? order by createTime DESC").setInteger(0, userId).list();
	}
	
	public List<Notification> getNotificationByBussinessId(long bussinessId){
		return this.sessionFactory.getCurrentSession().createQuery("FROM Notification where bussinessId=?").setLong(0, bussinessId).list();
	}
	
	public List<Notification> getNotiDetail(long bussinessId, int userId){
		return this.sessionFactory.getCurrentSession().createQuery("FROM Notification where bussinessId=:bussinessId and userId=:userId order by createTime DESC")
					.setLong("bussinessId", bussinessId).setInteger("userId", userId).list();
	}
	
	public Notification getNewestNotification(int userId){
		return (Notification) this.sessionFactory.getCurrentSession().createQuery("FROM Notification where userId=? order by createTime DESC").setInteger(0, userId).setMaxResults(1).setFirstResult(1).uniqueResult();
	}
	
	public long getSysNotificationCount(){
		return (Long) this.sessionFactory.getCurrentSession()
				.createQuery("SELECT count(*) FROM Notification where bussinessId=?").setLong(0, 0).uniqueResult();
	}
	
	public int getNewestCaseId(){
		return (Integer) this.sessionFactory.getCurrentSession()
				.createQuery("SELECT max(n.caseId) FROM Notification n where bussinessId=?").setInteger(0, 0).uniqueResult();
	}
	
	public Long getUnreadCount(long bussinessId, int userId){
		return (Long) this.sessionFactory.getCurrentSession().createQuery("SELECT count(*) FROM Notification where bussinessId=:bussinessId and userId=:userId and status=:status")
					.setLong("bussinessId", bussinessId).setInteger("userId", userId).setInteger("status", 0).uniqueResult();
	}
	
	// Update
	public void updateNotification(Notification notification) {
		this.getSession().update(notification);
	}
	
	// Delete
	public void deleteNotificationById(int id) {
		this.getSession().createQuery("delete Notification where id=?").setInteger(0, id).executeUpdate();
	}
	
	
}
