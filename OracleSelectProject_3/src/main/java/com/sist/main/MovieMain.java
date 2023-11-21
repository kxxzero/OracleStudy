package com.sist.main;

import java.util.*;
import com.sist.dao.*;
public class MovieMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("===== 메뉴 =====");
		System.out.println("1. 제목으로 검색");
		System.out.println("2. 장르로 검색");
		System.out.println("3. 배우로 검색");
		System.out.println("==============");
		Scanner scan=new Scanner(System.in);
		System.out.println("메뉴 선텍:");
		int menu=scan.nextInt();
		String s="";
		if(menu==1)
		{
			s="title";
		}
		else if(menu==2)
		{
			s="genre";
		}
		else if(menu==3)
		{
			s="actor";
		}
		
		System.out.println("검색어 입력:");
		String fd=scan.next();
		
		// 오라클에서 찾아옴
		MovieDAO dao=new MovieDAO();
		int count=dao.movieFindCount(s, fd);
		if(count==0)
		{
			System.out.println("검색된 결과가 업습니다.");
		}
		else
		{
			System.out.println("검색 결과:"+count+"건");
			System.out.println();
			ArrayList<MovieVO> list=dao.movieFindDate(s, fd);
			for(MovieVO vo:list)
			{
				System.out.println(vo.getTitle()+" "
						+vo.getGenre()+" "
						+vo.getActor());
			}
		}
	}

}
