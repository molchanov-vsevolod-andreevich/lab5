import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class AkkaStreamsApp {

    public static void main(String[] args) throws IOException {
        System.out.println(AkkaStreamsAppConstants.START_MESSAGE);
        ActorSystem system = ActorSystem.create(AkkaStreamsAppConstants.ACTOR_SYSTEM_NAME);
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = createRouteFlow(http, system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(AkkaStreamsAppConstants.HOST, AkkaStreamsAppConstants.PORT),
                materializer
        );
        System.out.println(AkkaStreamsAppConstants.START_SERVER_MESSAGE);
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }

    private static Flow<HttpRequest, HttpResponse, NotUsed> createRouteFlow(Http http, ActorSystem system, ActorMaterializer materializer) {
        ActorRef cacheActor = system.actorOf(CacheActor.props(), AkkaStreamsAppConstants.CACHE_ACTOR_NAME);

        return Flow.of(HttpRequest.class)
                .map(req -> {
                    Query requestQuery = req.getUri().query();
                    String url = requestQuery.getOrElse(AkkaStreamsAppConstants.TEST_URL_KEY, "");
                    Integer count = Integer.parseInt(requestQuery.getOrElse(AkkaStreamsAppConstants.COUNT_KEY, "-1"));

                    return new TestPing(url, count);
                })
                .mapAsync(testPing -> Patterns.ask(cacheActor, testPing, AkkaStreamsAppConstants.TIMEOUT)
                        .thenCompose()
                )
                .map(res -> {
                    cacheActor.tell(res, ActorRef.noSender());
                    return HttpResponse.create()
                            .withEntity(res.getUrl + " " + res.getPing);
                });
    }

}
