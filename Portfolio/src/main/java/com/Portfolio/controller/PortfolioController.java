package com.Portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortfolioController {

	@GetMapping("")
	public String root() {
		return "redirect:index";
	}
	@GetMapping("index")
	public void index() {
		
	}
}
