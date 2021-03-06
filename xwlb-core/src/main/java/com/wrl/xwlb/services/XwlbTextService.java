package com.wrl.xwlb.services;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.wrl.xwlb.model.XwlbTextModel;
import com.wrl.xwlb.model.XwlbWordModel;
import com.wrl.xwlb.model.generated.tables.records.XwlbTextRecord;
import com.wrl.xwlb.model.generated.tables.records.XwlbWordRecord;
import com.wrl.xwlb.services.VO.TextVO;
import com.wrl.xwlb.services.VO.XwlbTextVO;
import com.wrl.xwlb.util.ClockUtil;
import com.wrl.xwlb.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class XwlbTextService {

  @Autowired
  private XwlbTextModel xwlbTextModel;

  @Autowired
  private XwlbWordModel xwlbWordModel;

  @Value("${xwlb.file.dir}")
  private String fileDir;

  Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");

  private static JiebaSegmenter jiebaSegmenter = null;

  @Autowired
  public void XwlbTextService() {
    jiebaSegmenter = new JiebaSegmenter();
  }

  public void savePics(String base64Pic, long startDate, long endDate) {
    String fileName = String.format(
        "%s/xwlb_%s_to_%s.png",
        this.fileDir,
        ClockUtil.dateString(startDate),
        ClockUtil.dateString(endDate));

    File file = new File(fileName);

    if (file.exists()) {
      return;
    }

    FileUtil.convertBase64ToImage(base64Pic, fileName);
  }

  public List<XwlbTextVO> getXwlbTexts(long startDate, long endDate) {
    return xwlbTextModel.getByDateRange(startDate, endDate)
        .stream()
        .map(XwlbTextVO::fromRecord)
        .collect(Collectors.toList());
  }

  public List<XwlbTextVO> getNotSegementedXwlbTexts(long startDate, long endDate) {
    return xwlbTextModel.getByDateRange(startDate, endDate, false)
        .stream()
        .map(XwlbTextVO::fromRecord)
        .collect(Collectors.toList());
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
        .sorted(Comparator.comparingLong(XwlbTextRecord::getDate).reversed())
        .map(XwlbTextVO::fromRecord)
        .collect(Collectors.toList());
  }

  public List<XwlbTextVO> getXwlbTextByWordListAndDateRange(List<String> wordList, long startDate, long endDate) {
    List<XwlbWordRecord> wordRecords = xwlbWordModel.getByWordList(wordList);
    if (CollectionUtils.isEmpty(wordRecords)) {
      return new ArrayList<>();
    }

    List<Long> allIds = new ArrayList<>();
    for (XwlbWordRecord xwlbWordRecord : wordRecords) {
      List<Long> ids = Arrays.stream(xwlbWordRecord.getTextIds().split(","))
          .map(Long::valueOf)
          .collect(Collectors.toList());
      allIds.addAll(ids);
    }

    return xwlbTextModel.getByIdsAndDateRange(allIds, startDate, endDate).stream()
        .sorted(Comparator.comparingLong(XwlbTextRecord::getDate).reversed())
        .map(XwlbTextVO::fromRecord)
        .collect(Collectors.toList());
  }

  public List<TextVO> getTextByWordAndDateRange(String word, long startDate, long endDate) {
    List<TextVO> textVOS = new ArrayList<>();
    List<XwlbTextVO> xwlbTextVOS = getXwlbTextByWordAndDateRange(word, startDate, endDate);

    for (XwlbTextVO xwlbTextVO : xwlbTextVOS) {
      List<String> texts = Arrays.stream(xwlbTextVO.getContent().split("\n"))
          .filter(StringUtils::isNotBlank)
          .collect(Collectors.toList());
      StringBuilder t = new StringBuilder();

      for (int i=0; i < texts.size(); i++) {
        String text = texts.get(i);
        if (text.contains(word)) {
          if (!text.endsWith("???") && i + 1 < texts.size()) {
            t.append(text).append("???\n").append(texts.get(++i));
          } else {
            t.append(text);
          }
          t.append("\n\n");
        }
      }

      if (StringUtils.isNotBlank(t)) {
        textVOS.add(new TextVO(word, ClockUtil.dateStringChinese(xwlbTextVO.getDate()), t.toString()));
      }
    }
    return textVOS;
  }

  public List<TextVO> getTextByWordListAndDateRange(List<String> wordList, long startDate, long endDate) {
    List<TextVO> textVOS = new ArrayList<>();
    List<XwlbTextVO> xwlbTextVOS = getXwlbTextByWordListAndDateRange(wordList, startDate, endDate);

    for (XwlbTextVO xwlbTextVO : xwlbTextVOS) {
      List<String> texts = Arrays.stream(xwlbTextVO.getContent().split("\n")).collect(Collectors.toList());
      StringBuilder t = new StringBuilder();

      for (int i=0; i < texts.size(); i++) {
        String text = texts.get(i);
        if (wordList.stream().anyMatch(text::contains)) {
          if (!text.endsWith("???") && i + 1 < texts.size()) {
            t.append(text).append("???\n").append(texts.get(++i));
          } else {
            t.append(text);
          }
          t.append("\n\n");
        }
      }

      if (StringUtils.isNotBlank(t)) {
        textVOS.add(new TextVO(String.join(",", wordList), ClockUtil.dateStringChinese(xwlbTextVO.getDate()), t.toString()));
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
    return Arrays.asList("??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "?????????",
        "??????", "??????", "??????", "??????", "????????????", "?????????", "??????", "??????", "??????", "??????", "??????", "??????",
        "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????",
        "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????");
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
