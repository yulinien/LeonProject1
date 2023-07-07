package com.example.leonproject.dao.repository;

import com.example.leonproject.dao.entity.TimeSheetDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSheetRepository extends JpaRepository<TimeSheetDO,Integer> {

}
