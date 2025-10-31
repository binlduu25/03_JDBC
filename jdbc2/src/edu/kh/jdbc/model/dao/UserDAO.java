package edu.kh.jdbc.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// 10.A.6.7.1. 
 // -> JDBCTemplate 을 매번 불러와야 하는 수고로움을 덜기 위해 위와 같이 import 구문 수정
 // import (+static) edu.kh.jdbc.common.JDBCTemplate(+.*);
 // 설명 : JDBCTemplate 내 static 으로 선언된 필드 또는 메소드 모두를 이름을 호출하지 않고 필드명만 불러도 여기서 가져온 것으로 간주
 // 단점 : 파악하기 힘들다
import edu.kh.jdbc.model.dto.User;

// 모델(model) 중 하나 DAO : DATA ACCESS OBJECT (데이터 접근 객체)
// 데이터가 저장된 곳(DB) 에 접근하는 용도의 객체
 // -> DB에 접근하여 자바에서 원하는 결과를 얻기 위해 SQL 을 수행하고 반환받는 역할
 // EX. STATEMENT, RESULT SET.. 등등

public class UserDAO {

	// 5. DAO 내에는 DB 와 연결될 참조변수들을 설정해야 함
	 // 일단은 CONNECTION 을 제외한 참조변수들 선언
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	// 10.A.6. User 등록 DAO
	 // 10.A.3. (UserView > UserService) 에서 처럼, (UserService > UserDAO) 로 insertUser 메소드를 호출
	 // conn 과 user 객체 2개 전달
	 // 해당 메소드에서 DB 에 직접 INSERT 예정
	/**
	 * @param conn - DB 연결 정보가 담긴 Connection 객체
	 * @param user - 사용자가 입력한 User 정보(id,pw,name)가 세팅된 User 객체
	 * @return - INSERT 결과 행의 갯수
	 */
	public int insertUser(Connection conn, User user) throws Exception/*DAO로부터 전달된 예외처리*/ {
		
		// 10.A.6.1. 결과 저장용 변수 선언
		int result = 0;
		
		// 10.A.6.2. 예외처리 위한 try - catch
		 // 메소드 선언부에 throws Exception 통해 이 메소드를 호출한 곳(UserService)으로 던져야(throws) 함
		 // 즉, 여기서 발생한 예외는 UserService -> Userview 내 insertUser 메소드 -> mainMenu 로 도달할 것
		try {
			
			// 10.A.6.3. SQL 작성
			String sql = """
						 INSERT INTO TB_USER
						 VALUES (SEQ_USER_NO.NEXTVAL, ?, ?, ?, SYSDATE)	
						 """;
			
			// 10.A.6.4. PreparedStatement 이용한 객체 생성
			 // * PreparedStatement 는 Connection 이 만든다.
			pstmt = conn.prepareStatement(sql);
			
			// 10.A.6.5. 위치홀더(?) 에 알맞은 값 대입
			pstmt.setString(1, user.getUserId() );
			pstmt.setString(2, user.getUserPw() );
			pstmt.setString(3, user.getUserName() );
			
			// 10.A.6.6. 수행 후 결과 반환 받기
			 // 대입한 값 버스에 태워 DB 로 전송, 수행, 반환
			result = pstmt.executeUpdate();
			
		} finally {
			
			// 10.A.6.7.
			 // JDBCTemplate 내 close 를 이용해 반환
			 // 전달인자는 pstmt (해당 메소드에서 pstmt 만 사용)
			 // JDBCTemplate.close(pstmt); <- 원래는 이렇게 써야 함 10.A.6.7.1. 으로 이동(상단)
			 close(pstmt);
			
			 // 여기서 conn 을 닫지 않는 이유는 UserService 에서 트랜잭션 수행해주어야 함
		}
		
		return result; // 결과 저장용 변수에 저장된 최종 값 반환
	}
	
	// 10.B.6. User 전체 조회 DAO
	/** 
	 * @param - conn
	 * @return - List<User> userList
	 */
	public List<User> selectAll(Connection conn) throws Exception{
		
		// 10.B.6.1. 결과를 담을 ArrayList 빈 컬렉션 생성 
		List<User> userList = new ArrayList<User>();
		
		// 10.B.6.2. try - catch
		try {
			
			// 10.B.6.3. SQL 작성
			String sql = """
						 SELECT USER_NO, USER_ID, USER_PW, USER_NAME, TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') "ENROLL_DATE"
						 FROM TB_USER
						 ORDER BY USER_NO ASC
						 """;
			
			// 10.B.6.4. PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
			
			// 10.B.6.5. 위치홀더 값 없으니 패스
			
			// 10.B.6.6. SQL 수행 후 결과 반환
			rs = pstmt.executeQuery();
			
			// 10.B.6.7. ResultSet 에 저장된 값을 User 객체로 저장
			
			while(rs.next()) { // rs.next() 를 통해 1행씩 뽑아옴
				
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE"); 
				// 10.B.6.7.1. 위에서 TNROLL_DATE 를 TO_CHAR 를 통해 String 으로 저장함
				
				// 10.B.6.7.2. User 객체 새로 생성하여 DB 에서 얻어온 각각의 컬럼값을 필드로 세팅
				User user = new User(userNo, userId, userPw, userName, enrollDate);
				 // -> while 이 한번 돌 때마다 한 행의 정보가 User 객체에 저장됨
				
				// 10.B.6.7.3. 각각의 User 객체를 ArrayList 에 담기
				userList.add(user);
				}
			
		} finally { // 10.B.6.8. 사용한 자원 반환
			
			close(rs);
			close(pstmt);
		}
		
		// 10.B.6.9. 조회 결과 담긴 List 반환
		return userList;
	}		
	
	public List<User> selectName(Connection conn, String keyword) throws Exception {

		List<User> searchedUserList = new ArrayList<User>();
		try {
			String sql = """
						 SELECT USER_NO, USER_ID, USER_PW, USER_NAME, TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') "ENROLL_DATE"
						 FROM TB_USER
						 WHERE USER_NAME LIKE '%' || ? || '%'
						 ORDER BY USER_NO ASC
						 """;
			// 문자열 위치홀더 사용법 위 코드 참고
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, keyword);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("User_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");

				searchedUserList.add(new User(userNo, userId, userPw, userName, enrollDate));

			}
		} finally {
				close(rs);
				close(pstmt);
		}

		return searchedUserList;
	}
	
	public List<User> selectUser(Connection conn, int no) throws Exception {

		List<User> noUserList = new ArrayList<User>();

		try {

			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME, TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') AS "ENROLL_DATE"
					FROM TB_USER
					WHERE USER_NO = ?
					""";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, no);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("User_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");

				noUserList.add(new User(userNo, userId, userPw, userName, enrollDate));

			}

		} finally {

			close(rs);
			close(conn);

		}

		return noUserList;
	}
	
	public String deleteUser(Connection conn, int no) throws Exception { // 삭제 확인용
		
		String delName = null;
		
		try {
			
			String sql = """
					SELECT USER_NO, USER_NAME
					FROM TB_USER 
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, no);  
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				delName = rs.getString("USER_NAME");	
			}
			
		} finally {
			
			close(rs);
			close(pstmt);
			
		}
		
		return delName;
	}
	
	public int deleteUser2(Connection conn, int no) throws Exception { // 삭제

		int delResult = -1;

		try {

			String sql = """
					DELETE FROM TB_USER WHERE USER_NO = ?
					""";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			delResult = pstmt.executeUpdate();

		} finally {

			close(pstmt);

		}

		return delResult;

	}
}
