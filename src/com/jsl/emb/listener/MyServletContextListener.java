package com.jsl.emb.listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TimeOfDay;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;



import com.jsl.emb.job.OutCountriesJsonFileJob;
import com.jsl.emb.job.OutTypeJsonFileJob;

public class MyServletContextListener implements ServletContextListener{
	
	Logger logger= Logger.getLogger(MyServletContextListener.class);
	
	  // Grab the Scheduler instance from the Factory
    Scheduler scheduler ;

	

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Tomcat服务终止");
		shutdownScheduler();
	}




	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Tomcat服务启动");
		startQuarzScheduler();
	}




	void startQuarzScheduler() {
		 try {
			 // Grab the Scheduler instance from the Factory
			 scheduler = StdSchedulerFactory.getDefaultScheduler();
             // and start it off
             scheduler.start();
             
          // define the job and tie it to our HelloJob class
             JobDetail job1 = JobBuilder.newJob(OutCountriesJsonFileJob.class)
                 .withIdentity("job1", "group1")
                 .build();
             
             JobDetail job2 = JobBuilder.newJob(OutTypeJsonFileJob.class)
                     .withIdentity("job2", "group1")
                     .build();

     
             
             /*Trigger trigger1 = TriggerBuilder.newTrigger()
                 .withIdentity("trigger1", "group1")
                 .startAt(new SimpleDateFormat("hh:mm:ss").parse("10:01:00"))
                       .withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().
                         withIntervalInDays(1))            
                 .build();*/
             
             Trigger trigger1 = TriggerBuilder.newTrigger()
                     .withIdentity("trigger1", "group1")
                           .withSchedule(DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().startingDailyAt(TimeOfDay.hourAndMinuteOfDay(6, 1))
                        		   .endingDailyAt(TimeOfDay.hourAndMinuteOfDay(11, 1))
                        		   .onEveryDay()
                        		   .withIntervalInMinutes(1))            
                     .build();

             Trigger trigger2 = TriggerBuilder.newTrigger()
                     .withIdentity("trigger2", "group1")
                           .withSchedule(DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().startingDailyAt(TimeOfDay.hourAndMinuteOfDay(6, 1))
                        		   .endingDailyAt(TimeOfDay.hourAndMinuteOfDay(11, 1))
                        		   .onEveryDay()
                        		   .withIntervalInMinutes(1))            
                     .build();

             // Tell quartz to schedule the job using our trigger
             scheduler.scheduleJob(job1, trigger1);
             scheduler.scheduleJob(job2, trigger2);

         } catch (SchedulerException se) {
        	 logger.error(se.getMessage(), se);
         }
		
	}
	
	
	void shutdownScheduler(){
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	

}
