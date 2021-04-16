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
    long start = ClockUtil.getMinMillisOfDay(ClockUtil.now());
    long end = ClockUtil.getMaxMillisOfDay(ClockUtil.now());
    List<XwlbTextVO> todayXwlbTextVOS = xwlbTextService.getNotSegementedXwlbTexts(start, end);
    for (XwlbTextVO vo : todayXwlbTextVOS) {
      xwlbTextService.getXwlbKeywords(ClockUtil.getMinMillisOfDay(vo.getDate()), ClockUtil.getMaxMillisOfDay(vo.getDate()));
    }
    logger.info("job segmentText end run.");
  }
}
