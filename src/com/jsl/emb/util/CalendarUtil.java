package com.jsl.emb.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarUtil {
	
	/**
	 * 根据起止时间，遍历出中间的每一天
	 * @param startDate 起始时间
	 * @param endDate 终止时间 
	 * @return  每一天的
	 * @throws ParseException
	 */
	public static List<String> findEveryDay(String startDate , String endDate) throws ParseException{
		 List<String> lDate = new ArrayList<String>();
		 Date sDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);//定义起始日期
		 Date eDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);//定义结束日期
	     lDate.add(startDate);
	     Calendar calBegin = Calendar.getInstance();
	     // 使用给定的 Date 设置此 Calendar 的时间
	     calBegin.setTime(sDate);
	     Calendar calEnd = Calendar.getInstance();
	     // 使用给定的 Date 设置此 Calendar 的时间
	     calEnd.setTime(eDate);
	     // 测试此日期是否在指定日期之后
	     while (eDate.after(calBegin.getTime()))
	     {
	      // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
	      calBegin.add(Calendar.DAY_OF_MONTH, 1);
	      lDate.add(new SimpleDateFormat("yyyy-MM-dd").format(calBegin.getTime()));
	     }
	     return lDate;
	}
}
