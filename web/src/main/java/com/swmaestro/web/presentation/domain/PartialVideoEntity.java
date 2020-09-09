package com.swmaestro.web.presentation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PartialVideoEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = VideoEntity.class)
    @JoinColumn(name = "video_id")
    private VideoEntity videoId;

    @Column(name = "file_name")
    private String filename;

    @Column(name = "created_time")
    @CreationTimestamp
    private LocalDateTime createdTime;
}
