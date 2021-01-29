package com.example.demo.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import com.example.demo.entity.BuildInfo
import com.example.demo.repository.BuildInfoRepository
import com.example.demo.service.BuildService

@RestController
@RequestMapping("/")
public class BuildController {
	
	@Autowired
	BuildService buildService
	
	@Autowired
	BuildInfoRepository buildInfoRepository
	
	
	@CrossOrigin(origins = "*")
	@PostMapping("/build")
	def build(@RequestBody def data) {
		println "build method"
		println "hello"
		def buildResult = buildService.build(data)
		buildService.saveData(buildResult)
		println "buildresult $buildResult"
		return buildResult
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/buildInfo")
	def List<BuildInfo> getAll() {
		buildInfoRepository.findAll()
	}
	
	@GetMapping("build/{id}/status")
	def getStatus(@PathVariable("id") id) {
		println "inside getstatus method"
		buildService.getStatus(id)
	}
	
	@PostMapping("/upload")
	def upload(@RequestParam("file") MultipartFile file, def data)
	{
		println "inside upload method"
		buildService.upload(file, data);
		"redirect:"
	}
	
	/*@GetMapping("/build/artifacts")
	def downloadArtifacts() {
		println "insided download artifacts"
		buildService.downloadArtifact();
	}*/
}