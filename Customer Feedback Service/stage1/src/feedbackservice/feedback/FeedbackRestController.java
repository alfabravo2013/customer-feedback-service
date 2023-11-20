package feedbackservice.feedback;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeedbackRestController {
    private final FeedbackRepository repository;

    public FeedbackRestController(FeedbackRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/feedback")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadFeedback(@RequestBody FeedbackUploadRequest request) {
        var document = new FeedbackRecord();
        document.setText(request.text());
        document.setCustomer(request.customer());
        repository.save(document);
    }

    @GetMapping("/feedback")
    public List<FeedbackRecord> getAllFeedback() {
        var sort = Sort.by(Sort.Order.desc("createdAt"));
        return repository.findAll(sort);
    }
}
