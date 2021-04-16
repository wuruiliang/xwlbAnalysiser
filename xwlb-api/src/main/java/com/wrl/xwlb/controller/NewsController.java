package com.wrl.xwlb.controller;

import com.wrl.xwlb.common.exception.CommonException;
import com.wrl.xwlb.common.exception.ExceptionType;
import com.wrl.xwlb.common.mvc.CommonResponse;
import com.wrl.xwlb.services.VO.TextVO;
import com.wrl.xwlb.services.XwlbTextService;
import com.wrl.xwlb.util.ClockUtil;
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
  public CommonResponse keywords(
      @RequestParam(required = false) String date,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate){
    if (date == null && (startDate == null || endDate == null)) {
      throw new CommonException(ExceptionType.COMMON_ILLEGAL_PARAM);
    }
    long start;
    long end;
    if (date != null) {
      start = ClockUtil.dateStringToLong(date, ClockUtil.DATE_FORMAT);
      end = ClockUtil.dateStringToLong(date, ClockUtil.DATE_FORMAT);
    } else {
      start = ClockUtil.dateStringToLong(startDate, ClockUtil.DATE_FORMAT);
      end = ClockUtil.dateStringToLong(endDate, ClockUtil.DATE_FORMAT);
    }
    Map<String, Integer> result = xwlbTextService.getXwlbKeywords(start, end);
    List<Map.Entry<String, Integer>> entryList = result.entrySet().stream()
        .sorted(Comparator.comparingInt(Map.Entry::getValue))
        .collect(Collectors.toList());
    Collections.reverse(entryList);
    if (entryList.size() > MAX_KEYWORDS) {
      entryList = entryList.subList(0, MAX_KEYWORDS);
    }
    result.clear();
    for (Map.Entry<String, Integer> entry : entryList) {
      result.put(entry.getKey(), entry.getValue());
    }
    return CommonResponse.of(result);
  }

  @RequestMapping("/text")
  public CommonResponse text(
      @RequestParam String word,
      @RequestParam(required = false) String date,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate) {
    if (date == null && (startDate == null || endDate == null)) {
      throw new CommonException(ExceptionType.COMMON_ILLEGAL_PARAM);
    }
    long start;
    long end;
    if (date != null) {
      start = ClockUtil.dateStringToLong(date, ClockUtil.DATE_FORMAT);
      end = ClockUtil.dateStringToLong(date, ClockUtil.DATE_FORMAT);
    } else {
      start = ClockUtil.dateStringToLong(startDate, ClockUtil.DATE_FORMAT);
      end = ClockUtil.dateStringToLong(endDate, ClockUtil.DATE_FORMAT);
    }
    List<TextVO> textVOS = xwlbTextService.getTextByWordAndDateRange(StringEscapeUtils.unescapeHtml(word), start, end);
    return CommonResponse.of(textVOS);
  }
}
