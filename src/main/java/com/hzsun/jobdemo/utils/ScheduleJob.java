
package com.hzsun.jobdemo.utils;


import com.hzsun.jobdemo.entity.JobEntity;
import com.hzsun.jobdemo.service.JobService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;



/**
 * 通用任务
 * @author liuwenlong
 */
public class ScheduleJob extends QuartzJobBean {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private static JobService jobService;

	/**
	 * 获取JobService 对象
	 * @return
	 */
	private JobService getJobService(){
		if (jobService == null){
			jobService = (JobService) SpringContextUtil.getBean("jobServiceImpl");
		}
		return jobService;
	}


    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobEntity scheduleJob = (JobEntity) context.getMergedJobDataMap()
        		.get(JobEntity.JOB_PARAM_KEY);

        //任务开始时间
        long startTime = System.currentTimeMillis();
        
        try {
            //执行任务
        	logger.info("任务准备执行，任务ID：" + scheduleJob.getJobName());

			JobEntity jobEntity = this.getJobService().findById(scheduleJob.getId());

			if (jobEntity == null){
				//删除quartz 队列中的 任务
				this.getJobService().deleteJob(scheduleJob.getId());
				throw new RuntimeException("任务已经被删除");
			}else if (1 ==1){
				throw new RuntimeException("111111");
			}

			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			logger.info("任务执行完毕，任务ID：" + scheduleJob.getJobName() + "  总共耗时：" + times + "毫秒");
		} catch (Exception e) {
			logger.error("任务执行失败，任务ID：" + scheduleJob.getJobName(), e);
			//需要重试
			if (scheduleJob.getRetry() == 1 && scheduleJob.getRestryNum() > scheduleJob.getRetriedNum()){
				logger.error("任务准备在" + scheduleJob.getRetryInterval() + "秒后重试。任务名:"+ scheduleJob.getJobName());
				this.getJobService().reTry(scheduleJob);
			}

			//TODO  失败重试
		}
    }
}
