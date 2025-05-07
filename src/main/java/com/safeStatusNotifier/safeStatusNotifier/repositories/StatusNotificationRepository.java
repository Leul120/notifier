package com.safeStatusNotifier.safeStatusNotifier.repositories;

import com.safeStatusNotifier.safeStatusNotifier.entity.StatusNotification;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StatusNotificationRepository extends JpaRepository<StatusNotification, UUID> {
    List<StatusNotification> findByUserOrderByTimestampDesc(User user);
}
