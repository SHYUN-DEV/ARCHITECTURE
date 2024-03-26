package io.hhplus.architecture.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.architecture.domain.LectureInfo;

public interface LectureInfoRepository extends JpaRepository<LectureInfo, Long> {

	Optional<LectureInfo> findByIdWithWriteLock(Long lectureId);





}
