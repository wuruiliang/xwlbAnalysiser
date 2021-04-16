package com.wrl.xwlb.model;

import com.wrl.xwlb.model.core.BaseModel;
import com.wrl.xwlb.model.generated.Tables;
import com.wrl.xwlb.model.generated.tables.XwlbText;
import com.wrl.xwlb.model.generated.tables.records.XwlbTextRecord;
import com.wrl.xwlb.util.BooleanType;
import com.wrl.xwlb.util.ClockUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class XwlbTextModel extends BaseModel {
  private final XwlbText table = Tables.XWLB_TEXT;

  public List<XwlbTextRecord> getByDateRange(long startDate, long endDate) {
    return create().selectFrom(table).where(table.DATE.between(startDate, endDate)).fetch();
  }

  public List<XwlbTextRecord> getByDateRange(long startDate, long endDate, boolean segmented) {
    return create().selectFrom(table)
        .where(table.DATE.between(startDate, endDate))
        .and(table.SEGMENTED.eq(BooleanType.fromBoolean(segmented).charCode))
        .fetch();
  }

  public XwlbTextRecord getById(long id) {
    return create().selectFrom(table).where(table.ID.eq(id)).fetchOne();
  }

  public List<XwlbTextRecord> getByIdsAndDateRange(List<Long> ids, long startDate, long endDate) {
    return create().selectFrom(table).where(table.ID.in(ids)).and(table.DATE.between(startDate, endDate)).fetch();
  }

  public void updateSegmented(long recordId, Boolean segmented) {
    XwlbTextRecord record = getById(recordId);
    if (record == null) {
      return;
    }
    record.setSegmented(BooleanType.fromBoolean(segmented).charCode);
    record.setTimeUpdated(ClockUtil.now());
    record.update();
  }

}
