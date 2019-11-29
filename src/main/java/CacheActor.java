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
                .match(ResultPing.class, req -> {
                    String url = req.getUrl();
                    Integer result = req.getPing();
                    store.put(url, result);
                })
                .match(GetMessage.class, msg -> {
                    String url = msg.getUrl();
                    Integer result = store.get(url);
                    sender().tell(new ResultPing(url, result), self());
                })
                .build();
    }

    static class GetMessage {
        private final String url;

        GetMessage(String url) {
            this.url = url;
        }

        String getUrl() {
            return url;
        }
    }

}
