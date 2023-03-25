package com.bilgeadam.exception;

import lombok.*;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ErrorMessage {
    private int code;
    private String message;
    private List<String> fields;

    @Builder.Default
    private LocalDateTime date= LocalDateTime.now();
}
