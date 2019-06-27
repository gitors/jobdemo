package com.hzsun.jobdemo.service;

import com.hzsun.jobdemo.entity.JobEntity;

import java.util.List;

/**
 * @author : liuwenlong
 * @desc :
 * @company : 正元智慧
 * @date : 2019-06-26 15:05
 */
public interface JobService {
    /**
     * 查询所有
     * @return 任务列表
     */
    List<JobEntity> findAll();

    /**
     * 根据ID查询任务
     * @param id ID
     * @return
     */
    JobEntity findById(String id);

    /**
     * 重试
     * @param jobEntity
     */
    void reTry(JobEntity jobEntity);

    void deleteJob(String id);
}
