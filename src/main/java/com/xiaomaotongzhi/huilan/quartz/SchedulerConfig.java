package com.xiaomaotongzhi.huilan.quartz;

import javafx.beans.property.Property;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;

//@Configuration
//public class SchedulerConfig {
//    //DataSource要的是sql包下的DataSouce
//    @Autowired
//    private DataSource dataSource ;
//
//    //Scheduler要的是quartz包下的Scheduler
//    @Bean
//    public Scheduler scheduler() throws IOException {
//        return schedulerFactoryBean().getScheduler() ;
//    }
//
//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//        //设置Scheduler的名字
//        factory.setSchedulerName("cluster_scheduler");
//        //各个节点之间是通过数据库来保持通信，所以我们要注入DataSource数据源
//        factory.setDataSource(dataSource);
//        //接着给这个FactoryBean设置一个Key（可以设置也可以不设置）
//        factory.setApplicationContextSchedulerContextKey("application");
//        //设置factory对应的配置文件properties，所以接下来我们要创建一个properties的Bean
//        factory.setQuartzProperties(properties());
//        //设置factory中的线程池配置
//        factory.setTaskExecutor(executorPool());
//        //设置factory延时时间
//        factory.setStartupDelay(10);
//        return factory ;
//    }
//
//    @Bean
//    public Properties properties() throws IOException {
//        //spring的中有专门管理properties配置文件的Bean，是PropertiesFactoryBean
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//        //将路径传输进Bean中
//        propertiesFactoryBean.setLocation(new ClassPathResource("/spring-quartz.properties"));
//        //读取路径中的配置文件
//        propertiesFactoryBean.afterPropertiesSet();
//        return propertiesFactoryBean.getObject() ;
//    }
//
//    @Bean
//    public Executor executorPool(){
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        //设置核心线程数，Runtime.getRuntime().availableProcessors()根据CPU运行时间设置合适的线程数
//        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
//        //设置最大线程数
//        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
//        //设置队列容量
//        executor.setQueueCapacity(Runtime.getRuntime().availableProcessors());
//        return executor ;
//    }
//
//}
