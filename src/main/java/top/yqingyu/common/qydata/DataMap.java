package top.yqingyu.common.qydata;


import com.alibaba.fastjson2.*;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.schema.JSONSchema;
import top.yqingyu.common.utils.UnitUtil;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * @author YYJ
 * @version 1.0.1
 * @ClassName top.yqingyu.common.qydata.DataMap
 * @Description 彻底重构
 * @createTime 2022年07月06日 18:03:00
 */
@SuppressWarnings("All")
public class DataMap extends JSONObject implements Serializable, Cloneable {


    public DataMap() {
        super();
    }

    public DataMap(String json) {
        super();
        this.putAll(JSON.parseObject(json, DataMap.class));
    }

    public DataMap(Map map) {
        super(map);
    }

    public DataMap(List<?> list) {
        super();
        if (list != null && list.size() > 0)
            for (int i = 0; i < list.size(); i++) {
                this.put(String.valueOf(i), list.get(i));
            }
    }

    public DataMap(int initialCapacity) {
        super(initialCapacity);
    }


    /**
     * @param initialCapacity the initial capacity = (number of elements to store / load factor) + 1
     * @param loadFactor      the load factor
     * @throws IllegalArgumentException If the initial capacity is negative or the load factor is negative
     * @since 2.0.2
     */
    public DataMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * @param initialCapacity the initial capacity = (number of elements to store / load factor) + 1
     * @param loadFactor      the load factor
     * @param accessOrder     the ordering mode - true for access-order, false for insertion-order
     * @throws IllegalArgumentException If the initial capacity is negative or the load factor is negative
     * @since 2.0.2
     */
    public DataMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    /**
     * Returns the Object of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     */
    @Override
    public Object get(String key) {
        return super.get(key);
    }

    /**
     * Returns the Object of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @since 2.0.2
     */
    @Override
    public Object get(Object key) {
        return super.get(key);
    }

    /**
     * Returns true if this map contains a mapping for the specified key
     *
     * @param key the key whose presence in this map is to be tested
     */
    @Override
    public boolean containsKey(String key) {
        return super.containsKey(key);
    }

    /**
     * Returns true if this map contains a mapping for the specified key
     *
     * @param key the key whose presence in this map is to be tested
     */
    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    /**
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     */
    @Override
    public Object getOrDefault(String key, Object defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }

    /**
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @since 2.0.2
     */
    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }

    /**
     * Returns the {@link DataList} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link DataList} or null
     */
    public DataList getDataList(String key) {
        Object value = this.get(key);

        if (value instanceof JSONArray) {
            return new DataList((JSONArray) value);
        }

        if (value instanceof String) {
            String str = (String) value;

            if (str.isEmpty() || "null".equalsIgnoreCase(str)) {
                return null;
            }
            DataList objects = new DataList();
            objects.add(str);
            return objects;
        }

        if (value instanceof Collection) {
            return new DataList((Collection<?>) value);
        }

        if (value instanceof Object[]) {
            return new DataList(value);
        }

        if (value == null) {
            return null;
        }

        Class<?> valueClass = value.getClass();
        if (valueClass.isArray()) {
            int length = Array.getLength(value);
            DataList jsonArray = new DataList(length);
            for (int i = 0; i < length; i++) {
                Object item = Array.get(value, i);
                jsonArray.add(item);
            }
            return jsonArray;
        }
        return null;
    }


    /**
     * @param key       key
     * @param itemClass 类型
     * @param features  选项
     * @param <T>       泛型
     * @return List
     * @description 获取list
     */
    @Override
    public <T> List<T> getList(String key, Class<T> itemClass, JSONReader.Feature... features) {
        return super.getList(key, itemClass, features);
    }

    /**
     * Returns the {@link DataMap} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link DataMap} or null
     */
    public DataMap getDataMap(String key) {
        Object o = this.get(key);

        DataMap data = null;
        if (o instanceof JSONObject) {

            data = new DataMap();
            data.putAll((JSONObject) o);
            this.put(key, data);
        } else if (o instanceof JSONArray) {

            data = new DataMap((JSONArray) o);
        } else if (o instanceof Map<?, ?>) {

            data = new DataMap();
            data.putAll((Map) o);
            this.put(key, data);
        } else if (o instanceof List<?>) {

            data = new DataMap((List) o);
        } else {
            return null;
        }
        return data;
    }

    /**
     * Returns the {@link String} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link String} or null
     */
    @Override
    public String getString(String key) {
        return super.getString(key);
    }


    public Long $2B(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2B(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2KB(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2KB(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2MB(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2MB(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2GB(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2GB(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2TB(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2TB(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2YEAR(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2YEAR(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2QUARTER(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2QUARTER(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2MONTH(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2MONTH(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2WEEK(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2WEEK(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2DAY(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2DAY(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2H(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2H(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2MIN(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2MIN(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2S(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2S(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2MILLS(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2MILLS(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2MICOS(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2MICOS(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public Long $2NANOS(String name, Long defaultValue) {
        Long aLong = UnitUtil.$2NANOS(this.getString(name));
        if (aLong == null || aLong.longValue() == 0)
            return defaultValue;
        return aLong;
    }

    public String getString2(String name) {
        Object value = this.get(name);
        return value == null ? "" : value.toString();
    }

    public String getString(String key, String defaultValue) {
        String string = super.getString(key);
        return string == null ? defaultValue : string;
    }

    /**
     * Returns the {@link Double} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link Double} or null
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable double
     * @throws JSONException         Unsupported type conversion to {@link Double}
     */
    @Override
    public Double getDouble(String key) {
        return super.getDouble(key);
    }

    /**
     * Returns a double value of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return double
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable double
     * @throws JSONException         Unsupported type conversion to double value
     */
    @Override
    public double getDoubleValue(String key) {
        return super.getDoubleValue(key);
    }

    /**
     * Returns the {@link Float} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link Float} or null
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable float
     * @throws JSONException         Unsupported type conversion to {@link Float}
     */
    @Override
    public Float getFloat(String key) {
        return super.getFloat(key);
    }

    /**
     * Returns a float value of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return float
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable float
     * @throws JSONException         Unsupported type conversion to float value
     */
    @Override
    public float getFloatValue(String key) {
        return super.getFloatValue(key);
    }

    /**
     * Returns the {@link Long} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link Long} or null
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable long
     * @throws JSONException         Unsupported type conversion to {@link Long}
     */
    @Override
    public Long getLong(String key) {
        return super.getLong(key);
    }

    /**
     * Returns a long value of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return long
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable long
     * @throws JSONException         Unsupported type conversion to long value
     */
    @Override
    public long getLongValue(String key) {
        return super.getLongValue(key);
    }

    /**
     * Returns a long value of the associated keys in this {@link DataMap}.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return long
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable long
     * @throws JSONException         Unsupported type conversion to long value
     */
    @Override
    public long getLongValue(String key, long defaultValue) {
        return super.getLongValue(key, defaultValue);
    }

    /**
     * Returns the {@link Integer} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link Integer} or null
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable int
     * @throws JSONException         Unsupported type conversion to {@link Integer}
     */
    @Override
    public Integer getInteger(String key) {
        return super.getInteger(key);
    }

    /**
     * Returns an int value of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return int
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable int
     * @throws JSONException         Unsupported type conversion to int value
     */
    @Override
    public int getIntValue(String key) {
        return super.getIntValue(key);
    }

    /**
     * Returns an int value of the associated keys in this {@link DataMap}.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return int
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable int
     * @throws JSONException         Unsupported type conversion to int value
     */
    @Override
    public int getIntValue(String key, int defaultValue) {
        return super.getIntValue(key, defaultValue);
    }

    /**
     * Returns the {@link Short} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link Short} or null
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable short
     * @throws JSONException         Unsupported type conversion to {@link Short}
     */
    @Override
    public Short getShort(String key) {
        return super.getShort(key);
    }

    /**
     * Returns a short value of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return short
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable short
     * @throws JSONException         Unsupported type conversion to short value
     */
    @Override
    public short getShortValue(String key) {
        return super.getShortValue(key);
    }

    /**
     * Returns the {@link Byte} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link Byte} or null
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable byte
     * @throws JSONException         Unsupported type conversion to {@link Byte}
     */
    @Override
    public Byte getByte(String key) {
        return super.getByte(key);
    }

    /**
     * Returns a byte value of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return byte
     * @throws NumberFormatException If the value of get is {@link String} and it contains no parsable byte
     * @throws JSONException         Unsupported type conversion to byte value
     */
    @Override
    public byte getByteValue(String key) {
        return super.getByteValue(key);
    }

    /**
     * Returns the {@link Boolean} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link Boolean} or null
     * @throws JSONException Unsupported type conversion to {@link Boolean}
     */
    @Override
    public Boolean getBoolean(String key) {
        return super.getBoolean(key);
    }

    /**
     * Returns the {@link Boolean} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link Boolean} or default vaule
     * @throws JSONException Unsupported type conversion to {@link Boolean}
     */
    public Boolean getBoolean(String key,Boolean defaultValue) {
        Boolean aBoolean = super.getBoolean(key);
        return aBoolean == null? defaultValue : aBoolean;
    }


    /**
     * Returns a boolean value of the associated key in this object.
     *
     * @param key the key whose associated value is to be returned
     * @return boolean
     * @throws JSONException Unsupported type conversion to boolean value
     */
    @Override
    public boolean getBooleanValue(String key) {
        return super.getBooleanValue(key);
    }

    /**
     * Returns a boolean value of the associated key in this object.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return boolean
     * @throws JSONException Unsupported type conversion to boolean value
     */
    @Override
    public boolean getBooleanValue(String key, boolean defaultValue) {
        return super.getBooleanValue(key, defaultValue);
    }

    /**
     * Returns the {@link BigInteger} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link BigInteger} or null
     * @throws JSONException         Unsupported type conversion to {@link BigInteger}
     * @throws NumberFormatException If the value of get is {@link String} and it is not a valid representation of {@link BigInteger}
     */
    @Override
    public BigInteger getBigInteger(String key) {
        return super.getBigInteger(key);
    }

    /**
     * Returns the {@link BigDecimal} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link BigDecimal} or null
     * @throws JSONException         Unsupported type conversion to {@link BigDecimal}
     * @throws NumberFormatException If the value of get is {@link String} and it is not a valid representation of {@link BigDecimal}
     */
    @Override
    public BigDecimal getBigDecimal(String key) {
        return super.getBigDecimal(key);
    }

    /**
     * Returns the {@link Date} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link Date} or null
     */
    @Override
    public Date getDate(String key) {
        return super.getDate(key);
    }

    /**
     * Returns the {@link BigInteger} of the associated keys in this {@link DataMap}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link BigInteger} or null
     */
    @Override
    public Instant getInstant(String key) {
        return super.getInstant(key);
    }

    /**
     * Serialize to JSON {@link String}
     *
     * @param features features to be enabled in serialization
     * @return JSON {@link String}
     */
    @Override
    public String toString(JSONWriter.Feature... features) {
        return super.toString(features);
    }

    /**
     * Serialize to JSON {@link String}
     *
     * @param features features to be enabled in serialization
     * @return JSON {@link String}
     */
    @Override
    public String toJSONString(JSONWriter.Feature... features) {
        return super.toJSONString(features);
    }

    /**
     * Serialize to JSONB bytes
     *
     * @param features features to be enabled in serialization
     * @return JSONB bytes
     */
    @Override
    public byte[] toJSONBBytes(JSONWriter.Feature... features) {
        return super.toJSONBBytes(features);
    }

    /**
     * @param function
     * @since 2.0.4
     */
    @Override
    public <T> T to(Function<JSONObject, T> function) {
        return super.to(function);
    }

    /**
     * Convert this {@link DataMap} to the specified Object
     *
     * <pre>{@code
     * DataMap obj = ...
     * Map<String, User> users = obj.to(new TypeReference<HashMap<String, User>>(){}.getType());
     * }</pre>
     *
     * @param type     specify the {@link Type} to be converted
     * @param features features to be enabled in parsing
     * @since 2.0.4
     */
    @Override
    public <T> T to(Type type, JSONReader.Feature... features) {
        return super.to(type, features);
    }

    /**
     * Convert this {@link DataMap} to the specified Object
     *
     * <pre>{@code
     * DataMap obj = ...
     * Map<String, User> users = obj.to(new TypeReference<HashMap<String, User>>(){});
     * }</pre>
     *
     * @param typeReference specify the {@link TypeReference} to be converted
     * @param features      features to be enabled in parsing
     * @since 2.0.7
     */
    @Override
    public <T> T to(TypeReference<?> typeReference, JSONReader.Feature... features) {
        return super.to(typeReference, features);
    }

    /**
     * Convert this {@link DataMap} to the specified Object
     *
     * <pre>{@code
     * DataMap obj = ...
     * User user = obj.to(User.class);
     * }</pre>
     *
     * @param clazz    specify the {@code Class<T>} to be converted
     * @param features features to be enabled in parsing
     * @since 2.0.4
     */
    @Override
    public <T> T to(Class<T> clazz, JSONReader.Feature... features) {
        return super.to(clazz, features);
    }

    /**
     * Convert this {@link DataMap} to the specified Object
     *
     * @param clazz    specify the {@code Class<T>} to be converted
     * @param features features to be enabled in parsing
     * @deprecated since 2.0.4, please use {@link #to(Class, JSONReader.Feature...)}
     */
    @Override
    public <T> T toJavaObject(Class<T> clazz, JSONReader.Feature... features) {
        return super.toJavaObject(clazz, features);
    }

    /**
     * Convert this {@link DataMap} to the specified Object
     *
     * @param type     specify the {@link Type} to be converted
     * @param features features to be enabled in parsing
     * @deprecated since 2.0.4, please use {@link #to(Type, JSONReader.Feature...)}
     */
    @Override
    public <T> T toJavaObject(Type type, JSONReader.Feature... features) {
        return super.toJavaObject(type, features);
    }

    /**
     * Convert this {@link DataMap} to the specified Object
     *
     * @param typeReference specify the {@link TypeReference} to be converted
     * @param features      features to be enabled in parsing
     * @deprecated since 2.0.4, please use {@link #to(Type, JSONReader.Feature...)}
     */
    @Override
    public <T> T toJavaObject(TypeReference<?> typeReference, JSONReader.Feature... features) {
        return super.toJavaObject(typeReference, features);
    }

    /**
     * Returns the result of the {@link Type} converter conversion of the associated value in this {@link DataMap}.
     * <p>
     * {@code User user = DataMap.getObject("user", User.class);}
     *
     * @param key      the key whose associated value is to be returned
     * @param type     specify the {@link Class} to be converted
     * @param features
     * @return {@code <T>} or null
     * @throws JSONException If no suitable conversion method is found
     */
    @Override
    public <T> T getObject(String key, Class<T> type, JSONReader.Feature... features) {
        return super.getObject(key, type, features);
    }

    /**
     * Returns the result of the {@link Type} converter conversion of the associated value in this {@link DataMap}.
     * <p>
     * {@code User user = DataMap.getObject("user", User.class);}
     *
     * @param key      the key whose associated value is to be returned
     * @param type     specify the {@link Type} to be converted
     * @param features features to be enabled in parsing
     * @return {@code <T>} or {@code null}
     * @throws JSONException If no suitable conversion method is found
     */
    @Override
    public <T> T getObject(String key, Type type, JSONReader.Feature... features) {
        return super.getObject(key, type, features);
    }

    /**
     * Returns the result of the {@link Type} converter conversion of the associated value in this {@link DataMap}.
     * <p>
     * {@code User user = DataMap.getObject("user", User.class);}
     *
     * @param key           the key whose associated value is to be returned
     * @param typeReference specify the {@link TypeReference} to be converted
     * @param features      features to be enabled in parsing
     * @return {@code <T>} or {@code null}
     * @throws JSONException If no suitable conversion method is found
     * @since 2.0.3
     */
    @Override
    public <T> T getObject(String key, TypeReference<?> typeReference, JSONReader.Feature... features) {
        return super.getObject(key, typeReference, features);
    }

    /**
     * @param key
     * @param creator
     * @since 2.0.4
     */
    @Override
    public <T> T getObject(String key, Function<JSONObject, T> creator) {
        return super.getObject(key, creator);
    }

    /**
     * @param proxy  proxy object, currently useless
     * @param method methods that need reflection
     * @param args   parameters of invoke
     * @throws UnsupportedOperationException  If reflection for this method is not supported
     * @throws ArrayIndexOutOfBoundsException If the length of args does not match the length of the method parameter
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return super.invoke(proxy, method, args);
    }

    /**
     * Chained addition of elements
     *
     * <pre>
     * DataMap object = new DataMap().fluentPut("a", 1).fluentPut("b", 2).fluentPut("c", 3);
     * </pre>
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     */
    @Override
    public DataMap fluentPut(String key, Object value) {
        return (DataMap) super.fluentPut(key, value);
    }

    /**
     * @param schema
     * @since 2.0.4
     */
    @Override
    public boolean isValid(JSONSchema schema) {
        return super.isValid(schema);
    }

    /**
     * @param valueFilter
     * @since 2.0.3
     */
    @Override
    public void valueFilter(ValueFilter valueFilter) {
        super.valueFilter(valueFilter);
    }

    /**
     * @param nameFilter
     * @since 2.0.3
     */
    @Override
    public void nameFilter(NameFilter nameFilter) {
        super.nameFilter(nameFilter);
    }

    /**
     * @param path
     * @return
     */
    @Override
    public Object eval(JSONPath path) {
        return super.eval(path);
    }

    /**
     * Returns {@code true} if this map maps one or more keys to the
     * specified value.
     *
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the
     * specified value
     */
    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        super.clear();
    }

    /**
     * Returns {@code true} if this map should remove its eldest entry.
     * This method is invoked by {@code put} and {@code putAll} after
     * inserting a new entry into the map.  It provides the implementor
     * with the opportunity to remove the eldest entry each time a new one
     * is added.  This is useful if the map represents a cache: it allows
     * the map to reduce memory consumption by deleting stale entries.
     *
     * <p>Sample use: this override will allow the map to grow up to 100
     * entries and then delete the eldest entry each time a new entry is
     * added, maintaining a steady state of 100 entries.
     * <pre>
     *     private static final int MAX_ENTRIES = 100;
     *
     *     protected boolean removeEldestEntry(Map.Entry eldest) {
     *        return size() &gt; MAX_ENTRIES;
     *     }
     * </pre>
     *
     * <p>This method typically does not modify the map in any way,
     * instead allowing the map to modify itself as directed by its
     * return value.  It <i>is</i> permitted for this method to modify
     * the map directly, but if it does so, it <i>must</i> return
     * {@code false} (indicating that the map should not attempt any
     * further modification).  The effects of returning {@code true}
     * after modifying the map from within this method are unspecified.
     *
     * <p>This implementation merely returns {@code false} (so that this
     * map acts like a normal map - the eldest element is never removed).
     *
     * @param eldest The least recently inserted entry in the map, or if
     *               this is an access-ordered map, the least recently accessed
     *               entry.  This is the entry that will be removed it this
     *               method returns {@code true}.  If the map was empty prior
     *               to the {@code put} or {@code putAll} invocation resulting
     *               in this invocation, this will be the entry that was just
     *               inserted; in other words, if the map contains a single
     *               entry, the eldest entry is also the newest.
     * @return {@code true} if the eldest entry should be removed
     * from the map; {@code false} if it should be retained.
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
        return super.removeEldestEntry(eldest);
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation), the results of
     * the iteration are undefined.  The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * {@code Iterator.remove}, {@code Set.remove},
     * {@code removeAll}, {@code retainAll}, and {@code clear}
     * operations.  It does not support the {@code add} or {@code addAll}
     * operations.
     * Its {@link Spliterator} typically provides faster sequential
     * performance but much poorer parallel performance than that of
     * {@code HashMap}.
     *
     * @return a set view of the keys contained in this map
     */
    @Override
    public Set<String> keySet() {
        return super.keySet();
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own {@code remove} operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Collection.remove}, {@code removeAll},
     * {@code retainAll} and {@code clear} operations.  It does not
     * support the {@code add} or {@code addAll} operations.
     * Its {@link Spliterator} typically provides faster sequential
     * performance but much poorer parallel performance than that of
     * {@code HashMap}.
     *
     * @return a view of the values contained in this map
     */
    @Override
    public Collection<Object> values() {
        return super.values();
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own {@code remove} operation, or through the
     * {@code setValue} operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the {@code Iterator.remove},
     * {@code Set.remove}, {@code removeAll}, {@code retainAll} and
     * {@code clear} operations.  It does not support the
     * {@code add} or {@code addAll} operations.
     * Its {@link Spliterator} typically provides faster sequential
     * performance but much poorer parallel performance than that of
     * {@code HashMap}.
     *
     * @return a set view of the mappings contained in this map
     */
    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        return super.entrySet();
    }

    /**
     * @param action The action to be performed for each entry
     */
    @Override
    public void forEach(BiConsumer<? super String, ? super Object> action) {
        super.forEach(action);
    }

    /**
     * @param function the function to apply to each entry
     */
    @Override
    public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {
        super.replaceAll(function);
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return super.size();
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with {@code key}, or
     * {@code null} if there was no mapping for {@code key}.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with {@code key}.)
     */
    @Override
    public Object remove(Object key) {
        return super.remove(key);
    }

    /**
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return
     */
    @Override
    public Object putIfAbsent(String key, Object value) {
        return super.putIfAbsent(key, value);
    }

    /**
     * @param key   key with which the specified value is associated
     * @param value value expected to be associated with the specified key
     * @return
     */
    @Override
    public boolean remove(Object key, Object value) {
        return super.remove(key, value);
    }

    /**
     * @param key      key with which the specified value is associated
     * @param oldValue value expected to be associated with the specified key
     * @param newValue value to be associated with the specified key
     * @return
     */
    @Override
    public boolean replace(String key, Object oldValue, Object newValue) {
        return super.replace(key, oldValue, newValue);
    }

    /**
     * @param key   key with which the specified value is associated
     * @param value value to be associated with the specified key
     * @return
     */
    @Override
    public Object replace(String key, Object value) {
        return super.replace(key, value);
    }

    /**
     * {@inheritDoc}
     *
     * <p>This method will, on a best-effort basis, throw a
     * {@link ConcurrentModificationException} if it is detected that the
     * mapping function modifies this map during computation.
     *
     * @param key
     * @param mappingFunction
     * @throws ConcurrentModificationException if it is detected that the
     *                                         mapping function modified this map
     */
    @Override
    public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
        return super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * {@inheritDoc}
     *
     * <p>This method will, on a best-effort basis, throw a
     * {@link ConcurrentModificationException} if it is detected that the
     * remapping function modifies this map during computation.
     *
     * @param key
     * @param remappingFunction
     * @throws ConcurrentModificationException if it is detected that the
     *                                         remapping function modified this map
     */
    @Override
    public Object computeIfPresent(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        return super.computeIfPresent(key, remappingFunction);
    }

    /**
     * {@inheritDoc}
     *
     * <p>This method will, on a best-effort basis, throw a
     * {@link ConcurrentModificationException} if it is detected that the
     * remapping function modifies this map during computation.
     *
     * @param key
     * @param remappingFunction
     * @throws ConcurrentModificationException if it is detected that the
     *                                         remapping function modified this map
     */
    @Override
    public Object compute(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        return super.compute(key, remappingFunction);
    }

    /**
     * Returns the {@link JSONArray} of the associated keys in this {@link JSONObject}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link JSONArray} or null
     */
    @Override
    public JSONArray getJSONArray(String key) {
        return super.getJSONArray(key);
    }

    /**
     * Returns the {@link JSONObject} of the associated keys in this {@link JSONObject}.
     *
     * @param key the key whose associated value is to be returned
     * @return {@link JSONObject} or null
     */
    @Override
    public JSONObject getJSONObject(String key) {
        return super.getJSONObject(key);
    }

    /**
     * {@inheritDoc}
     *
     * <p>This method will, on a best-effort basis, throw a
     * {@link ConcurrentModificationException} if it is detected that the
     * remapping function modifies this map during computation.
     *
     * @param key
     * @param value
     * @param remappingFunction
     * @throws ConcurrentModificationException if it is detected that the
     *                                         remapping function modified this map
     */
    @Override
    public Object merge(String key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        return super.merge(key, value, remappingFunction);
    }

    /**
     * @param name 时间单位参数
     * @return 返回通用时间单位
     * @author YYJ
     * @version 1.0.0
     * @description 获取数据中的时间单位, 不区分大小写
     */
    public TimeUnit getTimeUnit(String name) {
        String value = this.getString(name, "");
        if ("NANOSECONDS".equals(value.toUpperCase()))
            return TimeUnit.NANOSECONDS;

        if ("MICROSECONDS".equals(value.toUpperCase()))
            return TimeUnit.MICROSECONDS;

        if ("MILLISECONDS".equals(value.toUpperCase()))
            return TimeUnit.MILLISECONDS;

        if ("SECONDS".equals(value.toUpperCase()))
            return TimeUnit.SECONDS;

        if ("MINUTES".equals(value.toUpperCase()))
            return TimeUnit.MINUTES;

        if ("HOURS".equals(value.toUpperCase()))
            return TimeUnit.HOURS;

        if ("DAYS".equals(value.toUpperCase()))
            return TimeUnit.DAYS;

        return null;
    }

    /**
     * @param name         时间单位参数
     * @param defaultValue 默认值
     * @return 返回通用时间单位
     * @author YYJ
     * @version 1.0.0
     * @description 获取数据中的时间单位, 不区分大小写
     */
    public TimeUnit getTimeUnit(String name, TimeUnit defaultValue) {
        String value = this.getString(name, "");
        if ("NANOSECONDS".equals(value.toUpperCase()))
            return TimeUnit.NANOSECONDS;

        if ("MICROSECONDS".equals(value.toUpperCase()))
            return TimeUnit.MICROSECONDS;

        if ("SECONDS".equals(value.toUpperCase()))
            return TimeUnit.SECONDS;

        if ("MINUTES".equals(value.toUpperCase()))
            return TimeUnit.MINUTES;

        if ("HOURS".equals(value.toUpperCase()))
            return TimeUnit.HOURS;

        if ("DAYS".equals(value.toUpperCase()))
            return TimeUnit.DAYS;

        if ("MILLISECONDS".equals(value.toUpperCase()))
            return TimeUnit.MILLISECONDS;

        return defaultValue;

    }


    public Charset getCharSet(String name) {
        String charset = this.getString2(name);
        charset = charset.toUpperCase();
        try {
            switch (charset) {
                case "BIG5":
                    return Charset.forName("Big5");
                case "BIG5-HKSCS":
                    return Charset.forName("Big5-HKSCS");
                case "CESU-8":
                    return Charset.forName("CESU-8");
                case "EUC-JP":
                    return Charset.forName("EUC-JP");
                case "EUC-KR":
                    return Charset.forName("EUC-KR");
                case "GB18030":
                    return Charset.forName("GB18030");
                case "GB2312":
                    return Charset.forName("GB2312");
                case "GBK":
                    return Charset.forName("GBK");
                case "IBM-THAI":
                    return Charset.forName("IBM-Thai");
                case "IBM00858":
                    return Charset.forName("IBM00858");
                case "IBM01140":
                    return Charset.forName("IBM01140");
                case "IBM01141":
                    return Charset.forName("IBM01141");
                case "IBM01142":
                    return Charset.forName("IBM01142");
                case "IBM01143":
                    return Charset.forName("IBM01143");
                case "IBM01144":
                    return Charset.forName("IBM01144");
                case "IBM01145":
                    return Charset.forName("IBM01145");
                case "IBM01146":
                    return Charset.forName("IBM01146");
                case "IBM01147":
                    return Charset.forName("IBM01147");
                case "IBM01148":
                    return Charset.forName("IBM01148");
                case "IBM01149":
                    return Charset.forName("IBM01149");
                case "IBM037":
                    return Charset.forName("IBM037");
                case "IBM1026":
                    return Charset.forName("IBM1026");
                case "IBM1047":
                    return Charset.forName("IBM1047");
                case "IBM273":
                    return Charset.forName("IBM273");
                case "IBM277":
                    return Charset.forName("IBM277");
                case "IBM278":
                    return Charset.forName("IBM278");
                case "IBM280":
                    return Charset.forName("IBM280");
                case "IBM284":
                    return Charset.forName("IBM284");
                case "IBM285":
                    return Charset.forName("IBM285");
                case "IBM290":
                    return Charset.forName("IBM290");
                case "IBM297":
                    return Charset.forName("IBM297");
                case "IBM420":
                    return Charset.forName("IBM420");
                case "IBM424":
                    return Charset.forName("IBM424");
                case "IBM437":
                    return Charset.forName("IBM437");
                case "IBM500":
                    return Charset.forName("IBM500");
                case "IBM775":
                    return Charset.forName("IBM775");
                case "IBM850":
                    return Charset.forName("IBM850");
                case "IBM852":
                    return Charset.forName("IBM852");
                case "IBM855":
                    return Charset.forName("IBM855");
                case "IBM857":
                    return Charset.forName("IBM857");
                case "IBM860":
                    return Charset.forName("IBM860");
                case "IBM861":
                    return Charset.forName("IBM861");
                case "IBM862":
                    return Charset.forName("IBM862");
                case "IBM863":
                    return Charset.forName("IBM863");
                case "IBM864":
                    return Charset.forName("IBM864");
                case "IBM865":
                    return Charset.forName("IBM865");
                case "IBM866":
                    return Charset.forName("IBM866");
                case "IBM868":
                    return Charset.forName("IBM868");
                case "IBM869":
                    return Charset.forName("IBM869");
                case "IBM870":
                    return Charset.forName("IBM870");
                case "IBM871":
                    return Charset.forName("IBM871");
                case "IBM918":
                    return Charset.forName("IBM918");
                case "ISO-2022-CN":
                    return Charset.forName("ISO-2022-CN");
                case "ISO-2022-JP":
                    return Charset.forName("ISO-2022-JP");
                case "ISO-2022-JP-2":
                    return Charset.forName("ISO-2022-JP-2");
                case "ISO-2022-KR":
                    return Charset.forName("ISO-2022-KR");
                case "ISO-8859-1":
                    return Charset.forName("ISO-8859-1");
                case "ISO-8859-13":
                    return Charset.forName("ISO-8859-13");
                case "ISO-8859-15":
                    return Charset.forName("ISO-8859-15");
                case "ISO-8859-16":
                    return Charset.forName("ISO-8859-16");
                case "ISO-8859-2":
                    return Charset.forName("ISO-8859-2");
                case "ISO-8859-3":
                    return Charset.forName("ISO-8859-3");
                case "ISO-8859-4":
                    return Charset.forName("ISO-8859-4");
                case "ISO-8859-5":
                    return Charset.forName("ISO-8859-5");
                case "ISO-8859-6":
                    return Charset.forName("ISO-8859-6");
                case "ISO-8859-7":
                    return Charset.forName("ISO-8859-7");
                case "ISO-8859-8":
                    return Charset.forName("ISO-8859-8");
                case "ISO-8859-9":
                    return Charset.forName("ISO-8859-9");
                case "JIS_X0201":
                    return Charset.forName("JIS_X0201");
                case "JIS_X0212-1990":
                    return Charset.forName("JIS_X0212-1990");
                case "KOI8-R":
                    return Charset.forName("KOI8-R");
                case "KOI8-U":
                    return Charset.forName("KOI8-U");
                case "SHIFT_JIS":
                    return Charset.forName("Shift_JIS");
                case "TIS-620":
                    return Charset.forName("TIS-620");
                case "US-ASCII":
                    return Charset.forName("US-ASCII");
                case "UTF-16":
                    return Charset.forName("UTF-16");
                case "UTF-16BE":
                    return Charset.forName("UTF-16BE");
                case "UTF-16LE":
                    return Charset.forName("UTF-16LE");
                case "UTF-32":
                    return Charset.forName("UTF-32");
                case "UTF-32BE":
                    return Charset.forName("UTF-32BE");
                case "UTF-32LE":
                    return Charset.forName("UTF-32LE");
                case "UTF-8":
                    return Charset.forName("UTF-8");
                case "WINDOWS-1250":
                    return Charset.forName("windows-1250");
                case "WINDOWS-1251":
                    return Charset.forName("windows-1251");
                case "WINDOWS-1252":
                    return Charset.forName("windows-1252");
                case "WINDOWS-1253":
                    return Charset.forName("windows-1253");
                case "WINDOWS-1254":
                    return Charset.forName("windows-1254");
                case "WINDOWS-1255":
                    return Charset.forName("windows-1255");
                case "WINDOWS-1256":
                    return Charset.forName("windows-1256");
                case "WINDOWS-1257":
                    return Charset.forName("windows-1257");
                case "WINDOWS-1258":
                    return Charset.forName("windows-1258");
                case "WINDOWS-31J":
                    return Charset.forName("windows-31j");
                case "X-BIG5-HKSCS-2001":
                    return Charset.forName("x-Big5-HKSCS-2001");
                case "X-BIG5-SOLARIS":
                    return Charset.forName("x-Big5-Solaris");
                case "X-EUC-JP-LINUX":
                    return Charset.forName("x-euc-jp-linux");
                case "X-EUC-TW":
                    return Charset.forName("x-EUC-TW");
                case "X-EUCJP-OPEN":
                    return Charset.forName("x-eucJP-Open");
                case "X-IBM1006":
                    return Charset.forName("x-IBM1006");
                case "X-IBM1025":
                    return Charset.forName("x-IBM1025");
                case "X-IBM1046":
                    return Charset.forName("x-IBM1046");
                case "X-IBM1097":
                    return Charset.forName("x-IBM1097");
                case "X-IBM1098":
                    return Charset.forName("x-IBM1098");
                case "X-IBM1112":
                    return Charset.forName("x-IBM1112");
                case "X-IBM1122":
                    return Charset.forName("x-IBM1122");
                case "X-IBM1123":
                    return Charset.forName("x-IBM1123");
                case "X-IBM1124":
                    return Charset.forName("x-IBM1124");
                case "X-IBM1129":
                    return Charset.forName("x-IBM1129");
                case "X-IBM1166":
                    return Charset.forName("x-IBM1166");
                case "X-IBM1364":
                    return Charset.forName("x-IBM1364");
                case "X-IBM1381":
                    return Charset.forName("x-IBM1381");
                case "X-IBM1383":
                    return Charset.forName("x-IBM1383");
                case "X-IBM29626C":
                    return Charset.forName("x-IBM29626C");
                case "X-IBM300":
                    return Charset.forName("x-IBM300");
                case "X-IBM33722":
                    return Charset.forName("x-IBM33722");
                case "X-IBM737":
                    return Charset.forName("x-IBM737");
                case "X-IBM833":
                    return Charset.forName("x-IBM833");
                case "X-IBM834":
                    return Charset.forName("x-IBM834");
                case "X-IBM856":
                    return Charset.forName("x-IBM856");
                case "X-IBM874":
                    return Charset.forName("x-IBM874");
                case "X-IBM875":
                    return Charset.forName("x-IBM875");
                case "X-IBM921":
                    return Charset.forName("x-IBM921");
                case "X-IBM922":
                    return Charset.forName("x-IBM922");
                case "X-IBM930":
                    return Charset.forName("x-IBM930");
                case "X-IBM933":
                    return Charset.forName("x-IBM933");
                case "X-IBM935":
                    return Charset.forName("x-IBM935");
                case "X-IBM937":
                    return Charset.forName("x-IBM937");
                case "X-IBM939":
                    return Charset.forName("x-IBM939");
                case "X-IBM942":
                    return Charset.forName("x-IBM942");
                case "X-IBM942C":
                    return Charset.forName("x-IBM942C");
                case "X-IBM943":
                    return Charset.forName("x-IBM943");
                case "X-IBM943C":
                    return Charset.forName("x-IBM943C");
                case "X-IBM948":
                    return Charset.forName("x-IBM948");
                case "X-IBM949":
                    return Charset.forName("x-IBM949");
                case "X-IBM949C":
                    return Charset.forName("x-IBM949C");
                case "X-IBM950":
                    return Charset.forName("x-IBM950");
                case "X-IBM964":
                    return Charset.forName("x-IBM964");
                case "X-IBM970":
                    return Charset.forName("x-IBM970");
                case "X-ISCII91":
                    return Charset.forName("x-ISCII91");
                case "X-ISO-2022-CN-CNS":
                    return Charset.forName("x-ISO-2022-CN-CNS");
                case "X-ISO-2022-CN-GB":
                    return Charset.forName("x-ISO-2022-CN-GB");
                case "X-ISO-8859-11":
                    return Charset.forName("x-iso-8859-11");
                case "X-JIS0208":
                    return Charset.forName("x-JIS0208");
                case "X-JISAUTODETECT":
                    return Charset.forName("x-JISAutoDetect");
                case "X-JOHAB":
                    return Charset.forName("x-Johab");
                case "X-MACARABIC":
                    return Charset.forName("x-MacArabic");
                case "X-MACCENTRALEUROPE":
                    return Charset.forName("x-MacCentralEurope");
                case "X-MACCROATIAN":
                    return Charset.forName("x-MacCroatian");
                case "X-MACCYRILLIC":
                    return Charset.forName("x-MacCyrillic");
                case "X-MACDINGBAT":
                    return Charset.forName("x-MacDingbat");
                case "X-MACGREEK":
                    return Charset.forName("x-MacGreek");
                case "X-MACHEBREW":
                    return Charset.forName("x-MacHebrew");
                case "X-MACICELAND":
                    return Charset.forName("x-MacIceland");
                case "X-MACROMAN":
                    return Charset.forName("x-MacRoman");
                case "X-MACROMANIA":
                    return Charset.forName("x-MacRomania");
                case "X-MACSYMBOL":
                    return Charset.forName("x-MacSymbol");
                case "X-MACTHAI":
                    return Charset.forName("x-MacThai");
                case "X-MACTURKISH":
                    return Charset.forName("x-MacTurkish");
                case "X-MACUKRAINE":
                    return Charset.forName("x-MacUkraine");
                case "X-MS932_0213":
                    return Charset.forName("x-MS932_0213");
                case "X-MS950-HKSCS":
                    return Charset.forName("x-MS950-HKSCS");
                case "X-MS950-HKSCS-XP":
                    return Charset.forName("x-MS950-HKSCS-XP");
                case "X-MSWIN-936":
                    return Charset.forName("x-mswin-936");
                case "X-PCK":
                    return Charset.forName("x-PCK");
                case "X-SJIS_0213":
                    return Charset.forName("x-SJIS_0213");
                case "X-UTF-16LE-BOM":
                    return Charset.forName("x-UTF-16LE-BOM");
                case "X-UTF-32BE-BOM":
                    return Charset.forName("X-UTF-32BE-BOM");
                case "X-UTF-32LE-BOM":
                    return Charset.forName("X-UTF-32LE-BOM");
                case "X-WINDOWS-50220":
                    return Charset.forName("x-windows-50220");
                case "X-WINDOWS-50221":
                    return Charset.forName("x-windows-50221");
                case "X-WINDOWS-874":
                    return Charset.forName("x-windows-874");
                case "X-WINDOWS-949":
                    return Charset.forName("x-windows-949");
                case "X-WINDOWS-950":
                    return Charset.forName("x-windows-950");
                case "X-WINDOWS-ISO2022JP":
                    return Charset.forName("x-windows-iso2022jp");
                default:
                    return null;
            }
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Charset getCharSet(String name, Charset defaultValue) {
        Charset charSet = this.getCharSet(name);
        return charSet == null ? defaultValue : charSet;
    }

    public Charset getCharSetDef2$UTF8(String name, String defaultValue) {
        Charset charSet = this.getCharSet(name);
        return charSet != null ? charSet : this.getCharSet(defaultValue, StandardCharsets.UTF_8);
    }

    public DataMap getData(String name) {
        return this.getDataMap(name);
    }

    /**
     *  当无值时 返回一个空的Map
     * @param name key
     * @author YYJ
     * @version 1.0.0
     * @description      */
    public DataMap getNotNUllData(String name) {
        DataMap dataMap = this.getDataMap(name);
        if (dataMap == null)
            dataMap = new DataMap(0);
        return dataMap;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }


    @Override
    public DataMap clone() {
        return new DataMap(this);

    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }

    @Override
    public void putAll(Map m) {
        super.putAll(m);
    }

    public static DataMap of() {
        return new DataMap();
    }

    /**
     * Pack a pair of key-values as {@link DataMap}
     *
     * <pre>
     * DataMap DataMap = DataMap.of("name", "fastjson2");
     * </pre>
     *
     * @param key   the key of the element
     * @param value the value of the element
     */
    public static DataMap of(String key, Object value) {
        DataMap object = new DataMap(2);
        object.put(key, value);
        return object;
    }

    /**
     * Pack two key-value pairs as {@link DataMap}
     *
     * <pre>
     * DataMap DataMap = DataMap.of("key1", "value1", "key2", "value2");
     * </pre>
     *
     * @param k1 first key
     * @param v1 first value
     * @param k2 second key
     * @param v2 second value
     * @since 2.0.2
     */
    public static DataMap of(String k1, Object v1, String k2, Object v2) {
        DataMap object = new DataMap(3);
        object.put(k1, v1);
        object.put(k2, v2);
        return object;
    }

    /**
     * Pack three key-value pairs as {@link DataMap}
     *
     * <pre>
     * DataMap DataMap = DataMap.of("key1", "value1", "key2", "value2", "key3", "value3");
     * </pre>
     *
     * @param k1 first key
     * @param v1 first value
     * @param k2 second key
     * @param v2 second value
     * @param k3 third key
     * @param v3 third value
     * @since 2.0.2
     */
    public static DataMap of(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        DataMap object = new DataMap(5);
        object.put(k1, v1);
        object.put(k2, v2);
        object.put(k3, v3);
        return object;
    }

    /**
     * Pack three key-value pairs as {@link DataMap}
     *
     * <pre>
     * DataMap DataMap = DataMap.of("key1", "value1", "key2", "value2", "key3", "value3");
     * </pre>
     *
     * @param k1 first key
     * @param v1 first value
     * @param k2 second key
     * @param v2 second value
     * @param k3 third key
     * @param v3 third value
     * @param k4 third key
     * @param v4 third value
     * @since 2.0.8
     */
    public static DataMap of(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4) {
        DataMap object = new DataMap(5);
        object.put(k1, v1);
        object.put(k2, v2);
        object.put(k3, v3);
        object.put(k4, v4);
        return object;
    }

    /**
     * See {@link DataMap#parseObject} for details
     */
    public static <T> T parseObject(String text, Class<T> objectClass) {
        return JSON.parseObject(text, objectClass);
    }

    /**
     * See {@link DataMap#parseObject} for details
     */
    public static <T> T parseObject(String text, Class<T> objectClass, JSONReader.Feature... features) {
        return DataMap.parseObject(text, objectClass, features);
    }

    /**
     * See {@link DataMap#parseObject} for details
     */
    public static <T> T parseObject(String text, Type objectType, JSONReader.Feature... features) {
        return JSON.parseObject(text, objectType, features);
    }

    /**
     * See {@link DataMap#parseObject} for details
     */
    public static <T> T parseObject(String text, TypeReference<?> typeReference, JSONReader.Feature... features) {
        return JSON.parseObject(text, typeReference, features);
    }

    /**
     * See {@link DataMap#parseObject} for details
     */
    public static DataMap parseObject(String text) {
        return (DataMap) JSON.parseObject(text);
    }
}
