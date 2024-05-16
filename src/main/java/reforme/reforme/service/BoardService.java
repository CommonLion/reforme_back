package reforme.reforme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reforme.reforme.dto.BoardDto;
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

    public List<BoardDto> getReformeData(Page<Reforme> boards){
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Reforme reforme : boards.getContent()) {
            BoardDto boardDto = new BoardDto(reforme);
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }

    public List<BoardDto> getReforyouData(Page<Reforyou> boards){
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Reforyou reforyou : boards.getContent()) {
            BoardDto boardDto = new BoardDto(reforyou);
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }

    public BoardDto findByIdReformeBoard(Long id){
        Optional<Reforme> result = reformeRepository.findById(id);
        if(result.isPresent()){
            Reforme reforme = result.get();
            return new BoardDto(reforme);
        }
        return null;
    }

    public BoardDto findByIdReforyouBoard(Long id){
        Optional<Reforyou> result = reforyouRepository.findById(id);
        if(result.isPresent()){
            Reforyou reforyou = result.get();
            return new BoardDto(reforyou);
        }
        return null;
    }



}
