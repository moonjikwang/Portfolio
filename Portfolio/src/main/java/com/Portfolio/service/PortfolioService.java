package com.Portfolio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Portfolio.dto.PortfolioDTO;
import com.Portfolio.dto.ProjectDTO;
import com.Portfolio.entity.Member;
import com.Portfolio.entity.Project;
import com.Portfolio.repository.MemberRepository;
import com.Portfolio.repository.ProjectRepository;

@Service
public class PortfolioService extends ProjectService{

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	ProjectRepository projectRepository;
	
	public List<PortfolioDTO> getList() {
		List<Member> list = memberRepository.findByStateTrue();
		List<PortfolioDTO> portfolio = new ArrayList<>();
		list.forEach(member->{
			List<ProjectDTO> projectDTOs = new ArrayList<>();
			List<Project> projects = projectRepository.findByWriter_Email(member.getEmail());
			projects.forEach(entities->{
				projectDTOs.add(entityToDTO(entities));
			});//project entity to DTO
			PortfolioDTO dto = PortfolioDTO.builder()
									.projects(projectDTOs)
									.email(member.getEmail())
									.name(member.getName())
									.profileImg(member.getProfileImg())
									.tel(member.getTel())
									.gitUrl(member.getGitUrl())
									.showEmail(member.getShowEmail())
									.skills(member.getSkills())
									.intro(member.getIntro())
									.regDate(member.getRegDate())
									.modDate(member.getModDate())
									.build();
			portfolio.add(dto);
		});
		return portfolio;
	}
}
