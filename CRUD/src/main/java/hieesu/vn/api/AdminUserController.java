package hieesu.vn.api;

import hieesu.vn.entity.AppUser;
import hieesu.vn.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;

    @GetMapping
    public Page<AppUser> list(@RequestParam(defaultValue = "") String q,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "id,desc") String sort) {
        String[] s = sort.split(",");
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(s.length > 1 ? s[1] : "desc"), s[0]);
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(order));
        return (q == null || q.isBlank())
                ? userRepository.findAll(pageable)
                : userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(q, q, pageable);
    }

    @GetMapping("/{id}")
    public AppUser get(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppUser create(@Valid @RequestBody AppUser u) {
        u.setId(null);
        return userRepository.save(u);
    }

    @PutMapping("/{id}")
    public AppUser update(@PathVariable Long id, @Valid @RequestBody AppUser body) {
        AppUser u = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        u.setUsername(body.getUsername());
        u.setEmail(body.getEmail());
        u.setFullName(body.getFullName());
        u.setActive(body.isActive());
        return userRepository.save(u);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!userRepository.existsById(id)) throw new NoSuchElementException("User not found");
        userRepository.deleteById(id);
    }
}
