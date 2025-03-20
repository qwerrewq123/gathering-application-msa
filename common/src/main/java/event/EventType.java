package event;

import event.payload.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    GATHERING_CREATED(GatheringCreatedEventPayload.class, Topic.MSA_GATHERING),
    GATHERING_UPDATED(GatheringUpdateEventPayload.class, Topic.MSA_GATHERING),
    GATHERING_DELETED(GatheringDeleteEventPayload.class, Topic.MSA_GATHERING),
    GATHERING_ENROLLMENT(GatheringEnrollmentEventPayload.class, Topic.MSA_GATHERING),
    GATHERING_DIS_ENROLLMENT(GatheringDisEnrollmentEventPayload.class, Topic.MSA_GATHERING),
    MEETING_CREATED(MeetingCreatedEventPayload.class, Topic.MSA_MEETING),
    MEETING_UPDATED(MeetingUpdateEventPayload.class, Topic.MSA_MEETING),
    MEETING_ATTEND(MeetingAttendEventPayload.class, Topic.MSA_MEETING),
    MEETING_DIS_ATTEND(MeetingDisAttendEventPayload.class, Topic.MSA_MEETING),
    CHAT_ROOM_CREATED(ChatRoomCreatedEventPayload.class, Topic.MSA_CHAT),
    CHAT_MESSAGE_CREATED(ChatMessageCreatedEventPayload.class, Topic.MSA_CHAT),
    CHAT_ROOM_ATTEND(ChatRoomAttendEventPayload.class, Topic.MSA_CHAT),
    ;

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from] type={}", type, e);
            return null;
        }
    }

    public static class Topic {
        public static final String MSA_GATHERING = "msa-gathering";
        public static final String MSA_MEETING = "msa-meeting";
        public static final String MSA_CHAT = "msa-chat";
    }
}
