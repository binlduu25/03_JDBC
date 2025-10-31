package jdbc1.edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JDBCExample7 {
	
	public static void main(String[] args) {
		
		// EMPLOYEE 테이블에서 사번, 이름, 성별, 급여, 직급명, 부서명을 조회
		 // 단, 입력 받은 조건에 맞는 결과만 조회하고 정렬할 것

			// - 조건 1 : 성별 (M, F)
			// - 조건 2 : 급여 범위
			// - 조건 3 : 급여 오름차순/내림차순
	
			// [실행화면]
			// 조회할 성별(M/F) : F
			// 급여 범위(최소, 최대 순서로 작성) :
			// 3000000
			// 4000000
			// 급여 정렬(1.ASC, 2.DESC) : 2
	
			// 사번 | 이름 | 성별 | 급여 | 직급명 | 부서명
			// --------------------------------------------------------
			// 217 | 전지연 | F | 3660000 | 대리 | 인사관리부
	
			// -------------------------------------------------------
	
			// 사번 | 이름 | 성별 | 급여 | 직급명 | 부서명
			// --------------------------------------------------------
			// 218 | 이오리 | F | 3890000 | 사원 | 없음
			// 203 | 송은희 | F | 3800000 | 차장 | 해외영업2부
			// 212 | 장쯔위 | F | 3550000 | 대리 | 기술지원부
			// 222 | 이태림 | F | 3436240 | 대리 | 기술지원부
			// 207 | 하이유 | F | 3200000 | 과장 | 해외영업1부
			// 210 | 윤은해 | F | 3000000 | 사원 | 해외영업1부
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Scanner sc = null;
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String userName = "c##sjh";
			String userPass = "sjh1234";
			
			conn = DriverManager.getConnection(url, userName, userPass);
			
			conn.setAutoCommit(false);
			
			sc = new Scanner(System.in);
			
			System.out.print("조회할 성별(M/F) : ");
			String gender = sc.next().toUpperCase();
			
			System.out.print("최소 급여 : ");
			int minSal = sc.nextInt();
			
			System.out.print("최대 급여 : ");
			int maxSal = sc.nextInt();
			
			System.out.print("오름/내림차순(1.ASC / 2.DESC) : ");
			int sort = sc.nextInt();
			
			String sql = 
					"""
					SELECT EMP_ID "사번", EMP_NAME "이름", DECODE(SUBSTR(EMP_NO, 8, 1), 1,'M', 2, 'F') "성별", SALARY "급여", JOB_NAME "직급", DEPT_TITLE "부서"
					FROM EMPLOYEE
					JOIN JOB USING (JOB_CODE)
					LEFT OUTER JOIN DEPARTMENT ON (DEPT_CODE = DEPT_ID)
					WHERE (SALARY BETWEEN ? AND ?)
					AND DECODE(SUBSTR(EMP_NO, 8, 1), 1, 'M', 2, 'F') = ?
					ORDER BY SALARY				
					""";
			
			// ORDER BY 절에 ? (위치홀더) 사용 시 오류 : SQL 명령어가 올바르게 종료되지 않았습니다.
			 // 왜? 
			 // PreparedStatement의 위치홀더(?)는 ** 데이터 값(리터럴) ** 을 대체하는 용도로만 사용가능.
			 // -> SQL에서 ORDER BY 절의 정렬 기준 (ASC/DESC)과 같은
			 // -> SQL 구문(문법)의 일부는 PreparedStatement의 위치 홀더(?)로 대체될 수 없음.
			
			// 급여의 오름차순인지 내림차순인지 조건에 따라 SQL 보완하기
			
			if(sort == 1) 
				sql += " ASC";
			else
				sql += " DESC";
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, minSal);
			pstmt.setInt(2, maxSal);
			pstmt.setString(3, gender);
			
			rs = pstmt.executeQuery();
		
			System.out.println("사번 | 이름   | 성별 | 급여    | 직급명 | 부서명");
			System.out.println("-----------------------------------------------");	
			
			boolean flag = true; // 조회 결과가 없을 수도 있으므로 flag 변수 생성
			
			while(rs.next()) { 
				
				flag = false;
				// rs.next() 가 1행씩 훑기 때문에 아무 행도 없을 경우 false 값으로 그대로 있게 되고
				// -> rs.next() 가 false 값인 이상 해당 while 문 자체가 수행되지 않기 때문에 flag 는 그대로 true 인 상태로 남아 있다.
				
				String empId = rs.getString("사번");
				String empName = rs.getString("이름");
				String ge = rs.getString("성별");
				int sal = rs.getInt("급여");
				String job = rs.getString("직급");
				String dept = rs.getString("부서");
				
				System.out.printf("%-4s | %3s | %-4s | %7d | %-3s  | %s \n", empId, empName, ge, sal, job, dept);
				
			}
			
			if (flag) System.out.println("조회 결과 없음");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			
			try {
				
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				if(sc != null) sc.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		
	}

}
