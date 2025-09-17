package hieesu.vn.repository;


import hieesu.vn.entity.Video;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VideoRepository extends JpaRepository<Video, Long> {
    Page<Video> findByTitleContainingIgnoreCase(String q, Pageable p);
    Page<Video> findByCategory_NameContainingIgnoreCase(String cat, Pageable p);
}