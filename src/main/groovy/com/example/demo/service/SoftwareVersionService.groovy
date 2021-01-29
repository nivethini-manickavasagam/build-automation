package com.example.demo.service;

import java.sql.Clob

import javax.transaction.Transactional

import org.apache.commons.io.IOUtils
import org.hibernate.engine.jdbc.ClobProxy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

import com.example.demo.entity.SoftwareContent
import com.example.demo.entity.SoftwareVersion
import com.example.demo.repository.SoftwareContentRepository
import com.example.demo.repository.SoftwareVersionRepository
import com.example.demo.service.scm.SCMService

@Service
public class SoftwareVersionService {

	@Autowired
	SoftwareVersionRepository softwareVersionRepository

	@Autowired
	SoftwareContentRepository contentRepository

	@Autowired
	SCMService scmService

	def findAll() {
		softwareVersionRepository.findAll(Sort.by(Sort.Direction.DESC, "uploadedDate"))
	}

	@Transactional
	def save(def data, String filename, String packageId) {

		def softwareVersion = new SoftwareVersion().with {
			title= data.title
			softwareType= data.softwareType
			classification= data.classification
			releaseDate= data.releaseDate
			majorVersion= data.majorVersion
			minorVersion= data.minorVersion
			patchVersion= data.patchVersion
			fileName= filename
			scmPackageId= packageId
			uploadedDate = new java.sql.Date(new java.util.Date().getTime())
			return it
		}
		softwareVersionRepository.save(softwareVersion)
	}

	@Transactional
	def uploadAndSave(MultipartFile file, def data) {
		String fileName = file.filename
		def fileAsBytes = new String(Base64.getEncoder().encode(file.bytes))
		scmService.uploadFile(fileName, fileAsBytes)
		def packageId = scmService.createPackage(fileName, data)
		def softwareVersion = save(data, fileName, packageId)
		upload(softwareVersion.id, fileAsBytes)
	}

	def upload(softwareVersionId, fileAsBytes) {
		Clob clob = ClobProxy.generateProxy(fileAsBytes)
		contentRepository.save(new SoftwareContent(
				softwareVersionId:  softwareVersionId,
				fileAsBytes:clob
				))
	}

	def getLatestVersion(classification, softwareType) {
		def softwareVersions = softwareVersionRepository.getGatewaySoftwareVersion(classification, softwareType)
		if (softwareVersions) {
			return softwareVersions[0]
		}
		return null
	}

	def getFile(id) {
		Optional<SoftwareContent> data = softwareVersionRepository.findById(id)
		Clob clob = contentRepository.findBySoftwareVersionId(id).fileAsBytes		
		[content: IOUtils.toByteArray(clob.getCharacterStream(), "UTF-8"), softwareVersion: data.get()]
	}
}
