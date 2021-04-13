package com.wrl.xwlb.model.core;

import org.jooq.DSLContext;
import org.jooq.TransactionalCallable;
import org.jooq.TransactionalRunnable;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
public class ThreadTransactionalModel {
  private final ThreadLocal<Stack<DSLContext>> threadLocalContexts = ThreadLocal.withInitial(Stack::new);

  @Autowired
  protected DSLContext dslContext;

  public void transaction(TransactionalRunnable transactionalRunnable) {
    getDslContext()
        .transaction(
            configuration -> {
              DSLContext dsl = DSL.using(configuration);
              threadLocalContexts.get().push(dsl);
              try {
                transactionalRunnable.run(configuration);
              } finally {
                threadLocalContexts.get().pop();
              }
            });
  }

  public <T> T transactionResult(TransactionalCallable<T> transactionalCallable) {
    return getDslContext()
        .transactionResult(
            configuration -> {
              DSLContext dsl = DSL.using(configuration);
              threadLocalContexts.get().push(dsl);
              try {
                return transactionalCallable.run(configuration);
              } finally {
                threadLocalContexts.get().pop();
              }
            });
  }

  private DSLContext getThreadLocalDslContext() {
    Stack<DSLContext> dslContexts = threadLocalContexts.get();
    if (dslContexts.empty()) {
      return null;
    }
    return dslContexts.peek();
  }

  public DSLContext getDslContext() {
    DSLContext threadLocalDslContext = getThreadLocalDslContext();
    if (threadLocalDslContext != null) {
      return threadLocalDslContext;
    }
    return dslContext;
  }
}
