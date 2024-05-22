package reforme.reforme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reforme.reforme.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}