package com.Portfolio.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import com.Portfolio.dto.MemberDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@PropertySource("classpath:application.properties")
public class GithubService {

	@Value("${github.client.id}")
	private String githubClientId;

	@Value("${github.client.secret}")
	private String githubClientSecret;

	public String getAccessToken(String authorize_code) throws Exception {
		String accessTokenValue = "";
		String reqURL = "https://github.com/login/oauth/access_token";
		try {
			URL url = new URL(reqURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuilder sb = new StringBuilder();
			sb.append("&client_id=" + githubClientId);
			sb.append("&client_secret=" + githubClientSecret);
			sb.append("&code=" + authorize_code);
			bw.write(sb.toString());
			bw.flush();
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String result = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response body : " + result);

			String[] keyValuePairs = result.split("&");

			for (String pair : keyValuePairs) {
				if (pair.startsWith("access_token=")) {
					int startIdx = pair.indexOf("access_token=") + "access_token=".length();
					accessTokenValue = pair.substring(startIdx);
					break;
				}
			}

			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accessTokenValue;
	}

	@SuppressWarnings("unchecked")
	public MemberDTO getUserInfo(String access_Token) throws Throwable {
		String reqURL = "https://api.github.com/user";
		MemberDTO dto = null;
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			// 요청에 필요한 Header에 포함될 내용
			conn.setRequestProperty("Authorization", "Bearer " + access_Token);

			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line = "";
			String result = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response body : " + result);
			System.out.println("result type" + result.getClass().getName()); // java.lang.String

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(result);
			String name = jsonNode.get("name").asText();
			String id = jsonNode.get("id").asText();
			String profile = jsonNode.get("avatar_url").asText();

			dto = MemberDTO.builder().email("Github_" + id).name(name).profileImg(profile).build();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return dto;
	}

}
