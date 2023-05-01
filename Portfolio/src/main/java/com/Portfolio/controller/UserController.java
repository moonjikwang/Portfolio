package com.Portfolio.controller;

import org.springframework.stereotype.Controller;

import com.Portfolio.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
}
