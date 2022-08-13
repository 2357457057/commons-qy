package top.yqingyu.common.qydata;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.IData.Anchor
 * @Description 偷来
 * @createTime 2022年07月06日 18:23:00
 */
public class Anchor {
    Anchor() {
    }

    public static int[] mark(QyDataset dataset, String fix, int fixType) {
        int[] marks = new int[dataset.size() + 1];
        int idx = 0;
        int i;
        QyData data;
        if (fixType == 2) {
            String preValue = null;
            String curValue = null;

            for(i = 0; i < dataset.size(); ++i) {
                data = (QyData)dataset.get(i);
                curValue = data.getString(fix);
                if (!curValue.equals(preValue)) {
                    marks[idx++] = i;
                }

                preValue = curValue;
            }
        } else if (fixType == 3) {
            int preValue = -2147483648;
            int curValue = -2147483648;

            for(i = 0; i < dataset.size(); ++i) {
                data = (QyData)dataset.get(i);
                curValue = data.getInt(fix);
                if (curValue != preValue) {
                    marks[idx++] = i;
                }

                preValue = curValue;
            }
        } else if (fixType == 4) {
            double preValue = 0.0D / 0.0;
            double curValue = 0.0D / 0.0;

            for( i = 0; i < dataset.size(); ++i) {
                 data = (QyData)dataset.get(i);
                curValue = data.getDouble(fix);
                if (curValue != preValue) {
                    marks[idx++] = i;
                }

                preValue = curValue;
            }
        }

        marks[idx] = dataset.size();
        return trimRight(marks);
    }

    private static int[] trimRight(int[] marks) {
        int tail = -1;

        for(int i = marks.length - 1; i >= 0; --i) {
            if (marks[i] != 0) {
                tail = i;
                break;
            }
        }

        int[] ms = new int[tail + 1];

        for(int i = 0; i < ms.length; ++i) {
            ms[i] = marks[i];
        }

        return ms;
    }
}
