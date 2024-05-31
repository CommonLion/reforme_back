package reforme.reforme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import reforme.reforme.dto.AiChatRequestDto;
import reforme.reforme.dto.AiChatResponseDto;
import reforme.reforme.service.AiService;
import reforme.reforme.util.FileUtils;

@RestController
@RequestMapping("/aichat")
public class AiChatController {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String URL;

    @Autowired
    private RestTemplate template;

    private final AiService aiService;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    public AiChatController(AiService aiService)
    {
        this.aiService = aiService;
    }

    @PostMapping("/chat_text")
    public ResponseEntity<String> aiChat(@RequestParam(name = "text") String prompt)
    {
        AiChatRequestDto request = new AiChatRequestDto(model, prompt);
        AiChatResponseDto aiChatResponseDto = template.postForObject(URL, request, AiChatResponseDto.class);

        return new ResponseEntity<>(aiChatResponseDto.getChoices().get(0).getMessage().getContent(), HttpStatus.OK);
    }

    @PostMapping("/chat_image")
    public ResponseEntity<?> generateImage(@RequestParam("prompt") String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return new ResponseEntity<>("Prompt cannot be empty", HttpStatus.BAD_REQUEST);
        }

        try {
            String imageData = aiService.generateImage(prompt);
            return new ResponseEntity<>(imageData, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/chat_modify")
    public ResponseEntity<?> aiImage(@RequestPart(value = "image") MultipartFile image,
                                     @RequestParam("prompt") String prompt,
                                     @RequestPart(value = "mask", required = false) MultipartFile mask)
    {
        if (prompt == null || prompt.trim().isEmpty()) {
            return new ResponseEntity<>("Prompt cannot be empty", HttpStatus.BAD_REQUEST);
        }

        try {
            String imageData;
            imageData = aiService.editImage(image, prompt, mask);   // 이미지 수정
            return new ResponseEntity<>(imageData, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}