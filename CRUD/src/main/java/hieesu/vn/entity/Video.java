package hieesu.vn.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.Instant;


@Getter @Setter
@Entity @Table(name = "videos")
public class Video {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Column(nullable = false, length = 200)
    private String title;


    @Column(length = 2000)
    private String description;


    @NotBlank
    @Column(nullable = false, length = 500)
    private String videoUrl; // ví dụ link YouTube hoặc CDN


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    private boolean published = true;
    private Instant uploadedAt = Instant.now();
}
