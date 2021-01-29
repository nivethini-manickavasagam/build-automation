package com.example.demo.entity;

import javax.persistence.Column
import javax.persistence.Entity;
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
public class SoftwareVersion {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id
	
	@Column
	String title
	
	@Column
	String softwareType
	
	@Column
	String classification
	
	@Column
	String releaseDate
	
	@Column
	Integer majorVersion
	
	@Column
	Integer minorVersion
	
	@Column
	Integer patchVersion
	
	@Column
	String fileName
	
	@Column
	String scmPackageId
	
	@Column
	Date uploadedDate
}