package com.example.demo.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "ssc")
@Configuration 
public class ScannerEnvironment {
	String projectVersionsURL;
	String issueBySeverityURL;
	String issueByCategoryURL;
	String unifiedToken;
	String reportToken;
	
	def getAccessToken() {
		"FortifyToken "+unifiedToken
	}
	
	def getReportToken() {
		"FortifyToken "+reportToken
	}
}
