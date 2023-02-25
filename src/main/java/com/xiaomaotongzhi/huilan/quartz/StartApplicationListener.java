package com.xiaomaotongzhi.huilan.quartz;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

//@Component
//public class StartApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
//
//    @Autowired
//    private Scheduler scheduler  ;
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        TriggerKey triggerKey = TriggerKey.triggerKey("trigger", "group");
//        try {
//            Trigger trigger = scheduler.getTrigger(triggerKey);
//            if (trigger==null){
//                trigger = TriggerBuilder.newTrigger()
//                        .withIdentity(triggerKey)
//                        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                                .withIntervalInSeconds(10).repeatForever())
//                        .build() ;
//
//                JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
//                        .withIdentity("job1", "detail1").build();
//
//                scheduler.scheduleJob(jobDetail,trigger) ;
//                scheduler.start();
//            }
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
