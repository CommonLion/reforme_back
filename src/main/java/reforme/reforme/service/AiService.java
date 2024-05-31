package reforme.reforme.service;

import com.theokanning.openai.image.CreateImageEditRequest;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AiService {

    private final OpenAiService openAiService;

    public String generateImage(String prompt) {
        CreateImageRequest createImageRequest = CreateImageRequest.builder()
                .prompt(prompt)
                .size("1024x1024")
                .model("dall-e-3")
                .n(1)
                .responseFormat("b64_json")
                .build();

        String b64 = openAiService.createImage(createImageRequest).getData().get(0).getB64Json();
        return b64;
    }

    public String editImage(MultipartFile imageFile, String prompt, MultipartFile maskFile) {
        try {
            // 이미지 파일 저장 및 RGBA 변환
            File image = convertMultipartFileToRGBAFile(imageFile);
            File mask;

            if (maskFile == null) {
                String originalFilename = imageFile.getOriginalFilename();
                mask = selectMaskFile(originalFilename);
            } else {
                mask = convertMultipartFileToMaskFile(maskFile);
            }

            // 이미지 수정 요청 생성
            CreateImageEditRequest createImageEditRequest = CreateImageEditRequest.builder()
                    .prompt(prompt)
                    .size("1024x1024")
                    .responseFormat("b64_json")
                    .build();

            // OpenAI API 호출
            String b64 = openAiService.createImageEdit(createImageEditRequest, image, mask).getData().get(0).getB64Json();

            // 응답 데이터 파일로 저장 (디버깅 용도)
            byte[] decodedImg = Base64.getDecoder().decode(b64);
            try (FileOutputStream fos = new FileOutputStream("edited_image.png")) {
                fos.write(decodedImg);
            }
            return b64;

        } catch (IOException e) {
            throw new RuntimeException("Failed to convert MultipartFile to File", e);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to edit image: " + e.getMessage(), e);
        }
    }

    private File convertMultipartFileToRGBAFile(MultipartFile file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        BufferedImage rgbaImage = new BufferedImage(
                bufferedImage.getWidth(),
                bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        rgbaImage.getGraphics().drawImage(bufferedImage, 0, 0, null);

        File tempFile = File.createTempFile("temp", ".png");
        ImageIO.write(rgbaImage, "png", tempFile);

        return tempFile;
    }

    private File convertMultipartFileToMaskFile(MultipartFile file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        File tempFile = File.createTempFile("mask", ".png");
        ImageIO.write(bufferedImage, "png", tempFile);
        return tempFile;
    }

    private File createDefaultMaskFile(File imageFile) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
        BufferedImage mask = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mask.createGraphics();
        g2d.setColor(new Color(255, 255, 255, 255));  // 흰색으로 채우기
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.dispose();

        File tempFile = File.createTempFile("mask", ".png");
        ImageIO.write(mask, "png", tempFile);

        return tempFile;
    }

    private File selectMaskFile(String originalFilename) throws IOException {
        String maskFileName;
        if (originalFilename.equals("shoes.png")) {
            maskFileName = "shoes mask.png";
        } else if (originalFilename.equals("shirts.png")) {
            maskFileName = "shirts mask.png";
        } else {
            throw new IOException("No matching mask file for the given image");
        }
        return loadMaskFile(maskFileName);
    }

    private File loadMaskFile(String maskFileName) throws IOException {
        // 로컬 파일 시스템에서 마스크 파일 로드
        File maskFile = Paths.get("C:\\Users\\bigbl\\Desktop\\maskImage", maskFileName).toFile();
        if (!maskFile.exists()) {
            throw new IOException("Mask file not found: " + maskFileName);
        }
        return maskFile;
    }

}
