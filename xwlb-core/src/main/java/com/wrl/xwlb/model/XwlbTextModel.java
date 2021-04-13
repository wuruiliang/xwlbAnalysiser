package com.wrl.xwlb.model;

import com.wrl.xwlb.model.core.BaseModel;
import com.wrl.xwlb.model.generated.Tables;
import com.wrl.xwlb.model.generated.tables.XwlbText;
import com.wrl.xwlb.model.generated.tables.records.XwlbTextRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class XwlbTextModel extends BaseModel {
  private final XwlbText table = Tables.XWLB_TEXT;

  public List<XwlbTextRecord> getByDateRange(long startDate, long endDate) {
    return create().selectFrom(table).where(table.DATE.between(startDate, endDate)).fetch();
  }

  public XwlbTextRecord getById(long id) {
    return create().selectFrom(table).where(table.ID.eq(id)).fetchOne();
  }

  public List<XwlbTextRecord> getByIdsAndDateRange(List<Long> ids, long startDate, long endDate) {
    return create().selectFrom(table).where(table.ID.in(ids)).and(table.DATE.between(startDate, endDate)).fetch();
  }

}
