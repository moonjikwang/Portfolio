package com.Portfolio;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Portfolio.entity.Board;
import com.Portfolio.entity.Member;
import com.Portfolio.repository.BoardRepository;

@SpringBootTest
class CtorApplicationTests {

	@Autowired
	BoardRepository boardRepository;
	
	@Test
	void contextLoads() {
		for(int i = 1; i < 30; i++) {
			Board board = Board.builder()
			.writer(Member.builder().email("kakao_문지광").build())
			.title("테스트 제목 " + i)
			.content("테스트 내용 "+ i)
			.build();
			boardRepository.save(board);
		}
	}

}
