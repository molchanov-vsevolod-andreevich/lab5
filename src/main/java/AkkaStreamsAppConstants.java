import java.time.Duration;

class AkkaStreamsAppConstants {
    // Actors Constants
    static final String ACTOR_SYSTEM_NAME = "routes";
    static final String ROUTE_ACTOR_NAME = "routeActor";
    static final String CACHE_ACTOR_NAME = "cacheActor";

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

    // HTTP Request and Response constants
    static final String TEST_URL_KEY = "testUrl";
    static final String COUNT_KEY = "count";

    // Other constants
    static final Duration TIMEOUT = Duration.ofMillis(5000);
    static final int PARALLELISM = 1;
    static final long ONE_SECOND_IN_NANO_SECONDS = 1_000_000_000L;
}
