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
	public int addReturnEntryID(Activity activity) throws DataAccessException {

		if (activity.getEntryID() != null) {
			incrementCountUsed(activity);
			return activity.getEntryID();
		}
		int entryID = ActivityEntryExistsReturnEntryID(activity);
		if (entryID != -1) {

			incrementCountUsed(activity);

			return entryID;
		} else {

			try {
				Connection conn = getConnection();
				String query = "INSERT INTO activitylist(name, MET, countUsed) VALUES (?, ?, ?)";
				PreparedStatement pstmt;

				pstmt = conn.prepareStatement(query,
						Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, activity.getName());
				pstmt.setDouble(2, activity.getMET());
				pstmt.setInt(3, 1);

				pstmt.executeUpdate();

				ResultSet rs = pstmt.getGeneratedKeys();

				if (rs.next())
					entryID = rs.getInt(1);
				conn.close();
				return entryID;

			} catch (Exception e) {
				throw new DataAccessException(
						"An error has occured while trying to access data from the database",
						e);
			}
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
						.getString("name"), rs.getDouble("MET"), rs
						.getInt("countUsed")));
			}
			conn.close();
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
			conn.close();
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}

		return activity;
	}

	@Override
	public List<Activity> search(String searchQuery) throws DataAccessException {

		List<Activity> activities = new ArrayList<Activity>();

		try {
			Connection conn = getConnection();
			String query = "SELECT * FROM activityList WHERE name LIKE ? ORDER BY countUsed DESC";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%" + searchQuery + "%");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				activities.add(new Activity(rs.getInt("id"), rs
						.getString("name"), rs.getDouble("MET"), rs
						.getInt("countUsed")));
			}
			conn.close();
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}

		return activities;
	}

	public void incrementCountUsed(Activity activity)
			throws DataAccessException {
		try {
			Connection conn = getConnection();
			String query = "UPDATE activityList SET countUsed = countUsed + 1 WHERE id = ? ";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, activity.getEntryID());

			pstmt.executeUpdate();

			conn.close();
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}
	}

	@Override
	public int ActivityEntryExistsReturnEntryID(Activity activity)
			throws DataAccessException {
		try {
			Connection conn = getConnection();
			String query = "SELECT * FROM activitylist WHERE "
					+ "name = ?, MET = ?, countUsed = ? ";
			PreparedStatement pstmt;

			pstmt = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, activity.getName());
			pstmt.setDouble(2, activity.getMET());
			pstmt.setInt(3, activity.getCountUsed());

			ResultSet rs = pstmt.getGeneratedKeys();

			int entryID = -1;
			if (rs.next())
				entryID = rs.getInt(1);

			conn.close();
			return entryID;

		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}
	}

	@Override
	public Double getActivityMET(String activityName)
			throws DataAccessException {

		Double MET = -1.0;

		try {
			Connection conn = getConnection();
			String query = "SELECT act.MET FROM activitylist act LEFT outer JOIN activitycorpus corpus ON act.id = corpus.activityID WHERE wordTenses = ? OR act.name = ?";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, activityName);
			pstmt.setString(2, activityName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				MET = rs.getDouble("MET");
			}
			conn.close();
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}

		return MET;
	}

	@Override
	public List<Activity> getActivityListGivenGymName(String gymName)
			throws DataAccessException {

		List<Activity> activities = new ArrayList<Activity>();

		try {
			Connection conn = getConnection();
			String query = "SELECT act.* FROM activitylist act JOIN gym_activity gym ON act.id = gym.activityID WHERE gym.gymID = ?";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, getGymID(gymName));

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				activities.add(new Activity(rs.getInt("id"), rs
						.getString("name"), rs.getDouble("MET"), rs
						.getInt("countUsed")));
			}
			conn.close();
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}
		return activities;
	}

	@Override
	public Integer getActivityID(String activityName)
			throws DataAccessException {

		try {
			Connection conn = getConnection();
			String query = "SELECT id FROM activityList WHERE name = ?";
			PreparedStatement pstmt;

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, activityName);

			ResultSet rs = pstmt.executeQuery();
			Integer id = null;
			if (rs.next())
				id = rs.getInt(1);

			conn.close();
			return id;
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}
	}

	@Override
	public Activity getActivityGivenName(String searchQuery)
			throws DataAccessException {
		Activity activity = null;
		try {
			Connection conn = getConnection();
			String query = "SELECT act.* FROM activitylist act LEFT outer JOIN  activitycorpus corpus ON act.id = corpus.activityID WHERE wordTenses = ? OR act.name = ?";
			PreparedStatement pstmt;

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, searchQuery);
			pstmt.setString(2, searchQuery);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				activity = new Activity(rs.getInt("id"));
				activity.setName(rs.getString("name"));
				activity.setMET(rs.getDouble("MET"));
				activity.setCountUsed(rs.getInt("countUsed"));
			}
			conn.close();
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}

		return activity;
	}

	@Override
	public Integer getGymID(String gymName) throws DataAccessException {

		try {
			Connection conn = getConnection();
			String query = "SELECT id FROM gymList WHERE name = ?";
			PreparedStatement pstmt;

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, gymName);

			ResultSet rs = pstmt.executeQuery();

			Integer id = null;
			if (rs.next())
				id = rs.getInt(1);

			conn.close();
			return id;
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}
	}

	@Override
	public void delete(Activity activity) {
		// TODO Auto-generated method stub

	}

}
