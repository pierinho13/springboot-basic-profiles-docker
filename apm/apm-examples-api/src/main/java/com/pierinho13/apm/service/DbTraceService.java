package com.pierinho13.apm.service;

import com.pierinho13.apm.db.*;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class DbTraceService {

    private final DemoEventRepository repo;
    private final EntityManager em;

    public DbTraceService(DemoEventRepository repo, EntityManager em) {
        this.repo = repo;
        this.em = em;
    }

    @Transactional
    public Map<String, Object> writeAndRead(String type, int writes, int reads) {
        for (int i = 0; i < writes; i++) {
            repo.save(new DemoEvent(type, "payload-" + UUID.randomUUID(), Instant.now()));
        }

        List<DemoEvent> latest = repo.findLatestByType(type, PageRequest.of(0, Math.max(1, reads)));
        long count = repo.countByType(type);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("type", type);
        out.put("writes", writes);
        out.put("reads", reads);
        out.put("count", count);
        out.put("latestIds", latest.stream().map(DemoEvent::getId).toList());
        return out;
    }

    @Transactional
    public void failDb() {
        em.createNativeQuery("select * from table_that_does_not_exist").getResultList();
    }
}
