class AkkaStreamsAppConstants {
    // Actors Constants
    static final String ACTOR_SYSTEM_NAME = "routes";
    static final String ROUTE_ACTOR_NAME = "routeActor";
    static final String STORE_ACTOR_NAME = "storeActor";
    static final String RUN_TEST_ACTOR_NAME = "runTestActor";

    // Server Constants
    static final String HOST = "localhost";
    static final int PORT = 8080;

    static final String SERVER_POST_PATH = "test";
    static final String SERVER_GET_PATH = "result";

    static final String PACKAGE_ID_PARAMETER_NAME = "packageId";

    // Messages
    static final String START_MESSAGE = "start!";
    static final String START_SERVER_MESSAGE = "Server online at http://localhost:8080/\nPress RETURN to stop...\n";
    static final String START_TEST_MESSAGE = "Test started!\n";
    static final String NO_SUCH_PACKAGE_MESSAGE = "There is no such package";

    // JSON Names
    static final String PACKAGE_ID_FIELD = "packageId";
    static final String JS_SCRIPT_FIELD = "jsScript";
    static final String FUNCTION_NAME_FIELD = "functionName";
    static final String TESTS_FIELD = "tests";

    static final String TEST_NAME_FIELD = "testName";
    static final String EXPECTED_RESULT_FIELD = "expectedResult";
    static final String PARAMS_FIELD = "params";

    static final String TEST_RESULTS_FIELD = "testsResults";
    static final String IS_CORRECT_FIELD = "isCorrect";
    static final String RESULT_FIELD = "result";

    // Other Constants
    static final String JS_ENGINE_NAME = "nashorn";
}
