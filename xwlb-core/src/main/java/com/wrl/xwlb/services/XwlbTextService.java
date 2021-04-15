package com.wrl.xwlb.services;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.wrl.xwlb.model.XwlbTextModel;
import com.wrl.xwlb.model.XwlbWordModel;
import com.wrl.xwlb.model.generated.tables.records.XwlbWordRecord;
import com.wrl.xwlb.services.VO.TextVO;
import com.wrl.xwlb.services.VO.XwlbTextVO;
import com.wrl.xwlb.util.ClockUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class XwlbTextService {

  @Autowired
  private XwlbTextModel xwlbTextModel;

  @Autowired
  private XwlbWordModel xwlbWordModel;

  Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");

  private static JiebaSegmenter jiebaSegmenter = null;

  @Autowired
  public void XwlbTextService() {
    jiebaSegmenter = new JiebaSegmenter();
  }

  public List<XwlbTextVO> getXwlbTexts(long startDate, long endDate) {
    return xwlbTextModel.getByDateRange(startDate, endDate).stream().map(XwlbTextVO::fromRecord).collect(Collectors.toList());
  }

  public Map<String, Integer> getXwlbKeywords(long startDate, long endDate) {
    return segment(getXwlbTexts(startDate, endDate));
  }

  public List<XwlbTextVO> getXwlbTextByWordAndDateRange(String word, long startDate, long endDate) {
    XwlbWordRecord wordRecord = xwlbWordModel.getByWord(word);
    if (wordRecord == null) {
      return new ArrayList<>();
    }
    List<Long> ids = Arrays.stream(wordRecord.getTextIds().split(","))
        .map(Long::valueOf)
        .collect(Collectors.toList());
    return xwlbTextModel.getByIdsAndDateRange(ids, startDate, endDate).stream()
        .map(XwlbTextVO::fromRecord)
        .collect(Collectors.toList());
  }

  public List<TextVO> getTextByWordAndDateRange(String word, long startDate, long endDate) {
    List<TextVO> textVOS = new ArrayList<>();
    List<XwlbTextVO> xwlbTextVOS = getXwlbTextByWordAndDateRange(word, startDate, endDate);
    for (XwlbTextVO xwlbTextVO : xwlbTextVOS) {
      List<String> texts = Arrays.stream(xwlbTextVO.getContent().split("\n")).collect(Collectors.toList());
      StringBuilder t = new StringBuilder();
      for (String text : texts) {
        if (text.contains(word)) {
          t.append(text).append("\n\n");
        }
      }
      if (StringUtils.isNotBlank(t)) {
        textVOS.add(new TextVO(ClockUtil.dateStringChinese(xwlbTextVO.getDate()), t.toString()));
      }
    }
    return textVOS;
  }

  public Map<String, Integer> segment(List<XwlbTextVO> xwlbTextVOS) {
    Map<String, Integer> resultMap = new HashMap<>();
    for (XwlbTextVO vo : xwlbTextVOS) {
      List<SegToken> segTokens = jiebaSegmenter.process(vo.getContent(), JiebaSegmenter.SegMode.SEARCH);
      List<String> words = segTokens.stream()
          .filter(s -> !getFilteredWords().contains(s.word) && isChinese(s.word) && s.word.length() > 1)
          .map(s -> s.word)
          .collect(Collectors.toList());
      for (String word : words) {
        resultMap.merge(word, 1, Integer::sum);
        if (!vo.getSegmented()) {
          xwlbWordModel.insertOrUpdate(word, vo.getId());
        }
      }
      xwlbTextModel.updateSegmented(vo.getId(), true);
    }
    return resultMap;
  }

  private List<String> getFilteredWords() {
    return Arrays.asList("今天", "今日", "近日", "累计", "重要", "目前", "一些", "一共", "基本", "一方面",
        "得以", "总计", "一直", "人们", "新闻联播", "文字版", "文字", "新闻", "主要", "内容", "以下", "详情",
        "详细", "全文", "就是", "以及", "同时", "必须", "一个", "一道", "一条", "表示", "举行", "开展", "截至");
  }
  private boolean isNumber(String word) {
    return pattern.matcher(word).matches();
  }

  private boolean isChinese(String string){
    int n = 0;
    for(int i = 0; i < string.length(); i++) {
      n = string.charAt(i);
      if(!(19968 <= n && n <40869)) {
        return false;
      }
    }
    return true;
  }

}
