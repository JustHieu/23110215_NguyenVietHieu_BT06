package hieesu.vn.repository;

import hieesu.vn.entity.AppUser;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface UserRepository extends JpaRepository<AppUser, Long> {
    Page<AppUser> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String u, String e, Pageable p);
    Optional<AppUser> findByUsername(String username);
}