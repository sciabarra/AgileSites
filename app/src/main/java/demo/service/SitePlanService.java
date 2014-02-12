package demo.service;

import wcs.api.Index;
import akka.actor.UntypedActor;

@Index("demo/services.txt")
public class SitePlanService extends UntypedActor {

	@Override
	public void onReceive(Object object) throws Exception {
		System.out.println("got a message!");
		getSender().tell("ok", getSelf());
	}

}
