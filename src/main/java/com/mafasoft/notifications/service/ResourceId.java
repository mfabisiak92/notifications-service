package com.mafasoft.notifications.service;

import static jakarta.persistence.EnumType.STRING;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import lombok.Data;

@Embeddable
@Data
public class ResourceId implements Serializable {

    private String resourceId;

    @Enumerated(STRING)
    private ResourceType resourceType;

    protected ResourceId() {}

    ResourceId(String resourceId, ResourceType resourceType) {
        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }
}
