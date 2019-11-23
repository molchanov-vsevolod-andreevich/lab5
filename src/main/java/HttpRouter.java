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
        routeActor = system.actorOf(RouteActor.props(), AkkaAppConstants.ROUTE_ACTOR_NAME);
    }

    Route createRoute() {
        return route(
                path(AkkaAppConstants.SERVER_POST_PATH, () ->
                        route(
                                post(() ->
                                        entity(Jackson.unmarshaller(TestPackageRequest.class), msg -> {
                                            routeActor.tell(msg, ActorRef.noSender());
                                            return complete(AkkaAppConstants.START_TEST_MESSAGE);
                                        })))),
                path(AkkaAppConstants.SERVER_GET_PATH, () ->
                        get(() ->
                                parameter(AkkaAppConstants.PACKAGE_ID_PARAMETER_NAME, (packageId) ->
                                {
                                    Future<Object> res = Patterns.ask(routeActor, new StoreActor.GetMessage(packageId), 5000);
                                    return completeOKWithFuture(res, Jackson.marshaller());
                                }))));
    }
}
