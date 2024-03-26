package io.hhplus.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import io.hhplus.architecture.domain.LectureInfo;
import io.hhplus.architecture.repository.LectureInfoRepository;
import io.hhplus.architecture.repository.ReserveInfoRepository;
import io.hhplus.architecture.service.LectureServiceImpl;
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
    @DisplayName("특정신청 성공")
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
        // given
        Long userId = 1L;
        Long lectureId = 1L;
        LectureInfo lectureInfo = new LectureInfo();
        lectureInfo.setCapacity((long) 30);
        lectureInfo.setReservationCount((long) 10);
        when(lectureInfoRepository.findByIdWithWriteLock(lectureId)).thenReturn(Optional.of(lectureInfo));
        when(reserveInfoRepository.existsByUserIdAndLectureId(userId, lectureId)).thenReturn(false);

        // when
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

        // then
        verify(lectureInfoRepository, times(1)).findByIdWithWriteLock(anyLong());
        verify(reserveInfoRepository, times(2)).existsByUserIdAndLectureId(anyLong(), anyLong());
        verify(lectureInfoRepository, times(1)).save(lectureInfo);
    }

    
    
    


    
//    @Test
//    @DisplayName("수강신청 - 실패")
//    void testApplyLectureFail() {
//        // given
//        Long userId = 1L;
//        Long lectureId = 1L;
//        ReserveResDTO existingReservation = new ReserveResDTO(); // 이미 수강 신청한 경우
//        when(reserveInfoRepository.selectById(userId, lectureId)).thenReturn(existingReservation);
//        
//        // when
//        String result = lectureService.applyLecture(userId, lectureId);
//        
//        // then
//        assertEquals("fail", result);
//    }
//    
    

    
    
    
    
    
    
}