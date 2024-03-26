package io.hhplus.architecture.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import io.hhplus.architecture.domain.LectureInfo;
import io.hhplus.architecture.dto.LectureResDTO;
import io.hhplus.architecture.dto.ReserveResDTO;
import jakarta.persistence.LockModeType;

public interface LectureInfoRepository extends JpaRepository<LectureInfo, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<LectureInfo> findByIdWithWriteLock(Long lectureId);

//	ReserveResDTO selectByUserIdByAndLectureId(long userId, Long lectureId);





}
