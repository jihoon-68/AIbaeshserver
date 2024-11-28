package com.example.Baesh.Service;

import com.example.Baesh.DTO.MessageDTO;
import com.example.Baesh.Entity.ChatRoomEntity;
import com.example.Baesh.Entity.MessageEntity;
import com.example.Baesh.Entity.UserEntity;
import com.example.Baesh.Interface.ChatRoomRepository;
import com.example.Baesh.Interface.MessageRepository;
import com.example.Baesh.Interface.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    //메세지 생성
    public void createMessage(Long userId,String content, Long chatRoomId){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("user not found"));
        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(()-> new RuntimeException("chatRoom not found"));

        MessageEntity message = new MessageEntity();
        message.setChatRoom(chatRoom);
        message.setSender(user);
        message.setContent(content);
        message.setSentTime(LocalDateTime.now());

        messageRepository.save(message);
    }

    //
    public MessageDTO MessageEntityToDTO(MessageEntity message){
        return new MessageDTO(
                message.getId(),
                message.getChatRoom().getId(),
                message.getSender().getId(),
                message.getContent(),
                message.getSentTime()
        );
    }

}
