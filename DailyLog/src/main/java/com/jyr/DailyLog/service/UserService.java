package com.jyr.DailyLog.service;

import com.jyr.DailyLog.domain.User;
import com.jyr.DailyLog.domain.enums.Role;
import com.jyr.DailyLog.dto.UserSignupRequestDto;
import com.jyr.DailyLog.dto.UserUpdateRequest;
import com.jyr.DailyLog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

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

    @Transactional
    public Map<String, Boolean> userUpdate(String email, UserUpdateRequest requestDto) {
        User user = findUser(email);

        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        Map<String, Boolean> changeStatus = new HashMap<>();
        changeStatus.put("nicknameChanged", false);
        changeStatus.put("emailChanged", false);
        changeStatus.put("passwordChanged", false);

        // 닉네임 변경
        if (!user.getNickname().equals(requestDto.getNickname())) {
            user.changeProfile(requestDto.getNickname(), user.getEmail());
            changeStatus.put("nicknameChanged", true);
        }

        // 이메일 변경
        if (!user.getEmail().equals(requestDto.getEmail())) {
            if (userRepository.existsByEmail(requestDto.getEmail())) {
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
            }
            user.changeProfile(user.getNickname(), requestDto.getEmail());
            changeStatus.put("emailChanged", true);
        }

        // 비밀번호 변경
        if (requestDto.getNewPassword() != null && !requestDto.getNewPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(requestDto.getNewPassword());
            user.changePassword(encodedPassword);
            changeStatus.put("passwordChanged", true);
        }

        return changeStatus;
    }
}
