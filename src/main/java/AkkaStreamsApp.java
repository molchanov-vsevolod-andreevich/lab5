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
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import javafx.util.Pair;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import scala.concurrent.Future;

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
        AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();
        testSink = Flow.<TestPing>create()
                .mapConcat(testPing -> Collections.nCopies(testPing.getCount(), testPing.getUrl()))
                .mapAsync(AkkaStreamsAppConstants.PARALLELISM, url -> {
                    long startTime = System.nanoTime();
                    return asyncHttpClient
                            .prepareGet(url)
                            .execute()
                            .toCompletableFuture()
                            .thenApply(response -> {
                                long resp =  
                                return resp;
                            });
                })
                .toMat(fold, Keep.right());

        return Flow.of(HttpRequest.class)
                .map(req -> {
                    Query requestQuery = req.getUri().query();
                    String url = requestQuery.getOrElse(AkkaStreamsAppConstants.TEST_URL_KEY, "");
                    Integer count = Integer.parseInt(requestQuery.getOrElse(AkkaStreamsAppConstants.COUNT_KEY, "-1"));

                    return new CacheActor.GetMessage(url);
                })
                .mapAsync(AkkaStreamsAppConstants.PARALLELISM, msg -> Patterns.ask(cacheActor, msg, AkkaStreamsAppConstants.TIMEOUT)
                        .thenApply(res -> (ResultPing) res)
                        .thenCompose(res -> {
                            if (res.getPing() != null) {
                                return CompletableFuture.completedFuture(res);
                            } else {
                                return Source.from(Collections.singletonList(res))
                                        .toMat(testSink, Keep.right()).run(materializer);
                            }
                        }))
                .map(res -> {
                    cacheActor.tell(res, ActorRef.noSender());
                    return HttpResponse.create()
                            .withEntity(res.getUrl + " " + res.getPing);
                });
    }

}
