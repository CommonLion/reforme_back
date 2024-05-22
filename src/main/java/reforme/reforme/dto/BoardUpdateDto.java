package reforme.reforme.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

//게시글 수정을 위한 DTO
@Getter
@Setter
public class BoardUpdateDto {
    String title;   //작성한 제목
    String body;    //작성 내용
    String category;//카테고리
    MultipartFile[] images; // 이미지
}
