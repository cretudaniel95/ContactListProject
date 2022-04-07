package com.onlineshop.business.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<Contact, UUID> {
    @Query(value = "select * from contact c where c.name like %:keyword%", nativeQuery = true)
    List<Contact> findByKeyword(@Param("keyword") String keyword);

}
