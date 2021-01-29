package com.example.demo.transformer

import org.springframework.stereotype.Component

@Component
class DeviceTransformer {
	
	def transform(data) {
		def latestPatch = "R2.1-SecurityPatch"
		def devices = []
		data.each {
			
			def isCompliant = it.GwBundleVersion.toLowerCase().contains(latestPatch.toLowerCase())
			devices.add(["name" : it.name, "isConnected" : it.isConnected, "version" : it.GwBundleVersion, "isCompliant" : isCompliant, nextVersion:isCompliant?"Already on latest":latestPatch])
		}
		devices
	}
	
}
