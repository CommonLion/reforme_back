package reforme.reforme.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BoardCreateDto {
    String title;   //작성한 제목
    String body;    //작성 내용
    String category;//카테고리
    String userId;  //사용자 아이디
    MultipartFile[] images; // 이미지
}
