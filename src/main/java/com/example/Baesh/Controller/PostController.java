package com.example.Baesh.Controller;

import com.example.Baesh.DTO.PostDTO;
import com.example.Baesh.Service.PostService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Parameter;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<PostDTO> posts(){
        return postService.getPosts();
    }

    @GetMapping("/{id}")
    public List<PostDTO> post(@PathVariable("id") Long postId){
        return postService.getPost(postId);
    }

    @PostMapping
    public void createPost(@RequestBody PostDTO postDTO){
        postService.createPost(postDTO);
    }

    @PutMapping("/{id}")
    public void updatePost(@PathVariable("id") Long postId,@RequestBody PostDTO postDTO){
        postService.updatePost(postId,postDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Long postId){
        postService.deletePost(postId);
    }



}
