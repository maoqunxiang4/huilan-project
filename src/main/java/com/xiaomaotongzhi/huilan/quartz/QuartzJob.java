package com.xiaomaotongzhi.huilan.quartz;

/*
*  检查是否截止比赛是否截止  TODO
*  发送邮件给用户
 */

import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class QuartzJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            Thread.sleep(3000);
            System.out.println(context.getScheduler().getSchedulerInstanceId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
