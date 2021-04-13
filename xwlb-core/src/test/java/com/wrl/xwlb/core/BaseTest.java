package com.wrl.xwlb.core;

import com.wrl.xwlb.CoreConfiguration;
import com.wrl.xwlb.model.core.BaseModel;
import org.jooq.Record;
import org.jooq.Result;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {CoreConfiguration.class})
@ActiveProfiles("dev")
public abstract class BaseTest extends BaseModel {

  public void setUp() {
    clearDB();
  }

  private void clearDB() {
    Result<Record> records =
        create()
            .resultQuery(
                "select * from information_schema.INNODB_SYS_TABLESTATS where name like 'xwlb/%' and name not in('xwlb/schema_version') ")
            .fetch();
    for (Record record : records) {
      String tbName = record.getValue("NAME", String.class).replace("/", ".");
      create().execute("truncate table " + tbName);
    }
  }

}
