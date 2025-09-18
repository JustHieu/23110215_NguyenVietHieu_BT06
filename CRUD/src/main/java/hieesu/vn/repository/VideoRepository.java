package hieesu.vn.repository;

import hieesu.vn.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("""
       SELECT v FROM Video v
       WHERE (:q IS NULL OR lower(v.title) LIKE lower(concat('%', :q, '%'))
              OR lower(v.description) LIKE lower(concat('%', :q, '%')))
         AND (:categoryId IS NULL OR v.category.id = :categoryId)
    """)
    Page<Video> search(@Param("q") String q,
                       @Param("categoryId") Long categoryId,
                       Pageable pageable);
}
