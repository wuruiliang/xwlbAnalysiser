package com.wrl.xwlb.controller;

import com.wrl.xwlb.services.VO.TextVO;
import com.wrl.xwlb.services.XwlbTextService;
import com.wrl.xwlb.util.ClockUtil;
import com.wrl.xwlb.util.JsonUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/news")
public class NewsController {

  @Autowired
  private XwlbTextService xwlbTextService;

  private static final Integer MAX_KEYWORDS = 100;

  /**
   * @param startDate 格式yyyyMMdd
   * @param endDate 格式yyyyMMdd
   * @return
   */
  @RequestMapping("/keywords")
  public String keywords(@RequestParam(name = "startDate") String startDate, @RequestParam(name = "endDate") String endDate){
    ClockUtil.dateStringToLong(startDate, ClockUtil.DATE_FORMAT);
    Map<String, Integer> result = xwlbTextService.segment(xwlbTextService.getXwlbTexts(ClockUtil.dateStringToLong(startDate, ClockUtil.DATE_FORMAT), ClockUtil.dateStringToLong(endDate, ClockUtil.DATE_FORMAT)));
    List<Map.Entry<String, Integer>> entryList = result.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).collect(Collectors.toList());
    Collections.reverse(entryList);
    if (entryList.size() > MAX_KEYWORDS) {
      entryList = entryList.subList(0, MAX_KEYWORDS);
    }
    result.clear();
    for (Map.Entry<String, Integer> entry : entryList) {
      result.put(entry.getKey(), entry.getValue());
    }
    return JsonUtil.toString(result);
  }

  @RequestMapping("/text")
  public String text(@RequestParam(name = "word") String word,
                     @RequestParam(name = "startDate") String startDate,
                     @RequestParam(name = "endDate") String endDate) {
    List<TextVO> textVOS = xwlbTextService.getTextByWordAndDateRange(StringEscapeUtils.unescapeHtml(word), ClockUtil.dateStringToLong(startDate, ClockUtil.DATE_FORMAT), ClockUtil.dateStringToLong(endDate, ClockUtil.DATE_FORMAT));
    return JsonUtil.toString(textVOS);
  }
}
