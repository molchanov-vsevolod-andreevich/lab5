public class TestToEval {
    private final String packageId;
    private final String jsScript;
    private final String functionName;
    private final TestPackageRequest.Test test;

    public TestToEval(String packageId, String jsScript, String functionName, TestPackageRequest.Test test) {
        this.packageId = packageId;
        this.jsScript = jsScript;
        this.functionName = functionName;
        this.test = test;
    }

    public String getPackageId() {
        return packageId;
    }

    public String getJsScript() {
        return jsScript;
    }

    public String getFunctionName() {
        return functionName;
    }

    public TestPackageRequest.Test getTest() {
        return test;
    }
}