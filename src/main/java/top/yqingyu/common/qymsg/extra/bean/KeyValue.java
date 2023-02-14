package top.yqingyu.common.qymsg.extra.bean;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qymsg.extra.bean.KeyValue
 * @description
 * @createTime 2023年02月14日 22:57:00
 */
public class KeyValue {


    private OperatingState operatingState;

    private String key;

    private DataType dataType;

    public KeyValue() {
    }

    public KeyValue(DataType dataType) {
        this.dataType = dataType;
    }


    public DataType getDataType() {
        return dataType;
    }

    public OperatingState getOperatingState() {
        return operatingState;
    }

    public void setOperatingState(OperatingState operatingState) {
        this.operatingState = operatingState;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public enum DataType {
        STRING("String"),
        SET("Set"),
        QUEUE("Queue"),
        STACK("Stack"),
        OTHER("other");

        private final String name;

        DataType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum OperatingState {
        ADD,
        REMOVE,
        GET,
        CREATE,
        POP,

    }
}
