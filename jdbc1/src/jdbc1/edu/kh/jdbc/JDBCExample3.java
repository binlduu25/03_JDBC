package jdbc1.edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample3 {
	
	public static void main(String[] args) {
		
		// 입력받은 최소 급여 이상, 최대 급여 이하를 받는 사원의 사번, 이름, 급여를 급여 내림차순으로 이클립스 콘솔 출력
		
		// [실행화면]
		// 최소 급여 : 1000000
		// 최대 급여 : 3000000
		// 사번 / 이름 / 급여
		// 사번 / 이름 / 급여
		// 사번 / 이름 / 급여
		// ....
		
		// 1. 참조변수 선언
		Connection conn = null; // 연결정보 담는 객체
		Statement stmt = null; // 정보 전송할 객체
		ResultSet rs = null; // 결과 담는 객체
		
		Scanner sc = null; // 입력 받을 예정이기에 스캐너 초기화
		
		try { // try - catch 로 예외 방지(필수)
			
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 드라이버 메모리에 탑재
			
			String dbInfo = "jdbc:oracle:thin:@localhost:1521:XE"; // DB 접속 정보 작성 
			String userName ="c##sjh"; 
			String password = "sjh1234"; 
			
			conn = DriverManager.getConnection(dbInfo, userName, password); // 작성된 접속 정보를 DB 에 접속하기 위한 객체 생성
			
			System.out.println(conn); // 연결되었는지 확인
			
			sc = new Scanner(System.in); // 입력정보 받기
			System.out.print("최소 급여 : ");
			int minSal = sc.nextInt();
			System.out.print("최대 급여 : ");
			int maxSal = sc.nextInt();
			
			String sql = "SELECT EMP_ID, EMP_NAME, SALARY FROM EMPLOYEE WHERE SALARY >= " + minSal + " AND SALARY <= " + maxSal + " ORDER BY SALARY DESC"; 
			// DB 에서 명령을 실행할 sql문 작성 후 String 에 담기
			
			// JAVA 13 이상부터 지원하는 TEXT BLOCK(""") 문법
			//			String sql = """
			//					SELECT EMP_ID, EMP_NAME, SALARY
			//					FROM EMPLOYEE
			//					WHERE SALARY BETWEEN
			//					""" + min + " AND " + max + " ORDER BY SALARY DESC";
			
			stmt = conn.createStatement(); // 만들어진 객체(conn) 을 stmt 라는 버스에 실어 보내고 반환 받을 버스 생성 (전송)
			
			rs = stmt.executeQuery(sql); // sql 정보를 실행한 결과를 rs 에 담아 stmt 에 실어서 반환 받기
			
			while(rs.next()) { // 실어온 정보를 출력하기 위해 반복문 사용, 1행씩 수행
				
				String empId = rs.getString("EMP_ID"); // 1행씩 돌면서 각 컬럼의 정보를 개별 변수에 담음
				String empName = rs.getString("EMP_NAME");
				int salary = rs.getInt("SALARY");
				
				System.out.printf("사번 : %s / 이름 : %s / 급여 : %d \n", empId, empName, salary); // 1행을 돌 때마다 해당 내용 출력
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				
				if(rs != null) rs.close(); // 객체 모두 닫아주기 연 순서의 역순으로
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
				if(sc != null) sc.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
		
		
	}

}
