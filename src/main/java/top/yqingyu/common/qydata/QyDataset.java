package top.yqingyu.common.qydata;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qydata.QyDataset
 * @Description 偷来 并改为FastJSON2
 * @createTime 2022年07月06日 17:56:00
 */



public interface QyDataset<K,D> extends List<D>, Serializable {

    /** @deprecated */
    BaseData getBaseData() throws Exception;

    /** @deprecated */
    void setBaseData(BaseData baseData) throws Exception;

    String getSerializableId();

    void setSerializableId(String var1);

    String getSerializablePath();

    void setSerializablePath(String var1);

    boolean isSerializable();

    void setSerializable(boolean var1);

    boolean isBatchSerializable();

    void setBatchSerializable(boolean var1);

    int getBatchPageSize();

    void setBatchPageSize(int var1);

    int getBatchPageCount();

    void setBatchPageCount(int var1);

    /** @deprecated */
    Map<K,D>  getTransactKeys() throws Exception;

    /** @deprecated */
    void setTransactKeys(Map<K,D>  var1) throws Exception;

    D get(int key);

    Object get(int key, String value);

    Object get(int key, String value, Object var3);

    QyData<K,D> getData(int key) throws Exception;

    String[] getNames();

    QyData<K,D>  toData() throws Exception;

    QyDataset<K,D> filter(String key) throws Exception;

    QyDataset<K,D> distinct(String key, String value) throws Exception;

    QyDataset<K,D> distinct(String key) throws Exception;

    int count();

    void setCount(int key);

    void sort(String key, int value);

    void sort(String key, int value, int var3);

    void sort(String key, int value, String var3, int var4);

    void sort(String key, int value, int var3, String var4, int var5, int var6);

}
