/**
 * 
 */
package com.example.demo.controller;

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

/**
 * @author User
 *
 */

@Controller
@RequestMapping("/")
public class IndexController {

	@GetMapping
	def index() {
		"index"
	}

	@GetMapping("/hello")
	@ResponseBody
	def hello() {
		"hello"		
		}
	
}
