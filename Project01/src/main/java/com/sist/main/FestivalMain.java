package com.sist.main;

import com.sist.dao.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.*;
import java.io.*;
public class FestivalMain {
	
	private static ArrayList<FestivalVO> flist=new ArrayList<FestivalVO>();
	private static ArrayList<String> Ilist=new ArrayList<String>();
	
	static FestivalDAO dao=new FestivalDAO();
	static
	{
		FileReader fr=null;
		BufferedReader br=null;
		ObjectInputStream ois=null;
		FileInputStream fis=null;
		
		StringBuffer sb=new StringBuffer();
		StringBuffer sb2=new StringBuffer();
		
		try
		{
			fr=new FileReader("c:\\Project\\Data\\festival.txt");
			String data="";
			int i=0;
			while((i=fr.read())!=-1)
			{
				data+=(char)i;
			}
			sb.append(data);
			fr.close();
			
			
			fr=new FileReader("c:\\Project\\Data\\festival_detail_image.txt");
			String data2="";
			int j=0;
			while((j=fr.read())!=-1)
			{
				data2+=(char)j;
			}
			sb2.append(data2);
			fr.close();
			
//			fis.close();
//			br.close();
			
			String festival_data=sb.toString();
			String[] fd=festival_data.split("\n");
			
			String festival_data2=sb2.toString();
			String[] fd2=festival_data2.split("\n");
			
			for(String s:fd)
			{
				
				FestivalVO vo=new FestivalVO();
				String[] detail=s.split("\\|");
				vo.setFno(Integer.parseInt(detail[0].trim()));
				vo.setTitle(detail[1]);
				vo.setPoster(detail[2]);
				vo.setCont(detail[3]);
				vo.setAddr(detail[4]);
				vo.setRate(detail[5]);				
				vo.setPhone(detail[6]);
				vo.setBhour(detail[7]);
				vo.setTag(detail[8]);
				flist.add(vo);
				
				
			}
			
			for(String s:fd2)
			{
				Ilist.add(s);
				//System.out.println(s);
			}
			
//			for(int a=0; a<Ilist.size(); a++)
//			{
//				String d=Ilist.get(a);
//				d=d.substring(0,d.indexOf("\r"));
//				flist.get(a).setDeimage(d);
//				
////				FestivalVO festivalVO = new FestivalVO();
////				festivalVO.setDeimage(p);
////				flist.add(festivalVO);
//			}
			
			for (int a = 0; a < Ilist.size(); a++) {
			    String d = Ilist.get(a);
			    int endIndex = d.indexOf("\r");
			    if (endIndex != -1) {
			        d = d.substring(0, endIndex);
			        flist.get(a).setDeimage(d);
			    } else {
			        // 적절한 처리를 수행하거나 오류를 기록
			        System.err.println("Error: Cannot find '\\r' in string at index " + a);
			    }
			}
			
			dao.festivalInsert(flist);
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				fr.close();
			}catch(Exception ex) {
				
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FestivalMain fm=new FestivalMain();
		// System.out.println("저장완료");
		for(FestivalVO vo:flist)
		{
			System.out.println("번호:"+vo.getFno());
			System.out.println("업체명:"+vo.getTitle());
			System.out.println("이미지:"+vo.getPoster());
			System.out.println("상세이미지:"+vo.getDeimage());
			System.out.println("설명:"+vo.getCont());
			System.out.println("주소:"+vo.getAddr());
			System.out.println("요금:"+vo.getRate());
			System.out.println("전화:"+vo.getPhone());
			System.out.println("시간:"+vo.getBhour());
			System.out.println("태그:"+vo.getTag());
			System.out.println("===========================");
		}
		

//		FestivalDAO dao=new FestivalDAO();
//	      try
//	      {
//	         Document doc=Jsoup.connect("https://www.genie.co.kr/chart/top200").get();
//	         Elements title=doc.select("table.list-wrap td.info a.title");
//	         Elements singer=doc.select("table.list-wrap td.info a.artist");
//	         Elements album=doc.select("table.list-wrap td.info a.albumtitle");
//	         Elements poster=doc.select("table.list-wrap a.cover img");
//	         
//	         for(int i=0;i<title.size();i++)
//	         {
//	        	FestivalVO vo=new FestivalVO();
//	            vo.setTitle(title.get(i).text());
//	            vo.setSinger(singer.get(i).text());
//	            vo.setAlbum(album.get(i).text()); // 태그 안이면 text()
//	            vo.setPoster(poster.get(i).attr("src")); // 태그 밖이면 attr 
//	            dao.musicInsert(vo);
//	         }
//	         System.out.println("Save End...");
//	      }catch(Exception ex) {}
		
	}

}
