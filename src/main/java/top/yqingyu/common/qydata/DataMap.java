package top.yqingyu.common.qydata;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import top.yqingyu.common.qymsg.QyMsgHeader;

import java.util.*;


/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.IData.DataMap
 * @Description 偷来 并改为FastJSON2
 * @createTime 2022年07月06日 18:03:00
 */
@SuppressWarnings("All")
public class DataMap extends HashMap implements QyData, Cloneable {
    public DataMap() {
    }

    public DataMap(String value) {
        List list = DataHelper.strToList(value);
        if (list != null && list.size() > 0) {
            this.putAll((Map) list.get(0));
        }

    }

    public Object get(Object name) {
        Object value = super.get(name);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JSONObject) {
                QyData data = new DataMap();
                data.putAll((JSONObject) value);
                value = data;
            } else if (value instanceof JSONArray) {
                QyDataset dataset = new DatasetList();
                dataset.addAll((JSONArray) value);
                value = dataset;
            }

            this.put(name, value);
            return value;
        }
    }

    public Object get(String name, Object def) {
        Object value = this.get(name);
        return value == null ? def : value;
    }

    public String[] getNames() {
        return this.getNames(true);
    }

    public String[] getNames(boolean sort) {
        String[] names = (String[]) ((String[]) this.keySet().toArray(new String[0]));
        if (sort) {
            Arrays.sort(names);
        }

        return names;
    }

    public String getString(String name) {
        Object value = this.get(name);
        return value == null ? null : value.toString();
    }

    public String getString2(String name) {
        Object value = this.get(name);
        return value == null ? "" : value.toString();
    }

    public String getString(String name, String defaultValue) {
        return this.get(name, defaultValue).toString();
    }

    public int getInt(String name) {
        return this.getInt(name, 0);
    }

    public int getInt(String name, int defaultValue) {
        String value = this.getString(name, "");
        return "".equals(value) ? defaultValue : Integer.parseInt(value);
    }

    public double getDouble(String name) {
        return this.getDouble(name, 0.0D);
    }

    public double getDouble(String name, double defaultValue) {
        String value = this.getString(name, "");
        return "".equals(value) ? defaultValue : Double.parseDouble(value);
    }

    public boolean getBoolean(String name) {
        return this.getBoolean(name, false);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        String value = this.getString(name, "");
        return "".equals(value) ? defaultValue : Boolean.valueOf(value);
    }

    public DataMap getData(String name) {
        return (DataMap) this.get(name);
    }

    public DatasetList getDataset(String name) {
        return (DatasetList) this.get(name);
    }

    public QyDataset toDataset() throws Exception {
        int size = 0;
        QyDataset dataset = new DatasetList();
        String[] names = this.getNames();

        int i;
        for (i = 0; i < names.length; ++i) {
            if (this.get(names[i]) instanceof List) {
                List list = (List) this.get(names[i]);
                if (size < list.size()) {
                    size = list.size();
                }
            }
        }

        for (i = 0; i < size; ++i) {
            QyData data = new DataMap();

            for (int j = 0; j < names.length; ++j) {
                Object obj = this.get(names[j]);
                if (obj instanceof List) {
                    List list = (List) obj;
                    if (i < list.size()) {
                        data.put(names[j], list.get(i));
                    } else {
                        data.put(names[j], "");
                    }
                } else {
                    data.put(names[j], obj);
                }
            }

            dataset.add(data);
        }

        return dataset;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }


    @Override
    public DataMap clone() {
        DataMap clone = (DataMap) super.clone();


        clone.forEach((key, obj) -> {
            if (obj instanceof JSONObject) {
                QyData data = new DataMap();
                data.putAll(((JSONObject) obj).clone());
                obj = data;
                clone.put(key, obj);
            } else if (obj instanceof JSONArray) {
                QyDataset dataset = new DatasetList();

                dataset.addAll((Collection) ((JSONArray) obj).clone());
                obj = dataset;
                clone.put(key, obj);
            }

        });

        return clone;

    }
}
