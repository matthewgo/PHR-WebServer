package phr.dao;

import java.util.List;

import phr.exceptions.DataAccessException;
import phr.exceptions.EntryNotFoundException;
import phr.models.FBPost;
import phr.models.UnverifiedActivityEntry;
import phr.models.UnverifiedFoodEntry;
import phr.models.UnverifiedRestaurantEntry;
import phr.models.UnverifiedSportsEstablishmentEntry;

public interface VerificationDao {

	public void addNewUnverifiedPosts(String userAccessToken,
			List<FBPost> newFbPosts) throws DataAccessException;

	public List<UnverifiedFoodEntry> getAllUnverifiedFoodPosts(
			String userAccessToken) throws DataAccessException;

	public List<UnverifiedActivityEntry> getAllUnverifiedActivityPosts(
			String userAccessToken) throws DataAccessException;

	public List<UnverifiedRestaurantEntry> getAllUnverifiedRestaurantPosts(
			String userAccessToken) throws DataAccessException;

	public List<UnverifiedSportsEstablishmentEntry> getAllUnverifiedSportsEstablishmentPosts(
			String userAccessToken) throws DataAccessException;

	public void delete(String accessToken, UnverifiedFoodEntry entry)
			throws EntryNotFoundException;

	public void delete(String accessToken, UnverifiedActivityEntry entry)
			throws EntryNotFoundException;

	public void delete(String accessToken, UnverifiedRestaurantEntry entry)
			throws EntryNotFoundException;

	public void delete(String accessToken, UnverifiedSportsEstablishmentEntry entry)
			throws EntryNotFoundException;
	
	public UnverifiedFoodEntry getUnverifiedFoodPost( UnverifiedFoodEntry entry) throws DataAccessException;
			
	public UnverifiedActivityEntry getUnverifiedActivityPost(UnverifiedActivityEntry entry) throws DataAccessException;
				
	public UnverifiedRestaurantEntry getUnverifiedRestaurantPost(UnverifiedRestaurantEntry entry) throws DataAccessException;
				
	public UnverifiedSportsEstablishmentEntry getUnverifiedSportsEstablishmentPost(UnverifiedSportsEstablishmentEntry entry) throws DataAccessException;

}
