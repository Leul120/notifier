package com.safeStatusNotifier.safeStatusNotifier.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "access_relationships")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private User target;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public enum AccessStatus {
        PENDING, APPROVED, DENIED
    }
}
