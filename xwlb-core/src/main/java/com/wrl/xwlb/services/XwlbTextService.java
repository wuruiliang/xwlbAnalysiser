package com.wrl.xwlb.services;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.wrl.xwlb.model.XwlbTextModel;
import com.wrl.xwlb.services.VO.XwlbTextVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class XwlbTextService {

  @Autowired
  private XwlbTextModel xwlbTextModel;

  Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");

  private static JiebaSegmenter jiebaSegmenter = null;

  @Autowired
  public void XwlbTextService() {
    jiebaSegmenter = new JiebaSegmenter();
  }

  public List<XwlbTextVO> getXwlbTexts(long startDate, long endDate) {
    return xwlbTextModel.getByDateRange(startDate, endDate).stream().map(XwlbTextVO::fromRecord).collect(Collectors.toList());
  }

  public List<String> segment(List<XwlbTextVO> xwlbTextVOS) {
    Set<String> result = new HashSet<>();
    for (XwlbTextVO vo : xwlbTextVOS) {
      List<SegToken> segTokens = jiebaSegmenter.process(vo.getContent(), JiebaSegmenter.SegMode.SEARCH);
      result.addAll(segTokens.stream()
          .filter(s -> !getFilteredWords().contains(s.word) && isChinese(s.word) && s.word.length() > 1)
          .map(s -> s.word)
          .collect(Collectors.toSet()));
    }
    return new ArrayList<>(result);
  }

  private List<String> getFilteredWords() {
    return Arrays.asList("今日", "近日", "一些", "一共", "基本", "一方面", "得以", "总计", "一直", "人们");
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
