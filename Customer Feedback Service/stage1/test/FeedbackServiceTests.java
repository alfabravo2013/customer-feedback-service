import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

public class FeedbackServiceTests extends SpringTest {

    CheckResult test() {
        return CheckResult.correct();
    }

    @DynamicTest
    DynamicTesting[] dt = {
            this::test
    };
}
