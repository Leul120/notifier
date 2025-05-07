package com.safeStatusNotifier.safeStatusNotifier.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessResponse {
    private boolean success;
    private String message;
    private AccessRelationshipDto relationship;
}
