package akka.first.app.java.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.first.app.java.messages.Result;

public class MasterActor extends UntypedActor {

	private ActorRef aggregateActor = getContext().actorOf(
			new Props(AggregateActor.class), "aggregate");

	private ActorRef reduceActor = getContext().actorOf(
			new Props(new UntypedActorFactory() {
				public UntypedActor create() {
					return new ReduceActor(aggregateActor);
				}
			}), "reduce");

	private ActorRef mapActor = getContext().actorOf(
			new Props(new UntypedActorFactory() {
				public UntypedActor create() {
					return new MapActor(reduceActor);
				}
			}), "map");

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
            System.out.println(11);
			mapActor.tell(message);
		} else if (message instanceof Result) {
            System.out.println(12);
			aggregateActor.tell(message);
		} else
			unhandled(message);
	}
}
