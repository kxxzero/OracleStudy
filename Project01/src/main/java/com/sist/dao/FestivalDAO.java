package com.sist.dao;

import java.util.*;
import java.sql.*;
public class FestivalDAO {

	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@211.238.142.113:1521:XE";
	// 에러 => output => this.conn NULL
	
	public FestivalDAO()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {}
	}
	
	public void getConnection()
	{
		try
		{
			conn=DriverManager.getConnection(URL, "hr", "happy");
		}catch(Exception ex) {}
	}
	
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
	
	
	
	public void festivalInsert(ArrayList<FestivalVO> voList)
	{
		try
		{
			getConnection();
			String sql="INSERT INTO festival VALUES("
					+ /* "festival_fno_seq.nextval, */ "?,?,?,?,?,0,?,?,?,?,?,0,0)";
			ps=conn.prepareStatement(sql);
			
			for(FestivalVO vo:voList) {
				ps.setInt(1, vo.getFno());
				ps.setString(2, vo.getTitle());
				ps.setString(3, vo.getPoster());
				ps.setString(4, vo.getDeimage());
				ps.setString(5, vo.getCont());
				ps.setString(6, vo.getAddr());
				ps.setString(7, vo.getRate());
				ps.setString(8, vo.getPhone());
				ps.setString(9, vo.getBhour());
				ps.setString(10, vo.getTag());
				
				ps.executeUpdate();
			}
						
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
	}
}


