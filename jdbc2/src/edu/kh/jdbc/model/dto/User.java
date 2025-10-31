package edu.kh.jdbc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// DTO : DATA TRANSFER OBJECT (데이터 전송 객체)
 // -> 값을 묶어 전달하는 용도의 객체
 // DB 에 데이터를 묶어 전달하거나, DB 에서 조회한 결과를 가져올 때 사용 (데이터 교환 목적의 개체)
 // DB 특정 테이블의 한 행의 데이터를 저장할 수 있는 형태로 CLASS 정의

// 1. DTO 세팅

// lombok 라이브러리 : 
// 자바 코드에서 반복적으로 작성해야 하는 코드( = 보일러플레이트 코드)를 내부적으로 자동 완성해주는 라이브러리
 // 사용법 :
  // 1. 사용하고자 하는 프로젝트에 라이브러리 추가(ojdbc 드라이버처럼)
  // 2. java 코드 작성하고 있는 IDE(툴, 현재는 이클립스) 에 설치해야 함

// lombok 설치 후 클래스 바깥에 아래 코드를 작성하면 손쉽게 기본코드 작성 가능

@Getter
@Setter
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 매개변수가 있는 생성자
@ToString

public class User {
	
	private int userNo;
	private String userId;
	private String userPw;
	private String userName;
	private String enrollDate; 
	// enrollDate 는 DB 에서 DATE 타입이며 JAVA 에서 java.sql.Date 를 이용해 날짜 형식으로 받아올 수 있지만
	// String 으로 받은 이유 :
	 // DB 조회 시 날짜 데이터를 원하는 형태의 문자열로 변환하여 조회할 예정이기 때문
	 // 2025-10-29 16:26:14.000 라는 데이터를 DB에서 TO_CHAR 를 이용하여 String 형태로 바꿀 것
	
	// TB_USER 의 한 행씩 가져와 하나의 User 객체로 작성할 예정
	// 즉, 위 변수 하나 하나는 User 객체의 한 요소이자 TB_USER 테이블의 각 컬럼명
	
}
