package au.usyd.elec5619.service.user;

import java.util.List;

import au.usyd.elec5619.domain.Record;

/**
 * Interface for managing visit records
 * @author Yiqing Yang yyan8151
 *
 */
public interface VisitRecordManager {
	public List<Record> getVisitRecord(int userId);
}
