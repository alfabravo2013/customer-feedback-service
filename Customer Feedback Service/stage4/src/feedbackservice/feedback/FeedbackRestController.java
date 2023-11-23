package feedbackservice.feedback;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FeedbackRestController {
    private static final int defaultPage = 1;
    private static final int defaultPerPage = 10;
    private static final int minPerPage = 5;
    private static final int maxPerPage = 20;

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
    public ResponseEntity<FeedbackPageResponse> getAllSortedBy(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer perPage,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) String vendor
    ) {
        int requestedPage = getSanitizedPage(page);
        int requestedPageSize = getSanitizedPerPage(perPage);

        var sortBy = Sort.by("id").descending();

        var probe = new FeedbackDocument();
        probe.setRating(rating);
        probe.setCustomer(customer);
        probe.setProduct(product);
        probe.setVendor(vendor);

        var example = Example.of(probe);

        var pageRequest = PageRequest.of(requestedPage - 1, requestedPageSize, sortBy);
        var fetchedPage = repository.findAll(example, pageRequest);

        var payload = FeedbackPageResponse.of(fetchedPage);

        return ResponseEntity.ok().body(payload);
    }

    @GetMapping("/feedback/{id}")
    public ResponseEntity<FeedbackDocument> getOne(@PathVariable String id) {
        return ResponseEntity.of(repository.findById(id));
    }

    private int getSanitizedPage(Integer page) {
        return page == null || page < 1
                ? defaultPage
                : page;
    }

    private int getSanitizedPerPage(Integer perPage) {
        return perPage == null || perPage < minPerPage || perPage > maxPerPage
                ? defaultPerPage
                : perPage;
    }
}
