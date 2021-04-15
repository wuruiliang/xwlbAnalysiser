package com.wrl.xwlb.jobs;

import com.wrl.xwlb.services.VO.XwlbTextVO;
import com.wrl.xwlb.services.XwlbTextService;
import com.wrl.xwlb.util.ClockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SegmentTextJob {

  private static final Logger logger = LoggerFactory.getLogger(SegmentTextJob.class);

  @Autowired
  private XwlbTextService xwlbTextService;

  @Scheduled(cron = "0 0 0/1 * * ?")
  public void execute() {
    logger.info("job segmentText run...");
    long todayStart = ClockUtil.getMinMillisOfDay(ClockUtil.now());
    long todayEnd = ClockUtil.getMaxMillisOfDay(ClockUtil.now());
    List<XwlbTextVO> todayXwlbTextVOS = xwlbTextService.getXwlbTexts(todayStart, todayEnd);
    for (XwlbTextVO vo : todayXwlbTextVOS) {
      if (!vo.getSegmented()) {
        xwlbTextService.getXwlbKeywords(todayStart, todayEnd);
      }
    }
    logger.info("job segmentText end run.");
  }
}
