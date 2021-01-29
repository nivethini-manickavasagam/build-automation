package com.example.demo.service.scm;

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate

import groovy.json.JsonOutput

@Service
public class SCMService {

	@Value('${app.url}')
	String appUrl

	@Value('${app.serviceUrl}')
	String serviceUrl
	
	@Value('${app.token}')
	String appToken

	@Value('${app.username}')
	private String userName
	
	@Value('${app.password}')
	private String password
	
	@Autowired
	RestTemplate restTemplate

	def uploadFile(String fileName, def fileAsBytes) {
		def fileUploadRequest = ["fileName" : fileName, "content":fileAsBytes]
		HttpEntity<Map> request = new HttpEntity<>(JsonOutput.toJson(fileUploadRequest), getHeaders());
		def response = restTemplate.postForEntity(appUrl+serviceUrl+"uploadPackageToRepository", request, Map.class)
		response.statusCode.is2xxSuccessful()
	}

	def createPackage(String fileName, def data) {
		def requestData = [
			"version1":data.majorVersion,
			"version2":data.minorVersion,
			"version3":data.patchVersion,
			"version4":"0",
			"packageName":data.title,
			"description":data.title,
			"fileName":fileName,
			"scriptName":"deploy.lua",
			"expirationDate":"2021-01-01T00:00:00.000Z"
		]
		HttpEntity<Map> request = new HttpEntity<>(JsonOutput.toJson(requestData), getHeaders());
		def response = restTemplate.postForEntity(appUrl+serviceUrl+"createPackage", request, Map.class)
		response.body.get("packageID")
	}

	def getHeaders() {
		def headers = new HttpHeaders()
		headers.setContentType(MediaType.APPLICATION_JSON)
		headers.set("appKey", appToken);
		headers.set("REC-API-KEY", appToken);

		return headers
	}
	
	def getAllDevices() {
		def appUrl =  appUrl + "Thingworx/Things/Utilities/Services/getAllDevices"//"Thingworx/Resources/SearchFunctions/Services/SearchThingsByTemplate" //"Thingworx/Things/Utilities/Services/getAllDevices
		def appKeyValue = "Basic " + Base64.getEncoder().encodeToString((userName + ":" + password).getBytes())
		def data = ["thingTemplate" : "FMC-GW-SL600"]
		HttpEntity<Map> request = new HttpEntity<>(JsonOutput.toJson(data), getHeaders());
		def response = [body:[array:[]]]//restTemplate.exchange(appUrl, HttpMethod.POST, request, Map.class) //getEntity("Authorization", appKeyValue, data)
		response.body.array
	}

	def updateDevices(devices, scmPackageId, classification) {
		def requestData = [
			"name":classification + "-" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
			"description":"Test Deployment",
			"maxAutoRetries":"2",
			"packageId":scmPackageId,
			"downloadImmediately":true,
			"installImmediately":true,
			"isTest":"true",
			"deliveryTargets":devices.collect{["assetName":it, "assetType":"FMC-GW-SL600"]}				
		]
		HttpEntity<Map> request = new HttpEntity<>(JsonOutput.toJson(requestData), getHeaders());
		def response = restTemplate.postForEntity(appUrl+serviceUrl+"createAndStartDeployment", request, Map.class)
		response.statusCode.is2xxSuccessful()
	}
	
	def getDeviceStatus(id) {
		def requestData = ["assetName":id]
		HttpEntity<Map> request = new HttpEntity<>(JsonOutput.toJson(requestData), getHeaders());
		println "Requesting status for :" + id
		def response = restTemplate.postForEntity(appUrl+serviceUrl+"getDeploymentsForAsset", request, Map.class)
		println "Response status for :" + response.body.deployments
		if (response.statusCode.is2xxSuccessful() && response.body.deployments && response.body.deployments.status) {
			return [id:id, status:response.body.deployments.status[0]]
		}
		return [id:id, status:"No current deployments found"]
	}
}
