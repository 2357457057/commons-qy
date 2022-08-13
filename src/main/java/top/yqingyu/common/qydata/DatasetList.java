package top.yqingyu.common.qydata;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.*;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.IData.DatasetList
 * @Description 偷来 并改为FastJSON2
 * @createTime 2022年07月06日 18:11:00
 */
public class DatasetList  extends ArrayList implements QyDataset {
    private String serializableId;
    private String serializablePath;
    private boolean serializable;
    private boolean batchSerializable;
    private int batchPageSize;
    private int batchPageCount;
    private Map transactKeys;
    private BaseData baseData;
    private int count = -1;

    public DatasetList() {
    }

    public DatasetList(List list) {
        this.addAll(list);
    }

    public DatasetList(String value) {
        List list = DataHelper.strToList(value);
        if (list != null) {
            this.addAll(list);
        }

    }

    /** @deprecated */
    public BaseData getBaseData() throws Exception {
        return this.baseData;
    }

    /** @deprecated */
    public void setBaseData(BaseData baseData) throws Exception {
        this.baseData = baseData;
    }

    public String getSerializableId() {
        return this.serializableId;
    }

    public void setSerializableId(String serializableId) {
        this.serializableId = serializableId;
    }

    public String getSerializablePath() {
        return this.serializablePath;
    }

    public void setSerializablePath(String serializablePath) {
        this.serializablePath = serializablePath;
    }

    public boolean isSerializable() {
        return this.serializable;
    }

    public void setSerializable(boolean serializable) {
        this.serializable = serializable;
    }

    public boolean isBatchSerializable() {
        return this.batchSerializable;
    }

    public void setBatchSerializable(boolean batchSerializable) {
        this.batchSerializable = batchSerializable;
    }

    public int getBatchPageSize() {
        return this.batchPageSize;
    }

    public void setBatchPageSize(int batchPageSize) {
        this.batchPageSize = batchPageSize;
    }

    public int getBatchPageCount() {
        return this.batchPageCount;
    }

    public void setBatchPageCount(int batchPageCount) {
        this.batchPageCount = batchPageCount;
    }

    /** @deprecated */
    public Map getTransactKeys() throws Exception {
        return this.transactKeys;
    }

    /** @deprecated */
    public void setTransactKeys(Map transactKeys) throws Exception {
        this.transactKeys = transactKeys;
    }

    public Object get(int index) {
        Object value = super.get(index);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JSONObject) {
                QyData data = new DataMap();
                data.putAll((JSONObject)value);
                value = data;
            } else if (value instanceof JSONArray) {
                QyDataset dataset = new DatasetList();
                dataset.addAll((JSONArray)value);
                value = dataset;
            }

            this.set(index, value);
            return value;
        }
    }

    public Object get(int index, String name) {
        QyData data = (QyData)this.get(index);
        return data == null ? null : data.get(name);
    }

    public Object get(int index, String name, Object def) {
        Object value = this.get(index, name);
        return value == null ? def : value;
    }

    public QyData getData(int index) throws Exception {
        return (QyData)this.get(index);
    }

    public String[] getNames() {
        return this.size() > 0 ? ((QyData)this.get(0)).getNames() : null;
    }

    public QyData toData() throws Exception {
        QyData data = new DataMap();
        Iterator it = this.iterator();

        while(it.hasNext()) {
            QyData element = (QyData)it.next();
            Iterator iterator = element.keySet().iterator();

            while(iterator.hasNext()) {
                String key = (String)iterator.next();
                if (data.containsKey(key)) {
                    QyDataset list = (QyDataset)data.get(key);
                    list.add(element.get(key));
                } else {
                    QyDataset list = new DatasetList();
                    list.add(element.get(key));
                    data.put(key, list);
                }
            }
        }

        data.put("X_RECORDNUM", String.valueOf(this.size()));
        return data;
    }

    public QyDataset filter(String filter) throws Exception {
        if (filter != null && !"".equals(filter)) {
            QyData ftdt = new DataMap();
            String[] fts = filter.split(",");

            for(int i = 0; i < fts.length; ++i) {
                String[] ft = fts[i].split("=");
                ftdt.put(ft[0], ft[1]);
            }

            QyDataset subset = new DatasetList();

            for(int i = 0; i < this.size(); ++i) {
                QyData subdata = (QyData)this.get(i);
                boolean include = true;
                String[] ftdtNames = ftdt.getNames();

                for(int j = 0; j < ftdtNames.length; ++j) {
                    String subvalue = (String)subdata.get(ftdtNames[j]);
                    if (subvalue == null || !subvalue.equals(ftdt.get(ftdtNames[j]))) {
                        include = false;
                        break;
                    }
                }

                if (include) {
                    subset.add(subdata);
                }
            }

            return subset;
        } else {
            return this;
        }
    }

    public QyDataset distinct(String fieldNames, String token) throws Exception {
        if ("".equals(fieldNames)) {
            return this;
        } else {
            List fieldValues = new ArrayList();
            QyDataset subset = new DatasetList();
            String theToken = token != null && !"".equals(token) ? token : ",";
            String[] keys = fieldNames.split(theToken);

            for(int i = 0; i < this.size(); ++i) {
                String fieldValue = "";

                for(int j = 0; j < keys.length; ++j) {
                    fieldValue = fieldValue + (String)this.get(i, keys[j]) + theToken;
                }

                if (!"".equals(fieldValue) && !fieldValues.contains(fieldValue)) {
                    fieldValues.add(fieldValue);
                    subset.add(this.get(i));
                }
            }

            return subset;
        }
    }

    public QyDataset distinct(String fieldNames) throws Exception {
        return this.distinct(fieldNames, ",");
    }

    public int count() {
        return this.count == -1 ? this.size() : this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void sort(String key, int keyType) {
        this.sort(key, keyType, 0);
    }

    public void sort(String key, int keyType, int order) {
        Object[] datas = (Object[])this.toArray();
        DataComparator c = new DataComparator(key, keyType, order);
        Arrays.sort(datas, c);
        List list = Arrays.asList(datas);
        this.clear();
        this.addAll(list);
    }

    public void sort(String key1, int keyType1, String key2, int keyType2) {
        this.sort(key1, keyType1, 0);
        this.sort(key2, keyType2, 0, key1, keyType1);
    }

    public void sort(String key1, int keyType1, int order1, String key2, int keyType2, int order2) {
        this.sort(key1, keyType1, order1);
        this.sort(key2, keyType2, order2, key1, keyType1);
    }

    private void sort(String key, int type, int order, String fix, int fixType) {
        Object[] datas = (Object[])this.toArray();
        DataComparator c = new DataComparator(key, type, order);
        if (fix == null) {
            Arrays.sort(datas, c);
        } else {
            int[] marks = Anchor.mark(this, fix, fixType);
            int pre = 0;

            for(int i = 1; i < marks.length; ++i) {
                Arrays.sort(datas, pre, marks[i], c);
                pre = marks[i];
            }
        }

        List list = Arrays.asList(datas);
        this.clear();
        this.addAll(list);
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

}