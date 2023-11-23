package feedbackservice;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.MongoDBContainer;

import java.util.List;

@SpringBootApplication
public class FeedbackApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeedbackApplication.class, args);
    }
}

@Component
class MongoContainerProvider {
    private final MongoDBContainer container;

    public MongoContainerProvider(@Value("${mongodb.image}") String mongodbImage) {
        container = new MongoDBContainer(mongodbImage);
        container.addEnv("MONGO_INITDB_DATABASE", "feedback_db");
        container.setPortBindings(List.of("27017:27017"));
        container.start();
    }

    @PreDestroy
    public void tearDown() {
        container.stop();
    }
}
