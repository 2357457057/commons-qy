package top.yqingyu.common.qydata;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.IData.DatasetResult
 * @Description 偷来 并改为FastJSON2
 * @createTime 2022年07月06日 18:37:00
 */
public class DatasetResult extends DatasetList implements QyDataset {
    private List names = new ArrayList();
    private int count;

    public DatasetResult() throws Exception {
    }

    public DatasetResult(ResultSet rs) throws Exception {
        while(rs.next()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            QyData data = new DataMap();

            for(int i = 1; i <= rsmd.getColumnCount(); ++i) {
                String name = rsmd.getColumnName(i).toUpperCase();
                data.put(name, getValueByResultSet(rs, rsmd.getColumnType(i), name));
                if (rs.isFirst()) {
                    this.names.add(name);
                }
            }

            this.add(data);
        }

        this.count = this.size();
    }

    public String[] getNames() {
        return (String[])((String[])this.names.toArray(new String[0]));
    }

    public int count() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void clear() {
        super.clear();
        this.names = new ArrayList();
        this.count = 0;
    }

    public static Object getValueByResultSet(ResultSet rs, int type, String name) throws Exception {
        return type == 2004 ? rs.getBlob(name) : rs.getString(name);
    }
}
