package com.sist.dao;
/*
 *	연동(중요) => 웹 프로그램의 핵심
 * 	
 *	브라우저(결과값 받아서 화면 출력) ==(요청, 전송)==> 자바 ==(SQL 전송, 결과값 받기)==> 오라클(SQL 문장 실행)
 *	
 *	*** 오라클 SQL 문장과 자바에서 전송하는 SQL 문장의 다른 점
 *	= LIKE이 약간 다름
 *	= 오라클/MySQL
 *	
 *	1) 연결(송수신) => 드라이버(클래스로 만들어져 있음) 설정
 *		class.forName("oracle.jdbc.driver.OracleDriver") => ojdbc8.jar(송수신을 담당하는것으로 없으면 연동 불가)
 * 	2) 오라클 연결
 * 		Connection conn=DriverManager.getConnection(URL, username(hr), password(happy))
 * 		URL : 	jdbc:업체명:드라이버타입:@IP:PORT:데이터베이스명
 * 				jdbc:oracle:thin:@localhost:1521:XE
 * 				=> SQLPlus와 동일
 * 	3) SQL 문장 전송
 * 		PreperedStatement ps=conn.preparestatement(SQL 문장);
 * 		SQL 문장 => SELECT... 
 * 	4) 오라클에서 실행된 데이터 받기
 * 		ResultSet rs=ps.executeQuery() => 실행된 결과를 메모리에 저장
 * 		SELECT ename, job
 * 		ResultSet
 * 		ENAME                JOB
		-------------------- ------------------
		CLARK                MANAGER	| 커서 이동 => next()
		JONES                MANAGER
		WARD                 SALESMAN
		BLAKE                MANAGER
		ALLEN                SALESMAN
		SCOTT                ANALYST
		MARTIN               SALESMAN
		SMITH                CLERK
		ADAMS                CLERK
		KING                 PRESIDENT
		TURNER               SALESMAN
		JAMES                CLERK
		FORD                 ANALYST
		MILLER               CLERK	| 커서 이동 => previous()
		'|' 커서가 여기에 존재
		=> Order by를 이용해서 데이터를 읽어옴 => next()
		while(rs.next())
		{
			=> VO에 값을 채움
		}
		=> 읽을 데이터가 존재하지 않음
		rs.close()
		ps.close()
		conn.close() => 종료
		----------> 코딩하는 패턴이 1개뿐
		----------> SQL문장을 정상 수행하게 제작
					----- 오라클 
		----------	DML, DQL => CRUD
	=> 반복하는 구간 => 연결 / 닫기 => 메소드화
	SELECT : 데이터 읽기(검색)
		- 형식
			SELECT * | column명
			FROM 테이블명
			[
				WHERE 조건문(연산자)
				GROUP BY 컬럼 | 함수 => 그룹
				HAVING 그룹에 대한 조건 => 반드시 GROUP BY가 있는 경우에만 사용 가능
				ORDER BY 컬럼(정렬 대상 | 위치한 번호) => ASC(생략 가능) | DESC
			]
 */

import java.util.*; // Date
import java.sql.*; // Connection / PreparedStatement / ResultSet
// 네트워크 프로그램 => 자바응용프로그램(클라이언트) <====> 오라클(서버)
// 요청(SQL) <====> 응답(실제 출력 결과값을 받음)
public class EmpDAO {
	// 연결 객체 선언 => Connection
	private Connection conn; // connection
	
	// SQL 문장 송수신
	private PreparedStatement ps; // read/write
	
	// 오라클 연결 => 오라클 주소 필요
	private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
	
	// 드라이버 등록 => 1번만 => 보통(생성자)
	public EmpDAO()
	{
		try
		{
			// 대소문자 구분
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//	메모리 할당 => 클래스명으로 메모리 할당이 가능 => 리플렉션
		}catch(Exception ex) {}
	}
	
	// 연결 => SQLPlus를 연결
	public void getConnection()
	{
		try
		{
			conn=DriverManager.getConnection(URL, "hr", "happy");
			// conn hr/happy
		}catch(Exception ex) {}
	}
	// 해제
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
			
			// exit
		}catch(Exception ex) {}
	}
	
	// 기능 수행 => 메소드 => 테이블 1개당 => VO, DAO 1개씩 생성 후 작업
	
	// SQL 문장 전송
	// emp에서 데이터 출력 => 사번, 이름, 입사일, 직위, 급여
	public void empListData()
	{
		try
		{
			// 1. 오라클 연결
			getConnection();
			// 2. SQL 문장 제작
			String sql="SELECT empno, ename, job, hiredate, sal " // 공백 부여
						+"FROM emp"; // 세미콜론(;)이 자동으로 생성되기 때문에 오라클 문장에는 세미콜론을 입력할 필요 없음
			// 3. SQL 문장을 오라클 전송
			ps=conn.prepareStatement(sql);
			// 4. 결과값을 받음
			ResultSet rs=ps.executeQuery();
			// 5. 결과값을 출력
			/*
			 * 	no	name	sex	regdate
			 * 	-------------------------
			 * 	1	홍길동	남자	23/11/13
			 * 	- rs.getInt(1) // 번호
			 * 	- rs.getString(2) // 이름
			 * 	- rs.getString(3) // 성별
			 * 	- rs.getDate(4) // 날짜
			 */
			while(rs.next())
			{
				System.out.println(rs.getInt(1)+" "
						+ rs.getString(2)+" "
						+ rs.getString(3)+" "
						+ rs.getDate(4)+" "
						+ rs.getInt(5));
			}
			rs.close();
		}
		catch(Exception ex)
		{
			// 오류 위치 확인
			ex.printStackTrace();
		}
		finally
		{
			// 닫기
			disConnection();
		}
	}
	
	// 사원의 이름, 직위, 급여, 입사일, 성과급 => 성과급이 없는 사원의 목록을 출력
	public void empNotCommListData()
	{
		try
		{
			getConnection();
			String sql="SELECT ename, job, hiredate, sal, comm "
					+ "FROM emp "
					+ "WHERE comm IS NULL";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				System.out.println(rs.getString(1)+" "
								+ rs.getString(2)+" "
								+ rs.getDate(3)+" "
								+ rs.getInt(4)+" "
								+ rs.getInt(5));
			}
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
	}
	
	// 사원의 이름, 직위, 급여, 입사일, 성과급 => 성과급이 있는 사원의 목록을 출력(0도 제외)
	public void empCommListData()
	{
		try
		{
			getConnection();
			String sql="SELECT ename, job, hiredate, sal, comm "
					+ "FROM emp "
					+ "WHERE comm IS NOT NULL AND comm<>0";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				System.out.println(rs.getString(1)+" "
								+ rs.getString(2)+" "
								+ rs.getDate(3)+" "
								+ rs.getInt(4)+" "
								+ rs.getInt(5));
			}
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
	}
	
	// 사용자로부터 검색어를 받아서 검색
	public void empFindData(String ename)
	{
		try
		{
			getConnection();
			String sql="SELECT ename, job, hiredate, sal "
					+ "FROM emp "
					+ "WHERE ename LIKE '%' || ? || '%'"; // LIKE 입력 방식이 약간 다름 => ?에 입력된 값이 들어옴
			ps=conn.prepareStatement(sql);
			// ?에 값을 채운 후 실행 요청
			ps.setString(1, ename); // 1 => 첫번째 물음표
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				System.out.println(rs.getString(1)+" "
								+ rs.getString(2)+ " "
								+ rs.getDate(3)+" "
								+ rs.getInt(4));
			}
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
		
	}
	// RPAD
	public void empRpadData()
	{
		try
		{
			// 연결
			getConnection();
			String sql="SELECT ename, RPAD(SUBSTR(ename,1,2),LENGTH(ename), '*') "
					+ "FROM emp"; 
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				System.out.println(rs.getString(1)+" "
								+ rs.getString(2));
			}
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
		
	}
	
	public void empSalInfoData()
	{
		try
		{
			getConnection();
			String sql="SELECT ename, ROUND(MONTHS_BETWEEN(SYSDATE, hiredate)), sal, sal*12, sal+NVL(comm,0),"
					+ "		TO_CHAR(sal,'$999,999'),"
					+ "		TO_CHAR(sal*12, '$999,999'),"
					+ "		TO_CHAR(sal+NVL(comm, 0), '$999,999'),"
					+ "		TO_CHAR(hiredate, 'YYYY-MM-DD HH24:MI:SS') "
					+ "FROM emp";
			// 콤마(,) 다음에는 공백 필요 없음
			ps=conn.prepareStatement(sql);
			// TO_CHAR 문자열로 변화 => rs.getString을 이용
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				System.out.println(rs.getString(1)+" "
								+ rs.getInt(2)+" "
								+ rs.getString(3)+" "
								+ rs.getString(4)+" "
								+ rs.getString(5)+" "
								+ rs.getString(6));
			}
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
	}
	
	public void empGroupByData()
	{
		try
		{
			getConnection();
			String sql="SELECT TO_CHAR(hiredate, 'YYYY'), count(*), sum(sal), ROUND(avg(sal)), max(sal), min(sal) "
					+ "FROM emp "
					+ "GROUP BY TO_CHAR(hiredate, 'YYYY') "
					+ "ORDER BY 1 ASC;";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				System.out.println(rs.getString(1)+" "
								+ rs.getInt(2)+" "
								+ rs.getInt(3)+" "
								+ rs.getDouble(4)+" "
								+ rs.getInt(5)+" "
								+ rs.getInt(6));
			}
			rs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
	}
	
	
	// 서브 쿼리를 사용하지 않는 경우
	public void subQueryNotData()
	{
		try
		{
			getConnection();
			String sql="SELECT ROUND(AVG(sal)) "
					+ "FROM emp";
			ps=conn.prepareStatement(sql);
			
			// sql 문장은 1개 전송 가능
			ResultSet rs=ps.executeQuery();
			rs.next(); // 커서의 위치 변경
			int avg=rs.getInt(1);
			rs.close();
			
			sql="SELECT ename, job, hiredate, sal "
					+ "FROM emp "
					+ "WHERE sal<?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, avg);
			rs=ps.executeQuery();
			
			while(rs.next())
			{
				System.out.println(rs.getString(1)+" "
								+ rs.getString(2)+" "
								+ rs.getDate(3)+" "
								+ rs.getInt(4));
			}
			rs.close();	
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			disConnection();
		}
	}
	
	// 서브 쿼리를 사용하는 경우
	public void subQueryData()
	{
		try
		{
			getConnection();			
			String sql="SELECT ename, job, hiredate, sal "
					+ "FROM emp "
					+ "WHERE sal<(SELECT ROUND(AVG)(sal) FROM emp)";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next())
			{
				System.out.println(rs.getString(1)+" "
								+ rs.getString(2)+" "
								+ rs.getDate(3)+" "
								+ rs.getInt(4));
			}
			rs.close();		   
	   	}catch(Exception ex)
	   	{
	   		ex.printStackTrace();
	   	}
	   	finally
	   	{
	   		disConnection();
	   	}
	}
}
	
