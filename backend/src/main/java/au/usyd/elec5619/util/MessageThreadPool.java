package au.usyd.elec5619.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import au.usyd.elec5619.service.NotificationDAO;
import au.usyd.elec5619.domain.User;


/**
 * Using ThreadPool to implement producer-consumer pattern to support asynchronous writing to database.
 * The incoming task would be put in a buffer(Blocking Queue). There are fixed numbers of threads.
 * Threads in thread pool would run the task when they are idle. Only 5 core threads would maintain if there is no task.
 * If the buffer is full. Would create new threads to handle the task, and those threads are not core fixed thread, they would be destroy when they finish their task.
 * @author Yonghe Tan
 */
@Component
@EnableAsync
public class MessageThreadPool {
	
	@Autowired
	private NotificationDAO notificationDAO;
	
	// assume tasks:50 per/sec taskcout 0.1s
	// threadcount = tasks/(1/taskcost) =tasks*taskcout= 5 
	// queueCapacity = (coreSizePool/taskcost)*responsetime
	// maxPoolSize = (max(tasks)- queueCapacity)/(1/taskcost)
	private ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 30
			, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50),new ThreadPoolExecutor.CallerRunsPolicy());
	
	
	public void sendMessages(List<Map<String,Object>> list, int nId, long bussinessId, String content, int caseId) throws InterruptedException {
		executor.execute(new MessageRunner(list, nId, bussinessId, content, caseId, notificationDAO));
	}
	
	
}
