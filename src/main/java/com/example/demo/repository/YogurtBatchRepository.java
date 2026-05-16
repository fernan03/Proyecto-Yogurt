package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.YogurtBatch;
import com.example.demo.model.YogurtBatch.BatchStatus;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface YogurtBatchRepository extends JpaRepository<YogurtBatch, Long> {
    List<YogurtBatch> findByStatus(BatchStatus status);
    
    List<YogurtBatch> findByRecipeId(Long recipeId);
    
    @Query("SELECT yb FROM YogurtBatch yb WHERE yb.status = :status AND yb.createdAt BETWEEN :startDate AND :endDate")
    List<YogurtBatch> findByStatusAndDateRange(@Param("status") BatchStatus status, 
                                               @Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT yb FROM YogurtBatch yb WHERE yb.batchCode LIKE %:code%")
    List<YogurtBatch> searchByBatchCode(@Param("code") String code);
    
    @Query("SELECT COUNT(yb) FROM YogurtBatch yb WHERE yb.status = :status")
    long countByStatus(@Param("status") BatchStatus status);
}
