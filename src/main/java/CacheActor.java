import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class CacheActor extends AbstractActor {

    static Props props() {

        return Props.create(CacheActor.class);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(GetMessage.class, req -> {
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
}
