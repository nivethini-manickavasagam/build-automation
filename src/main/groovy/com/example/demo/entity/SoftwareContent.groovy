package com.example.demo.entity;

import java.sql.Clob

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob

@Entity
public class SoftwareContent {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id
	
	@Column
	Long softwareVersionId
	
	@Column 
	@Lob
	Clob fileAsBytes
}
