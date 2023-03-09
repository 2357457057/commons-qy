package top.yqingyu.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 虚拟console表格适用于等线的字体
 * 关于坐标 左上角第一位坐标为（0，0）【x,y】
 *
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.utils.VirtualConsoleTable
 * @createTime 2023年02月19日 02:10:00
 */
public class VirtualConsoleTable {
    private final AtomicInteger X = new AtomicInteger(0);
    private final AtomicInteger Y = new AtomicInteger(0);
    private final AtomicInteger maxX = new AtomicInteger(0);
    private final AtomicInteger maxY = new AtomicInteger(0);
    private final List<Column> columns = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final String colSpacing;

    public VirtualConsoleTable() {
        colSpacing = " ";
    }

    /**
     * @param num 间距
     */
    public VirtualConsoleTable(int num) {
        this.colSpacing = StringUtil.SPACE.repeat(num);
    }

    /**
     * @param s 放置的字符串
     * @param x x 横坐标
     * @param y y 纵坐标
     * @author YYJ
     * @description 替换，或增加至某一处坐标
     */
    public VirtualConsoleTable put(String s, int x, int y) {
        try {
            lock.lock();
            if (x + 1 > maxX.get()) {
                for (int i = 0; i < x + 1 - maxX.get(); i++) {
                    columns.add(new Column());
                }
                maxX.set(x);
            }
            Column column = columns.get(x);
            column.put(y, s);
            if (y > maxY.get()) maxY.set(y);
        } finally {
            lock.unlock();
        }
        return this;
    }

    public VirtualConsoleTable append(String s) {
        if (s == null) s = "";
        try {
            lock.lock();
            if (X.get() + 1 > columns.size()) {
                columns.add(new Column());
            }
            Column column = columns.get(X.getAndIncrement());
            if (!column.add(Y.get(), s)) {
                append(s);
            }
            if (X.get() > maxX.get()) {
                maxX.set(X.get());
            }
            if (Y.get() > maxY.get()) maxY.set(Y.get());
        } finally {
            lock.unlock();
        }
        return this;
    }

    public VirtualConsoleTable append(String... s) {
        for (String s1 : s) {
            append(s1);
        }
        return this;
    }

    public VirtualConsoleTable appendCrossCol(String s, int num) {
        s = "c$" + s;
        append(s);
        for (int i = 1; i < num; i++) {
            append(s);
        }
        return this;
    }

    public VirtualConsoleTable appendCrossRow(String s, int num) {
        s = "r$" + s;
        append(s);
        Column column = columns.get(Y.get());
        for (int i = 0; i < num; i++) {
            column.add(Y.get() + i, s);
        }
        return this;
    }

    public VirtualConsoleTable newLine() {
        X.set(0);
        Y.incrementAndGet();
        return this;
    }

    private static class Column {
        private final HashMap<Integer, String> column = new HashMap<>();
        private int maxLength = 0;

        private boolean add(int y, String s) {
            if (column.get(y) != null) { // 考虑跨行
                return false;
            }
            if (s.length() > maxLength)
                maxLength = s.length();
            column.put(y, s);
            return true;
        }

        private boolean put(int y, String s) {
            if (s.length() > maxLength)
                maxLength = s.length();
            column.put(y, s);
            return true;
        }

        public String getColumn(int i) {
            String cell = column.get(i) == null ? "" : column.get(i);
            return StringUtil.rightPad(cell, maxLength, " ");
        }

        public int getMaxLength() {
            return maxLength;
        }
    }

    public String toString() {
        try {
            lock.lock();
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < maxY.get(); j++) {
                sb.append("\n");
                for (Column value : columns) {
                    String column = value.getColumn(j);
                    if (column != null) {
                        sb.append(column);
                        sb.append(colSpacing);
                    }
                }
            }
            return sb.toString().replaceFirst("\n", "");
        } finally {
            lock.unlock();
        }
    }

}
