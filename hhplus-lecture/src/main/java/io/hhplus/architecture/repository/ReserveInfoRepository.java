package io.hhplus.architecture.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import io.hhplus.architecture.domain.ReserveInfo;


public interface ReserveInfoRepository extends JpaRepository<ReserveInfo, Long>{


	boolean existsByUserIdAndLectureId(Long userId, Long lectureId);

	Optional<ReserveInfo> findByUserIdAndLectureId(long userId, Long lectureId);

}
