package com.jyr.DailyLog.api;

import com.jyr.DailyLog.dto.DeleteUserRequest;
import com.jyr.DailyLog.dto.LoginRequestDto;
import com.jyr.DailyLog.dto.UserSignupRequestDto;
import com.jyr.DailyLog.dto.UserUpdateRequest;
import com.jyr.DailyLog.security.JwtUtil;
import com.jyr.DailyLog.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupRequestDto requestDto,
                                    BindingResult result){
        if (result.hasErrors()){
            List<String> errorMessage = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            return ResponseEntity.badRequest().body(Map.of("errors", errorMessage));
        }

        try {
            userService.signup(requestDto);
            return ResponseEntity.ok(Map.of("message", "회원가입이 완료되었습니다."));
        }catch (IllegalArgumentException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/user-update")
    public ResponseEntity<?> userUpdate(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserUpdateRequest requestDto,
            BindingResult result) {

        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            Map<String, Boolean> changeStatus = userService.userUpdate(userDetails.getUsername(), requestDto);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원정보 수정이 완료되었습니다.");
            response.putAll(changeStatus);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            String token = jwtUtil.generateToken(userDetails.getUsername(), roles);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of("success", true));
        }catch (AuthenticationException e){
            Map<String, String> error = new HashMap<>();
            error.put("error","로그인 실패: 이메일 또는 비밀번호가 올바르지 않습니다.");
            return ResponseEntity.status(401).body(error);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("success",true));
    }

    @PostMapping("/user-delete")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserRequest request,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long userId = userService.findUser(userDetails.getUsername()).getId();

            boolean deleted = userService.deleteUser(userId, request.getCurrentPassword());
            if (deleted) {
                return ResponseEntity.ok().body(Map.of("message", "회원 탈퇴 완료"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "비밀번호가 올바르지 않습니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "회원 탈퇴 중 오류가 발생했습니다."));
        }
    }
}
