package com.Portfolio.service;

import java.util.Optional;

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
	MemberRepository memberRepository ;
	
	//변환메서드
	private Feedback dtoToEntity(FeedbackDTO dto) {
	    
	    Member receiver = memberRepository.findById(dto.getReceiver()).get();
	    Member writer = memberRepository.findById(dto.getWriter()).get();
	    return Feedback.builder()
	            .feedbackNo(dto.getFeedbackNo())
	            .comments(dto.getComments())
	            .writer(writer)
	            .receiver(receiver)
	            .build();
	}
	
	private FeedbackDTO entityToDto(Feedback entity) {
		return FeedbackDTO.builder()
				.feedbackNo(entity.getFeedbackNo())
				.comments(entity.getComments())
				.writer( entity.getWriter().getEmail() )
				.receiver(entity.getReceiver().getEmail() )
				.regDate(entity.getRegDate())
				.modDate(entity.getModDate())
				.build();
	}
	
	
	//피드백 등록
	public void register(FeedbackDTO dto) {
		feedbackRepository.save(dtoToEntity(dto));
	}
	//피드백 삭제
	public void remove(long feedbackNo) {
		feedbackRepository.deleteById(feedbackNo);
	}
	//피드백 수정
	public void modify(FeedbackDTO dto) {
		Optional<Feedback> result = feedbackRepository.findById(dto.getFeedbackNo());
		Feedback feedback = result.get();
		feedback.changeComments(dto.getComments());
	}
	
}
