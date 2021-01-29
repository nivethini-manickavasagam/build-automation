package com.example.demo.controller

//import javax.servlet.http.HttpServletRequest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.example.demo.service.BranchService

@RestController
@RequestMapping("/")
public class BranchController {
	
	@Autowired
	BranchService branchService

	@GetMapping("/branch")
	def getBranch() {
		println "Trying to get branches"
		def branch = branchService.getAllBranches()
		branch
	}
	
	@GetMapping("/tags") 
	def getTags(){
		println "inside gettags method"
		def tags = branchService.getAllTags()
		return tags
	}
}
