package com.example.demo.service

import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

import javax.ws.rs.HttpMethod

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.support.BasicAuthorizationInterceptor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile

import com.example.demo.entity.BuildInfo
import com.example.demo.repository.BuildInfoRepository

import groovy.json.JsonOutput

@Service
class BuildService {
	@Autowired
	RestTemplate restTemplate
	
	@Autowired
	BuildInfoRepository buildInfoRepository

	@Value('${app.upload.dir:${user.home}}')
	String uploadDir;
	
	def build(data) {
		println "inside build service"
		println "selected version $data.version.value"
		def versionInfo = "$data.version.value"
		def val = versionInfo.replaceAll("\\s","")
		println "val $val"
		restTemplate.getInterceptors().add(
				new BasicAuthorizationInterceptor("Nivethini", "ca8191eb928e4e2ebd3917de1574a1ca"));
		def crumbResponse = restTemplate.getForEntity("http://localhost:8080/crumbIssuer/api/json", Map.class)
		def crumbValue = crumbResponse.body.get("crumb")
		def crumbField = crumbResponse.body.get("crumbRequestField")
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON)
		headers.set("user", "Nivethini:ca8191eb928e4e2ebd3917de1574a1ca");
		headers.set("username", "Nivethini");
		headers.set("password", "ca8191eb928e4e2ebd3917de1574a1ca");
		println "Basic " + Base64.getEncoder().encodeToString("Nivethini:ca8191eb928e4e2ebd3917de1574a1ca".bytes)
		headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("Nivethini:ca8191eb928e4e2ebd3917de1574a1ca".bytes))
		headers.set(crumbField, crumbValue)// "d9fcea332b4a76e592d0f6937bd56dcf8c94ce6fc7db846810fff72632912264")
		HttpEntity entity = new HttpEntity("", headers);
		def response = restTemplate.postForEntity("http://localhost:8080/job/make/buildWithParameters?token=1181208af398b439d2e0d1a62a42a69206&branch=${val}&${crumbField}=${crumbValue}", entity, Map.class)
		//def url = "http://localhost:8080/job/branch/buildWithParameters?token=1181208af398b439d2e0d1a62a42a69206&branch=${val}&${crumbField}=${crumbValue}"
		//println "URL $url"
		//def response = restTemplate.postForEntity(url, entity, Map.class)
		println response.statusCode.is2xxSuccessful()
		TimeUnit.SECONDS.sleep(10);
		//println response.toString()
		return buildResponse()
	}

	def buildInfo(buildNumber, buildTime, buildStatus){
		buildInfoRepository.save(new BuildInfo(
					buildNumber: buildNumber,
					buildTime: buildTime,
					buildStatus: buildStatus
					))
		}
		
	def buildResponse()
	{
		restTemplate.getInterceptors().add(
			new BasicAuthorizationInterceptor("Nivethini", "ca8191eb928e4e2ebd3917de1574a1ca"));
	
		def crumbResponse = restTemplate.getForEntity("http://localhost:8080/crumbIssuer/api/json", Map.class)
		def crumbValue = crumbResponse.body.get("crumb")
		def crumbField = crumbResponse.body.get("crumbRequestField")
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON)
		headers.set("user", "Nivethini:ca8191eb928e4e2ebd3917de1574a1ca");
		headers.set("username", "Nivethini");
		headers.set("password", "ca8191eb928e4e2ebd3917de1574a1ca");
		headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("Nivethini:ca8191eb928e4e2ebd3917de1574a1ca".bytes))
		headers.set(crumbField, crumbValue)
		println "after set headers"
		def requestData = ["assetName": ""]
		HttpEntity<Map> request = new HttpEntity<>(JsonOutput.toJson(requestData), headers);
		//HttpEntity entity = new HttpEntity("", headers);
		println "after set entity"
		//def response = restTemplate.postForEntity("http://localhost:8080/job/make/lastBuild/api/json?token=1181208af398b439d2e0d1a62a42a69206&${crumbField}=${crumbValue}", request, Map.class)
		def response = restTemplate.postForEntity("http://localhost:8080/job/make/api/json/?token=1181208af398b439d2e0d1a62a42a69206&${crumbField}=${crumbValue}", request, Map.class)
		def id = response.body.builds[0].number
		return getStatus(id)
	}
	
	def getStatus(id)
	{
		println "inside get status method"
		restTemplate.getInterceptors().add(
			new BasicAuthorizationInterceptor("Nivethini", "ca8191eb928e4e2ebd3917de1574a1ca"));
	
		def crumbResponse = restTemplate.getForEntity("http://localhost:8080/crumbIssuer/api/json", Map.class)
		def crumbValue = crumbResponse.body.get("crumb")
		def crumbField = crumbResponse.body.get("crumbRequestField")
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON)
		headers.set("user", "Nivethini:ca8191eb928e4e2ebd3917de1574a1ca");
		headers.set("username", "Nivethini");
		headers.set("password", "ca8191eb928e4e2ebd3917de1574a1ca");
		headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("Nivethini:ca8191eb928e4e2ebd3917de1574a1ca".bytes))
		headers.set(crumbField, crumbValue)
		println "after set headers"
		def requestData = ["assetName":id]
		HttpEntity<Map> request = new HttpEntity<>(JsonOutput.toJson(requestData), headers);
		//HttpEntity entity = new HttpEntity("", headers);
		println "after set entity"
		//def response = restTemplate.postForEntity("http://localhost:8080/job/make/lastBuild/api/json?token=1181208af398b439d2e0d1a62a42a69206&${crumbField}=${crumbValue}", request, Map.class)
		//def response = restTemplate.postForEntity("http://localhost:8080/job/make/api/json/?token=1181208af398b439d2e0d1a62a42a69206&${crumbField}=${crumbValue}", request, Map.class)
		def response = restTemplate.postForEntity("http://localhost:8080/job/make/" + id + "/api/json/?token=1181208af398b439d2e0d1a62a42a69206&${crumbField}=${crumbValue}", request, Map.class)
		//println response.body.builds
		println response.body.result
		if(response.body.result == null)
		{
			println response.body.result = "IN_PROGRESS"
		}
		println response.body.number
		def dateUnix = response.body.timestamp
		def date = new Date( dateUnix ).toString()
		Date dateObj =  new Date( ((long)dateUnix))
		//println "date obj $dateObj"
		def cleanDate = new SimpleDateFormat('yyyy-MM-dd').format(dateObj)
		println "clean date $cleanDate"
		
		//println "date $date"
		println response.statusCode.is2xxSuccessful()
		return [response.body.number, cleanDate, response.body.result]
	}
	
	def saveData(data) {
		println data;
		println data[0];
		println data[1];
		println data[2];
		buildInfoRepository.save(new BuildInfo(
			buildNumber: data[0],
			buildTime: data[1],
			buildStatus: data[2]))
		println "data saved successfully"
	}
	def createPackage(String fileName) {
		def requestData = [
			"fileName":fileName
		]
		HttpEntity<Map> request = new HttpEntity<>(JsonOutput.toJson(requestData), getHeaders());
		def response = restTemplate.postForEntity(appUrl+serviceUrl+"createPackage", request, Map.class)
		response.body.get("packageID")
	}
	
	@Transactional
	def upload(MultipartFile file, def data) {
		String fileName = file.filename
		def fileAsBytes = new String(Base64.getEncoder().encode(file.bytes))
		uploadFile(fileName, fileAsBytes)
		def packageId = createPackage(fileName)
	}
	
	def uploadFile(String fileName, def fileAsBytes) {
		def fileUploadRequest = ["fileName" : fileName, "content":fileAsBytes]
		HttpEntity<Map> request = new HttpEntity<>(JsonOutput.toJson(fileUploadRequest), getHeaders());
		def response = restTemplate.postForEntity(appUrl+serviceUrl+"uploadPackageToRepository", request, Map.class)
		response.statusCode.is2xxSuccessful()
	}
	
	def getHeaders() {
		def headers = new HttpHeaders()
		headers.setContentType(MediaType.APPLICATION_JSON)
		headers.set("appKey", appToken);
		headers.set("REC-API-KEY", appToken);
		return headers
	}
	
	def downloadArtifact()
	{
		println "inside download artifact service"
		restTemplate.getInterceptors().add(
			new BasicAuthorizationInterceptor("Nivethini", "ca8191eb928e4e2ebd3917de1574a1ca"));
		def crumbResponse = restTemplate.getForEntity("http://localhost:8080/crumbIssuer/api/json", Map.class)
		def crumbValue = crumbResponse.body.get("crumb")
		def crumbField = crumbResponse.body.get("crumbRequestField")
		HttpHeaders headers = new HttpHeaders();
		println "b4 set headers"
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM)
		headers.set("user", "Nivethini:ca8191eb928e4e2ebd3917de1574a1ca");
		headers.set("username", "Nivethini");
		headers.set("password", "ca8191eb928e4e2ebd3917de1574a1ca");
		headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("Nivethini:ca8191eb928e4e2ebd3917de1574a1ca".bytes))
		headers.set(crumbField, crumbValue)
		println "after set headers"
		HttpEntity entity = new HttpEntity("", headers);
		println "before response"
		def response = restTemplate.exchange("http://localhost:8080/job/make/lastSuccessfulBuild/artifact/build/*zip*/build.zip?token=1181208af398b439d2e0d1a62a42a69206", HttpMethod.GET, entity, Map.class)
		println response.statusCode.is2xxSuccessful()
	}
}
