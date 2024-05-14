package reforme.reforme.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import reforme.reforme.entity.YouCategory;
import reforme.reforme.entity.board.Reforyou;

import java.util.List;

public interface ReforyouRepository extends JpaRepository<Reforyou, Long> {

    Page<Reforyou> findAllByOrderByCreatedDateTimeDesc(PageRequest of);


    // 특정 카테고리에 속하는 게시글들을 가져오는 메서드
    Page<Reforyou> findByCategory(YouCategory category, Pageable pageable);

    Page<Reforyou> findByTitleContaining(String searchWord, PageRequest of);

    Page<Reforyou> findByCategoryAndTitleContaining(YouCategory category, String searchWord, PageRequest of);
}
