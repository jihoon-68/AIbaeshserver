package com.example.Baesh.Service;


import com.example.Baesh.DTO.Oauth.IdTokenRequestDTO;
import com.example.Baesh.DTO.User.ChatRoomuserDTO;
import com.example.Baesh.DTO.User.PostUserDTO;
import com.example.Baesh.DTO.User.UserDTO;
import com.example.Baesh.Entity.UserEntity;
import com.example.Baesh.Interface.UserRepository;
import com.example.Baesh.JWT.JWTUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServcie {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final GoogleIdTokenVerifier verifier;

    public UserServcie(@Value("${app.googleClientId}") String clientId, UserRepository userRepository,
                          JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public UserEntity getUser (Long id) {return userRepository.findById(id).orElse(null);}

    public List<UserDTO> getUserList(){
        List<UserEntity> userEntityList =userRepository.findAll();
        return userEntityList
                .stream()
                .map(this::userEntityToDTO)
                .collect(Collectors.toList());
    }

    public String loginOAuthGoogle(IdTokenRequestDTO requestDTO){
        System.out.println("Starting OAuth Google Login");

        // Google ID 토큰 검증
        UserEntity user = verifyIDToken(requestDTO.getIdToken());
        if(user == null){
            System.out.println("Invalid ID Token");
            throw new IllegalArgumentException("Invalid ID Token");
        }

        System.out.println("Google OAuth verified, account: " + user.getEmail());

        user = createOrUpdateUser(user);
        System.out.println("Account after create or update: " + user.getEmail());

        return jwtUtils.createToken(user, false);
    }

    public UserEntity createOrUpdateUser(UserEntity userEntity){

        UserEntity existingUser = userRepository.findByEmail(userEntity.getEmail()).orElse(null);

        if(existingUser == null){
            //유저가 없다면 계정 새로 등록
            userEntity.setRoles("ROLE_USER");
            userRepository.save(userEntity);
            System.out.println("New account created in MySQL: " + userEntity.getEmail());
            return userEntity;
        }

        // 기존 계정 업데이트

        existingUser.setFirstName(userEntity.getFirstName());
        existingUser.setLastName(userEntity.getLastName());
        existingUser.setPictureUrl(userEntity.getPictureUrl());
        userRepository.save(existingUser);

        System.out.println("Updated account in MySQL: " + existingUser.getEmail());
        return existingUser;
    }

    public UserEntity verifyIDToken(String idToken){
        try{
            System.out.println("Verifying ID Token: " + idToken);

            GoogleIdToken idTokenObj = verifier.verify(idToken);
            if(idTokenObj == null){
                System.out.println("ID Token is invalid or expired");
                return null;
            }

            GoogleIdToken.Payload payload = idTokenObj.getPayload();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String email = payload.getEmail();
            String pictureUrl = (String) payload.get("picture");

            System.out.println("ID Token verified: " + email);

            return new UserEntity(firstName,lastName,email,pictureUrl);

        } catch (GeneralSecurityException | IOException e) {
            System.out.println("Error verifying ID Token: " + e.getMessage());
            return null;
        }
    }

    public UserDTO userEntityToDTO(UserEntity user){
        return new UserDTO(
                user.getId(),
                user.getFirstName()+user.getLastName(),
                user.getEmail().split("@")[0],
                user.getMbti(),
                user.getPictureUrl(),
                user.getFollowing().size(),
                user.getFollowers().size()

        );
    }

    public ChatRoomuserDTO ChatUserEntityToDTO(UserEntity user){
        return new ChatRoomuserDTO(
                user.getId(),
                user.getFirstName()+user.getLastName(),
                user.getPictureUrl()
        );
    }

    public PostUserDTO postUserEntityToDTO(UserEntity user){
        return new PostUserDTO(
                user.getId(),
                user.getLastName(),
                user.getPictureUrl()
        );
    }
}
