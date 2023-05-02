package com.Portfolio.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Portfolio.dto.MemberDTO;
import com.Portfolio.entity.Member;
import com.Portfolio.repository.MemberRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MemberService {

	@Autowired
	public MemberRepository kakaoRepository;
	
	public String getAccessToken(String authorize_code) throws Exception {
		String access_Token = "";
		String refresh_Token = "";
		String reqURL = "https://kauth.kakao.com/oauth/token";

		try {
			URL url = new URL(reqURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=7226e94e9966b51700e15e1823408600");
			sb.append("&redirect_uri=https://tomcat.jikwang.net/Portfolio/oauth");
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

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
			});

			access_Token = jsonMap.get("access_token").toString();
			refresh_Token = jsonMap.get("refresh_token").toString();

			System.out.println("access_token : " + access_Token);
			System.out.println("refresh_token : " + refresh_Token);

			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return access_Token;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getUserInfo(String access_Token) throws Throwable {
		// 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
		HashMap<String, Object> userInfo = new HashMap<String, Object>();
		String reqURL = "https://kapi.kakao.com/v2/user/me";

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

			try {
				// jackson objectmapper 객체 생성
				ObjectMapper objectMapper = new ObjectMapper();
				// JSON String -> Map
				Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
				});

				System.out.println(jsonMap.get("properties"));

				Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
				Map<String, Object> kakao_account = (Map<String, Object>) jsonMap.get("kakao_account");

				// System.out.println(properties.get("nickname"));
				// System.out.println(kakao_account.get("email"));

				String nickname = properties.get("nickname").toString();
				String profileImg = properties.get("profile_image").toString();
				//String email = kakao_account.get("email").toString();

				userInfo.put("profileImg", profileImg);
				userInfo.put("nickname", nickname);
				//userInfo.put("email", email);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return userInfo;
	}

	public MemberDTO findByEmail(String email) {
		Member member = kakaoRepository.getMemberWithEmail(email);
		if(member != null) {
		return entityToDto(member);
		}else {
			return null;
		}
	}
	@Transactional
	public MemberDTO register(MemberDTO dto) {
		Member member = dtoToEntity(dto);
		Member result = kakaoRepository.save(member);
		return entityToDto(result);
	}
	
	public MemberDTO modify(MemberDTO dto) {
		Member member = kakaoRepository.findById(dto.getEmail()).get();
		member.setShowEmail(dto.getShowEmail());
		member.setGitUrl(dto.getGitUrl());
		member.setTel(dto.getTel());
		member.setIntro(dto.getIntro());
		member.setSkills(dto.getSkills());
		kakaoRepository.save(member);
		return entityToDto(member);
	}

	public MemberDTO login(String email, String password) {
		Optional<Member> res = kakaoRepository.findById(email);
		if(res.isPresent()) {
		if(res.get() == null) {
			return null;
		}else {
			if(res.get().getPassword().equals(password)) {
				return entityToDto(res.get());
			}else {
				return null;
			}
		}
		}else {
			return null;
		}
		
	}

	public List<MemberDTO> findAll() {
		List<MemberDTO> result = new ArrayList<>();
		kakaoRepository.findAll().forEach(entity -> result.add(entityToDto(entity)));
		return result;
	}

	public void deleteById(String email) {
		kakaoRepository.deleteById(email);
	}
	private MemberDTO entityToDto(Member member) {
		MemberDTO dto = MemberDTO.builder()
		.email(member.getEmail())
		.password(member.getPassword())
		.name(member.getName())
		.tel(member.getTel())
		.showEmail(member.getShowEmail())
		.gitUrl(member.getGitUrl())
		.skills(member.getSkills())
		.intro(member.getIntro())
		.profileImg(member.getProfileImg())
		.build();
		return dto;
	}
	private Member dtoToEntity(MemberDTO dto) {
		Member entity = Member.builder()
		.email(dto.getEmail())
		.name(dto.getName())
		.tel(dto.getTel())
		.intro(dto.getIntro())
		.showEmail(dto.getShowEmail())
		.gitUrl(dto.getGitUrl())
		.skills(dto.getSkills())
		.password(dto.getPassword())
		.profileImg(dto.getProfileImg())
		.build();
		return entity;
	}

	

}
