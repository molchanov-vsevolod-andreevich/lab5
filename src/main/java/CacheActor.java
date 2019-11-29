import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CacheActor extends AbstractActor {
    private Map<String, ArrayList<TestPackageResponse.TestResult>> store = new HashMap<>();

    static Props props() {

        return Props.create(CacheActor.class);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(GetPing.class, req -> {
                    storeActor.tell(req, sender());
                })
                .match(TestPing.class, msg -> {
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
