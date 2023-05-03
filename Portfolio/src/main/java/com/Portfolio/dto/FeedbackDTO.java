package com.Portfolio.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {

    private long feedbackNo;
    private String comments;
    private String writer;
    private String receiver;
    
    private LocalDateTime regDate,modDate;
    
}
