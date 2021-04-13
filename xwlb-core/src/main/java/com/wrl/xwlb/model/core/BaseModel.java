package com.wrl.xwlb.model.core;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseModel {

  @Autowired
  private
  ThreadTransactionalModel transactionalModel;

  protected DSLContext create() {
    return transactionalModel.getDslContext();
  }

  public List<Record> executeSql(String sql) {
    return create().resultQuery(sql).fetch();
  }
}
