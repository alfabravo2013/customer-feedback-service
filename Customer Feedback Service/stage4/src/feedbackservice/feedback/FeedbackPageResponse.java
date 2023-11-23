package feedbackservice.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FeedbackPageResponse(
        @JsonProperty("total_documents")
        long totalDocuments,

        @JsonProperty("is_first_page")
        boolean isFirstPage,

        @JsonProperty("is_last_page")
        boolean isLastPage,

        List<FeedbackDocument> documents
) {
}
