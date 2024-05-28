package reforme.reforme.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import reforme.reforme.entity.MeCategory;
import reforme.reforme.entity.board.Reforme;
import java.util.List;

public interface ReformeRepository extends JpaRepository<Reforme, Long> {

    Page<Reforme> findAllByOrderByCreatedDateTimeDesc(Pageable page);


    // 특정 카테고리에 속하는 게시글들을 가져오는 메서드
    Page<Reforme> findByCategory(MeCategory category, Pageable pageable);

    Page<Reforme> findByTitleContaining(String searchWord, PageRequest of);

    Page<Reforme> findByCategoryAndTitleContaining(MeCategory category, String searchWord, PageRequest of);
}
