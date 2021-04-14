package com.wrl.xwlb.util;

import com.fasterxml.jackson.core.type.TypeReference;

public class JsonUtil {
  private static JsonConverter ENABLE_LOGGING_CONVERTER = new JsonConverter(true);
  private static JsonConverter DISABLE_LOGGING_CONVERTER = new JsonConverter(false);
  private static JsonConverter converter;

  public JsonUtil() {
  }

  public static void disableLogging() {
    converter = DISABLE_LOGGING_CONVERTER;
  }

  public static void enableLogging() {
    converter = ENABLE_LOGGING_CONVERTER;
  }

  public static <M> M from(String value, Class<M> clazz) {
    return converter.from(value, clazz);
  }

  public static <M> M from(String value, TypeReference<M> clazz) {
    return converter.from(value, clazz);
  }

  public static <M> M fromOrException(String value, TypeReference<M> clazz) {
    return converter.fromOrException(value, clazz);
  }

  public static <M> M fromOrException(String value, Class<M> clazz) {
    return converter.fromOrException(value, clazz);
  }

  public static <M> M convertOrException(Object value, Class<M> clazz) {
    return converter.convert(value, clazz);
  }

  public static <M> M convertOrException(Object value, TypeReference<M> clazz) {
    return converter.convert(value, clazz);
  }

  public static String toString(Object obj) {
    return converter.to(obj);
  }

  static {
    converter = ENABLE_LOGGING_CONVERTER;
  }
}
