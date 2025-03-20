package gathering.msa.recommend.service.eventhandler;


import event.Event;
import event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
    Long findGatheringId(Event<T> event);
}
