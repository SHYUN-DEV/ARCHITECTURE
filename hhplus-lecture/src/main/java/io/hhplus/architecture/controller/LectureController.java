package io.hhplus.architecture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.architecture.dto.LectureResDTO;
import io.hhplus.architecture.dto.ReserveResDTO;
import io.hhplus.architecture.service.LectureService;

@RestController
@RequestMapping("/lecture")
public class LectureController {
    
    private final LectureService lectureService;
   
    
    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }
    
    //특강 신청
    @PostMapping("/{userId}/apply")
    public String apply(@PathVariable Long userId,
                               @RequestBody Long lectureId) {
        
    	return lectureService.applyLecture(userId, lectureId);
    }
    
    //특강신청 여부 조회
    @GetMapping("/{userId}/{lectureId}/inquire")
    public ReserveResDTO inquire(@PathVariable long userId,
    							 @PathVariable Long lectureId) {
        
        return lectureService.inquirLecture(userId, lectureId);
        
    }
}

