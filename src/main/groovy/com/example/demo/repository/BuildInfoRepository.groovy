package com.example.demo.repository

import org.springframework.data.repository.CrudRepository

import com.example.demo.entity.BuildInfo

public interface BuildInfoRepository extends CrudRepository<BuildInfo, Long> {
}