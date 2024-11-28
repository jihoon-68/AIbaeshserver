package com.example.Baesh.Controller;

import com.example.Baesh.DTO.FineTune.FineTuneRequest;
import com.example.Baesh.DTO.FineTune.FineTuneResponse;
import com.example.Baesh.Entity.ChatHistoryEntity;
import com.example.Baesh.Interface.ChatHistoryRepository;
import com.example.Baesh.Service.OpenAiClientService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/gpt")
public class GptController {

    private final OpenAiClientService openAiClientService;
    private final OpenAiChatModel chatModel;

    public GptController(OpenAiChatModel chatModel, ChatHistoryRepository chatHistoryRepository,
                         OpenAiClientService openAiClientService) {
        this.chatModel = chatModel;
        this.openAiClientService = openAiClientService;
    }

    @PostMapping("/generate")
    public Map<String, String> generate(@RequestBody Map<String, Object> request) {
        String userMessage = (String) request.get("message");
        Long userId = ((Number) request.get("userId")).longValue();
        // JSON 배열로 대화 관리
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject(Map.of("role", "user", "content", userMessage)));

        // 사용자 입력을 AI 모델에 전달하여 봇의 응답 생성
        String botReply = chatModel.call(userMessage);

        // AI 봇의 응답을 JSON 객체로 추가
        messages.put(new JSONObject(Map.of("role", "assistant", "content", botReply)));

        openAiClientService.saveChatMessage(messages,userId);
        // AI 응답 반환
        return Map.of("genera", botReply);
    }

    @PostMapping("/update")
    public FineTuneResponse gptFineTun(@RequestBody Long userId) throws IOException {
        String fineTuneId;
        fineTuneId = openAiClientService.gptFineTunUpdate(userId);
        return openAiClientService.getFineTuneStatus(fineTuneId);
    }

    /* gptFineTun 하나의 요청으로 합침
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }

        // 파일을 OpenAI API로 업로드하고 fileId를 받음
        String fileId = openAiClientService.uploadFile(convFile.getPath());

        // 업로드된 파일 삭제
        convFile.delete();

        // fileId가 null이거나 비어 있으면 오류 메시지 반환
        if (fileId == null || fileId.isEmpty()) {
            return "{\"error\": \"파일 업로드 실패. fileId를 받지 못했습니다.\"}";
        }

        // 성공적으로 fileId 반환
        return "{\"fileId\": \"" + fileId + "\"}";
    }

    @PostMapping("/finetune")
    public String startFineTuning(@RequestBody FineTuneRequest fineTuneRequest) throws IOException {
        FineTuneResponse fineTune = openAiClientService.startFineTuning(
                fineTuneRequest.getTrainingFile(),
                fineTuneRequest.getModel()
        );
        return fineTune != null ? fineTune.getId() : "Error in fine-tuning";
    }
    */

    @GetMapping("/finetune/status/{fineTuneId}")
    public FineTuneResponse getFineTuneStatus(@PathVariable("fineTuneId") String fineTuneId) throws IOException {
        return openAiClientService.getFineTuneStatus(fineTuneId);
    }
}
