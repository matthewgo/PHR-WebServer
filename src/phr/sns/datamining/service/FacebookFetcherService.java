package phr.sns.datamining.service;

import java.sql.Timestamp;
import java.util.List;

import phr.exceptions.SNSException;
import phr.models.FBPost;

public interface FacebookFetcherService {

	public List<FBPost> getNewPostsAfterDate(Timestamp timestamp,
			String userFBAccessToken, String userAccessToken)
			throws SNSException;

}
