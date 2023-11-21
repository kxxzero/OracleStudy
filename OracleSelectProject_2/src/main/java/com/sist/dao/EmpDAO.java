package com.sist.dao;

// VO = table(column)개수만큼 생성, DAO = 1개만 생성 가능

/*
 *  조인 : 2개 이상의 테이블을 연결해서 하나의 테이블처럼 필요한 데이터를 추출하는 과정
 *  	=> 데이터 추출의 목적을 가짐
 *  	=> 종류
 *  		- INNER JOIN
 *  			· EQUI_JOIN(=)
 *  			· NON_EQUI_JOIN(논리연산자, BETWEEN~END)
 *  			* 포함 시에도 조인 가능
 *  			* 단점 : NULL 값 처리 불가
 *  		- OUTER JOIN
 *  			· LEFT OUTER JOIN
 *  			· RIGHT OUTER JOIN
 *  			* 장점 : NULL 값 처리 가능(INNER JOIN 보완)
 */

import java.util.*;
import java.sql.*;
public class EmpDAO {

	// 오라클 연결
	private Connection conn;
	
	// SQL 문장 전송 => 결과값
	private PreparedStatement ps;
	
	// 오라클 연결 => URL 주소
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	// 1. 드라이버 등록 => 각 데이터베이스 업체에서 제공
	// 1번만 사용 => 생성자에서 주로 사용
	public EmpDAO()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			/*
			 *  thin : 오라클과 자바응용프로그램을 연결만 해주는 역할(무료)
			 *  oci : 드라이버에 오라클 기능을 가지고 있음(유료)
			 *	=> 클래스명으로 메모리 할당 => Spring, MyBatis => 라이브러리에 주로 사용
			 *
			 *	리플렉션 => 메모리 할당 => 클래스 등록(XML)
			 */
			 
		}catch(Exception ex) {}
		
	}
	
	// 오라클 연결
	public void getConnection()
	{
		try
		{
			// 오라클로 전송하는 명령어 : conn hr/happy
			conn=DriverManager.getConnection(URL, "hr", "happy");
		}catch(Exception ex) {}
	}
	
	// 오라클 닫기
	public void disConnection()
	{
		try
		{
			// OutputStream / BufferedReader
			if(ps!=null)
			{
				ps.close();
			}
			// Socket
			if(conn!=null)
			{
				conn.close();
			}
		}catch(Exception ex) {}
	}
	// -------------------------> DAO의 필수 입력 과정
	/*
	 * 	DAO : 원래 1개의 테이블만 제어할 수 있게 제작 => 통합해서 사용(게시판 + 댓글 / 찜하기 + 좋아요) => Service(DAO 여러 개를 묶은 것)
	 * 	*** DAO와 Service의 차이점(중요)
	 * 	=> 회원 / 게시판 / 영화 / 음악 / 맛집 / 레시피
	 * 	=> 재사용 목적
	 * 	
	 */
	// DAO(JDBC) => DBCP => ORM(MyBatis => JAP)
	
	// 기능 => 사원의 정보 => 급여 등급, 부서명, 근무지
	// JOIN => emp=dept, emp=salgrade
	public ArrayList<EmpVO> empAllData()
	{
		// emp, dept, salgrade 정보를 한 번에 모아서 전송
		ArrayList<EmpVO> list=new ArrayList<EmpVO>();
		
		try
		{
			// 1. 연결
			getConnection();
			
			// 2. SQL 문장 제작
//			String sql="SELECT empno, ename, job, hiredate,"
//					+"sal, emp.deptno, dname, loc, grade "
//					+"FROM emp, dept, salgrade "
//					+"WHERE emp.deptno=dept.deptno "
//					+"AND sal BETWEEN losal AND hisal";
			
			String sql="SELECT empno, ename, job, hiredate,"
					+"sal, emp.deptno, dname, loc, grade "
					+"FROM emp JOIN dept "
					+"ON emp.deptno=dept.deptno "
					+"JOIN salgrade "
					+"ON sal BETWEEN losal AND hisal";
			
			ps=conn.prepareStatement(sql);
			
			// 3. 오라클 전송
			ResultSet rs=ps.executeQuery();
			
			// 4. 실행 후 결과값을 받아 옴
			while(rs.next())
			{
				EmpVO vo=new EmpVO();
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.setJob(rs.getString(3));
				vo.setHiredate(rs.getDate(4));
				vo.setSal(rs.getInt(5));
				vo.setDeptno(rs.getInt(6));
				vo.getDvo().setDname(rs.getString(7));
				vo.getDvo().setLoc(rs.getString(8));
				vo.getSvo().setGrade(rs.getInt(9));
				list.add(vo);
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
	
	// SubQuery : SQL 문장 여러 개를 하나로 합쳐서 한 번에 처리하는 역할
		
	/*
	 * MainQuery = (SubQuery)
	 * SubQuery 종류
	 * 	= WHERE 뒤에 조건
	 * 		- 단일행 서브쿼리
	 * 			· 비교연산자(=, !=, <, >, <=, >=)
	 * 		- 다중행 서브쿼리
	 * 			· IN
	 * 			· ANY
	 * 			· ALL 		 
	 * 	= SELECT 뒤에(컬럼 대신 사용) => 스칼라 서브쿼리(4장)
	 * 	= FROM 뒤에(테이블 대신 사용) => 인라인뷰(4장)
	 */
	
	// KING이 있는 부서에서 근무하는 사원의 사번, 이름 , 부서명, 부서번호, 근무지, 입사일, 급여 출력 
	public ArrayList<EmpVO> subqueryEmpData()
	{
		ArrayList<EmpVO> list=new ArrayList<EmpVO>();
		try
		{
			// 서브 쿼리가 먼저 실행 => ()
			// 서브 쿼리에서는 ORDER BY 사용 불가
			getConnection();
			String sql="SELECT empno, ename, dname, loc, hiredate, sal "
					+"FROM emp, dept "
					+"WHERE emp.deptno=dept.deptno "
					+"AND emp.deptno=(SELECT deptno FROM emp WHERE ename='KING')";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				EmpVO vo=new EmpVO(); // SQL
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.getDvo().setDname(rs.getString(3));
				vo.getDvo().setLoc(rs.getString(4));
				vo.setHiredate(rs.getDate(5));
				vo.setSal(rs.getInt(6));
				list.add(vo);
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
	
	// 다중행 서브 쿼리 => IN => 이름 중에 A를 포함하고 있는 사원의 부서에서 근무하는 사원의 사번, 이름, 부서명, 근무지, 입사일, 급여
	public ArrayList<EmpVO> subqueryInEmpListData()
	{
		ArrayList<EmpVO> list=new ArrayList<EmpVO>();
		try
		{
			getConnection();
			String sql="SELECT empno, ename, dname, loc, hiredate, sal "
					+"FROM emp JOIN dept "
					+"ON emp.deptno=dept.deptno "
					+"AND emp.deptno IN(SELECT DISTINCT deptno FROM emp WEHRE ename LIKE '%A%') "
					+"ORDER BY emp.deptno ASC";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				EmpVO vo=new EmpVO(); // SQL
				vo.setEmpno(rs.getInt(1));
				vo.setEname(rs.getString(2));
				vo.getDvo().setDname(rs.getString(3));
				vo.getDvo().setLoc(rs.getString(4));
				vo.setHiredate(rs.getDate(5));
				vo.setSal(rs.getInt(6));
				list.add(vo);
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
}
