package com.project.donate.repository;


import com.project.donate.enums.Status;
import com.project.donate.model.Market;
import com.project.donate.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    List<Organization> findByIsActiveTrue();

    // is active sadece soft delete icin kullanılacak geri kalanlar status üzerinden olacak
    List<Organization> getOrganizationsByStatusAndIsActiveTrue(Status status);

    Page<Organization> getOrganizationsByStatusAndIsActiveTrue(Status status, Pageable pageable);

    List<Organization> findAllByStatus(Status status);
}
