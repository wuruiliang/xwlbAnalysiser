package com.wrl.xwlb.util;

public enum BooleanType {
    TRUE("T", true),
    FALSE("F", false);

    public String charCode;
    public Boolean bool;

    private BooleanType(String charCode, Boolean bool) {
      this.charCode = charCode;
      this.bool = bool;
    }

    public static BooleanType fromCharCodeOrNull(String charCode) {
      BooleanType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
        BooleanType booleanType = var1[var3];
        if (booleanType.charCode.equals(charCode)) {
          return booleanType;
        }
      }

      return null;
    }

    public static BooleanType fromCharCode(String charCode) {
      BooleanType type = fromCharCodeOrNull(charCode);
      if (type == null) {
        throw new RuntimeException("BooleanType is unknown for charcode = " + charCode);
      } else {
        return type;
      }
    }

    public static BooleanType fromBooleanOrNull(Boolean bool) {
      BooleanType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
        BooleanType booleanType = var1[var3];
        if (booleanType.bool.equals(bool)) {
          return booleanType;
        }
      }

      return null;
    }

    public static BooleanType fromBoolean(Boolean bool) {
      BooleanType type = fromBooleanOrNull(bool);
      if (type == null) {
        throw new RuntimeException("BooleanType is unknown for charcode = " + bool);
      } else {
        return type;
      }
    }
}
