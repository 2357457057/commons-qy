package top.yqingyu.common.qydata;

import lombok.extern.slf4j.Slf4j;



/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.IData.BaseData
 * @Description 偷来
 * @createTime 2022年07月06日 17:58:00
 */
@Slf4j
@SuppressWarnings("all")
public class BaseData {
    private QyData currentData = new DataMap();


    public BaseData() throws Exception {
    }


    public String getParameter(String name) throws Exception {
        return (String)this.currentData.get(name);
    }



    public QyData getData(String group, boolean istrim) throws Exception {
        QyData element = new DataMap();
        String[] names = this.currentData.getNames();

        for(int i = 0; i < names.length; ++i) {
            if (names[i].startsWith(group + "_")) {
                element.put(istrim ? names[i].substring((group + "_").length()) : names[i], this.currentData.get(names[i]));
            }
        }

        return element;
    }

    public QyData getData() throws Exception {
        return this.currentData;
    }

    public void setData(QyData data) throws Exception {
        this.currentData = data;
    }

    public void setData(QyData data, String group) throws Exception {
        QyData element = new DataMap();
        String[] names = data.getNames();

        for(int i = 0; i < names.length; ++i) {
            element.put(group + "_" + names[i], data.get(names[i]));
        }

        this.currentData = element;
    }

}
