package com.example.flow_manager.model.dto;

import java.io.Serializable;

public record SubscriptionTypeResponse(
    String nameSubscription, 
    Long sizeFileCanBeUploaded
) implements Serializable { }
