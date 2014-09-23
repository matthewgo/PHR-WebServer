package phr.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import phr.dao.CheckUpDao;
import phr.dao.UserDao;
import phr.exceptions.DataAccessException;
import phr.exceptions.ServiceException;
import phr.service.CheckUpService;
import phr.web.models.CheckUp;
import phr.web.models.User;

@Service("checkUpService")
public class CheckUpServiceImpl implements CheckUpService {
	
	@Autowired
	CheckUpDao checkUpDao;

	@Autowired
	UserDao userDao;

	@Override
	public void add(String accessToken, CheckUp checkUp) throws ServiceException {
		try {
			int userID = userDao.getUserIDGivenAccessToken(accessToken);
			checkUp.setUser(new User(userID));
			checkUpDao.add(checkUp);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(
					"Error has occurred while adding a checkup entry", e);
		}
	}

	@Override
	public void edit(String accessToken, CheckUp object)
			throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String accessToken, CheckUp object)
			throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<CheckUp> getAll(String accessToken)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getEntryId(CheckUp checkUp) throws ServiceException {
		if (checkUp.getUser() != null)
			return checkUp.getEntryID();
		else
			try {
				return checkUpDao.getEntryId(checkUp);
			} catch (DataAccessException e) {
				throw new ServiceException(
						"Error has occurred while adding a checkup entry",
						e);
			}
	}
}
