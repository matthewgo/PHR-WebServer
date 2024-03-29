package phr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import phr.dao.UserDao;
import phr.dao.sqlimpl.UserDaoSqlImpl;
import phr.exceptions.DataAccessException;
import phr.exceptions.UserServiceException;
import phr.exceptions.UsernameAlreadyExistsException;
import phr.models.User;
import phr.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	UserDao userDao = new UserDaoSqlImpl();

	@Override
	public void addUser(User user) throws UsernameAlreadyExistsException,
			UserServiceException {
		try {
			userDao.addUser(user);
		} catch (DataAccessException e) {
			throw new UserServiceException(
					"An error has occurred during registration", e);
		}
	}

	@Override
	public void deleteUser(User newUser) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isValidUser(User userToValidate) throws UserServiceException {
		try {
			return userDao.isValidUser(userToValidate);
		} catch (DataAccessException e) {
			throw new UserServiceException("Error in validating the user", e);
		}
	}

	@Override
	public void assignAccessToken(String username, String accessToken)
			throws UserServiceException {
		try {
			userDao.assignAccessToken(username, accessToken);
		} catch (DataAccessException e) {
			throw new UserServiceException("Error in assigning access token", e);
		}
	}

	@Override
	public boolean isValidAccessToken(String accessToken, String username)
			throws UserServiceException {
		try {
			return userDao.isValidAccessToken(accessToken, username);
		} catch (DataAccessException e) {
			throw new UserServiceException("Error in validating access token",
					e);
		}
	}

	@Override
	public boolean usernameAlreadyExists(String username)
			throws UserServiceException {
		try {
			return userDao.usernameAlreadyExists(username);
		} catch (DataAccessException e) {
			throw new UserServiceException("Error", e);
		}
	}

	@Override
	public User getUserGivenAccessToken(String accessToken)
			throws UserServiceException {
		try {
			return userDao.getUserGivenAccessToken(accessToken);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new UserServiceException("Error", e);
		}
	}

	@Override
	public void edit(User user) throws UserServiceException {
		try {
			userDao.edit(user);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new UserServiceException("Error", e);
		}
	}

}
