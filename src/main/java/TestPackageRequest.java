import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestPackageRequest {
    private String packageId;
    private String jsScript;
    private String functionName;
    private Test[] tests;

    @JsonCreator
    public TestPackageRequest(@JsonProperty(AkkaAppConstants.PACKAGE_ID_FIELD) String packageId,
                              @JsonProperty(AkkaAppConstants.JS_SCRIPT_FIELD) String jsScript,
                              @JsonProperty(AkkaAppConstants.FUNCTION_NAME_FIELD) String functionName,
                              @JsonProperty(AkkaAppConstants.TESTS_FIELD) Test[] tests) {
        this.packageId = packageId;
        this.jsScript = jsScript;
        this.functionName = functionName;
        this.tests = tests;
    }

    String getPackageId() {
        return packageId;
    }

    String getJsScript() {
        return jsScript;
    }

    String getFunctionName() {
        return functionName;
    }

    Test[] getTests() {
        return tests;
    }

    public static class Test {
        private String testName;
        private String expectedResult;
        private Object[] params;

        @JsonCreator
        public Test(@JsonProperty(AkkaAppConstants.TEST_NAME_FIELD) String testName,
                    @JsonProperty(AkkaAppConstants.EXPECTED_RESULT_FIELD) String expectedResult,
                    @JsonProperty(AkkaAppConstants.PARAMS_FIELD) Object[] params) {
            this.testName = testName;
            this.expectedResult = expectedResult;
            this.params = params;
        }

        String getTestName() {
            return testName;
        }

        String getExpectedResult() {
            return expectedResult;
        }

        Object[] getParams() {
            return params;
        }
    }
}