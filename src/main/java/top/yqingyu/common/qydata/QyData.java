package top.yqingyu.common.qydata;

import java.io.Serializable;
import java.util.Map;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qydata.QyData
 * @Description 偷来 并改为FastJSON2
 * @createTime 2022年07月06日 17:55:00
 */

public interface  QyData<K,V> extends Map<K,V>, Serializable,Cloneable {
    V get(Object key);

    V get(String key, Object value);

    String[] getNames();

    String[] getNames(boolean name);

    String getString(String key);

    String getString(String key, String value);

    int getInt(String key);

    int getInt(String key, int value);

    double getDouble(String key);

    double getDouble(String key, double value);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean value);

    QyData<K,V> getData(String key);

    QyDataset<K,V> getDataset(String key);

    QyDataset<K,V> toDataset() throws Exception;

}
