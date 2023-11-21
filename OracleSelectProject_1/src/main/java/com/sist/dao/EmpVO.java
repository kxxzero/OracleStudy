package com.sist.dao;

/*
 *	오라클 데이터형
 *	문자형
 *		- CHAR(1~2000byte) : 고정 바이트(설정된 메모리 크기)
 *		- VARCHAR2(1~4000byte) : 가변(글자 수에 따라 메모리 할당)
 *		- CLOB(4기가) : 가변
 *		* String으로 잡아놓고 실행
 *	숫자형
 *		- NUMBER(8자리)
 *		예) 	NUMBER(4) => 4자리
 *			NUMBER(7,2) => 7자리, 소수점 2자린
 *		* 저장된 데이터 int / double
 *	날짜형
 *		- DATE
 *		* java.util.Date
 *	=> 저장된 데이터를 받을 준비
 *	=> 오라클 단위 : ROW 단위(Record) => 1번 수행할 때마다 1줄씩 읽어옴
 *	=> 데이터 매칭 시 : DESC table_name
 *			
 *
 *	 이름                                      널?      유형
 ----------------------------------------- -------- ----------------------------
 EMPNO                                     NOT NULL NUMBER(4)		=> int
 ENAME                                              VARCHAR2(10)	=> String
 JOB                                                VARCHAR2(9)		=> Stirng
 MGR                                                NUMBER(4)		=> int
 HIREDATE                                           DATE			=> Date
 SAL                                                NUMBER(7,2)		=> int
 COMM                                               NUMBER(7,2)		=> int
 DEPTNO                                             NUMBER(2)		=> int
 
 */

import java.util.*;
public class EmpVO {
// column명과 매칭
	private int empno;
	private String ename;
	private String job;
	private int mgr;
	private Date hiredate;
	private int sal;
	private int comm;
	private int deptno;
	
	public int getEmpno() {
		return empno;
	}
	public void setEmpno(int empno) {
		this.empno = empno;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public int getMgr() {
		return mgr;
	}
	public void setMgr(int mgr) {
		this.mgr = mgr;
	}
	public Date getHiredate() {
		return hiredate;
	}
	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}
	public int getSal() {
		return sal;
	}
	public void setSal(int sal) {
		this.sal = sal;
	}
	public int getComm() {
		return comm;
	}
	public void setComm(int comm) {
		this.comm = comm;
	}
	public int getDeptno() {
		return deptno;
	}
	public void setDeptno(int deptno) {
		this.deptno = deptno;
	}
	
	
}
