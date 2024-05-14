package reforme.reforme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reforme.reforme.dto.UserDto;
import reforme.reforme.entity.User;
import reforme.reforme.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입
     */
    @Transactional
    public String join(UserDto userDto) {

        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setNickname(userDto.getNickname());

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

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        List<User> findUsers = this.userRepository.findById(userId);
        if (findUsers.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        User user = findUsers.get(0);
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(userId)) {
            authorities.add(new SimpleGrantedAuthority("admin"));
        } else {
            authorities.add(new SimpleGrantedAuthority("user"));
        }
        return new org.springframework.security.core.userdetails.User(user.getId(), user.getPassword(), authorities);
    }
}
