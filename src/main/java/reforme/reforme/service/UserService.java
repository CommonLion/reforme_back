package reforme.reforme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reforme.reforme.entity.User;
import reforme.reforme.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public String join(User user) {
        userRepository.save(user);
        return user.getId();
    }

    /**
     * id 중복 검사
     */
    public void validateDuplicateUser(User user) {
        List<User> findUsers = userRepository.findById(user.getId());
        if (!findUsers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 id입니다.");
        }
    }

    /**
     * 사용자 전체 조회
     */
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public User findOne(String userId) {
        return userRepository.findOne(userId);
    }
}
