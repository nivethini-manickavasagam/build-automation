package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest
import javax.websocket.server.PathParam

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.SoftwareVersionService

@Controller
public class SoftwareVersionController {

	@Autowired
	SoftwareVersionService softwareVersionService

	@GetMapping("/software")
	@ResponseBody
	def getAll() {
		softwareVersionService.findAll()
	}


	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("title") String title,
			@RequestParam("softwareType") String softwareType, @RequestParam("classification") String classification,
			@RequestParam("releaseDate") String releaseDate, @RequestParam("majorVersion") Integer majorVersion,
			@RequestParam("minorVersion") Integer minorVersion, @RequestParam("patchVersion") Integer patchVersion,
			HttpServletRequest request) {

		softwareVersionService.uploadAndSave(file, [title:title, softwareType:softwareType, classification: classification,
			releaseDate:releaseDate, majorVersion:majorVersion, minorVersion:minorVersion, patchVersion:patchVersion])
		"redirect:"
	}
	
	@GetMapping("/download/{id}")
	def download(@PathVariable("id") Long id){
		println "Download"
		def data = softwareVersionService.getFile(id)
		Resource resource = null;
		try {
			resource = new ByteArrayResource(data.content);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + data.softwareVersion.fileName + "\"")
				.body(resource);
	}
}
