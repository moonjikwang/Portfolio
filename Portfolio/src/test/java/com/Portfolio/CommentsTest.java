package com.Portfolio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Portfolio.entity.Board;
import com.Portfolio.entity.Comments;
import com.Portfolio.entity.Member;
import com.Portfolio.repository.BoardRepository;
import com.Portfolio.repository.CommentsRepository;

@SpringBootTest
public class CommentsTest {

	@Autowired
	CommentsRepository commentsRepository;
	
	@Test
	void commentsRegister() {
		for(int i = 1; i < 50; i++) {
			Comments comments = Comments.builder()
					.text("댓글내용"+i)
					.replyer("kakao_문지광")
					.board(Board.builder().bno((long) Math.floor(Math.random() * 31)).build())
					.writer(Member.builder().email("kakao_문지광").build())
					.build();
			commentsRepository.save(comments);
							
		}
	}
}
