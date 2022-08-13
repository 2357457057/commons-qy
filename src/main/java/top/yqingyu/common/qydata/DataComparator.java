package top.yqingyu.common.qydata;

import java.util.Comparator;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.IData.DataComparator
 * @Description 偷来
 * @createTime 2022年07月06日 18:15:00
 */
public class DataComparator implements Comparator {
    private String key;
    private int keyType;
    private int order;

    public DataComparator(String key, int keyType, int order) {
        this.key = key;
        this.keyType = keyType;
        this.order = order;
    }

    public int compare(Object o1, Object o2) {
        QyData data1 = (QyData)o1;
        QyData data2 = (QyData)o2;
        double double1;
        double double2;
        int int1;
        int int2;
        String string1;
        String string2;
        if (this.order == 0) {
            if (this.keyType == 2) {
                string1 = data1.getString(this.key);
                string2 = data2.getString(this.key);
                return string1.compareTo(string2);
            }

            if (this.keyType == 3) {
                int1 = data1.getInt(this.key);
                int2 = data2.getInt(this.key);
                return Integer.compare(int1, int2);
            }

            if (this.keyType == 4) {
                double1 = data1.getDouble(this.key);
                double2 = data2.getDouble(this.key);
                return Double.compare(double1, double2);
            }
        } else {
            if (this.keyType == 2) {
                string1 = data1.getString(this.key);
                string2 = data2.getString(this.key);
                return string2.compareTo(string1);
            }

            if (this.keyType == 3) {
                int1 = data1.getInt(this.key);
                int2 = data2.getInt(this.key);
                return Integer.compare(int2, int1);
            }

            if (this.keyType == 4) {
                double1 = data1.getDouble(this.key);
                double2 = data2.getDouble(this.key);
                return Double.compare(double2,double1);
            }
        }

        return 0;
    }
}
