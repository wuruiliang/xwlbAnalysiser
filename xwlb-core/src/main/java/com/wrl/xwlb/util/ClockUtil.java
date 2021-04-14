package com.wrl.xwlb.util;

import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;

public class ClockUtil {
  public static String timezone = "Asia/Shanghai";
  public static final String DATE_FORMAT = "yyyyMMdd";
  public static final String DATE_FORMAT_CHINESE = "yyyy年MM月dd日";
  public static final String BIRTHDAY_FORMAT = "MMdd";
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_TIME_FORMAT_CHINESE = "yyyy年MM月dd日 HH:mm:ss";
  public static final Long MILLS_PER_SECOND = 1000L;
  public static final Long MILLS_PER_HOUR = 3600000L;
  public static final Long MILLS_PER_DAY = 86400000L;
  public static final Long MILLS_PER_MONTH = 2592000000L;
  public static final Long MILLS_OF_TWO_HOURS = 7200000L;
  public static final Long MILLS_OF_NINE_HOURS = 32400000L;
  public static final long MILLS_PER_MINUTE = 60000L;
  public static final int SECONDS_PER_DAY = 86400;
  public static final int YEAR_IN_DAYS = 365;
  public static final int WEEK_IN_DAYS = 7;
  public static final int YEAR_IN_MONTHS = 12;

  public ClockUtil() {
  }

  public static long nowDateInMillis() {
    return nowDate().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
  }

  public static long nowDateInMillsWithHour(int hour) {
    return nowDate().withHourOfDay(hour).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
  }

  public static void setTimeZone(String timezoneId) {
    timezone = timezoneId;
  }

  public static long daysAgoInMillis(int daysAgo) {
    return nowDateInMillis() - MILLS_PER_DAY * (long)daysAgo;
  }

  public static long daysAfterInMills(int afterDays) {
    return nowDateInMillis() + MILLS_PER_DAY * (long)afterDays;
  }

  public static long daysAfterInMills(Long timeStamp, int afterDays) {
    return timeStamp + MILLS_PER_DAY * (long)afterDays;
  }

  public static long hoursAgoInMills(int hoursAgo) {
    return now() - MILLS_PER_HOUR * (long)hoursAgo;
  }

  public static long timeHoursAgoInMills(long time, int hoursAgo) {
    return time - MILLS_PER_HOUR * (long)hoursAgo;
  }

  public static long timeHoursAfterInMills(long time, int hoursAfter) {
    return time + MILLS_PER_HOUR * (long)hoursAfter;
  }

  public static long monthsAgoInMills(int monthsAgo) {
    return nowDateInMillis() - MILLS_PER_MONTH * (long)monthsAgo;
  }

  public static long nowMonthInMillis() {
    return nowDate().withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
  }

  public static long getMinMillisOfMonth(Long timestamp) {
    return getMinMillisOfDay((new DateTime(timestamp, getTimeZone())).withDayOfMonth(1).getMillis());
  }

  public static long getMaxMillisOfMonth(Long timestamp) {
    return getMaxMillisOfDay((new DateTime(timestamp, getTimeZone())).plusMonths(1).withDayOfMonth(1).minusDays(1).getMillis());
  }

  public static long getRemainingMillisOfMonth() {
    return getMaxMillisOfMonth(now()) - now();
  }

  public static long getRemainingMillisOfDay() {
    return getMaxMillisOfDay(now()) - now();
  }

  public static long nowWeekInMillis() {
    return nowDate().withDayOfWeek(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
  }

  public static long now() {
    return nowDate().getMillis();
  }

  public static DateTimeZone getTimeZone() {
    return DateTimeZone.forID(timezone);
  }

  public static DateTime nowDate() {
    return new DateTime(getTimeZone());
  }

  public static DateTime dateOfYear(int year) {
    return (new DateTime(getTimeZone())).withYear(year).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
  }

  public static DateTime date(Long timestamp) {
    return new DateTime(timestamp, getTimeZone());
  }

  public static long dateInMills(Long timestamp) {
    return (new DateTime(timestamp, getTimeZone())).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
  }

  public static long monthInMills(Long timestamp) {
    return (new DateTime(timestamp, getTimeZone())).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
  }

  public static long weekInMills(Long timestamp) {
    return (new DateTime(timestamp, getTimeZone())).withDayOfWeek(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
  }

  public static long getMinMillisOfDay(Long millis) {
    return date(millis).millisOfDay().withMinimumValue().getMillis();
  }

  public static int getDaysWithHeadAndTail(long startMillis, long endMills) {
    if (endMills < startMillis) {
      throw new RuntimeException("参数异常,endMills必须不小于startMillis");
    } else {
      long maxMillisOfFirstDay = getMaxMillisOfDay(startMillis);
      if (maxMillisOfFirstDay > endMills) {
        return 1;
      } else {
        long leftMillis = endMills - maxMillisOfFirstDay;
        int leftDays = (int)(leftMillis % MILLS_PER_DAY == 0L ? leftMillis / MILLS_PER_DAY : leftMillis / MILLS_PER_DAY + 1L);
        return leftDays + 1;
      }
    }
  }

  public static long getMaxMillisOfDay(Long millis) {
    return date(millis).millisOfDay().withMaximumValue().getMillis();
  }

  public static int getDaysBetween(Long startMillis, Long endMills) {
    return Days.daysBetween(date(startMillis), date(endMills)).getDays() + 1;
  }

  public static int getDaysWithHeadBetween(Long startMillis, Long endMills) {
    if (endMills < startMillis) {
      throw new RuntimeException("参数异常,endMills必须不小于startMillis");
    } else {
      int days = Days.daysBetween(date(startMillis), date(endMills)).getDays();
      if (days == 0) {
        ++days;
      }

      return days;
    }
  }

  public static int getMonthsBetween(Long startMillis, Long endMills) {
    return Months.monthsBetween(date(startMillis), date(endMills)).getMonths() + 1;
  }

  public static int getRoughMonthsBetween(Long startMillis, Long endMills) {
    if (startMillis > endMills) {
      throw new RuntimeException("参数异常,endMills必须不小于startMillis");
    } else {
      DateTime startDate = date(startMillis);
      DateTime endDate = date(endMills);
      return 12 * (endDate.getYear() - startDate.getYear()) + (endDate.getMonthOfYear() - startDate.getMonthOfYear());
    }
  }

  public static int getCalenderDaysBetween(Long startMillis, Long endMills) {
    return Days.daysBetween(date(getMinMillisOfDay(startMillis)), date(getMinMillisOfDay(endMills))).getDays();
  }

  public static int getHoursBetween(Long startMillis, Long endMills) {
    return Hours.hoursBetween(date(startMillis), date(endMills)).getHours() + 1;
  }

  public static int getMinutesBetween(Long startMillis, Long endMills) {
    return Minutes.minutesBetween(date(startMillis), date(endMills)).getMinutes() + 1;
  }

  public static long getSecondsBetween(Long startMillis, Long endMills) {
    return (endMills - startMillis) / 1000L;
  }

  public static int getRemainingSecondOfDay() {
    return nowDate().secondOfDay().withMaximumValue().getSecondOfDay() - nowDate().getSecondOfDay();
  }

  public static DateTime jodaDateTimeFromString(String dateStr) {
    return StringUtils.isBlank(dateStr) ? null : date(Long.valueOf(dateStr));
  }

  public static String utcDateTimeStringFromTimestamp(Long timestamp) {
    return null == timestamp ? null : (new DateTime(timestamp, DateTimeZone.UTC)).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
  }

  public static String dateTimeStringFromTimestamp(Long timestamp) {
    return dateTimeStringFromTimestamp(timestamp, "yyyy-MM-dd HH:mm:ss");
  }

  public static String dateTimeStringFromTimestamp(Long timestamp, String format) {
    return null == timestamp ? null : date(timestamp).toString(DateTimeFormat.forPattern(format));
  }

  public static String dateString(Long timestamp) {
    return date(timestamp).toString("yyyyMMdd");
  }

  public static String dateString(Long timestamp, String format) {
    return date(timestamp).toString(format);
  }

  public static String dateStringChinese(Long timestamp) {
    return date(timestamp).toString("yyyy年MM月dd日");
  }

  public static String dateStringHour(Long timestamp) {
    return date(timestamp).toString("HH:mm:ss");
  }

  public static String birthdayString(Long timestamp) {
    return date(timestamp).toString("MMdd");
  }

  public static Long dateStringToLong(String dateStr, String format) {
    if (dateStr == null) {
      return 0L;
    } else {
      long timestampUTC = DateTimeFormat.forPattern(format).withZoneUTC().parseDateTime(dateStr).getMillis();
      int offset = getTimeZone().toTimeZone().getRawOffset();
      return timestampUTC - (long)offset;
    }
  }

  public static int dayOfMonth(Long timestamp) {
    return date(timestamp).dayOfMonth().get();
  }

  public static int hourOfDay(Long timestamp) {
    return date(timestamp).hourOfDay().get();
  }

  public static int year(long timestamp) {
    return date(timestamp).year().get();
  }

  public static int monthOfYear(long timestamp) {
    return date(timestamp).monthOfYear().get();
  }

  public static boolean millisIsDate(long millis) {
    return Long.compare(getMinMillisOfDay(millis), millis) == 0;
  }

  public static long sameDayOfMonthsAfter(long millis, int months) {
    return date(millis).plusMonths(months).getMillis();
  }

  public static long getHoursBefore(long millis, int hours) {
    return date(millis).minusHours(hours).getMillis();
  }

  public static long sameDayOfYearsAfter(long millis, int years) {
    return date(millis).plusYears(years).getMillis();
  }

  public static Calendar getCalendar() {
    Calendar cal = Calendar.getInstance();
    cal.setTimeZone(getTimeZone().toTimeZone());
    return cal;
  }

  public static int getDaysOfCurrentMonth(Long currentMonthTimestamp) {
    Calendar cal = getCalendar();
    int currentMonth = date(currentMonthTimestamp).getMonthOfYear();
    cal.set(2, currentMonth - 1);
    return cal.getActualMaximum(5);
  }

  public static long nowDateWithHour(int hour) {
    return nowDate().withHourOfDay(hour).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
  }

  public static long nowHour() {
    return nowDate().withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
  }

  public static long getTimestampWithDayAndHour(Long dayTimestamp, int hour) {
    return date(dayTimestamp).withHourOfDay(hour).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
  }

  public static long getDateWithDay(long millis, int day) {
    return date(millis).withDayOfMonth(day).getMillis();
  }

  public static int compareDateOnly(long timestamp1, long timestamp2) {
    return DateTimeComparator.getDateOnlyInstance().compare(timestamp1, timestamp2);
  }

  public static boolean isWeekend(long timestamp) {
    int weekday = date(timestamp).getDayOfWeek();
    return weekday > 5;
  }

  public static int getCalenderMonthsBetween(Long startDate, Long endDate) {
    return monthOfYear(endDate) + 12 * (year(endDate) - year(startDate)) - monthOfYear(startDate);
  }
}
