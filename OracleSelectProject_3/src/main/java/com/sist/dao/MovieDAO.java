package com.sist.dao;

import java.util.*;
import java.sql.*;

public class MovieDAO {

	// 연결 객체 선언 => Connection
	private Connection conn; // connection
	
	// SQL 문장 송수신
	private PreparedStatement ps; // read/write
	
	// 오라클 연결 => 오라클 주소 필요
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	// 드라이버 등록 => 생성자
	public MovieDAO()
	{
		try
		{
			// 클래스 메모리 할당(new, Class.forName) => Spring
			// 클래스명 등록 시 패키지부터 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {}
	}
	
	// 오라클 연결
	public void getConnection()
	{
		try
		{
			conn=DriverManager.getConnection(URL, "hr", "happy");
		}catch(Exception ex) {}
	}

	// 오라클 닫기
	public void disConnection()
	{
		try
		{
			if(ps!=null)
			{
				ps.close();
			}
			if(conn!=null)
			{
				conn.close();
			}
		}catch(Exception ex) {}
	}
	// DAO는 기본적으로 동일
	
	// 기능 설정
	// 검색 : 값이 여러 개 => ArrayList / 1개 => VO
	public ArrayList<MovieVO> movieFindDate(String column, String fd) // 해당 컬럼에서 값을 찾음
	{
		ArrayList<MovieVO> list = new ArrayList<MovieVO>();
		try
		{
			getConnection();
			String sql="SELECT title, genre, actor "
					+ "FROM movie "
					+ "WHERE "+column+" LIKE '%' || ? || '%'"; // 컬럼과 테이블은 따옴표를 붙이지 않음 => 물음표를 사용하지 않고 매개변수 사용
			ps=conn.prepareStatement(sql);
			ps.setString(1, fd);
			// 결과값 받기 => 물음표가 있는데 값을 설정하지 않는 경우 사용
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				// rs.next() => 1줄씩 읽어옴
				
				MovieVO vo=new MovieVO();
				vo.setTitle(rs.getString(1));
				vo.setGenre(rs.getString(2));
				vo.setActor(rs.getString(3));
				
				list.add(vo); // 데이터를 모아서 넘김
			}
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
		return list;
	}
	
	public int movieFindCount(String column, String fd)
	{
		int count=0;
		try
		{
			getConnection();
			String sql="SELECT COUNT(*) "
					+ "FROM movie "
					+ "WHERE "+column+" LIKE '%' ||?|| '%'";
			ps=conn.prepareStatement(sql);
			ps.setString(1, fd);
			ResultSet rs=ps.executeQuery();
			rs.next();
			count=rs.getInt(1);
			rs.close();
			// 0 : 검색 결과가 없는 상태
					
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
		return count;
	}
}
