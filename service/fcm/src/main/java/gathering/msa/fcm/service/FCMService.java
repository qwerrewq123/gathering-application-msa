package gathering.msa.fcm.service;

import com.google.firebase.messaging.*;
import dto.request.fcm.TopicNotificationRequestDto;
import dto.request.user.SignInRequest;
import dto.response.user.UserResponse;
import exception.fcm.AlreadySubscribeTopicException;
import exception.fcm.NotFoundTopicException;
import exception.user.NotFoundUserException;
import gathering.msa.fcm.client.UserServiceClient;
import gathering.msa.fcm.entity.FCMToken;
import gathering.msa.fcm.entity.FCMTokenTopic;
import gathering.msa.fcm.entity.Topic;
import gathering.msa.fcm.entity.UserTopic;
import gathering.msa.fcm.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static util.ConstClass.SUCCESS_CODE;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

	private final TopicRepository topicRepository;
	private final FCMTokenRepository fcmTokenRepository;
	private final FCMTokenTopicRepository fcmTokenTopicRepository;
	private final UserTopicRepository userTopicRepository;
	private final UserServiceClient userServiceClient;
	private final NotificationRepository notificationRepository;


	final int TOKEN_EXPIRATION_MONTHS = 2;

	public void sendByTopic(TopicNotificationRequestDto topicNotificationRequestDto, Topic topic) {

		Message message = Message.builder()
			.setTopic(topicNotificationRequestDto.getTopic())
			.setNotification(Notification.builder()
				.setTitle(topicNotificationRequestDto.getTitle())
				.setBody(topicNotificationRequestDto.getContent())
				.setImage(topicNotificationRequestDto.getImg())
				.build())
			.putData("click_action", topicNotificationRequestDto.getUrl())
			.build();

		try {
			FirebaseMessaging.getInstance().send(message);
			notificationRepository.save(gathering.msa.fcm.entity.Notification.builder()
					.topic(topic)
					.img(topicNotificationRequestDto.getImg())
					.url(topicNotificationRequestDto.getUrl())
					.title(topicNotificationRequestDto.getTitle())
					.content(topicNotificationRequestDto.getContent())
					.build());

		} catch (FirebaseMessagingException e) {
			throw new RuntimeException(e);
		}

	}

	public void subscribeToTopic(String topicName, List<String> tokens) {

		List<String> failedTokens = new ArrayList<>();
		try {
			TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopicAsync(tokens, topicName).get();
			if (response.getFailureCount() > 0) {
				response.getErrors().forEach(error -> {
					failedTokens.add(tokens.get(error.getIndex()));
				});
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException();
		}
		if (!failedTokens.isEmpty()) {
			fcmTokenTopicRepository.deleteByTokenValueIn(failedTokens);
			fcmTokenRepository.deleteByTokenValueIn(failedTokens);
		}
	}

	@Transactional
	public void subscribeToTopics(String topicName, String username) {

		Topic topic = topicRepository.findByTopicName(topicName)
				.orElseThrow(() -> new NotFoundTopicException("not found topic"));
		UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
		if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
		if (userTopicRepository.existsByTopicAndUser(topicName, userResponse.getId())) {
			throw new AlreadySubscribeTopicException("already subscribe topic");
		}
		Long userId = userResponse.getId();
		List<FCMToken> userTokens = fcmTokenRepository.findByUserId(userId);
		UserTopic topicMember = UserTopic.builder()
				.topic(topic)
				.userId(userId)
				.build();
		userTopicRepository.save(topicMember);
		List<FCMTokenTopic> fcmTokenTopics = userTokens.stream()
				.map(token -> new FCMTokenTopic(topic, token))
				.collect(Collectors.toList());
		fcmTokenTopicRepository.saveAll(fcmTokenTopics);
		List<String> tokenValues = userTokens.stream()
				.map(fcmToken -> fcmToken.getTokenValue())
				.collect(Collectors.toList());
		subscribeToTopic(topicName, tokenValues);
	}

	public void unsubscribeFromTopic(String topic, List<String> tokens) {

		List<String> failedTokens = new ArrayList<>();
		try {
			TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopicAsync(tokens, topic).get();
			if (response.getFailureCount() > 0) {
				response.getErrors().forEach(error -> {
					failedTokens.add(tokens.get(error.getIndex()));
				});
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException();
		}
		if (!failedTokens.isEmpty()) {
			fcmTokenRepository.deleteByTokenValueIn(failedTokens);
		}
	}

	@Transactional
	public void unsubscribeFromTopics(String topicName, String username) {

		Topic topic = topicRepository.findByTopicName(topicName)
				.orElseThrow(() -> new NotFoundTopicException("not found topic"));
		UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
		if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
		Long userId = userResponse.getId();
		Long topicId = topic.getId();
		userTopicRepository.deleteByTopicAndUser(topicId, userId);
		fcmTokenTopicRepository.deleteByTopic(topic);
		List<FCMToken> memberTokens = fcmTokenRepository.findByUserId(userId);
		List<String> tokenValues = memberTokens.stream()
				.map(fcmToken -> fcmToken.getTokenValue())
				.collect(Collectors.toList());
		unsubscribeFromTopic(topicName, tokenValues);
	}

	@Transactional
	public void saveFCMToken(SignInRequest signInRequest) {

		UserResponse userResponse = userServiceClient.fetchUserByUsername(signInRequest.getUsername());
		if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
		Long userId = userResponse.getId();
		Optional<FCMToken> existingToken = fcmTokenRepository.findByTokenAndUser(signInRequest.getFcmToken(), userId);
		if (existingToken.isPresent()) {
			FCMToken fcmToken = existingToken.get();
			fcmToken.changeExpirationDate(2);
		} else {
			FCMToken fcmToken = FCMToken.builder()
					.tokenValue(signInRequest.getFcmToken())
					.userId(userId)
					.expirationDate(LocalDate.now().plusMonths(TOKEN_EXPIRATION_MONTHS))
					.build();
			fcmTokenRepository.save(fcmToken);

			List<UserTopic> userTopics = userTopicRepository.findByUser(userId);
			List<Topic> subscribedTopics = userTopics.stream()
					.map(UserTopic::getTopic)
					.distinct()
					.collect(Collectors.toList());

			List<FCMTokenTopic> newSubscriptions = subscribedTopics.stream()
					.map(topic -> new FCMTokenTopic(topic, fcmToken))
					.collect(Collectors.toList());
			fcmTokenTopicRepository.saveAll(newSubscriptions);

			for (Topic topic : subscribedTopics) {
				subscribeToTopic(topic.getTopicName(), Collections.singletonList(fcmToken.getTokenValue()));
			}
		}
	}

	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void unsubscribeExpiredTokens() {
		LocalDate now = LocalDate.now();
		List<FCMToken> expiredTokens = fcmTokenRepository.findByExpirationDate(now);
		List<FCMTokenTopic> topicTokens = fcmTokenTopicRepository.findByFcmTokenIn(expiredTokens);
		List<String> tokenValues = expiredTokens.stream()
				.map(token -> token.getTokenValue())
				.collect(Collectors.toList());
		topicTokens.forEach(topicToken -> {
			unsubscribeFromTopic(topicToken.getTopic().getTopicName(), tokenValues);
		});
		fcmTokenTopicRepository.deleteAll(topicTokens);
		fcmTokenRepository.deleteAll(expiredTokens);
	}
}
