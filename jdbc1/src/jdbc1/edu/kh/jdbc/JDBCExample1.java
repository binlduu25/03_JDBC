package jdbc1.edu.kh.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExample1 { // JAVA 코드를 이용해 EMPLOYEE 테이블에서 사번, 이름, 부서코드, 직급코드, 급여, 입사일 조회 후 이클립스 콘솔에 출력
	
	// JDBC (Java DataBase Connectivity)
	 
	 // - Java 에서 DB에 연결 할 수 있게 해주는 Java API (Java 에서 제공하는 코드)
	  // -> java.sql 패키지에 존재함 	
		
	public static void main(String[] args) {

	// 1. JDBC 객체 참조용 변수 선언
	
		// java.sql.Connection 
		 // 특정 DBMS 와 연결하기 위한 정보를 저장하는 객체 (인터페이스, DB 종류와 상관없이 연결하기 위해서)
		 // = DBeaver 에서 사용하는 DB 연결과 같은 역할의 객체
		 // DB 서버 주소, 포트번호, DB 이름, 계정명, 비밀번호

		Connection conn = null;
		
		// java.sql.Statement - 스쿨버스에 비유
		 // 1) 자바에서 만든 SQL 을 DB 에 전달 (JAVA -> DB)
		 // 2) DB 에서 SQL 을 수행한 결과를 반환 받아옴 (DB -> JAVA)
		 // Statement 이라는 버스에 SELECT ~ 등의 구문을 담아 DB 에 전달하고
		 // 해당 구문을 수행한 RESULT SET 을 다시 Statement 에 실어 JAVA 로 옴
		
		Statement stmt = null;
		
		// java.sql.ResultSet
		 // - SELECT 조회 후 결과(RESULT SET)을 저장하는 객체
		
		ResultSet rs = null;
		
		try {
		
		// 2. DriverManager 객체 이용해 Connection 객체 생성
	 
		// java.sql.DriverManager
		 // - DB 연결 정보와 JDBC 드라이버를 이용해서
		 // 원하는 DB 와 연결할 수 있는 Connection 객체를 생성하는 객체
		
			// 2-1. Oracle JDBC Driver 객체를 메모리에 로드해놓기
			 // checked Exception 발생시키기에 try-catch 로 처리
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// Class.forName("패키지명 + 클래스명");
			 // -> 해당 클래스를 읽어 메모리에 적재
			 // 즉, JVM 이 프로그램 동작에 사용할 객체를 생성하는 구문
			
			// oracle.jdbc.driver.OracleDriver 
			 // -> Oracle DBMS 연결 시 필요한 코드가 담겨 있는 클래스
			 // ojdbc 라이브러리 파일 내 존재
			
			// 2-2. DB 연결 정보 작성
			String type = "jdbc:oracle:thin:@"; // 드라이버 종류
			String host = "localhost"; // DB 서버 컴퓨터의 IP 또는 도메인 주소 -> 현재 컴퓨터 : localhost
			String port = ":1521"; // DB 서버에 연결하기 위한 PORT 번호 
			String dbName = ":XE"; // DBMS 이름 (eXpress Edition)
			String userName ="c##sjh"; // 사용자 계정명	
			String password = "sjh1234"; // 사용자 계정 비밀번호
			
			// 2-3. DB 연결 정보와 DriverManager 이용해서 Connection 객체 생성
			conn = DriverManager.getConnection( type + host + port + dbName, userName, password );
			
			// 2-4. Connection 객체 잘 생성되었는지 확인
			System.out.println(conn);
			
		// 3. SQL 작성
		 // 주의사항 !
		 // -> JDBC 코드에서 SQL 작성 시 세미콜론(;) 작성하면 안 됨
		 // -> " SQL 명령어가 올바르게 종료되지 않았다"는 오류 발생
			
			// EMPLOYEE 테이블에서 사번, 이름, 부서코드, 직급코드, 급여, 입사일 조회
			String sql = "SELECT EMP_ID, EMP_NAME, DEPT_CODE, JOB_CODE, SALARY, HIRE_DATE FROM EMPLOYEE";
			
		// 4. Statement 객체 생성
		 // 자바에서 만들어진 객체를 버스를 태워 DB 로 보내야 함
			
			// 연결된 DB 에 SQL 을 전달하고 결과를 반환 받을 역할을 stmt 라는 참조변수에 담는다
			stmt = conn.createStatement();
				
		// 5. Statement 객체 이용해 SQL 수행 후 결과를 반환 받기
			
			// 1) Statement.executeQuery(sql)
				// -> 전달한 sql 이 SELECT 문일 때 결과로 ResultSet 을 객체로 반환받아 돌아오는 역할
				// 반환형 : ResultSet
			
			// 2) Statement.executeUpdate(sql)
				// -> sql 이 DML(INSERT,UPDATE,DELETE)일 때 실행 메소드
				// -> 결과로 int 반환 (삽입, 수정, 삭제된 행의 갯수)
				// 반환형 : int
				 // DML 이 잘 수행되었을 땐 1 이상의 값
			 	 // DML 이 수행되지 않았을 때 0 값 반환
			
			rs = stmt.executeQuery(sql);
			// 수행한 sql 결과를 rs 에 저장한다.
			
		// 6. 조회 결과가 담긴 rs 를 커서(cursor)를 이용하여 '1행 씩' 접근하여 각 행에 작성된 컬럼을 얻어온다.
			
			// ResultSet.next() 
			 // 반환형 : boolean
			 // 이동된 행에 값이 있으면 true, 없으면 false 반환
			 // 맨 처음 호출 시 1행부터 시작
			
			while(rs.next()) {
				
				// while 문 안에서 각 컬럼값을 뽑아오는 역할을 수행해야 한다.
				 // ResultSet.get자료형(컬럼명 | 순서)
				 // -> 지정된 자료형 형태로 값이 반환됨
				 // 자료형을 잘못 지정 시 Exception 발생
				
				// [java]					[DB]
				// String					CHAR, VARCHAR2, NVARCHAR2
				// int, long				NUMBER (정수만 저장된 컬럼)
				// float, double			NUMBER (정수 + 실수)
				// java.sql.Date			DATE
				
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String deptCode = rs.getString("DEPT_CODE");
				String jobCode = rs.getString("JOB_CODE");
				int salary = rs.getInt("SALARY");
				Date hireDate = rs.getDate("HIRE_DATE"); // DATE 타입 IMPORT 필요 
				
				System.out.printf("사번 : %s / 이름 : %s / 부서코드 : %s / 직급코드 : %s / 급여 : %d / 입사일 : %s \n"
								  ,empId, empName, deptCode, jobCode, salary, hireDate.toString() );
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println("해당 클래스를 찾을 수 없음");
			e.printStackTrace();
			
		} catch (SQLException e) {
			// SQLException : DB 연결과 관련된 모든 예외의 최상위 부모
			e.printStackTrace();
			
		} finally {
			
			// 7. 사용 완료된 jdbc 객체 자원 반환 (close)
			 // 열렸던 통로 (Connection) 닫기
			 // 자원 반환 하지 않을 시 DB 와 연결된 Connection 이 그대로 남아 있어서
			 // 다른 클라이언트(ex. 자바 프로그램) 가 추가로 연결되지 못하는 상황 발생할 수 있음
			 // -> DBMS 는 최대 Connection 의 개수 제한이 있다
			
			try {
				
				// close 수행 시 만들어진 '역순'으로 close 하는 것을 권장
				 // rs -> stmt -> conn
				
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
