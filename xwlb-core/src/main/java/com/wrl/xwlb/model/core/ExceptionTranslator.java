package com.wrl.xwlb.model.core;

import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultExecuteListener;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

public class ExceptionTranslator extends DefaultExecuteListener {
  private static final long serialVersionUID = 3324249182609301005L;

  @Override
  public void exception(ExecuteContext context) {
    if (context.sqlException() != null) {
      SQLDialect dialect = context.configuration().dialect();
      SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dialect.name());
      context.exception(
          translator.translate(
              "Access database using jOOQ", context.sql(), context.sqlException()));
    }
  }
}
