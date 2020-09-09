package com.swmaestro.web.presentation.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PowerpointEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner")
    private String username;

    @Column(name = "file_id")
    private String filename; // filename or file id

    @CreationTimestamp
    @Column(name = "created_time")
    private LocalDateTime createdTime;
}
