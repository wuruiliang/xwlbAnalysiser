package com.wrl.xwlb.model;

import com.wrl.xwlb.model.core.BaseModel;
import com.wrl.xwlb.model.generated.Tables;
import com.wrl.xwlb.model.generated.tables.XwlbWord;
import com.wrl.xwlb.model.generated.tables.records.XwlbWordRecord;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class XwlbWordModel extends BaseModel {
  private final XwlbWord table = Tables.XWLB_WORD;

  public void insertOrUpdate(String word, Long textId) {
    XwlbWordRecord record = getByWord(word);
    if (record == null) {
      insert(word, textId);
    } else {
      update(record, textId);
    }
  }

  public void insert(String word, Long textId) {
    XwlbWordRecord record = create().newRecord(table);
    record.setWord(word);
    record.setTextIds(String.valueOf(textId));
    long now = System.currentTimeMillis();
    record.setTimeCreated(now);
    record.setTimeUpdated(now);
    record.insert();
  }

  public void update(XwlbWordRecord record, Long textId) {
    List<Long> ids = Arrays.stream(record.getTextIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
    if (!ids.contains(textId)) {
      record.setTextIds(String.format("%s,%s", record.getTextIds(), textId));
      record.setTimeUpdated(System.currentTimeMillis());
      record.update();
    }
  }

  public XwlbWordRecord getById(long id) {
    return create().selectFrom(table).where(table.ID.eq(id)).fetchOne();
  }

  public XwlbWordRecord getByWord(String word) {
    return create().selectFrom(table).where(table.WORD.eq(word)).fetchOne();
  }

}
