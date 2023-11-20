package feedbackservice.feedback;

public record FeedbackUploadRequest(
        String text,
        String customer
) {
}
