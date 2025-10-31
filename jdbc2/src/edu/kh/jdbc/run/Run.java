package edu.kh.jdbc.run;

import edu.kh.jdbc.view.UserView;

public class Run {
	
	public static void main(String[] args) {
		
		// 2. 실행 클래스인 Run 에서 View 를 호출하기 위해 객체를 먼저 생성한다.
		UserView view = new UserView(); 
		
		// 2-1. view 를 호출하기 위한 메소드, 9. 부터 주석처리
		// view.test(); 
		
		view.mainMenu();
		
	}

}
