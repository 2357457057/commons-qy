//package top.yqingyu.common.cfg;
//
//
//import cn.hutool.core.date.LocalDateTimeUtil;
//import org.apache.log4j.Level;
//import org.apache.log4j.PatternLayout;
//import org.apache.log4j.spi.LoggingEvent;
//
//
//import java.time.LocalDateTime;
//import java.util.Date;
//
//
///**
// * description: 彩色日志 格式化日志
// *
// * @author 杨永基
// * date: 2021/09/10
// */
//public class ColorLog extends PatternLayout {
//    @Override
//    public String format(LoggingEvent event) {
//        Level level = event.getLevel();
//        String prefix = "\033[33m";
//        String suffix = "\033[0m";
//
//        LocalDateTime localDateTime = LocalDateTimeUtil.of(event.getTimeStamp());
//
//        String formatDate = LocalDateTimeUtil.format(localDateTime,"yyyy-MM-dd HH:mm:ss.SSS");
//        String levelStr = level.toString();
//        Object message = event.getMessage();
//        String className = event.getLocationInformation().getClassName();
//        String lineNumber = event.getLocationInformation().getLineNumber();
//        String threadName = event.getThreadName();
//
//        if (levelStr.length() < 5) {
//            levelStr = addPrefix(levelStr, 5 - levelStr.length(), " ");
//        }
//
//        if (lineNumber.length() < 5) {
//            lineNumber = addPrefix(lineNumber, 5 - levelStr.length(), " ");
//        }
//
//        if (threadName.length() > 15) {
//            threadName = replacePrefix(threadName, threadName.length() - 15, "");
//        }
//
//        if (threadName.length() < 15) {
//            threadName = addPrefix(threadName, 15 + 1 - levelStr.length(), " ");
//        }
//
//        if (className.length() > 39) {
//            String[] split = className.split("\\.");
//            for (int i = 0; i < split.length; i++) {
//                split[i] = split[i].substring(0, 1);
//                int temp = 0;
//                for (String s : split) {
//                    temp += s.length();
//                }
//                //计数加上中间的点的数量
//                if (temp + split.length - 1 <= 39) {
//                    break;
//                }
//            }
//            //加点
//            StringBuilder stringBuilder = new StringBuilder();
//            for (int i = 0; i < split.length; i++) {
//                stringBuilder.append(split[i]);
//                //不添加最后一个点
//                if (i != split.length - 1)
//                    stringBuilder.append(".");
//            }
//            className = stringBuilder.toString();
//        }
//
//        if (className.length() < 39) {
//            className = addSuffix(className, 39 + 1 - className.length(), " ");
//        }
//
//        switch (level.toInt()) {
//            case Level.TRACE_INT:
//                prefix = " \033[30m";
//                break;
//            case Level.DEBUG_INT:
//                prefix = " \033[34m";
//                break;
//            case Level.INFO_INT:
//                prefix = " \033[35m";
//                break;
//            case Level.WARN_INT:
//                prefix = " \033[33m";
//                break;
//            case Level.ERROR_INT:
//                prefix = " \033[31m";
//                break;
//        }
//
//
//        StringBuilder sb = new StringBuilder();
//
//        sb.append(formatDate)
//                .append(prefix)
//                .append(levelStr)
//                .append("\033[0m\t")
//                .append(lineNumber)
//                .append("\t---\t[")
//                .append(threadName)
//                .append("]")
//                .append(prefix)
//                .append(className)
//                .append("\033[0m : ")
//                .append(message)
//                .append("\n");
//        return sb.toString();
//    }
//
//
//    private static String addPrefix(String originStr, int time, String temp) {
//
//        StringBuilder OriginStr = new StringBuilder(originStr);
//        for (int i = 0; i < time; i++) {
//            OriginStr.insert(0, temp);
//        }
//        return OriginStr.toString();
//    }
//
//    private static String addSuffix(String originStr, int time, String temp) {
//        int length = originStr.length();
//        StringBuilder OriginStr = new StringBuilder(originStr);
//        for (int i = 0; i < time; i++) {
//            OriginStr.insert(length, temp);
//        }
//        return OriginStr.toString();
//    }
//
//    private static String replacePrefix(String originStr, int time, String temp) {
//
//        StringBuilder OriginStr = new StringBuilder(originStr);
//        for (int i = 0; i < time; i++) {
//            OriginStr.replace(0, 1, temp);
//        }
//        return OriginStr.toString();
//    }
//
//    private static String replaceSuffix(String originStr, int time, String temp) {
//
//        StringBuilder OriginStr = new StringBuilder(originStr);
//        for (int i = 0; i < time; i++) {
//            OriginStr.replace(OriginStr.length(), OriginStr.length(), temp);
//        }
//        return OriginStr.toString();
//    }
//
//}
