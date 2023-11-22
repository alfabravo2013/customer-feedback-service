package feedbackservice.feedback;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}
