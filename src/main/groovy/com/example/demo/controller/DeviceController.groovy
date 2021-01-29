package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.example.demo.service.DeviceService
import com.example.demo.transformer.DeviceTransformer

@RestController
@RequestMapping("/devices")
public class DeviceController {

	@Autowired
	DeviceService deviceService
	
	@Autowired
	DeviceTransformer deviceTransformer
	
	@GetMapping
	def getAll() {
		//deviceTransformer.transform(deviceService.getAllDevices())
		deviceService.getAllDevices()
	}
	
	@PostMapping("/update")
	def upgradeDevices(@RequestBody def data) {
		deviceService.updateDevices(data)
	}
	
	@GetMapping("/{id}/status")
	def getStatus(@PathVariable("id") id) {
		deviceService.getStatus(id)
	}
}
