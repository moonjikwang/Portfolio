package com.Portfolio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Portfolio.dto.ProjectDTO;
import com.Portfolio.entity.Member;
import com.Portfolio.entity.Project;
import com.Portfolio.repository.MemberRepository;
import com.Portfolio.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	MemberRepository memberRepository;
	
	public List<ProjectDTO> getList(String email){
		List<Project> entities = projectRepository.findByWriter_Email(email);
		List<ProjectDTO> result = new ArrayList<>();
		entities.forEach(entity->{
			ProjectDTO dto = entityToDTO(entity);
			result.add(dto);
		});
		return result;
	}
	
	public boolean addProject(ProjectDTO dto) {
		Project entity = dtoToEntity(dto);
		System.out.println(entity);
		Project result = projectRepository.save(entity);
		return result != null;
	}
	
	private Project dtoToEntity(ProjectDTO dto) {
		return Project.builder()
				.title(dto.getTitle())
				.date(dto.getDate())
				.introduce(dto.getIntroduce())
				.skill(dto.getSkill())
				.description(dto.getDescription())
				.webLink(dto.getWebLink())
				.gitLink(dto.getGitLink())
				.imgUrl(dto.getImgUrl())
				.writer(Member.builder().email(dto.getEmail()).build())
				.build();
	}
	private ProjectDTO entityToDTO(Project entity) {
		return ProjectDTO.builder()
				.title(entity.getTitle())
				.date(entity.getDate())
				.description(entity.getDescription())
				.introduce(entity.getIntroduce())
				.skill(entity.getSkill())
				.webLink(entity.getWebLink())
				.gitLink(entity.getGitLink())
				.imgUrl(entity.getImgUrl())
				.email(entity.getWriter().getEmail())
				.build();
	}
}
