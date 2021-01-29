package com.example.demo.service

import javax.persistence.Column

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import groovy.json.JsonSlurper

@Service
class BranchService {
	
	@Column
	String version
	
	@Column
	String environment
	
	@Value('${git.url}')
	String gitUrl
	
	@Value('${git.tags}')
	String gitTag
	
	@Value('${git.branch}')
	String gitBranch
		
	@Value('${jenkin.userId}')
	String jenkinUserId
		
	@Value('${jenkin.apiToken}')
	String jenkinApiToken
		
	@Value('${jenkin.url}')
	String jenkinUrl
	
	@Autowired
	RestTemplate restTemplate
	
	def getAllBranches = {
		println "inside get all branches method"
		print gitUrl
		
		def command = "git ls-remote -t -h ${gitUrl}"
		
		print command
		
		def proc = command.execute()
		
		print proc
		
		proc.waitFor()
		
		println proc.exitValue()
		
		if ( proc.exitValue() != 0 ) {
		   println "Error, ${proc.err.text}"
		   System.exit(-1)
		}
		
		def branches = proc.in.text.readLines().collect {
			it.split()[1].replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
		}
		
		//println branches
		println '\n'
		String branch = branches.findAll { item -> item.startsWith('refs/heads/')}
		def retVal = branch.replaceAll("refs/heads/", " ");
		
		println retVal
		return retVal
	}
	
	def getAllTags = {
		def gitUrl =  gitUrl
		print gitUrl
		
		def command = "git ls-remote -t -h ${gitUrl}"
		
		print command
		
		def proc = command.execute()
		
		print proc
		
		proc.waitFor()
		
		println proc.exitValue()
		
		if ( proc.exitValue() != 0 ) {
		   println "Error, ${proc.err.text}"
		   System.exit(-1)
		}
		
		def branches = proc.in.text.readLines().collect {
			it.split()[1].replaceAll(/[a-z0-9]*\trefs\/heads\//, '')
		}
		
		//println branches
		//println '\n'
		def retVal = branches.findAll { item -> item.startsWith('refs/tags/')}
		println retVal
		println '\n'
	
	}
	
	
/*	def getStatus = {
		def command = "curl -s http://localhost:8080/job/make/lastBuild/api/json --user nivethini:11888052dbe4b4fe246342baae78058a37"
		print command
		def proc = command.execute()
		print proc
		proc.waitFor()
		println proc.exitValue()
		if ( proc.exitValue() != 0 ) {
		   println "Error, ${proc.err.text}"
		   System.exit(-1)
		}
		def branches = proc.in.text.readLines().collect {
			 it.split().findAll()
		}
		println '\n'
		println branches
	}*/
	
}
