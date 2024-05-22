package reforme.reforme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reforme.reforme.entity.board.Board;

@Repository
public interface ReformeRepository extends JpaRepository<Board, Long> {

}
