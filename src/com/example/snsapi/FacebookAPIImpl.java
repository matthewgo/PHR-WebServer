package com.example.snsapi;

import java.util.List;

import com.example.exceptions.HttpRequestException;
import com.example.exceptions.SNSException;
import com.example.tools.HttpRequestHandler;

public class FacebookAPIImpl implements FacebookAPI {

	public static final String fbGraphUrl = "https://www.graph.facebook.com/";

	@Override
	public List<Object> getPosts(String accessToken) throws SNSException {
		String url = fbGraphUrl + "me?posts&access_token=" + accessToken;
		try {
			String response = HttpRequestHandler.performHttpRequestGet(url);
			return null;
		} catch (HttpRequestException e) {
			throw new SNSException("Error in retrieving from Facebook", e);
		}

	}

}
