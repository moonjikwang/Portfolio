package com.Portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Portfolio.dto.FeedbackDTO;
import com.Portfolio.service.FeedbackService;

@RestController
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;
	
	@PostMapping("/addFeedback")
	public void addFeedback(FeedbackDTO dto) {
		feedbackService.register(dto);
	}
}
