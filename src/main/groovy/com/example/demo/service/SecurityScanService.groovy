package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Service
public class SecurityScanService {

	@Autowired
	ScannerEnvironment scannerEnvironment

	@Autowired
	RestTemplate restTemplate

	def	getComplianctMatrix() {

		def projectMap = [:]
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON)
		headers.set("Authorization", scannerEnvironment.getAccessToken());
		HttpEntity<Map> request = new HttpEntity(headers);
		def projectVersion = [data:[]]
		try {
			projectVersion = restTemplate.exchange(scannerEnvironment.projectVersionsURL, HttpMethod.GET, request, Map.class).body;
		}catch(Exception e ) {
			
		}
		projectVersion.data.each {
			if (it.name.toLowerCase().contains("gateway")) {
				projectMap.put(String.valueOf(it.id), [name:it.name])
			}
		}
		//headers.set("Authorization", scannerEnvironment.getReportToken());
		//HttpEntity<Map> reportRequest = new HttpEntity(headers);
		projectMap.each {id, details->
			details.put ("bySeverity", restTemplate.exchange(scannerEnvironment.issueBySeverityURL.replace("#id",id), HttpMethod.GET, request, Map.class).body.data);
			details.put ("byCategory", restTemplate.exchange(scannerEnvironment.issueByCategoryURL.replace("#id",id), HttpMethod.GET, request, Map.class).body.data);
		}
		println projectMap
		projectMap
	}
}
