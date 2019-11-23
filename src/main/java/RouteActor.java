import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.RoundRobinPool;

public class RouteActor extends AbstractActor {
    private final ActorRef storeActor;
    private final ActorRef runTestActor;

    public RouteActor() {
        storeActor = getContext().actorOf(StoreActor.props(), AkkaAppConstants.STORE_ACTOR_NAME);
        runTestActor = getContext().actorOf(new RoundRobinPool(5).props(RunTestActor.props(storeActor)), AkkaAppConstants.RUN_TEST_ACTOR_NAME);
    }

    static Props props() {
        return Props.create(RouteActor.class);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(StoreActor.GetMessage.class, req -> {
                    storeActor.tell(req, sender());
                })
                .match(TestPackageRequest.class, msg -> {
                    for (TestPackageRequest.Test test : msg.getTests()) {
                        runTestActor.tell(new TestToEval(msg.getPackageId(),
                                        msg.getJsScript(),
                                        msg.getFunctionName(),
                                        test),
                                self());
                    }
                })
                .build();
    }
}
