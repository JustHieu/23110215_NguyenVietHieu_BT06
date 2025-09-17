package hieesu.vn.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.Instant;


@Getter @Setter
@Entity @Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Column(nullable = false, length = 150)
    private String name;


    @Column(length = 1000)
    private String description;


    // đường dẫn/ tên file ảnh lưu trên đĩa
    @Column(length = 300)
    private String imagePath;


    @Column(nullable = false)
    private Instant createdAt;


    @Column(nullable = false)
    private Instant updatedAt;


    @PrePersist
    public void prePersist(){
        var now = Instant.now();
        createdAt = now; updatedAt = now;
    }
    @PreUpdate
    public void preUpdate(){
        updatedAt = Instant.now();
    }
}