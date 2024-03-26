package io.hhplus.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.hhplus.architecture.domain.ReserveInfo;
import io.hhplus.architecture.dto.ReserveResDTO;
import io.hhplus.architecture.domain.LectureInfo;
import io.hhplus.architecture.repository.LectureInfoRepository;
import io.hhplus.architecture.repository.ReserveInfoRepository;
import io.hhplus.architecture.service.LectureServiceImpl;
import jakarta.persistence.EntityNotFoundException;

import static org.mockito.ArgumentMatchers.any;


public class LectureServiceTest {
    
    @Mock
    private LectureInfoRepository lectureInfoRepository;
    
    @Mock
    private ReserveInfoRepository reserveInfoRepository;
    
    @InjectMocks
    private LectureServiceImpl lectureService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    @DisplayName("특강신청 성공")
    void testApplyLecture_Success() {
        // given
        Long lectureId = 1L;
        Long userId = 1L;
        Long capacity = 30L;
        Long reservationCount = 10L;

        
        LectureInfo lectureInfo = new LectureInfo();
        lectureInfo.setCapacity(capacity);
        lectureInfo.setReservationCount(reservationCount);
        when(lectureInfoRepository.findByIdWithWriteLock(lectureId)).thenReturn(Optional.of(lectureInfo));

       
        when(reserveInfoRepository.existsByUserIdAndLectureId(userId, lectureId)).thenReturn(false);

    
        LocalDateTime mockDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = mockDateTime.format(formatter);

        
        String result = lectureService.applyLecture(lectureId, userId);

        
        assertEquals("success", result);
        verify(reserveInfoRepository, times(1)).save(any(ReserveInfo.class));
        verify(lectureInfoRepository, times(1)).save(any(LectureInfo.class));
        assertEquals(reservationCount + 1, lectureInfo.getReservationCount());
    }
    
    
    @Test
    @DisplayName("한 유저가 여러번 신청한 경우")
    void testUserApplyMultipleTimes() {
        // given
        Long userId = 1L;
        Long lectureId = 1L;
        LectureInfo lectureInfo = new LectureInfo();
        lectureInfo.setCapacity((long) 30);
        lectureInfo.setReservationCount((long) 10);
        when(lectureInfoRepository.findByIdWithWriteLock(lectureId)).thenReturn(Optional.of(lectureInfo));
        when(reserveInfoRepository.existsByUserIdAndLectureId(userId, lectureId)).thenReturn(false);

        // when
        String result1 = lectureService.applyLecture(lectureId, userId);
        String result2 = lectureService.applyLecture(lectureId, userId);

        // then
        assertEquals("success", result1);
        assertEquals("fail", result2);

        verify(lectureInfoRepository, times(1)).findByIdWithWriteLock(anyLong());
        verify(reserveInfoRepository, times(2)).existsByUserIdAndLectureId(anyLong(), anyLong());
        verify(lectureInfoRepository, times(1)).save(lectureInfo);
    }


    @Test
    @DisplayName("동시성 테스트")
    void testConcurrency() throws InterruptedException {
        
        Long userId = 1L;
        Long lectureId = 1L;
        LectureInfo lectureInfo = new LectureInfo();
        lectureInfo.setCapacity((long) 30);
        lectureInfo.setReservationCount((long) 10);
        when(lectureInfoRepository.findByIdWithWriteLock(lectureId)).thenReturn(Optional.of(lectureInfo));
        when(reserveInfoRepository.existsByUserIdAndLectureId(userId, lectureId)).thenReturn(false);

       
        Thread thread1 = new Thread(() -> {
            lectureService.applyLecture(lectureId, userId);
        });

        Thread thread2 = new Thread(() -> {
            lectureService.applyLecture(lectureId, userId);
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        
        verify(lectureInfoRepository, times(1)).findByIdWithWriteLock(anyLong());
        verify(reserveInfoRepository, times(2)).existsByUserIdAndLectureId(anyLong(), anyLong());
        verify(lectureInfoRepository, times(1)).save(lectureInfo);
    }

    
    
    @Test
    @DisplayName("수강신청 정보가 존재하는 경우")
    void testInquiryExistingEnrollment() {
        // given
        long userId = 1L;
        Long lectureId = 1L;
        ReserveInfo reserveInfo = new ReserveInfo();
        reserveInfo.setLectureId(lectureId);
        reserveInfo.setUserId(userId);

        when(reserveInfoRepository.findByUserIdAndLectureId(userId, lectureId)).thenReturn(Optional.of(reserveInfo));

        // when
        ReserveResDTO result = lectureService.inquirLecture(userId, lectureId);

        // then
        assertNotNull(result);
        assertEquals(lectureId, result.getLectureId());
        assertEquals(userId, result.getUserId());
    }

    
    @Test
    @DisplayName("수강신청 정보가 존재하지 않는 경우")
    void testInquiryNonExistingEnrollment() {
        
        long userId = 1L;
        Long lectureId = 1L;

        when(reserveInfoRepository.findByUserIdAndLectureId(userId, lectureId)).thenReturn(java.util.Optional.empty());

        
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
        	lectureService.inquirLecture(userId, lectureId);
        });

        
        assertEquals("Enrollment not found", exception.getMessage());
    }
    



    
    
    
    
    
}
