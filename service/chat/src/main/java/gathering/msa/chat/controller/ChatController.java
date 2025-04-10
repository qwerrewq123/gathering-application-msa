package gathering.msa.chat.controller;

import dto.response.chat.*;
import gathering.msa.chat.annotation.Username;
import gathering.msa.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private ChatService chatService;

    @PostMapping("/gathering/{gatheringId}/chat")
    public ResponseEntity<AddChatRoomResponse> addChatRoom(@PathVariable("gatheringId") Long gatheringId,@RequestParam String roomName, @Username String username){
        AddChatRoomResponse addChatResponse = chatService.addChatRoom(roomName,username,gatheringId);
        return new ResponseEntity<>(addChatResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/{gatheringId}/chats")
    public ResponseEntity<ChatRoomsResponse> fetchChatRooms(@RequestParam Integer pageNum, @Username String username){
        ChatRoomsResponse chatRoomsResponse = chatService.fetchChatRooms(pageNum,username);
        return new ResponseEntity<>(chatRoomsResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/{gatheringId}/chats/my")
    public ResponseEntity<ChatMyRoomsResponse> fetchMyChatRooms(@RequestParam Integer pageNum, @Username String username){
        ChatMyRoomsResponse chatMyRoomsResponse = chatService.fetchMyChatRooms(pageNum,username);
        return new ResponseEntity<>(chatMyRoomsResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/{gatheringId}/messages/{chatId}")
    public ResponseEntity<FetchMessagesResponse> fetchMessages(@PathVariable Long chatId, @Username String username){
        FetchMessagesResponse fetchMessagesResponse = chatService.fetchMessages(chatId,username);
        return new ResponseEntity<>(fetchMessagesResponse, HttpStatus.OK);
    }

    @PostMapping("/gathering/{gatheringId}/chat/{chatId}")
    public ResponseEntity<ReadChatMessageResponse> readChatMessage(@PathVariable Long chatId, @Username String username){
        ReadChatMessageResponse readChatMessageResponse = chatService.readChatMessage(chatId,username);
        return new ResponseEntity<>(readChatMessageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/gathering/{gatheringId}/chat/{chatId}")
    public ResponseEntity<LeaveChatResponse> leaveChat(@PathVariable Long chatId,@Username String username){
        LeaveChatResponse leaveChatResponse = chatService.leaveChat(chatId,username);
        return new ResponseEntity<>(leaveChatResponse, HttpStatus.OK);
    }

    @PostMapping("/gathering/{gatheringId}/attend/chat/{chatId}")
    public ResponseEntity<AttendChatResponse> attendChat(@RequestParam Long chatId, @Username String username,@PathVariable("gatheringId") Long gatheringId){
        AttendChatResponse attendChatResponse = chatService.attendChat(chatId,username,gatheringId);
        return new ResponseEntity<>(attendChatResponse, HttpStatus.OK);
    }



}
