package hieesu.vn.api;

import hieesu.vn.entity.Video;
import hieesu.vn.repository.VideoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/videos")
@RequiredArgsConstructor
public class AdminVideoController {

    private final VideoRepository videoRepository;

    @GetMapping
    public Page<Video> list(@RequestParam(defaultValue = "") String q,
                            @RequestParam(required = false) Long categoryId,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(defaultValue = "id,desc") String sort) {
        String[] s = sort.split(",");
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(s.length > 1 ? s[1] : "desc"), s[0]);
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(order));
        String query = (q == null || q.isBlank()) ? null : q;
        return videoRepository.search(query, categoryId, pageable);
    }

    @GetMapping("/{id}")
    public Video get(@PathVariable Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Video not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Video create(@Valid @RequestBody Video v) {
        v.setId(null);
        return videoRepository.save(v);
    }

    @PutMapping("/{id}")
    public Video update(@PathVariable Long id, @Valid @RequestBody Video body) {
        Video v = videoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Video not found"));
        v.setTitle(body.getTitle());
        v.setDescription(body.getDescription());
        v.setVideoUrl(body.getVideoUrl());
        v.setCategory(body.getCategory());
        v.setPublished(body.isPublished());
        v.setUploadedBy(body.getUploadedBy());
        return videoRepository.save(v);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!videoRepository.existsById(id)) throw new NoSuchElementException("Video not found");
        videoRepository.deleteById(id);
    }
}
