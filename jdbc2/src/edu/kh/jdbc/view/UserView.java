package edu.kh.jdbc.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.model.dto.User;
import edu.kh.jdbc.model.service.UserService;

// VIEW : 사용자와 직접 상호작용하는 화면(UI) 담당,
// 사용자에게 입력받아 결과를 출력하는 역할

public class UserView {
	
	 // 3. VIEW 에서 UserService 객체 생성
	  // UserService 의 기능을 불러와야 함
	  // 필드 내에 생성한다 -> private/public 등 설정 필요
	 // Service 객체 생성
	private UserService service = new UserService();
	
	// 3-1. 사용자가 입력을 해야 하기 때문에 VIEW 내 Scanner 객체 생성
	private Scanner sc = new Scanner(System.in);

	// 2-2. run 에서 호출당할 메소드 (test), 9.부터 주석처리
	// public void test() {}

	// 9. User 에 대한 CRUD 를 모두 수행할 수 있는 프로그램 만들어보기 
	 
	 // 코드 작성에 앞서,
	  // 조회, 삽입, 수정, 삭제 를 모두 수행을 위한 기능을 8개 만든다고 하면
	  // java - DB 간 소통 횟수가 총 8번이란 뜻
	
	  // 하지만 해당 소통 횟수마다 CONEECTION 을 만드는 것은 비효율적
	  // 따라서 JDBC 에서 계속 작성했었던 중복되는 코드를 하나의 클래스로 만들어 재사용 할 계획
	  // -> COMMON 에 TEMPLATE 클래스 생성
	
	
	// 10. 메인 메뉴 메소드 생성
	public void mainMenu() {
		
		int input = 0; // 메뉴 선택용 변수
		
		do {
			
			try {
				
				System.out.println("\n===== User 관리 프로그램 =====\n");
				System.out.println("1. User 등록(INSERT)");
				System.out.println("2. User 전체 조회(SELECT)");
				System.out.println("3. User 중 이름에 검색어가 포함된 회원 조회 (SELECT)");
				System.out.println("4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)");
				System.out.println("5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)");
				System.out.println("6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)");
				System.out.println("7. User 등록(아이디 중복 검사)");
				System.out.println("8. 여러 User 등록하기");
				System.out.println("0. 프로그램 종료");
				System.out.print("메뉴 선택 : ");
				
				input = sc.nextInt();
				sc.nextLine(); // 버퍼에 남은 개행문자 제거
				
				switch (input) {
					case 1: insertUser(); break;
					case 2: selectAll(); break;
					case 3: selectName(); break;
					case 4: selectUser(); break;
					case 5: deleteUser(); break;
					/*case 6: updateName(); break;
					case 7: insertUser2(); break;
					case 8: multiInsertUser(); break;*/
					case 0: System.out.println("\n[프로그램 종료]\n"); break;
					default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
				}
				System.out.println("\n-------------------------------------\n");
				
			} catch (InputMismatchException e) { // int 형 입력값 제외한 예외 발생만을 잡기 위한 catch
				System.out.println("******* 잘못 입력하셨습니다. ********");
				input = -1; // input 이 0 인 상태이기에 while 문 다시 수행하기 위한 input 값 설정
				sc.nextLine(); // 입력 버퍼에 남아 있는 잘못된 문자 제거
				
			} catch (Exception e) { // 다른 발생 예외 전부 해당 catch 문에서 처리
				e.printStackTrace();				
			}
			
		} while (input != 0);
		
		
	}

	/**
	 * 10.A.
	 * USER 등록 관련된 View(INSERT)
	 */
	private void insertUser() throws Exception/*DAO로부터 전달된 예외처리*/ {
		
		System.out.println("\n====1. User 등록 ====\n");
		
		System.out.print("ID : ");
		String userId = sc.next();
		
		System.out.print("PW : ");
		String userPw = sc.next();
		
		System.out.print("Name : ");
		String userName = sc.next();
		
		// 10.A.1.
		 // 위 입력받은 3개의 값을 DAO 로 전달하여 처리해야 한다.
		 // 3개를 한번에 묶어 전달한다.
		 // -> User 객체를 이용한다.
		User user = new User();  // User 객체가 생성되었고, JVM 에 의해 필드값 기본생성자는 NULL 인 상태일 것.
		
		// 10.A.2.
		 // User 객체의 기본생성자는 5개가 있고 현재 입력할 수 있는 값은 3개이므로 Setter 를 이용한다.
		user.setUserId(userId);
		user.setUserPw(userPw);
		user.setUserName(userName);
		// 이제 user 객체 내에 위에서 입력한 user 정보가 들어간 상태.
		
		// 10.A.3.
		 // 위 정보로 INSERT를 해야 하므로 서비스 호출하여 결과를 반환 받기
		 // DML 은 행의 갯수(int) 를 반환한다.
		// 3. 에서, 필드에 작성한 User Service 객체를 이용해 UserService 내 insertUser 라는 메소드를 호출하고 user 객체 전달 
		int result = service.insertUser(user); 
		
		// 10.A.10. - 반환 결과에 따라 출력 내용 선택
		
		if(result > 0) // 
			System.out.println("\n" + userId + "사용자가 등록되었습니다.\n");
		else { // 실패
			System.out.println("\n사용자 등록 실패!\n");
			
		}
		
		JDBCTemplate.getConnection();

	}
	
	/**
	 * 10.B.
	 * USER 전체 조회 관련 View(SELECT)
	 * 
	 */
	private void selectAll() throws Exception/*DAO로부터 전달된 예외처리*/{
		
		System.out.println("\n====2. User 전체 조회 ====\n");
		
		// 10.B.1. - 서비스 호출(SELECT) 후 결과 반환 받기
		 // 결과는 ResultSet
		 // DB 에서 Java 로 한 행씩 뽑아와 User 객체로 만들 예정
		 // User 클래스를 이용해 DB 에 있는 모든 객체를 한번에 담아야 함 -> List (컬렉션) 이용
		 // 따라서 결과값은 List 로 반환 받아야 함
		
		List<User> userList = service.selectAll();
		// 10.B.1.1. 전달인자 없다
		
		// 10.B.6.10. 조회결과가 없을 경우
		if(userList.isEmpty()) { 
			
			System.out.println("\n****조회 결과가 없습니다.****\n");
			return;
			
		}else { 
			
		// 10.B.6.11. 조회결과가 있을 경우 - userList 에 있는 모든 User 객체 출력
			for(int i = 0; i < userList.size(); i++) {
				System.out.println(userList.get(i));
				
				
			}
				
		}
	}
	
	// 10.C. 여기서부터 강의 안 보고 자체 작성
	private void selectName() throws Exception{
		
		System.out.println("\n====3. 검색된 User 조회(이름 포함) ====\n");
		System.out.print("검색할 키워드 입력(이름) : ");
		String keyword = sc.next();
		
		List<User> searchedUserList = service.selectName(keyword);
		
		if(searchedUserList.isEmpty()) {
			System.out.println("\n****조회 결과가 없습니다.****\n");
		}else {
			for(User result : searchedUserList) {
				System.out.println(result);
			}
		}
	}
	
	private void selectUser() throws Exception {
		
		System.out.println("\n====4. 검색된 User 조회(USER_NO 검색) ====\n");
		System.out.print("검색할 번호 입력 : ");
		int no = sc.nextInt();
		
		List<User> noUserList = service.selectUser(no);
		
		if(noUserList.isEmpty()) {
			System.out.println("\n****조회 결과가 없습니다.****\n");
		}else {
			for(User result : noUserList) {
				System.out.println(result);
			}
		}
		
	}
	
	private void deleteUser() throws Exception {
		
		System.out.println("\n====4. 검색된 User 조회(USER_NO 검색) ====\n");
		System.out.print("삭제할 번호 입력 : ");
		int no = sc.nextInt();
		
		String delName = service.deleteUser(no);
		
		if(delName == null) {
			System.out.println("입력한 번호에 해당하는 USER 정보가 없습니다.");
			return;
		}
		
		System.out.println("\n" + delName + "의 정보를 삭제하시겠습니까?(y/n)");
		String yn = sc.next().toUpperCase();
		
		if (yn.equals("N")) {return;}
	
		int delResult = service.deleteUser2(no);
			
		if (delResult > 0 ) System.out.println("삭제 완료");
		else {
			System.out.println("삭제 실패");
			return;
		}
		
	}
}
