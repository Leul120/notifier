package com.safeStatusNotifier.safeStatusNotifier.requests;



import com.safeStatusNotifier.safeStatusNotifier.entity.AccessRelationship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRelationshipDto {
    private String id;
    private String requesterId;
    private String requesterName;
    private String requesterEmail;
    private String targetId;
    private String targetName;
    private String targetEmail;
    private AccessRelationship.AccessStatus status;
    private String createdAt;
}
