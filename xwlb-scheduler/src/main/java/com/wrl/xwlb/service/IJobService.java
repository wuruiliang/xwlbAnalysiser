package com.wrl.xwlb.service;

import com.wrl.xwlb.service.vo.JobVO;

public interface IJobService {

  /**
   * 增加定时任务
   * @param jobVO 任务参数
   */
  void addJob(JobVO jobVO);

  /**
   * 临时触发一次已存在的定时任务
   * @param name 任务名
   * @param param 临时触发的参数
   */
  void triggerJob(String name, String param);

  /**
   * 暂停定时任务
   * @param name 任务名
   */
  void pauseJob(String name);

  /**
   * 重启暂停的定时任务
   * @param name 任务名
   */
  void resumeJob(String name);

  /**
   * 删除定时任务
   * @param name 任务名
   */
  void removeJob(String name);

  /**
   * 更新定时任务
   * @param jobVO 任务参数
   */
  void updateJob(JobVO jobVO);

}
