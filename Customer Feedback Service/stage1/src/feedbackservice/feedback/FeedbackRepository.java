package feedbackservice.feedback;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackRepository extends MongoRepository<FeedbackRecord, String> {
}
