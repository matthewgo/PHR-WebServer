package com.example.dao.sqlimpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.example.exceptions.DataAccessException;

public abstract class BaseDaoSqlImpl {

	protected Connection getConnection() throws DataAccessException {
		String url = "jdbc:mysql://localhost:3306/phr";
		String username = "root";
		String password = "";
		Connection conn;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			throw new DataAccessException("Error in establishing connection", e);
		}
		return conn;
	}
}
