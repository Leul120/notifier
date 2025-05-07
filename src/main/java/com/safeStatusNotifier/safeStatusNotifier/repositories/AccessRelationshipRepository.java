package com.safeStatusNotifier.safeStatusNotifier.repositories;

import com.safeStatusNotifier.safeStatusNotifier.entity.AccessRelationship;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessRelationshipRepository extends JpaRepository<AccessRelationship, String> {
    List<AccessRelationship> findByTargetAndStatus(User target, AccessRelationship.AccessStatus status);
    List<AccessRelationship> findByRequesterAndStatus(User requester, AccessRelationship.AccessStatus status);
    Optional<AccessRelationship> findByRequesterAndTarget(User requester, User target);
    List<AccessRelationship> findByTarget(User target);
}
