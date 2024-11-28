package com.example.Baesh.Service;

import com.example.Baesh.DTO.ChatRoom.ChatRoomDTO;
import com.example.Baesh.Entity.ChatRoomEntity;
import com.example.Baesh.Entity.MessageEntity;
import com.example.Baesh.Entity.UserEntity;
import com.example.Baesh.Interface.ChatRoomRepository;
import com.example.Baesh.Interface.MessageRepository;
import com.example.Baesh.Interface.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserServcie userServcie;

    //유저 포함 채팅방 목록 호출
    public List<ChatRoomDTO> getAllChatRoom(Long userId){

        UserEntity userEntity = userRepository.findById(userId).
                orElseThrow(()-> new RuntimeException("user not found"));
        List<ChatRoomEntity> chatRooms = chatRoomRepository.findByUserId(userEntity.getId());

        return chatRooms.stream()
                .map(this::chatRoomEntityToDTO)
                .collect(Collectors.toList());
    }


    //특정 채팅방 정보 호출
    public ChatRoomDTO getChatRoom(Long chatRoomId){

        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(()->new RuntimeException("chatRoom not found"));

        List<MessageEntity> messages = messageRepository.findByChatRoomIdOrderBySentTime(chatRoom.getId());
        chatRoom.setMessages(messages);


        return chatRoomEntityToDTO(chatRoom);
    }

    //채팅방 생성
    public void createChatRoom(Long user1Id,Long user2Id){
        UserEntity user1 = userRepository.findById(user1Id)
                .orElseThrow(()-> new RuntimeException("user1 not found"));
        UserEntity user2= userRepository.findById(user2Id)
                .orElseThrow(()-> new RuntimeException("user2 not found"));

        ChatRoomEntity chatRoom = new ChatRoomEntity();
        chatRoom.setUser1(user1);
        chatRoom.setUser2(user2);
        chatRoom.setCreatedTime(LocalDateTime.now());
        chatRoom.setMessages(new ArrayList<>());

        chatRoomRepository.save(chatRoom);
    }

    //채팅방 삭제
    public void deleteChatRoom(Long chatRoomId){
        if(!chatRoomRepository.existsById(chatRoomId)){
            throw new RuntimeException("chatRoom not found");
        }
        chatRoomRepository.deleteById(chatRoomId);
    }

    //재팅방 엔티티 -> DTO 변환
    public ChatRoomDTO chatRoomEntityToDTO (ChatRoomEntity chatRoom){
        return new ChatRoomDTO(
                chatRoom.getId(),
                userServcie.ChatUserEntityToDTO(chatRoom.getUser1()),
                userServcie.ChatUserEntityToDTO(chatRoom.getUser2()),
                chatRoom.getCreatedTime(),
                chatRoom.getMessages()
                        .stream()
                        .map(message -> messageService.MessageEntityToDTO(message))
                        .collect(Collectors.toList())
        );
    }
}
