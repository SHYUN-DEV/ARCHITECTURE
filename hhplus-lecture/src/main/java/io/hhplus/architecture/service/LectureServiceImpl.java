package io.hhplus.architecture.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import io.hhplus.architecture.domain.LectureInfo;
import io.hhplus.architecture.domain.ReserveInfo;
import io.hhplus.architecture.dto.LectureResDTO;
import io.hhplus.architecture.dto.ReserveResDTO;
import io.hhplus.architecture.repository.LectureInfoRepository;
import io.hhplus.architecture.repository.ReserveInfoRepository;
import jakarta.persistence.EntityNotFoundException;


public class LectureServiceImpl implements LectureService {
	
	
	private final LectureInfoRepository lectureInfoRepository;
	private final ReserveInfoRepository reserveInfoRepository;
	//pivate final LectureServieImpl lectureServieImpl;
	    
	@Autowired
	public LectureServiceImpl(LectureInfoRepository lectureRepository, ReserveInfoRepository reserveInfoRepository) {
	    this.lectureInfoRepository = lectureRepository;
	    this.reserveInfoRepository = reserveInfoRepository;
	}
	
	//수강신청
	@Override
	@Transactional
	public String applyLecture(Long lectureId, Long userId) {
	    // 강의 정보 조회
	    LectureInfo lectureInfo = lectureInfoRepository.findByIdWithWriteLock(lectureId)
	            .orElseThrow(() -> new RuntimeException("Lecture not found"));

	    // 이미 예약된 유저인지 확인
	    boolean userAlreadyEnrolled = reserveInfoRepository.existsByUserIdAndLectureId(userId, lectureId);
	    if (userAlreadyEnrolled) {
	        return "fail"; // 이미 예약된 경우
	    }

	    // 남은 좌석 수 확인
	    int remainingSeats = (int) (lectureInfo.getCapacity() - lectureInfo.getReservationCount());

	    // 예약 가능한지 확인
	    if (remainingSeats > 0) {
	        // 예약 정보 저장
	        ReserveInfo reserveInfo = new ReserveInfo();
	        reserveInfo.setLectureId(lectureId);
	        reserveInfo.setUserId(userId);
	        LocalDateTime now = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	        reserveInfo.setReserveDate(now.format(formatter));
	        reserveInfoRepository.save(reserveInfo);

	        // 예약 수 증가 및 저장
	        lectureInfo.setReservationCount(lectureInfo.getReservationCount() + 1);
	        lectureInfoRepository.save(lectureInfo);

	        return "success";
	    } else {
	        return "fail"; 
	    }
	}

	//수강신청조회  
	@Override
	@Transactional(readOnly = true)
	public ReserveResDTO inquirLecture(long userId, Long lectureId) {
	
		// 수강신청 정보 조회
	    ReserveInfo reserveInfo = reserveInfoRepository.findByUserIdAndLectureId(userId, lectureId)
	            .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));

	 
	    ReserveResDTO responseDTO = new ReserveResDTO();
	    responseDTO.setLectureId(reserveInfo.getLectureId());
	    responseDTO.setUserId(reserveInfo.getUserId());
	    responseDTO.setReserveDate(reserveInfo.getReserveDate());

	    return responseDTO;
	}

}
