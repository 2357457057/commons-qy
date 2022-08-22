package top.yqingyu.common.qydata;


import com.alibaba.fastjson2.*;
import com.alibaba.fastjson2.filter.NameFilter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.alibaba.fastjson2.schema.JSONSchema;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * @author YYJ
 * @version 1.0.1
 * @ClassName top.yqingyu.common.IData.DataMap
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
        return (DataList) super.getJSONArray(key);
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
        return (DataMap) super.getJSONObject(key);
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
    public <T> T to(TypeReference typeReference, JSONReader.Feature... features) {
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
    public <T> T toJavaObject(TypeReference typeReference, JSONReader.Feature... features) {
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
        return (DataMap)super.fluentPut(key, value);
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


    public DataMap getData(String name) {
        return (DataMap) super.getJSONObject(name);
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
    public static <T> T parseObject(String text, TypeReference typeReference, JSONReader.Feature... features) {
        return JSON.parseObject(text, typeReference, features);
    }

    /**
     * See {@link DataMap#parseObject} for details
     */
    public static DataMap parseObject(String text) {
        return (DataMap) JSON.parseObject(text);
    }
}
