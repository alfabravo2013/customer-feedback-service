package feedbackservice;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
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

    public MongoContainerProvider() {
        container = new MongoDBContainer("mongo:6-jammy")
                .withCreateContainerCmdModifier(cmd -> cmd.withName("hs-mongo"));
        container.setPortBindings(List.of("27017:27017"));
    }

    @PostConstruct
    public void init() {
        container.start();
    }

    @PreDestroy
    public void tearDown() {
        container.stop();
    }
}
