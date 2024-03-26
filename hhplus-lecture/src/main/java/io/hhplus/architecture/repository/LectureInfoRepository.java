package io.hhplus.architecture.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.architecture.domain.LectureInfo;
import io.hhplus.architecture.dto.LectureResDTO;
import io.hhplus.architecture.dto.ReserveResDTO;

public interface LectureInfoRepository extends JpaRepository<LectureInfo, Long> {

	Optional<LectureInfo> findByIdWithWriteLock(Long lectureId);

//	ReserveResDTO selectByUserIdByAndLectureId(long userId, Long lectureId);





}
