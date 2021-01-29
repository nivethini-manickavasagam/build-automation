package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

import com.example.demo.entity.SoftwareVersion

public interface SoftwareVersionRepository extends JpaRepository<SoftwareVersion, Long> {

	@Query("select sv from SoftwareVersion sv where sv.classification= :classification and sv.softwareType = :softwareType order by sv.uploadedDate desc")
	List<SoftwareVersion> getGatewaySoftwareVersion(@Param("classification") classification, @Param("softwareType") softwareType) 

}
