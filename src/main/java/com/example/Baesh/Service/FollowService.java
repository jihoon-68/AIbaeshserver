package com.example.Baesh.Service;

import com.example.Baesh.DTO.Follow.FollowDTO;
import com.example.Baesh.DTO.Follow.FollowListDTO;
import com.example.Baesh.Entity.FollowEntity;
import com.example.Baesh.Entity.UserEntity;
import com.example.Baesh.Interface.FollowRepository;
import com.example.Baesh.Interface.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserServcie userServcie;

    // 팔로우/팔로잉 각각 명수,사용자 이름 사진
    public FollowListDTO Follows (Long userId){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("user not found"));

        List<FollowEntity> followEntities = followRepository.findByFollowerId(user.getId());
        List<FollowEntity> followingEntities = followRepository.findByFollowingId(user.getId());

        return new FollowListDTO(
                followEntities
                        .stream()
                        .map(follow -> followEntityToDTO(follow).getFollowing())
                        .collect(Collectors.toList()),
                followingEntities
                        .stream()
                        .map(follow -> followEntityToDTO(follow).getFollower())
                        .collect(Collectors.toList())
        );
    }

    // 팔로우/팔로잉 추가
    public void follow(Long user1Id, Long user2Id){
        UserEntity user1 = userRepository.findById(user1Id)
                .orElseThrow(()-> new RuntimeException("user not found"));
        UserEntity user2 = userRepository.findById(user2Id)
                .orElseThrow(()-> new RuntimeException("user not found"));

        FollowEntity followEntity = new FollowEntity();
        followEntity.setFollower(user1);
        followEntity.setFollowing(user2);
        followEntity.setFollowTime(LocalDateTime.now());
        followRepository.save(followEntity);
    }

    // 팔로우/팔로잉 취소
    public void deleteFollow(Long user1Id, Long user2Id){
        UserEntity user1 = userRepository.findById(user1Id)
                .orElseThrow(()-> new RuntimeException("user not found"));
        UserEntity user2 = userRepository.findById(user2Id)
                .orElseThrow(()-> new RuntimeException("user not found"));

        FollowEntity followEntity = followRepository.findByFollowerIdAndFollowingId(user1.getId(),user2.getId());
        followRepository.delete(followEntity);
    }

    //Entity -> DTO 변환
    public FollowDTO followEntityToDTO(FollowEntity follow){
        return new FollowDTO(
                userServcie.userEntityToDTO(follow.getFollower()),
                userServcie.userEntityToDTO(follow.getFollowing()),
                follow.getFollowTime()
        );
    }

}
