package com.wrl.xwlb.services;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.wrl.xwlb.core.BaseTest;
import com.wrl.xwlb.services.VO.TextVO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class XwlbTextServiceTest extends BaseTest {

  @Autowired
  private XwlbTextService xwlbTextService;

  @Before
  public void setUp() {

  }

  @Test
  public void testJieba() {
    JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
    String[] strings = new String[]{"我今天上海北京去哪里", "天下无敌海上有船", "哈哈哈新闻联播"};
    for (String s : strings) {
      List<SegToken> tokens = jiebaSegmenter.process(s, JiebaSegmenter.SegMode.INDEX);
      System.out.println(tokens.toString());
      List<SegToken> tokens2 = jiebaSegmenter.process(s, JiebaSegmenter.SegMode.SEARCH);
      System.out.println(tokens2.toString());
    }
  }

  @Test
  public void testSegment() {
    List<String> ss = xwlbTextService.segment(xwlbTextService.getXwlbTexts(1617379200000L, 1617984000000L));
    System.out.println(ss.toString());
  }

  @Test
  public void testGetTextByWord() {
    List<TextVO> vos = xwlbTextService.getTextByWordAndDateRange("就业", 1617379200000L, 1617984000000L);
    System.out.println(vos.toString());
  }
}
