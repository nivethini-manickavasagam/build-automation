package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate

import com.example.demo.service.scm.SCMService

@Service
public class DeviceService {
	
	@Autowired
	SCMService scmService
	
	@Autowired
	SoftwareVersionService softwareVersionService
	
	def getAllDevices() {
		scmService.getAllDevices()
	}

	def updateDevices(data) {
		def latestVersion = softwareVersionService.getLatestVersion(data.updateToVersion, data.softwareType)
		if (latestVersion) {
			def scmPackageId = latestVersion.scmPackageId
			println "Status of SCM deployment request: ${scmService.updateDevices(data.devices, scmPackageId, data.updateToVersion)}"
		}
		println data
	}
	
	def getStatus(id) {
		scmService.getDeviceStatus(id)
	}
}
