import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class RunTestActor extends AbstractActor {
    private final ActorRef storeActor;

    static Props props(ActorRef storeActor) {
        return Props.create(RunTestActor.class, storeActor);
    }

    public RunTestActor(ActorRef storeActor) {
        this.storeActor = storeActor;
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(TestToEval.class, msg -> {
                    boolean isCorrect;
                    String result;
                    String expectedResult = msg.getTest().getExpectedResult();
                    Object[] params = msg.getTest().getParams();
                    String testName = msg.getTest().getTestName();

                    ScriptEngine engine = new ScriptEngineManager().getEngineByName(AkkaAppConstants.JS_ENGINE_NAME);
                    engine.eval(msg.getJsScript());
                    Invocable invocable = (Invocable) engine;
                    result = invocable.invokeFunction(msg.getFunctionName(), params).toString();
                    isCorrect = result.equals(expectedResult);

                    storeActor.tell(new TestPackageResponse.TestResult(msg.getPackageId(), testName,
                                    isCorrect,
                                    result,
                                    expectedResult,
                                    params),
                            self());
                })
                .build();
    }
}
