package com.mafasoft.notifications.service;

import org.springframework.data.jpa.repository.JpaRepository;

interface ResourceRepository extends JpaRepository<Resource, ResourceId> {}
