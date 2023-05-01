package com.Portfolio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import com.Portfolio.dto.BoardDTO;
import com.Portfolio.entity.Board;
import com.Portfolio.entity.Comments;
import com.Portfolio.entity.Member;
import com.Portfolio.repository.BoardRepository;
import com.Portfolio.repository.CommentsRepository;
import com.Portfolio.service.BoardService;

@SpringBootTest
public class BoardTest {

	@Autowired
	BoardService service;
	
//	@Test
//	void boardRegister() {
//		BoardDTO boardDTO = BoardDTO.builder()
//					.email("kakao_유현")
//					.content("게시글 작성 테스트 내용")
//					.title("게시글 작성 테스트 제목")
//					.name("유현")
//					.build();
//		service.register(boardDTO);
//	}
	@Test
	void boardDelete() {

		service.removeWithComments(33L);
	}
//	
//	@Test
//	void searchTest() {
//		
//	}
}
