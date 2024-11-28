package com.example.Baesh.Controller;

import com.example.Baesh.DTO.ChatRoom.ChatRoomDTO;
import com.example.Baesh.DTO.ChatRoom.CreateChatRoom;
import com.example.Baesh.DTO.MessageDTO;
import com.example.Baesh.Service.ChatRoomService;
import com.example.Baesh.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatroom")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private MessageService messageService;


    @PostMapping("{id}")
    public List<ChatRoomDTO> chatRooms(@PathVariable("id") Long userId){
        return chatRoomService.getAllChatRoom(userId);
    }

    @GetMapping("/{id}")
    public ChatRoomDTO chatRoomList (@PathVariable("id") Long userId){
        return chatRoomService.getChatRoom(userId);
    }

    @PostMapping("/create")
    public void createChatRoom (@RequestBody CreateChatRoom chatRoom){
        chatRoomService.createChatRoom(chatRoom.getUser1Id(),chatRoom.getUser2Id());

    }

    @DeleteMapping
    public void deleteChatRoom(@RequestBody Long chatRoomId){
        chatRoomService.deleteChatRoom(chatRoomId);
    }

    @PostMapping("/{id}/message")
    public void sendMessage (@PathVariable("id") Long chatRoom, @RequestBody MessageDTO message){
        System.out.println("id: "+chatRoom+" message: "+message);
        messageService.createMessage(message.getUserId(),message.getContent(),chatRoom);
    }




}
