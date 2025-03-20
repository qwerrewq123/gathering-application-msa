package gathering.msa.chat.service;

import dto.request.chat.ChatMessageRequest;
import dto.response.chat.*;
import dto.response.user.UserResponse;
import dto.response.user.UserResponses;
import dto.response.user.UserResponsesElement;
import exception.chat.NotFoundChatMessageException;
import exception.chat.NotFoundChatParticipantException;
import exception.chat.NotFoundChatRoomException;
import exception.user.NotFoundUserException;
import gathering.msa.chat.client.UserServiceClient;
import gathering.msa.chat.entity.ChatMessage;
import gathering.msa.chat.entity.ChatParticipant;
import gathering.msa.chat.entity.ChatRoom;
import gathering.msa.chat.entity.ReadStatus;
import gathering.msa.chat.repository.ChatMessageRepository;
import gathering.msa.chat.repository.ChatParticipantRepository;
import gathering.msa.chat.repository.ChatRoomRepository;
import gathering.msa.chat.repository.ReadStatusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static util.ConstClass.*;


@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserServiceClient userServiceClient;


    public AddChatRoomResponse addChatRoom(String roomName, String username) {
        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Long userId = userResponse.getId();
        ChatRoom chatRoom = ChatRoom.of(roomName, userId);
        ChatParticipant chatParticipant = ChatParticipant.of(chatRoom, userId);
        chatRoomRepository.save(chatRoom);
        chatParticipantRepository.save(chatParticipant);
        return AddChatRoomResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public ChatRoomsResponse fetchChatRooms(Integer pageNum, String username) {
        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Long userId = userResponse.getId();
        PageRequest pageRequest = PageRequest.of(pageNum, 5);
        Page<ChatRoomResponse> page = chatRoomRepository.fetchChatRooms(pageRequest,userId);
        boolean hasNext = page.hasNext();
        List<ChatRoomResponse> content = page.getContent();
        List<ChatRoomsResponseElement> element = toChatRoomsResponseElements(content);
        return ChatRoomsResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,element,hasNext);
    }

    public ChatMyRoomsResponse fetchMyChatRooms(Integer pageNum, String username) {
        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Long userId = userResponse.getId();
        PageRequest pageRequest = PageRequest.of(pageNum, 5);
        Page<ChatMyRoomResponse> page = chatRoomRepository.fetchMyChatRooms(pageRequest,userId);
        boolean hasNext = page.hasNext();
        List<ChatMyRoomResponse> content = page.getContent();
        List<ChatMyRoomsResponseElement> element = toChatMyRoomsResponseElements(content);
        return ChatMyRoomsResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,element,hasNext);
    }

    public LeaveChatResponse leaveChat(Long chatId, String username) {
        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Long userId = userResponse.getId();
        ChatRoom chatRoom = chatRoomRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,userId,true)
                .orElseThrow(()-> new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        chatParticipant.changeStatus(false);
        return LeaveChatResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public ReadChatMessageResponse readChatMessage(Long chatId, String username) {
        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Long userId = userResponse.getId();
        ChatRoom chatRoom = chatRoomRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,userId,true)
                .orElseThrow(()-> new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomAndChatParticipant(chatRoom,chatParticipant);
        if(chatMessages.isEmpty()) throw new NotFoundChatMessageException("no exist ChatMessage!!!");
        Long chatParticipantId = chatParticipant.getId();
        List<Long> chatMessagesId = chatMessages.stream().map(c -> c.getId()).toList();
        readStatusRepository.readChatMessage(chatParticipantId,chatMessagesId);
        return ReadChatMessageResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }



    public void saveMessage(Long roomId, ChatMessageRequest chatMessageRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        UserResponse userResponse = userServiceClient.fetchUserByUsername(chatMessageRequest.getUsername());
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Long userId = userResponse.getId();
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,userId,true)
                .orElseThrow(()->new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        ChatMessage chatMessage = ChatMessage.of(chatRoom,chatParticipant,chatMessageRequest);
        List<ChatParticipant> trueChatParticipants =
                chatParticipantRepository.findAllByChatRoomAndStatus(chatRoom, true);
        List<ChatParticipant> falseChatParticipants =
                chatParticipantRepository.findAllByChatRoomAndStatus(chatRoom, false);
        chatMessageRepository.save(chatMessage);
        saveReadStatus(trueChatParticipants, chatMessage, falseChatParticipants);
    }

    public AttendChatResponse attendChat(Long roomId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Long userId = userResponse.getId();
        Optional<ChatParticipant> optionalChatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,userId,false);

        if(optionalChatParticipant.isPresent()) optionalChatParticipant.get().changeStatus(true);
        if(optionalChatParticipant.isEmpty()){
            chatParticipantRepository.save(ChatParticipant.builder()
                    .chatRoom(chatRoom)
                    .userId(userId)
                    .status(true)
                    .build());
        }
        chatRoom.changeCount(chatRoom.getCount()+1);
        return AttendChatResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public FetchMessagesResponse fetchMessages(Long roomId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Long userId = userResponse.getId();
        ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,userId,true)
                .orElseThrow(()->new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        Long chatParticipantId = chatParticipant.getId();
        List<ChatMessageResponse> chatMessageResponses = chatMessageRepository.fetchMessages(roomId,chatParticipantId);
        return FetchMessagesResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,chatMessageResponses);
    }

    public boolean isRoomParticipant(String username, long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundChatRoomException("no exist ChatRoom!!"));
        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Long userId = userResponse.getId();
        chatParticipantRepository.findByChatRoomAndUserAndStatus(chatRoom,userId,true)
                .orElseThrow(()->new NotFoundChatParticipantException("no exist ChatParticipant!!"));
        return true;
    }

    private void saveReadStatus(List<ChatParticipant> trueChatParticipants, ChatMessage chatMessage, List<ChatParticipant> falseChatParticipants) {
        for (ChatParticipant trueChatParticipant : trueChatParticipants) {
            readStatusRepository.save(ReadStatus.builder()
                    .chatMessage(chatMessage)
                    .chatParticipant(trueChatParticipant)
                    .status(true)
                    .build());
        }
        for (ChatParticipant falsechatParticipant : falseChatParticipants) {
            readStatusRepository.save(ReadStatus.builder()
                    .chatMessage(chatMessage)
                    .chatParticipant(falsechatParticipant)
                    .status(false)
                    .build());
        }
    }

    private List<ChatMyRoomsResponseElement> toChatMyRoomsResponseElements(List<ChatMyRoomResponse> content) {
        List<Long> userIds = content.stream()
                .map(ChatMyRoomResponse::getUserId)
                .toList();
        UserResponses userResponses = userServiceClient.fetchUserByIds(userIds);
        List<UserResponsesElement> elements = userResponses.getElements();
        return IntStream.range(0,content.size())
                .mapToObj(i -> ChatMyRoomsResponseElement.builder()
                        .id(content.get(i).getId())
                        .name(content.get(i).getName())
                        .count(content.get(i).getCount())
                        .status(content.get(i).isStatus())
                        .username(elements.get(i).getUsername())
                        .unReadCount(content.get(i).getUnReadCount())
                        .build()
                ).toList();
    }

    private List<ChatRoomsResponseElement> toChatRoomsResponseElements(List<ChatRoomResponse> content) {
        List<Long> userIds = content.stream()
                .map(ChatRoomResponse::getUserId)
                .toList();
        UserResponses userResponses = userServiceClient.fetchUserByIds(userIds);
        List<UserResponsesElement> elements = userResponses.getElements();
        return IntStream.range(0,content.size())
                .mapToObj(i -> ChatRoomsResponseElement.builder()
                        .id(content.get(i).getId())
                        .name(content.get(i).getName())
                        .count(content.get(i).getCount())
                        .status(content.get(i).isStatus())
                        .username(elements.get(i).getUsername())
                        .build()
                ).toList();

    }
}
