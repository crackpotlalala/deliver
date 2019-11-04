package com.deliver.demo.entity;

import lombok.Data;

@Data
public class WebhookRequest {
    private String action;
    private String dialogNodeName;
    private Object context;
    private String value;
    private String sessionId;
    private String queryId;
}