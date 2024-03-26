package io.hhplus.architecture.domain;

import java.util.Date;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "lectureInfo")
public class LectureInfo {
	

		@Id
		@Column(name = "lecture_id")
		private Long lectureId;
		
		
		
		@Column(name = "lecture_title")
		private String lectureTitle;
		
		

		@Column(name = "start_time")
		private String startTime;
		
		
		
		@Column(name = "reserve_date")
		private String reserveDate;
		
		
	
		@Column(name = "capacity")
		private Long capacity;
		
		
		@Column(name = "instructor")
		private String instructor;
		

		@Column(name = "place")
		private String place;

		
		
		@Column(name = "reservation_count")
		private Long reservationCount;
		
	

}
