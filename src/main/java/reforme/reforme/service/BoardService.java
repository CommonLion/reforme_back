package reforme.reforme.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import reforme.reforme.dto.BoardDto;
import reforme.reforme.dto.BoardUpdateDto;
import reforme.reforme.dto.ResponseBody;


//게시판 생성 수정 삭제 기능에 대한 서비스
public interface BoardService {
    //GPT애서 나온거
    //ResponseEntity<?> createBoard(String title, String body, String category, MultipartFile[] images);
    ResponseBody<?> createBoard(BoardDto boardDto, MultipartFile[] images, String repositoryType);
    ResponseBody<?> updateBoard(Long boardId, BoardUpdateDto updateDto, MultipartFile[] images, String repositoryType);
    ResponseBody<?> deleteBoard(Long boardId, String repositoryType);
}
