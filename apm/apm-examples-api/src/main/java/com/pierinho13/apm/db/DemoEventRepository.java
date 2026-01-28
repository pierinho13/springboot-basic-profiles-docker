package com.pierinho13.apm.db;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DemoEventRepository extends JpaRepository<DemoEvent, Long> {

    @Query("select e from DemoEvent e where e.type = :type order by e.id desc")
    List<DemoEvent> findLatestByType(@Param("type") String type, Pageable pageable);

    long countByType(String type);
}
