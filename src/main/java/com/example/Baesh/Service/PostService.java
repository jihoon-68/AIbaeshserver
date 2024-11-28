package com.example.Baesh.Service;

import com.example.Baesh.DTO.PostDTO;
import com.example.Baesh.Entity.PostEntity;
import com.example.Baesh.Entity.UserEntity;
import com.example.Baesh.Interface.PostRepository;
import com.example.Baesh.Interface.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServcie userServcie;

    public List<PostDTO> getPosts(){

        List<PostEntity> posts = postRepository.findAll();
        return posts.stream()
                .map(this::PostEntityTODTO)
                .collect(Collectors.toList());
    }

    //트정 유저 게시글 목록 호출
    public List<PostDTO> getPost(Long userId){
        UserEntity user =userRepository.findById(userId).orElse(null);
        List<PostEntity> posts =postRepository.findAllByAuthorId(user);
        return posts.stream()
                .map(this::PostEntityTODTO)
                .collect(Collectors.toList());
    }

    //게시글 생성
    public void createPost (PostDTO post){
        UserEntity userEntity = userRepository.findById(post.getUserDTO().getId())
                .orElseThrow(()->new RuntimeException("user not fount"));

        PostEntity postEntity = new PostEntity();

        postEntity.setContent(post.getContent());
        postEntity.setImageUrl(post.getImageUrl());
        postEntity.setAuthorName(userEntity.getFirstName());
        postEntity.setAuthorId(userEntity);
        postEntity.setCreatedTime(LocalDateTime.now());


        postRepository.save(postEntity);
    }

    //게시글 수정
    public void updatePost (Long postId,PostDTO post){
        PostEntity postEntity = postRepository.findById(post.getId())
                .orElseThrow(()-> new RuntimeException("post not found"));

        if(!post.getContent().isEmpty()){
            postEntity.setContent(post.getContent());
        }
        if(!post.getImageUrl().isEmpty()){
            postEntity.setImageUrl(post.getImageUrl());
        }

        postRepository.save(postEntity);
    }

    //게시글 삭제
    public void deletePost(Long postId){
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("post not found");
        }
        postRepository.deleteById(postId);
    }

    public PostDTO PostEntityTODTO(PostEntity post){
        return new PostDTO(
                post.getId(),
                post.getContent(),
                post.getImageUrl(),
                userServcie.postUserEntityToDTO(post.getAuthorId()),
                post.getCreatedTime()
        );

    }
}
