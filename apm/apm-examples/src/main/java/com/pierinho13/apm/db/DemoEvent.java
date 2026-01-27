package com.pierinho13.apm.db;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "demo_event")
public class DemoEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, length = 2000)
    private String payload;

    @Column(nullable = false)
    private Instant createdAt;

    protected DemoEvent() {}

    public DemoEvent(String type, String payload, Instant createdAt) {
        this.type = type;
        this.payload = payload;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public String getPayload() { return payload; }
    public Instant getCreatedAt() { return createdAt; }
}
