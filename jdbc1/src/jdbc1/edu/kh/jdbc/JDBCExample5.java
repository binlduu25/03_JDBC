package jdbc1.edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample5 {
	
	public static void main(String[] args) {
		
		// 아이디, 비밀번호, 이름을 입력받아 TB_USER 테이블에 삽입(INSERT) 하기
		
		// 1. JDBC 객체 참조변수 선언
		
		Connection conn = null;
		
		// java.sql.PreparedStatement
		 // - SQL 중간에 ? (위치홀더, placeholder) 를 작성하여 ? 자리에 java 값을 대입할 준비가 되어 있는 Statement
		 // ** PreparedStatement 는 Statement 의 자식 클래스 **
		 // 예시. "SELECT EMP_ID, EMP_NAME 
		 //		   FROM EMPLOYEE
		 // 	   WHERE EMP_NAME = '?'"
		 // 	   ? = 노옹철
		 // 위 방식으로 작성 가능
		
		// 장점 1 : SQL 작성 간편
		// 장점 2 : ? 에 값 대입 시 자료형에 맞는 형태의 리터럴으로 대입됨
		//			> String 일 시 홑따옴표 자동으로 붙여줌
		//			> int 대입 시 : 값
		// 장점 3 : 일반 Statement 보다 성능, 속도에서 우위
		
		PreparedStatement pstmt = null;
		
		// SELECT 가 아니기 때문에 ResultSet 필요 없다
		
		Scanner sc = null;
		
		try {
			
			// 2. DriverManager 통해 Connection 객체 생성
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE"; // DB 접속 정보 작성 
			String userName ="c##sjh"; 
			String password = "sjh1234"; 
			
			conn = DriverManager.getConnection(url, userName, password);
			
			System.out.println(conn);
			
			// 3. SQL 작성
			
			sc = new Scanner(System.in);
			
			System.out.print("아이디 입력 : ");
			String inputId  = sc.next();
			
			System.out.print("비밀번호 입력 : ");
			String inputPw  = sc.next();
			
			System.out.print("이름 입력 : ");
			String inputName  = sc.next();
			
			String sql = """ 
						 INSERT INTO TB_USER 
						 VALUES (SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT)	
						 """; 
						 // placeholder 3개
						  
			// 4. PreparedStatement 객체 생성
			 // -> 미리 위치홀더 '?' 에 값을 받을 준비를 해야 하기에 객체 생성과 동시에 SQL 이 담긴다
			
			pstmt = conn.prepareStatement(sql);
			// 객체를 생성할 때부터 sql 버스(pstmt) 에 실려 있다.
			
			// 5. '? 위치홀더에 알맞은 값 대입 (? 순서대로)
			 // pstmt.set자료형(?순서, 대입할 값);
			pstmt.setString(1,inputId);
			pstmt.setString(2, inputId);
			pstmt.setString(3, inputName);
			
			// 이상까지 온전한 sql 완성
			
			// 6. SQL(INSERT) 수행 후 결과(int) 반환 받기
			 // DML 수행 전에 해줘야 할 것
			 // Connection 의 AutoCommit 끄기
			 // - > 끄는 이유 : 개발자가 트랜잭션을 마음대로 제어하기 위해
			
			conn.setAutoCommit(false); 
			// DML 수행 전 해야 함
			
			// executeUdate() : DML 수행, 결과 행의 갯수(INT) 반환
			 // -> 보통 실패 시 0, 성공 시 0 초과된 값 반환
			
			int result = pstmt.executeUpdate(); 
			// sql 을 이미 실었기 때문에 다시 싣지 않는다.
			 // PreparedStatement 를 사용 시 결과를 반환하는 메소드에 sql 을 삽입하면 안 됨
			
			// 7. result 값에 따른 결과 처리 + 트랜잭션 제어처리
			 // DML 시 트랜잭션 처리 중요!
			if (result > 0) { // result 가 INSERT 성공 시
				
				conn.commit(); // -> commit 실행 -> DB에 INSERT 데이터 영구 반영
				System.out.println(inputId + "님이 추가되었습니다.");
			
			} else { // 실패 시
				
				conn.rollback(); // -> 실패 시 rollback
				System.out.println("추가 실패");
				
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			
			} finally {
			
				try {
					
					if(pstmt != null) pstmt.close();
					if(conn != null) conn.close();
					if(sc != null) sc.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
			
		
	}

}
