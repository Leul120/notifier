package com.safeStatusNotifier.safeStatusNotifier.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "device_statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean isOnline;

    @Column(nullable = false)
    private int batteryLevel;

    @Column(nullable = false)
    private String networkType;

    @Column(nullable = false)
    private LocalDateTime lastSeen;
}
