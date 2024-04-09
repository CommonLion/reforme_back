package reforme.reforme.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reforme.reforme.entity.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findOne(String id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public List<User> findById(String id) {
        return em.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id", id)
                .getResultList();
    }
}
