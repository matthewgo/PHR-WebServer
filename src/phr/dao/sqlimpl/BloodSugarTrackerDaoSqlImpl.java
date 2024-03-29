package phr.dao.sqlimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import phr.dao.BloodSugarTrackerDao;
import phr.dao.UserDao;
import phr.exceptions.DataAccessException;
import phr.exceptions.EntryNotFoundException;
import phr.models.BloodSugar;
import phr.models.FBPost;
import phr.models.PHRImage;
import phr.models.PHRImageType;
import phr.tools.ImageHandler;

@Repository("bloodSugarTrackerDao")
public class BloodSugarTrackerDaoSqlImpl extends BaseDaoSqlImpl implements BloodSugarTrackerDao {

	//@Autowired
	//UserDao userDao;
	
	UserDao userDao = new UserDaoSqlImpl();

	@Override
	public int addReturnsEntryID(BloodSugar bloodSugar)
			throws DataAccessException {
		try {
			Connection conn = getConnection();
			String query = "INSERT INTO bloodsugartracker (bloodsugar, type,  dateAdded, status, userID, facebookID, photo) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt;

			pstmt = conn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setDouble(1, bloodSugar.getBloodSugar());
			pstmt.setString(2, bloodSugar.getType());
			pstmt.setTimestamp(3, bloodSugar.getTimestamp());
			pstmt.setString(4, bloodSugar.getStatus());
			pstmt.setInt(5, bloodSugar.getUserID());
			if (bloodSugar.getFacebookID() != null)
				pstmt.setString(6, bloodSugar.getFacebookID());
			else
				pstmt.setNull(6, Types.NULL);
			
			if(bloodSugar.getImage()!= null){
				String encodedImage = bloodSugar.getImage().getEncodedImage();
				String fileName = ImageHandler
						.saveImage_ReturnFilePath(encodedImage);
				bloodSugar.getImage().setFileName(fileName);
				pstmt.setString(7, bloodSugar.getImage().getFileName());
			}
			else
				pstmt.setString(7, null);

			pstmt.executeUpdate();

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
	public void edit(BloodSugar bloodSugar) throws DataAccessException,
			EntryNotFoundException {
		try {
			Connection conn = getConnection();
			String query = "UPDATE bloodsugartracker SET bloodsugar = ?, type = ?,  dateAdded = ?, status=?, photo=? "
					+ "WHERE id = ?";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);
			pstmt.setDouble(1, bloodSugar.getBloodSugar());
			pstmt.setString(2, bloodSugar.getType());
			pstmt.setTimestamp(3, bloodSugar.getTimestamp());
			pstmt.setString(4, bloodSugar.getStatus());
			if (bloodSugar.getImage() != null) {
				String encodedImage = bloodSugar.getImage()
						.getEncodedImage();
				String fileName = ImageHandler
						.saveImage_ReturnFilePath(encodedImage);
				bloodSugar.getImage().setFileName(fileName);
				pstmt.setString(5, bloodSugar.getImage().getFileName());
			}
			else
				pstmt.setNull(5, Types.NULL);
			pstmt.setInt(6, bloodSugar.getEntryID());

			pstmt.executeUpdate();

			conn.close();
		} catch (Exception e) {
			throw new EntryNotFoundException(
					"Object ID not found in the database", e);
		}

	}

	@Override
	public void delete(BloodSugar bloodSugar) throws DataAccessException,
			EntryNotFoundException {
		try {
			Connection conn = getConnection();
			String query = "DELETE FROM bloodsugartracker WHERE id = ?";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, bloodSugar.getEntryID());

			pstmt.executeUpdate();

			conn.close();
		} catch (Exception e) {
			throw new EntryNotFoundException(
					"Object ID not found in the database", e);
		}
	}

	@Override
	public List<BloodSugar> getAll(String userAccessToken)
			throws DataAccessException {
		
		List<BloodSugar> bloodsugars = new ArrayList<BloodSugar>();
		try {
			Connection conn = getConnection();
			String query = "SELECT id, facebookID, bloodsugar, type,  status, photo, dateAdded FROM bloodsugartracker WHERE userID = ?";

			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, userDao.getUserIDGivenAccessToken(userAccessToken));

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				PHRImage image = null;
				if(rs.getString("photo") == null)
					image = null;
				else{
					String encodedImage = ImageHandler.getEncodedImageFromFile(rs.getString("photo"));
					image = new PHRImage(encodedImage, PHRImageType.IMAGE);
				}
				bloodsugars.add(new BloodSugar(rs.getInt("id"), rs.getString("facebookID"), rs.getTimestamp("dateAdded"), rs
						.getString("status"), image,
						rs.getDouble("bloodsugar"), rs.getString("type")));
			}
			conn.close();
		} catch (Exception e) {
			throw new DataAccessException(
					"An error has occured while trying to access data from the database",
					e);
		}

		return bloodsugars;
	}

	@Override
	public Integer getEntryId(BloodSugar bloodSugar) throws DataAccessException {
		try {
			Connection conn = getConnection();
			String query = "SELECT id FROM bloodsugartracker WHERE "
					+ "userID = ? AND dateAdded = ?";
			PreparedStatement pstmt;

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, bloodSugar.getUserID());
			pstmt.setTimestamp(2, bloodSugar.getTimestamp());

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
}
