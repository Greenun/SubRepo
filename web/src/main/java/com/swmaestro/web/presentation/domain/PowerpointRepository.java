package com.swmaestro.web.presentation.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerpointRepository extends JpaRepository<PowerpointEntity, Long> {
    public PowerpointEntity findByFilename(String filename);

    public PowerpointEntity findTopByUsernameOrderByIdDesc(String username);
}
