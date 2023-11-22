package feedbackservice.feedback;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class FeedbackRestController {
    private final FeedbackRepository repository;

    public FeedbackRestController(FeedbackRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/feedback")
    public ResponseEntity<Void> uploadFeedback(@RequestBody FeedbackUploadRequest request) {
        var document = new FeedbackDocument();
        document.setRating(request.rating());
        document.setFeedback(request.feedback());
        document.setCustomer(request.customer());
        document.setProduct(request.product());
        document.setVendor(request.vendor());
        var id = repository.save(document).getId();

        return ResponseEntity.created(URI.create("/feedback/" + id)).build();
    }

    @GetMapping("/feedback")
    public ResponseEntity<List<FeedbackDocument>> getAllSortedBy(
            @RequestParam(required = false, defaultValue = "id") String sort,
            @RequestParam(required = false, defaultValue = "id") String desc
    ) {
        Sort sortBy = Sort.by(sort).and(Sort.by(desc).descending());
        return ResponseEntity.ok()
                .body(repository.findAll(sortBy));
    }

    @GetMapping("/feedback/{id}")
    public ResponseEntity<FeedbackDocument> getOne(@PathVariable String id) {
        return ResponseEntity.of(repository.findById(id));
    }
}
