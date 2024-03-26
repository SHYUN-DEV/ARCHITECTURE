package io.hhplus.architecture.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.architecture.domain.ReserveInfo;
import io.hhplus.architecture.dto.ReserveResDTO;

public interface ReserveInfoRepository extends JpaRepository<ReserveInfo, Long>{

	void save(Long userId, Long lectureId);

	ReserveResDTO selectById(Long userId, Long lectureId);

	boolean existsByUserIdAndLectureId(Long userId, Long lectureId);

}
