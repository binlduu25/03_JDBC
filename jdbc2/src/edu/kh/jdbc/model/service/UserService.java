package edu.kh.jdbc.model.service;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.model.dao.UserDAO;
import edu.kh.jdbc.model.dto.User;

// Model 중 하나
// Service : 비즈니스 로직을 처리하는 계층
 // 데이터 가공 및 트랜잭션(commit, rollback) 관리 수행

public class UserService {
	
	// 4. service 에선 dao 를 호출해야 하기 때문에 필드 내 dao 객체 생성
	private UserDAO dao = new UserDAO();
	
	// 10.A.4. UserView 에서 insertUser 에서 자동완성
	 // int result = service.insertUser(user); 
	 // -> A.1. 에서 만들어진 user 는 User 타입이므로 매개인자에 자동으로 User 타입으로 작성됨
	 // 반환형이 int 인 이유 역시 result 가 int 형으로 선언되었기 때문에 자동으로 int 형으로 설정
	  // "호출한다" 는 말은 즉 어떤 기능을 수행하고 반환값을 가지고 되돌아감을 의미
	  // 즉 되돌아갈 자리가 int 이므로 int 를 가지고 돌아감
	 // 정리하면, User 라는 객체의 매개인자를 가져와 여기서 기능을 수행하고 결과값을 int 형으로 되돌려줄 것
	/**
	 * @param user
	    - param : parameter(매개변수)
	    - user : 10.A.1. 에서 입력받은 userId, userPw, userName 이 세팅된 객체
	 * @return : insert 된 결과 행의 갯수
	 */
	public int insertUser(User user) throws Exception{
		
		// 10.A.4.1. - Connection 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 10.A.4.2. - 데이터 가공
		 // 일반적으로 Service 에서 전달받은 데이터를 가공(암호화, 문자열 추가 등)이 필요하나 현재 단계에서는 생략
		
		// 10.A.4.3. - DAO 메소드 호출 후 결과 반환 받기
		 // Connection 과 User 둘 다 전달해야 한다!
		 int result = dao.insertUser(conn, user);
		
		// 10.A.7. - DML 수행 결과에 따른 트랜잭션 제어 처리
		 // 10.A.4.3. 의 메소드 수행 후 트랜잭션 반드시 실시 (DML이므로)
		 if(result > 0) { // insert 성공 - commit
			 
			 JDBCTemplate.commit(conn);
			 
		 } else { // insert 실패 - rollback
			 
			 JDBCTemplate.rollback(conn);
			 
		 }
		 
		 // 10.A.8. Connection 반환
		 	
		 JDBCTemplate.close(conn);
		
		 // 10.A.9. UserView 로 결과 반환
		return result;
	}

		
	/** 1O.B.2. User 전체 조회 서비스
	 * @return : 조회된 User 들이 담긴 List
	 */
	public List<User> selectAll() throws Exception{
		
		// 10.B.3. Connection 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 10.B.4. 데이터 가공 없음
		
		// 10.B.5. DAO 호출 후 결과 반환 받기 (List<User>)
		List<User> userList = dao.selectAll(conn);
		
		// 10.B.7. 트랜잭션 필요 없음
		
		// 10.B.8. Connection 반환
		JDBCTemplate.close(conn);
		
		// 10.B.9. DAO에서 들고 온 결과값 반환	
		return userList;
	}

	/**
	 * @param keyword
	 * @return - searchedUserList
	 * @throws Exception
	 */
	public List<User> selectName(String keyword) throws Exception{ 
		
		Connection conn = JDBCTemplate.getConnection();
		
		List<User> searchedUserList = dao.selectName(conn, keyword);
		
		JDBCTemplate.close(conn);
	
		return searchedUserList;
	}
	
	
	public List<User> selectUser(int no) throws Exception{
		
		Connection conn = JDBCTemplate.getConnection();
		
		List<User> noUserList = dao.selectUser(conn, no);
		
		JDBCTemplate.close(conn);
		
		return noUserList;
	}
	
	public String deleteUser(int no) throws Exception{ // 삭제 확인용
	
		Connection conn = JDBCTemplate.getConnection();
		
		String delName = dao.deleteUser(conn, no);
		
		JDBCTemplate.close(conn);
		
	return delName;
		
	}
	
	public int deleteUser2(int no) throws Exception{ // 삭제 메소드
		
		Connection conn = JDBCTemplate.getConnection();
		
		int delResult = dao.deleteUser2(conn, no);
		
		if(delResult > 0) JDBCTemplate.commit(conn);
		else { JDBCTemplate.rollback(conn);}
		
		JDBCTemplate.close(conn);
		
		return delResult;
	}
	
	
	public List<User> updateInputCheck(String userId, String userPw) throws Exception{ // 정보수정 회원 확인용
		
		Connection conn = JDBCTemplate.getConnection();
		
		List<User> updateInputCheck = dao.updateInputCheck(conn, userId, userPw);
		
		JDBCTemplate.close(conn);
		
		return updateInputCheck;
		
	}
	
	public int updateInfo(String inputName, String inputId, String inputPw) throws Exception { // 정보수정 회원 실행용

		Connection conn = JDBCTemplate.getConnection();

		int updateInfo = dao.updateInfo(conn, inputName, inputId, inputPw);

		if (updateInfo > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}

		JDBCTemplate.close(conn);

		return updateInfo;

	}
	

}
