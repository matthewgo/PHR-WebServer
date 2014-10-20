package phr.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import phr.exceptions.JSONConverterException;
import phr.exceptions.ServiceException;
import phr.exceptions.UserServiceException;
import phr.models.UnverifiedFoodEntry;
import phr.service.UserService;
import phr.service.VerificationService;
import phr.tools.GSONConverter;
import phr.tools.JSONParser;
import phr.tools.JSONResponseCreator;

@Controller
public class VerificationController {

	@Autowired
	VerificationService verificationService;

	@Autowired
	UserService userService;

	@RequestMapping("verification/addNewPosts")
	public void addNewPosts(HttpServletRequest request,
			HttpServletResponse response) {

	}

	@RequestMapping("verification/getAllFoods")
	public void getAllFoods(HttpServletRequest request,
			HttpServletResponse response) throws IOException, JSONException {
		PrintWriter writer = response.getWriter();
		JSONObject jsonResponse = null;
		try {
			JSONObject json = GSONConverter.getJSONObjectFromReader(request
					.getReader());
			System.out.println("JSON From Request: " + json);
			JSONObject data = JSONParser.getData(json);
			String accessToken = data.getString("accessToken");
			String username = data.getString("username");
			if (userService.isValidAccessToken(accessToken, username)) {
				List<UnverifiedFoodEntry> unverifiedFoods = verificationService
						.getAllUnverifiedFoodPosts(accessToken);
				JSONArray jsonArray = GSONConverter
						.convertListToJSONArray(unverifiedFoods);

				JSONObject dataForResponse = new JSONObject();
				dataForResponse.put("array", jsonArray);
				jsonResponse = JSONResponseCreator.createJSONResponse(
						"success", dataForResponse,
						"Process has been completed");
			} else {
				JSONObject dataForResponse = new JSONObject();
				dataForResponse.put("isValidAccessToken", "false");
				jsonResponse = JSONResponseCreator
						.createJSONResponse("success", dataForResponse,
								"Access token is invalid, please ask user to log in again.");
			}

		} catch (JSONException | ServiceException | JSONConverterException
				| UserServiceException e) {
			jsonResponse = JSONResponseCreator.createJSONResponse("fail", null,
					"Process cannot be completed, an error has occured in the web server + "
							+ e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Response JSON To Be Sent Back To App: "
				+ jsonResponse);
		writer.write(jsonResponse.toString());

	}

	@RequestMapping("verification/getAllActivities")
	public void getAllActivities(HttpServletRequest request,
			HttpServletResponse response) {

	}

	@RequestMapping("verification/getAllRestaurants")
	public void getAllRestaurants(HttpServletRequest request,
			HttpServletResponse response) {

	}

	@RequestMapping("verification/getAllSportsEstablishments")
	public void getAllSportsEstablishments(HttpServletRequest request,
			HttpServletResponse response) {

	}

	@RequestMapping("verification/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) {

	}
}