package com.mafasoft.notifications.service;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(
            """
            FROM Notification n JOIN n.resource
            WHERE n.status = 'PENDING' AND n.resource.blocked = FALSE AND (n.resource.nextAttemptOn IS NULL OR n.resource.nextAttemptOn <= :currentDate)
            ORDER BY n.resource.nextAttemptOn ASC, n.createdAt ASC
            """)
    List<Notification> findPendingNotifications(ZonedDateTime currentDate, Pageable pageable);

    @Query(
            """
            SELECT n.resource.resourceId FROM Notification n JOIN n.resource
            WHERE n.status = 'SCHEDULED' AND n.resource.resourceId IN :resourceIds
            """)
    List<ResourceId> findScheduledResourceIds(Collection<ResourceId> resourceIds);

    @Query(
            """
            SELECT n FROM Notification n
            WHERE n.resource.resourceId.resourceId = :resourceId
            ORDER BY n.createdAt DESC
            """)
    List<Notification> findByResourceId(String resourceId);

    @Transactional
    @Modifying
    @Query("FROM Notification n WHERE n.status = 'SCHEDULED' AND (n.nextAttemptOn IS NULL OR n.nextAttemptOn <= :threshold)")
    List<Notification> findStuckNotifications(ZonedDateTime threshold);
}
