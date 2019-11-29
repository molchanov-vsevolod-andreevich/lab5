import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;
import java.util.Map;

public class CacheActor extends AbstractActor {
    private Map<String, Integer> store = new HashMap<>();

    static Props props() {

        return Props.create(CacheActor.class);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestPing.class, req -> {

                })
                .match(GetPing.class, msg -> {
                    Integer result = store.get()
                    sender().tell(, self());
                })
                .build();
    }
    }
}
