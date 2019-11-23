import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import scala.concurrent.Future;

class HttpRouter extends AllDirectives {
    private final ActorRef routeActor;

    HttpRouter(ActorSystem system) {
        routeActor = system.actorOf(RouteActor.props(), AkkaStreamsAppConstants.ROUTE_ACTOR_NAME);
    }

    Route createRoute() {
        return route(
                path(AkkaStreamsAppConstants.SERVER_POST_PATH, () ->
                        route(
                                post(() ->
                                        entity(Jackson.unmarshaller(TestPackageRequest.class), msg -> {
                                            routeActor.tell(msg, ActorRef.noSender());
                                            return complete(AkkaStreamsAppConstants.START_TEST_MESSAGE);
                                        })))),
                path(AkkaStreamsAppConstants.SERVER_GET_PATH, () ->
                        get(() ->
                                parameter(AkkaStreamsAppConstants.PACKAGE_ID_PARAMETER_NAME, (packageId) ->
                                {
                                    Future<Object> res = Patterns.ask(routeActor, new StoreActor.GetMessage(packageId), 5000);
                                    return completeOKWithFuture(res, Jackson.marshaller());
                                }))));
    }
}
