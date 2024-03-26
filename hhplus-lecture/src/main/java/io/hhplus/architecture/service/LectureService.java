package io.hhplus.architecture.service;


import org.springframework.stereotype.Service;

import io.hhplus.architecture.dto.LectureResDTO;
import io.hhplus.architecture.dto.ReserveResDTO;

@Service
public interface LectureService {

	//강의신청
	public String  applyLecture(Long userId, Long lectureId); 
	//강의 신청 조회
	public ReserveResDTO inquirLecture(long userId, Long lectureId);

}
