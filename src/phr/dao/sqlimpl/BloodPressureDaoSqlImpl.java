package phr.dao.sqlimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import phr.dao.BloodPressureDao;
import phr.dao.UserDao;
import phr.exceptions.DataAccessException;
import phr.exceptions.EntryNotFoundException;
import phr.tools.ImageHandler;
import phr.web.models.BloodPressure;
import phr.web.models.FBPost;
import phr.web.models.PHRImage;
import phr.web.models.PHRImageType;

@Repository("bloodPressureDao")
public class BloodPressureDaoSqlImpl extends BaseDaoSqlImpl implements
		BloodPressureDao {

	@Autowired
	UserDao userDao;

	@Override
	public void add(BloodPressure bloodPressure) throws DataAccessException {
		try {
			Connection conn = getConnection();
			String query = "INSERT INTO bloodpressuretracker(systolic, diastolic, dateAdded, status, userID, fbPostID, photo) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt;

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, bloodPressure.getSystolic());
			pstmt.setInt(2, bloodPressure.getDiastolic());
			pstmt.setTimestamp(3, bloodPressure.getTimestamp());
			pstmt.setString(4, bloodPressure.getStatus());
			pstmt.setInt(5, bloodPressure.getUserID());
			if (bloodPressure.getFbPost() != null)
				pstmt.setInt(6, bloodPressure.getFbPost().getId());
			else
				pstmt.setInt(6, -1);
			if (bloodPressure.getImage().getFileName() == null) {
				String encodedImage = bloodPressure.getImage()
						.getEncodedImage();
				String fileName = ImageHandler
						.saveImage_ReturnFilePath(encodedImage);
				bloodPressure.getImage().setFileName(fileName);
			}
			pstmt.setString(7, bloodPressure.getImage().getFileName());

			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}
	}

	@Override
	public void edit(BloodPressure object) throws DataAccessException,
			EntryNotFoundException {
		try {
			Connection conn = getConnection();
			String query = "UPDATE bloodpressuretracker SET systolic = ?, diastolic = ?, dateAdded = ?, status=?, photo=?"
					+ "WHERE id = ?";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, object.getSystolic());
			pstmt.setInt(2, object.getDiastolic());
			pstmt.setTimestamp(3, object.getTimestamp());
			pstmt.setString(4, object.getStatus());
			pstmt.setString(5, object.getImage().getFileName());
			pstmt.setInt(6, object.getEntryID());

			pstmt.executeUpdate();

		} catch (Exception e) {
			throw new EntryNotFoundException(
					"Object ID not found in the database", e);
		}
	}

	@Override
	public void delete(BloodPressure object) throws DataAccessException,
			EntryNotFoundException {
		try {
			Connection conn = getConnection();
			String query = "DELETE FROM bloodpressuretracker WHERE id = ?";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, object.getEntryID());

			pstmt.executeUpdate();

		} catch (Exception e) {
			throw new EntryNotFoundException(
					"Object ID not found in the database", e);
		}
	}

	@Override
	public ArrayList<BloodPressure> getAll(String userAccessToken)
			throws DataAccessException {

		ArrayList<BloodPressure> bloodpressures = new ArrayList<BloodPressure>();
		try {
			Connection conn = getConnection();
			String query = "SELECT fbPostID, systolic, diastolic, status, photo, dateAdded FROM bloodpressuretracker WHERE userID = ?";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, userDao.getUserIDGivenAccessToken(userAccessToken));

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				PHRImage image = new PHRImage(rs.getString("photo"),
						PHRImageType.FILENAME);
				bloodpressures.add(new BloodPressure(new FBPost(rs
						.getInt("fbPostID")), rs.getTimestamp("dateAdded"), rs
						.getString("status"), image, rs.getInt("systolic"), rs
						.getInt("diastolic")));
			}
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}

		return bloodpressures;
	}

	@Override
	public Integer getEntryId(BloodPressure bloodPressure)
			throws DataAccessException {

		try {
			Connection conn = getConnection();
			String query = "SELECT id FROM bloodpressuretracker WHERE "
					+ "userID = ? AND dateAdded = ?";
			PreparedStatement pstmt;

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, bloodPressure.getUserID());
			pstmt.setTimestamp(2, bloodPressure.getTimestamp());

			ResultSet rs = pstmt.executeQuery();

			if (rs.next())
				return rs.getInt(1);
			else
				return null;
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}

	}

}
