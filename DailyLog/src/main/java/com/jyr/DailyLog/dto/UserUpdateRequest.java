package com.jyr.DailyLog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 1, max = 10, message = "닉네임은 1~10자여야 합니다.")
    private String nickname;

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}:;\"',.<>/?]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String currentPassword;

    @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}:;\"',.<>/?]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String newPassword;

    @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}:;\"',.<>/?]).{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String confirmNewPassword;
}
