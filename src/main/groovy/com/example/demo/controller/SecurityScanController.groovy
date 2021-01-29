/**
 * 
 */
package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.SecurityScanService

/**
 * @author User
 *
 */
@RestController
@RequestMapping("/security")
public class SecurityScanController {

	@Autowired
	SecurityScanService securityScanService;
	
	@GetMapping("/packages")
	def getCompliantMatrix() {
		securityScanService.getComplianctMatrix()
	}	
}
