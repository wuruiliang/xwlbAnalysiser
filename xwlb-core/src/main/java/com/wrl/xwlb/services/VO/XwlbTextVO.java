package com.wrl.xwlb.services.VO;

import com.wrl.xwlb.common.exception.CommonException;
import com.wrl.xwlb.model.generated.tables.records.XwlbTextRecord;
import lombok.Data;
import sun.misc.BASE64Decoder;

import java.io.IOException;

@Data
public class XwlbTextVO {
  private Long id;
  private Long date;
  private String url;
  private String summary;
  private String content;
  private Long timeCreated;
  private Long timeUpdated;

  public static XwlbTextVO fromRecord(XwlbTextRecord record) {
    XwlbTextVO xwlbTextVO = new XwlbTextVO();
    try {
      BASE64Decoder decoder = new BASE64Decoder();
      xwlbTextVO.setId(record.getId());
      xwlbTextVO.setDate(record.getDate());
      xwlbTextVO.setUrl(record.getUrl());
      xwlbTextVO.setSummary(new String(decoder.decodeBuffer(record.getSummary())));
      xwlbTextVO.setContent(new String(decoder.decodeBuffer(record.getContent())));
      xwlbTextVO.setTimeCreated(record.getTimeCreated());
      xwlbTextVO.setTimeUpdated(record.getTimeUpdated());
    } catch (IOException e) {
      throw CommonException.wrap(e);
    }
    return xwlbTextVO;
  }
}
