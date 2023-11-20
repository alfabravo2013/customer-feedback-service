import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.UUID;
import java.util.stream.Stream;

import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isArray;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isObject;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isString;

public class FeedbackServiceTests extends SpringTest {
    private final Gson gson = new Gson();

    CheckResult testPostFeedback(FeedbackItem[] data) {
        for (var item : data) {
            var payload = gson.toJson(item);
            HttpResponse response = post("/feedback", payload).send();
            if (response.getStatusCode() != 201) {
                return CheckResult.wrong("""
                        Request: POST /feedback
                        Request body: %s
                        Expected response status code 201 but received %d
                        """.formatted(payload, response.getStatusCode()));
            }
        }
        return CheckResult.correct();
    }

    CheckResult testGetFeedback(FeedbackItem[] data) {
        HttpResponse response = get("/feedback").send();
        expect(response.getContent()).asJson().check(
                isArray()
                        .item(isObject()
                                .value("id", isString())
                                .value("text", data[2].text())
                                .value("customer", data[2].customer())
                                .value("createdAt", isString())
                        )
                        .item(isObject()
                                .value("id", isString())
                                .value("text", data[1].text())
                                .value("customer", data[1].customer())
                                .value("createdAt", isString())
                        )
                        .item(isObject()
                                .value("id", isString())
                                .value("text", data[0].text())
                                .value("customer", data[0].customer())
                                .value("createdAt", isString())
                        )
        );
        return CheckResult.correct();
    }

    CheckResult testMongoCollection(FeedbackItem[] data) {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        String connectionString = "mongodb://localhost:27017/hs-test?directConnection=true";
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase("feedback_db");
            var count = database.getCollection("feedback").countDocuments();
            if (count != data.length) {
                return CheckResult.wrong("""
                        Wrong number of documents in the 'feedback' collection in the 'feedback_db' database.
                        Expected: %d, found: %d")
                        """.formatted(feedbackItems.length, count));
            }
        } catch (MongoException e) {
            return CheckResult.wrong("Failed to connect the 'feedback_db' database");
        }
        return CheckResult.correct();
    }

    FeedbackItem[] feedbackItems = Stream
            .generate(() -> {
                var rndText = String.valueOf(System.currentTimeMillis());
                var rndCustomer = "customer-" + UUID.randomUUID();
                return new FeedbackItem(rndText, rndCustomer);
            })
            .limit(3)
            .toArray(FeedbackItem[]::new);

    @DynamicTest
    DynamicTesting[] dt = {
            () -> testPostFeedback(feedbackItems),
            () -> testGetFeedback(feedbackItems),
            () -> testMongoCollection(feedbackItems),
    };
}

record FeedbackItem(String text, String customer) {
}
