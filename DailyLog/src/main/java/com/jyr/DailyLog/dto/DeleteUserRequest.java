package com.jyr.DailyLog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteUserRequest {
    private String currentPassword;
}
