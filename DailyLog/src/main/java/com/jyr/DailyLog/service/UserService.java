package com.jyr.DailyLog.service;

import com.jyr.DailyLog.domain.User;
import com.jyr.DailyLog.dto.UserSignupRequestDto;
import com.jyr.DailyLog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
                .build();

        try{
            userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            throw new IllegalArgumentException("이미 가입된 이메일입니다: "+ requestDto.getEmail());
        }
    }
}
