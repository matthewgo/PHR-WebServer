package phr.dao.sqlimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import phr.dao.ActivityDao;
import phr.exceptions.DataAccessException;
import phr.models.Activity;

@Repository("activityDao")
public class ActivityDaoSqlImpl extends BaseDaoSqlImpl implements ActivityDao {

	@Override
	public int addActivityListEntryReturnEntryID(Activity activity)
			throws DataAccessException {

		try {
			Connection conn = getConnection();
			String query = "INSERT INTO activitylist(name, MET, countUsed) VALUES (?, ?, ?)";
			PreparedStatement pstmt;

			pstmt = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, activity.getName());
			pstmt.setDouble(2, activity.getMET());
			pstmt.setInt(3, 0);

			pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();

			int entryID = -1;
			if (rs.next())
				entryID = rs.getInt(1);

			return entryID;

		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}

	}

	@Override
	public List<Activity> getAllActivity() throws DataAccessException {

		List<Activity> activities = new ArrayList<Activity>();

		try {
			Connection conn = getConnection();
			String query = "SELECT id, name, MET FROM activityList";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				activities.add(new Activity(rs.getInt("id"), rs
						.getString("name"), rs.getDouble("MET")));
			}
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}

		return activities;

	}

	@Override
	public Activity getActivity(int entryID) throws DataAccessException {

		Activity activity = new Activity(entryID);

		try {
			Connection conn = getConnection();
			String query = "SELECT name, MET FROM activityList WHERE id = ?";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, entryID);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				activity.setName(rs.getString("name"));
				activity.setMET(rs.getDouble("MET"));
			}
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}

		return activity;
	}

	@Override
	public List<Activity> search(String serachQuery) throws DataAccessException {

		List<Activity> activities = new ArrayList<Activity>();

		try {
			Connection conn = getConnection();
			String query = "SELECT * FROM activityList WHERE name LIKE ? ORDER BY countUsed DESC";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, serachQuery);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				activities.add(new Activity(rs.getInt("id"), rs
						.getString("name"), rs.getDouble("MET")));
			}
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}

		return activities;
	}

	public void incrementCountUsed(Activity activity)
			throws DataAccessException {
		// Change implementation, countused = countused + 1 where id = id
		/*
		 * try { Connection conn = getConnection(); String query =
		 * "UPDATE activityList SET countUsed = ? WHERE id = ? ";
		 * 
		 * PreparedStatement pstmt; pstmt = conn.prepareStatement(query);
		 * pstmt.setInt(1, activity.getCountUsed() + 1); pstmt.setInt(2,
		 * activity.getEntryID());
		 * 
		 * pstmt.executeUpdate();
		 * 
		 * } catch (Exception e) { throw new DataAccessException(
		 * "An error has occured while trying to access data from the database",
		 * e); }
		 */
	}

}
