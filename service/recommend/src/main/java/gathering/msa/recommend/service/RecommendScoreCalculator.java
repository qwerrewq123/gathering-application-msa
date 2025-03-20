package gathering.msa.recommend.service;

import gathering.msa.recommend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendScoreCalculator {
    private final GatheringViewRepository gatheringViewRepository;
    private final GatheringCountRepository gatheringCountRepository;
    private final GatheringCreateRepository gatheringCreateRepository;
    private final GatheringMeetingCountRepository gatheringMeetingCountRepository;
    private final GatheringMeetingCreateRepository gatheringMeetingCreateRepository;
    private final GatheringChatCreateRepository gatheringChatCreateRepository;
    private final GatheringChatAttendCreateRepository gatheringChatAttendCreateRepository;
    private final GatheringChatSendCreateRepository gatheringChatSendCreateRepository;

    private static final long GATHERING_VIEW_WEIGHT = 5;
    private static final long GATHERING_COUNT_WEIGHT = 5;
    private static final long GATHERING_CREATED_WEIGHT = 5;
    private static final long GATHERING_MEETING_CREATED_WEIGHT = 5;
    private static final long GATHERING_MEETING_COUNT_WEIGHT = 5;
    private static final long GATHERING_CHAT_WEIGHT = 1;

    public long calculate(Long articleId) {
        Long gatheringView = gatheringViewRepository.read(articleId);
        Long gatheringCount = gatheringCountRepository.read(articleId);
        Long gatheringCreated = gatheringCreateRepository.read(articleId);
        Long gatheringMeetingCount = gatheringMeetingCountRepository.read(articleId);
        Long gatheringMeetingCreated = gatheringMeetingCreateRepository.read(articleId);
        Long gatheringChatCreated = gatheringChatCreateRepository.read(articleId);
        Long gatheringChatAttended = gatheringChatAttendCreateRepository.read(articleId);
        Long gatheringChatSended = gatheringChatSendCreateRepository.read(articleId);

        return GATHERING_VIEW_WEIGHT * gatheringView
                + GATHERING_COUNT_WEIGHT * gatheringCount
                + GATHERING_CREATED_WEIGHT * gatheringCreated
                + GATHERING_MEETING_CREATED_WEIGHT * gatheringMeetingCount
                + GATHERING_MEETING_COUNT_WEIGHT * gatheringMeetingCreated
                + GATHERING_CHAT_WEIGHT * gatheringChatCreated
                + GATHERING_CHAT_WEIGHT * gatheringChatAttended
                + GATHERING_CHAT_WEIGHT * gatheringChatSended;
    }

}
