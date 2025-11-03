package edu.kh.jdbc.model.dao;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplateH;
import edu.kh.jdbc.model.dto.Student;

public class StudentDAO {

	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public String findGender(Connection conn, String inputName) throws Exception{
		
		String gender = null;
		
		try {
		
			String sql = """
						 SELECT GENDER
						 FROM KH_STUDENT
						 WHERE STD_NAME = ?
						 """;
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inputName);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				gender = rs.getString("GENDER");
			}
			
		}finally {
			
			JDBCTemplateH.close(rs);
			JDBCTemplateH.close(pstmt);
		}
		
		return gender;
	}
	
	public List<String> selectStdMatched(Connection conn, String gender, int age, int score, String club, String major, String inputName) throws Exception{
		
		List<String> matchedStudentList = new ArrayList<String>();
		
		try {
		
			String sql = null;
		
			if(age == 1 && score == 1){
				sql = """
					  SELECT M.STD_NAME
					  FROM KH_STUDENT M
					  WHERE M.MAJOR = ?
					  AND M.CLUB = ?
					  AND M.SCORE <= (SELECT M1.SCORE FROM KH_STUDENT M1 WHERE M1.STD_NAME = ?)
					  AND M.STD_AGE <= (SELECT M1.STD_AGE FROM KH_STUDENT M1 WHERE M1.STD_NAME = ?)
					  AND M.GENDER != ?
					  """;
			}else if(age == 1 && score == 2){
				sql = """
					  SELECT M.STD_NAME
					  FROM KH_STUDENT M
					  WHERE M.MAJOR = ?
					  AND M.CLUB = ?
					  AND M.SCORE >= (SELECT M1.SCORE FROM KH_STUDENT M1 WHERE M1.STD_NAME = ?)
					  AND M.STD_AGE <= (SELECT M1.STD_AGE FROM KH_STUDENT M1 WHERE M1.STD_NAME = ?)
					  AND M.GENDER != ?
					  """;
			}else if(age == 2 && score == 1){
				sql = """
					  SELECT M.STD_NAME
					  FROM KH_STUDENT M
					  WHERE M.MAJOR = ?
					  AND M.CLUB = ?
					  AND M.SCORE <= (SELECT M1.SCORE FROM KH_STUDENT M1 WHERE M1.STD_NAME = ?)
					  AND M.STD_AGE >= (SELECT M1.STD_AGE FROM KH_STUDENT M1 WHERE M1.STD_NAME = ?)
					  AND M.GENDER != ?
					  """;
			}else if(age == 2 && score == 2){
				sql = """
					  SELECT M.STD_NAME
					  FROM KH_STUDENT M
					  WHERE M.MAJOR = ?
					  AND M.CLUB = ?
					  AND M.SCORE >= (SELECT M1.SCORE FROM KH_STUDENT M1 WHERE M1.STD_NAME = ?)
					  AND M.STD_AGE >= (SELECT M1.STD_AGE FROM KH_STUDENT M1 WHERE M1.STD_NAME = ?)
					  AND M.GENDER != ?
					  """;
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, major);
			pstmt.setString(2, club);
			pstmt.setString(3, inputName);
			pstmt.setString(4, inputName);
			pstmt.setString(5, gender);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String name = rs.getString("M.STD_NAME");
				matchedStudentList.add(name);
			}
			
		}finally {
			
			JDBCTemplateH.close(pstmt);
			JDBCTemplateH.close(rs);
		}
		
		return matchedStudentList;
	}

	public int insertStd(Connection conn, String inputName, int age, String major, int score, String gender, String club) throws Exception{
		
		int result = 0;
		
		try {
			
			String sql = """
						 INSERT INTO KH_STUDENT VALUES(SEQ_STD_NO.NEXTVAL, ?, ?, ?, ?, ?, ?, SYSDATE)
						 """;
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, inputName);
			pstmt.setInt(2, age);
			pstmt.setString(3, major);
			pstmt.setInt(4, score);
			pstmt.setString(5, gender);
			pstmt.setString(6, club);
			
			result = pstmt.executeUpdate();
			
		} finally {
			
			JDBCTemplateH.close(pstmt);
			
		}
		
		return result;
	}

	public List<Student> selectStd(Connection conn) throws Exception{
		
		List<Student> selectStd = new ArrayList<Student>();
		
		try {
			
			String sql = """
						 SELECT STD_NO, STD_NAME, STD_AGE, MAJOR, SCORE, GENDER, CLUB, TO_CHAR(ENT_DATE, 'YYYY"년" MM"월" DD"일"') AS "ENT_DATE"
						 FROM KH_STUDENT
						 """;
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				int no = rs.getInt("STD_NO");
				String name = rs.getString("STD_NAME");
				int age = rs.getInt("STD_AGE");
				String major = rs.getString("MAJOR");
				int score = rs.getInt("SCORE");
				String gender = rs.getString("GENDER");
				String club = rs.getString("CLUB");
				String date = rs.getString("ENT_DATE");
				
				selectStd.add(new Student(no, name, age, major, score, gender, club, date));
			}
			
		} finally {
			
			JDBCTemplateH.close(rs);
			JDBCTemplateH.close(stmt);
			
		}
		
		return selectStd;
	}

}
