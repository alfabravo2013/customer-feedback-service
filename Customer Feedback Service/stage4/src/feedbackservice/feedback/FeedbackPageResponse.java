package feedbackservice.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

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
        public static FeedbackPageResponse of(Page<FeedbackDocument> page) {
                return new FeedbackPageResponse(
                        page.getTotalElements(),
                        page.isFirst(),
                        page.isLast(),
                        page.getContent()
                );
        }
}
