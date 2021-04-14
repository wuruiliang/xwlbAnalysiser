package com.wrl.xwlb.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.wrl.xwlb.common.exception.JsonIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Supplier;

public class JsonConverter {
  private static final Logger log = LoggerFactory.getLogger(JsonConverter.class);
  private static final ObjectMapper mapper = new ObjectMapper();
  private final boolean logging;

  /** @deprecated */
  @Deprecated
  public static JsonConverter getInstance() {
    return JsonConverter.InstanceHolder.instance;
  }

  public static ObjectMapper getMapper() {
    return mapper;
  }

  JsonConverter(boolean logging) {
    this.logging = logging;
  }

  public <M> M from(String value, Class<M> clazz) {
    return this.exceptionToNull(() -> {
      return this.fromOrException(value, clazz);
    });
  }

  public <M> M from(String value, TypeReference<M> typeReference) {
    return this.exceptionToNull(() -> {
      return this.fromOrException(value, typeReference);
    });
  }

  public String to(Object obj) {
    return (String)this.exceptionToNull(() -> {
      return this.toOrException(obj);
    });
  }

  public <M> M convert(Object value, Class<M> clazz) {
    return mapper.convertValue(value, clazz);
  }

  public <M> M convert(Object value, TypeReference<M> typeReference) {
    return mapper.convertValue(value, typeReference);
  }

  public <M> M fromOrException(String value, Class<M> clazz) {
    try {
      return mapper.readValue(value, clazz);
    } catch (IOException var4) {
      throw new JsonIOException(var4);
    }
  }

  public <M> M fromOrException(String value, TypeReference<M> typeReference) {
    try {
      return mapper.readValue(value, typeReference);
    } catch (IOException var4) {
      throw new JsonIOException(var4);
    }
  }

  public String toOrException(Object obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException var3) {
      throw new JsonIOException(var3);
    }
  }

  private <M> M exceptionToNull(Supplier<M> supplier) {
    try {
      return supplier.get();
    } catch (JsonIOException var3) {
      this.doLogging(var3);
      return null;
    }
  }

  private void doLogging(JsonIOException e) {
    if (this.logging) {
      log.error("", e);
    }

  }

  static {
    mapper.setTimeZone(ClockUtil.getTimeZone().toTimeZone());
    mapper.registerModule(new JodaModule());
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  static class InstanceHolder {
    static JsonConverter instance = new JsonConverter(true);

    InstanceHolder() {
    }
  }
}
