package au.usyd.elec5619.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.usyd.elec5619.domain.Business;
@Service(value="businessService")
public class BusinessServiceImpl implements BusinessService {
	
	@Autowired
	private BusinessDao businessDao;
	
	@Override
	@Transactional
	public List<Business> getBusinesses() {
		return businessDao.getBusinesses();
	}

	@Override
	@Transactional
	public Business getBusinessById(long id) {
		return businessDao.getBusinessById(id);
	}

}
