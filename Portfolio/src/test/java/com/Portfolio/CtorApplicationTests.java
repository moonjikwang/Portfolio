package com.Portfolio;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Portfolio.dto.ProjectDTO;
import com.Portfolio.entity.Board;
import com.Portfolio.entity.Member;
import com.Portfolio.repository.BoardRepository;
import com.Portfolio.service.ProjectService;

@SpringBootTest
class CtorApplicationTests {

	@Autowired
	ProjectService projectService;
	
	@Test
	void contextLoads() {
		System.out.println(projectService.addProject(ProjectDTO.builder().title("gdgd").email("Github_60185586").build()));
		}
	

}
