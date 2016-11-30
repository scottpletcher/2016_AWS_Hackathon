package com.jecapps.dumpMe;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import javax.json.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
public class MainController {

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ModelAndView defaultPage() {
		ModelAndView mav = new ModelAndView("redirect:/dashboard");
		return mav;
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboardPage() {
		ArrayList<JSONObject> addressesList = null;
		String[] result = null;
		try {
			URL url = new URL("https://yeg4tfkkal.execute-api.us-west-2.amazonaws.com/prod/address");
			InputStream input = url.openStream();
			addressesList = ReadJSON(input, "UTF-8");
		} catch (IOException d) {
			System.out.println(d);
		}
		ArrayList<String> addresses = new ArrayList<String>();
		ArrayList<String> deviceId = new ArrayList<String>();
		for (JSONObject obj : addressesList) {
			JSONObject something = obj;
			for (JSONObject address : (ArrayList<JSONObject>) something.get("Items")) {
				if (address.get("deviceId").toString().toLowerCase().contains("web")) {
					addresses.add((String) address.get("streetAddress"));
					deviceId.add((String) address.get("deviceId"));
				}
			}

		}

		ModelAndView model = new ModelAndView();
		model.addObject("addresses", addresses);
		model.addObject("devices", deviceId);
		model.setViewName("dashboard");
		return model;

	}

	@RequestMapping(value = "/schedule", params = { "schedule" }, method = RequestMethod.POST)
	public ModelAndView schedule(@RequestParam("address") String address, @RequestParam("type") String type,
			@RequestParam(value = "schedule") String buttonType) {

		if (buttonType.equals("Schedule Pickup")) {

			try {
				String url = "https://yeg4tfkkal.execute-api.us-west-2.amazonaws.com/prod/request";
				URL object = new URL(url);

				HttpURLConnection con = (HttpURLConnection) object.openConnection();
				con.setDoOutput(true);
				con.setDoInput(true);
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestProperty("Accept", "application/json");
				con.setRequestMethod("POST");

				JsonObject request = Json.createObjectBuilder().add("deviceId", address).add("deviceType", type)
						.add("status", "requested").build();

				OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
				wr.write(request.toString());
				wr.flush();

				// display what returns the POST request

				StringBuilder sb = new StringBuilder();
				int HttpResult = con.getResponseCode();
				if (HttpResult == HttpURLConnection.HTTP_OK) {
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
					String line = null;
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
					System.out.println("" + sb.toString());
				} else {
					System.out.println(con.getResponseMessage());
				}
			} catch (IOException ex) {
				System.out.println(ex);
			}

			ModelAndView model = new ModelAndView();
			model.addObject("address", address);
			model.addObject("type", type);
			model.setViewName("scheduleResp");

			return model;

		} else if (buttonType.equals("Get History")) {
			ArrayList<JSONObject> addressesList = null;
			ArrayList<JSONObject> scheduleHistory = null;
			String[] result = null;
			try {
				URL url = new URL("https://yeg4tfkkal.execute-api.us-west-2.amazonaws.com/prod/address");
				InputStream input = url.openStream();
				addressesList = ReadJSON(input, "UTF-8");
			} catch (IOException d) {
				System.out.println(d);
			}
			ArrayList<String> deviceIds = new ArrayList<String>();
			for (JSONObject obj : addressesList) {
				JSONObject something = obj;
				System.out.println(address);
				for (JSONObject address1 : (ArrayList<JSONObject>) something.get("Items")) {
					if (address1.get("streetAddress").toString().contains(address)) {
						deviceIds.add((String) address1.get("deviceId"));
						System.out.println(address1.get("deviceId"));

					}
					else 
						System.out.println(address1.get("streetAddress"));
				}

			}
			ArrayList<String> timeHist = new ArrayList<String>();
			ArrayList<String> typeHist = new ArrayList<String>();
			for (String ID : deviceIds) {
				try {
					URL url = new URL("https://yeg4tfkkal.execute-api.us-west-2.amazonaws.com/history/" + ID);
					InputStream input = url.openStream();
					scheduleHistory = ReadJSON(input, "UTF-8");

					for (JSONObject obj : scheduleHistory) {
						JSONObject something = obj;
						for (JSONObject ids : (ArrayList<JSONObject>) something.get("Items")) {
							timeHist.add((String) ids.get("timestamp"));
							typeHist.add((String) ids.get("deviceType"));
						}

					}
				} catch (IOException d) {
					System.out.println(d);
				}
			}

			ModelAndView model = new ModelAndView();
			model.addObject("time", timeHist);
			model.addObject("type", typeHist);
			model.setViewName("scheduleHist");

			return model;
		} else
			throw new IllegalArgumentException("Need either approve or deny!");

	}

	// for 403 access denied page
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied() {

		ModelAndView model = new ModelAndView();

		// check if user is login
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			model.addObject("username", userDetail.getUsername());
		}

		model.setViewName("403");
		return model;

	}

	public static synchronized ArrayList<JSONObject> ReadJSON(InputStream input, String Encoding) {
		Scanner scn = new Scanner(input, Encoding);
		ArrayList<JSONObject> json = new ArrayList<JSONObject>();
		// Reading and Parsing Strings to Json
		while (scn.hasNext()) {
			JSONObject obj = null;
			try {
				obj = (JSONObject) new JSONParser().parse(scn.nextLine());
			} catch (org.json.simple.parser.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			json.add(obj);
		}
		// Here Printing Json Objects
		return json;
	}

}