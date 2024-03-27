package io.hhplus.architecture.service;


import io.hhplus.architecture.dto.ReserveResDTO;


public interface LectureService {

	//강의신청
	public String  applyLecture(Long userId, Long lectureId); 
	//강의 신청 조회
	public ReserveResDTO inquirLecture(long userId, Long lectureId);

}
