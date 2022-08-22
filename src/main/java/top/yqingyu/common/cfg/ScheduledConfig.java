//package top.yqingyu.common.cfg;
//
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.stereotype.Component;
//
///**
// * @author YYJ
// * @version 1.0.0
// * @ClassName top.yqingyu.common.cfg.ScheduledConfig
// * @Description
// * @createTime 2022年06月09日 19:40:00
// */
//@Component
//public class ScheduledConfig implements SchedulingConfigurer {
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
//        ThreadPoolTaskScheduler pool = new ThreadPoolTaskScheduler();
//        pool.setPoolSize(24);
//        pool.initialize();
//        scheduledTaskRegistrar.setTaskScheduler(pool);
//    }
//}
