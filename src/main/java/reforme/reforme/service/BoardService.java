package reforme.reforme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reforme.reforme.dto.BoardDetailDto;
import reforme.reforme.dto.BoardDto;
import reforme.reforme.entity.Comment;
import reforme.reforme.entity.Image;
import reforme.reforme.entity.board.Reforme;
import reforme.reforme.entity.board.Reforyou;
import reforme.reforme.repository.CommentRepository;
import reforme.reforme.repository.ReformeRepository;
import reforme.reforme.repository.ReforyouRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final ReformeRepository reformeRepository;
    private final ReforyouRepository reforyouRepository;
    private final CommentRepository commentRepository;

    public List<BoardDto> getReformeData(Page<Reforme> boards){
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Reforme reforme : boards.getContent()) {
            BoardDto dto = new BoardDto();
            dto.setId(reforme.getId());
            dto.setTitle(reforme.getTitle());
            dto.setCategory(String.valueOf(reforme.getCategory()));
            List<Image> images = reforme.getImages();
            if (!images.isEmpty()) {
                Image image = images.get(0);
                dto.setImagePath(image.getImagePath());
            }
            dto.setComment(reforme.getComments().size());
            dto.setCreatedDateTime(String.valueOf(reforme.getCreatedDateTime()));
            boardDtos.add(dto);
        }
        return boardDtos;
    }

    public List<BoardDto> getReforyouData(Page<Reforyou> boards){
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Reforyou reforyou : boards.getContent()) {
            BoardDto dto = new BoardDto();
            dto.setId(reforyou.getId());
            dto.setTitle(reforyou.getTitle());
            dto.setCategory(String.valueOf(reforyou.getCategory()));
            List<Image> images = reforyou.getImages();
            if (!images.isEmpty()) {
                Image image = images.get(0);
                dto.setImagePath(image.getImagePath());
            }
            dto.setComment(reforyou.getComments().size());
            dto.setCreatedDateTime(String.valueOf(reforyou.getCreatedDateTime()));
            boardDtos.add(dto);
        }
        return boardDtos;
    }




    public BoardDetailDto findByIdReformeBoard(Long id){
        Optional<Reforme> result = reformeRepository.findById(id);
        if(result.isPresent()){
            Reforme reforme = result.get();
            return new BoardDetailDto(reforme);
        }
        return null;
    }

    public BoardDetailDto findByIdReforyouBoard(Long id){
        Optional<Reforyou> result = reforyouRepository.findById(id);
        if(result.isPresent()){
            Reforyou reforyou = result.get();
            return new BoardDetailDto(reforyou);
        }
        return null;
    }



}
