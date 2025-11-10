package com.jyr.DailyLog.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String nickname;
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
