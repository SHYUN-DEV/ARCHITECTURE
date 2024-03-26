package io.hhplus.architecture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureResDTO {
    private Long   lectureId;
    private String lectureTitle;
    private String startTime;
    private String endTime;
    private Long   capacity;
    private String instructor;
    private String place;
    private Long   reservationCount;
}
