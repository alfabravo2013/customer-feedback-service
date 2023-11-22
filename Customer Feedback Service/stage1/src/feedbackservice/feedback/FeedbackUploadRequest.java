package feedbackservice.feedback;

public record FeedbackUploadRequest(
        Integer rating,

        String feedback,
        String customer,
        String product,
        String vendor
) {
}
