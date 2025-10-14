package com.jyr.DailyLog.service;

import com.jyr.DailyLog.domain.User;
import com.jyr.DailyLog.domain.enums.Role;
import com.jyr.DailyLog.dto.UserSignupRequestDto;
import com.jyr.DailyLog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findUser(String email){
        if (email == null || email.isBlank()){
            throw new IllegalArgumentException("email must not be null or blank");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email"));
    }

    @Transactional
    public void signup(UserSignupRequestDto requestDto){
        if (userRepository.existsByEmail(requestDto.getEmail())){
            throw new IllegalArgumentException("이미 가입된 이메일입니다: "+ requestDto.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .nickname(requestDto.getNickname())
                .role(Role.ROLE_USER)
                .build();

        try{
            userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            throw new IllegalArgumentException("이미 가입된 이메일입니다: "+ requestDto.getEmail());
        }
    }
}
