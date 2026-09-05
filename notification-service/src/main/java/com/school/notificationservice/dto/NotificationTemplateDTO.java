package com.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationTemplateDTO {
    private Long id;

    @NotBlank(message = "Template name is required")
    private String templateName;

    @NotBlank(message = "Notification type is required")
    private String notificationType;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Message template is required")
    private String messageTemplate;

    private Boolean isActive;
}
