package com.Portfolio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Portfolio.dto.FeedbackDTO;
import com.Portfolio.entity.Feedback;
import com.Portfolio.entity.Member;
import com.Portfolio.repository.FeedbackRepository;
import com.Portfolio.repository.MemberRepository;

@Service
public class FeedbackService {

	@Autowired
	FeedbackRepository feedbackRepository;
	@Autowired
	MemberRepository memberRepository;
	
	public void writeFeedback(FeedbackDTO dto) {
		feedbackRepository.save(dtoToEntity(dto));
	}
	public void removeFeedback(Long no) {
		feedbackRepository.deleteById(no);
	}
	
	public List<FeedbackDTO> getList(String email) {
		Member member = memberRepository.findById(email).get();
		List<Feedback> list = feedbackRepository.findByWriter(member);
		List<FeedbackDTO> result = new ArrayList<>();
		list.forEach(entity->{
			result.add(entityToDto(entity));
		});
		return result;
	}
	
	private FeedbackDTO entityToDto(Feedback entity) {
		return FeedbackDTO.builder()
				.no(entity.getNo())
				.email(entity.getWriter().getEmail())
				.replyer(entity.getReplyer())
				.content(entity.getContent())
				.regDate(entity.getRegDate())
				.modDate(entity.getModDate())
				.build();
	}
	
	private Feedback dtoToEntity(FeedbackDTO dto) {
		return Feedback.builder()
				.writer(Member.builder().email(dto.getEmail()).build())
				.content(dto.getContent())
				.replyer(dto.getReplyer())
				.build();
	}
}
