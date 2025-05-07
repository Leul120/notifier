package com.safeStatusNotifier.safeStatusNotifier.repositories;

import com.safeStatusNotifier.safeStatusNotifier.entity.DeviceStatus;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, UUID> {
    Optional<DeviceStatus> findTopByUserOrderByLastSeenDesc(User user);
}
