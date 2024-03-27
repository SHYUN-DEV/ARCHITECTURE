package io.hhplus.architecture.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.hhplus.architecture.domain.ReserveInfo;

@Repository
public interface ReserveInfoRepository extends JpaRepository<ReserveInfo, Long>{


	boolean existsByUserIdAndLectureId(Long userId, Long lectureId);

	Optional<ReserveInfo> findByUserIdAndLectureId(long userId, Long lectureId);

}
