package top.yqingyu.common.qydata;


import com.alibaba.fastjson2.*;
import com.alibaba.fastjson2.schema.JSONSchema;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * @author YYJ
 * @version 1.0.1
 * @ClassName top.yqingyu.common.qydata.DataList
 * @description 彻底重构
 * @createTime 2022年07月06日 18:11:00
 */
public class DataList extends JSONArray implements Cloneable, Serializable {


    /**
     * default
     */
    public DataList() {
        super();
    }

    /**
     * @param initialCapacity the initial capacity of the {@link DataList}
     * @throws IllegalArgumentException If the specified initial capacity is negative
     */
    public DataList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * @param collection the collection whose elements are to be placed into this {@link DataList}
     * @throws NullPointerException If the specified collection is null
     */
    public DataList(Collection<?> collection) {
        super(collection);
    }

    /**
     * @param items the array whose elements are to be placed into this {@link DataList}
     * @throws NullPointerException If the specified items is null
     */
    public DataList(Object... items) {
        super(items);
    }

    /**
     * Replaces the element at the specified position with the specified element
     *
     * <pre>{@code
     *    DataList array = new DataList();
     *    array.add(-1); // [-1]
     *    array.add(2); // [-1,2]
     *    array.set(0, 1); // [1,2]
     *    array.set(4, 3); // [1,2,null,null,3]
     *    array.set(-1, -1); // [1,2,null,null,-1]
     *    array.set(-2, -2); // [1,2,null,-2,-1]
     *    array.set(-6, -6); // [-6,1,2,null,-2,-1]
     * }</pre>
     *
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @since 2.0.3
     */
    @Override
    public Object set(int index, Object element) {
        return super.set(index, element);
    }

    /**
     * Returns the {@link DataList} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link DataList} or null
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */

    public DataList getDataList(int index) {
        Object value = get(index);

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
     * Returns the {@link DataMap} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link DataMap} or null
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    public DataMap getDataMap(int index) {
        Object o = this.get(index);

        DataMap data = null;
        if (o instanceof JSONObject) {
            data = new DataMap();
            data.putAll((JSONObject) o);
            this.set(index, data);
        } else if (o instanceof JSONArray) {
            data = new DataMap((JSONArray) o);

        } else if (o instanceof Map<?, ?>) {
            data = new DataMap();
            data.putAll((Map) o);
            this.set(index, data);
        } else if (o instanceof List<?>) {
            data = new DataMap((List<?>) o);
        } else {
            return null;
        }

        return data;
    }

    /**
     * Returns the {@link String} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link String} or null
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public String getString(int index) {
        return super.getString(index);
    }

    /**
     * Returns the {@link Double} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link Double} or null
     * @throws NumberFormatException     If the value of get is {@link String} and it contains no parsable double
     * @throws JSONException             Unsupported type conversion to {@link Double}
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public Double getDouble(int index) {
        return super.getDouble(index);
    }

    /**
     * Returns a double value at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return double
     * @throws NumberFormatException     If the value of get is {@link String} and it contains no parsable double
     * @throws JSONException             Unsupported type conversion to double value
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public double getDoubleValue(int index) {
        return super.getDoubleValue(index);
    }

    /**
     * Returns the {@link Float} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link Float} or null
     * @throws NumberFormatException     If the value of get is {@link String} and it contains no parsable float
     * @throws JSONException             Unsupported type conversion to {@link Float}
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public Float getFloat(int index) {
        return super.getFloat(index);
    }

    /**
     * Returns a float value at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return float
     * @throws NumberFormatException     If the value of get is {@link String} and it contains no parsable float
     * @throws JSONException             Unsupported type conversion to float value
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public float getFloatValue(int index) {
        return super.getFloatValue(index);
    }

    /**
     * Returns the {@link Long} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link Long} or null
     * @throws NumberFormatException     If the value of get is {@link String} and it contains no parsable long
     * @throws JSONException             Unsupported type conversion to {@link Long}
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public Long getLong(int index) {
        return super.getLong(index);
    }

    /**
     * Returns a long value at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return long
     * @throws NumberFormatException     If the value of get is {@link String} and it contains no parsable long
     * @throws JSONException             Unsupported type conversion to long value
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public long getLongValue(int index) {
        return super.getLongValue(index);
    }

    /**
     * Returns the {@link Integer} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link Integer} or null
     * @throws NumberFormatException     If the value of get is {@link String} and it contains no parsable int
     * @throws JSONException             Unsupported type conversion to {@link Integer}
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public Integer getInteger(int index) {
        return super.getInteger(index);
    }

    /**
     * Returns an int value at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return int
     * @throws NumberFormatException     If the value of get is {@link String} and it contains no parsable int
     * @throws JSONException             Unsupported type conversion to int value
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public int getIntValue(int index) {
        return super.getIntValue(index);
    }

    /**
     * Returns the {@link Short} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link Short} or null
     * @throws NumberFormatException     If the value of get is {@link String} and it contains no parsable short
     * @throws JSONException             Unsupported type conversion to {@link Short}
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public Short getShort(int index) {
        return super.getShort(index);
    }

    /**
     * Returns a short value at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return short
     * @throws NumberFormatException     If the value of get is {@link String} and it contains no parsable short
     * @throws JSONException             Unsupported type conversion to short value
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public short getShortValue(int index) {
        return super.getShortValue(index);
    }

    /**
     * Returns the {@link Byte} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link Byte} or null
     * @throws NumberFormatException     If the value of get is {@link String} and it contains no parsable byte
     * @throws JSONException             Unsupported type conversion to {@link Byte}
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public Byte getByte(int index) {
        return super.getByte(index);
    }

    /**
     * Returns a byte value at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return byte
     * @throws NumberFormatException     If the value of get is {@link String} and it contains no parsable byte
     * @throws JSONException             Unsupported type conversion to byte value
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public byte getByteValue(int index) {
        return super.getByteValue(index);
    }

    /**
     * Returns the {@link Boolean} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link Boolean} or null
     * @throws JSONException             Unsupported type conversion to {@link Boolean}
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public Boolean getBoolean(int index) {
        return super.getBoolean(index);
    }

    /**
     * Returns a boolean value at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return boolean
     * @throws JSONException             Unsupported type conversion to boolean value
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public boolean getBooleanValue(int index) {
        return super.getBooleanValue(index);
    }

    /**
     * Returns the {@link BigInteger} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link BigInteger} or null
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     * @throws JSONException             Unsupported type conversion to {@link BigInteger}
     * @throws NumberFormatException     If the value of get is {@link String} and it is not a valid representation of {@link BigInteger}
     */
    @Override
    public BigInteger getBigInteger(int index) {
        return super.getBigInteger(index);
    }

    /**
     * Returns the {@link BigDecimal} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link BigDecimal} or null
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     * @throws JSONException             Unsupported type conversion to {@link BigDecimal}
     * @throws NumberFormatException     If the value of get is {@link String} and it is not a valid representation of {@link BigDecimal}
     */
    @Override
    public BigDecimal getBigDecimal(int index) {
        return super.getBigDecimal(index);
    }

    /**
     * Returns the {@link Date} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link Date} or null
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public Date getDate(int index) {
        return super.getDate(index);
    }

    /**
     * Returns the {@link Instant} at the specified location in this {@link DataList}.
     *
     * @param index index of the element to return
     * @return {@link Instant} or null
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public Instant getInstant(int index) {
        return super.getInstant(index);
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
     * Convert this {@link DataList} to the specified Object
     *
     * <pre>{@code
     * DataList array = ...
     * List<User> users = array.to(new TypeReference<ArrayList<User>>(){}.getType());
     * }</pre>
     *
     * @param type specify the {@link Type} to be converted
     * @since 2.0.4
     */
    @Override
    public <T> T to(Type type) {
        return super.to(type);
    }

    /**
     * @param type 类型
     * @param <T>  泛型
     * @return T
     */
    @Override
    public <T> T to(Class<T> type) {
        return super.to(type);
    }

    /**
     * Convert this {@link DataList} to the specified Object
     *
     * @param type specify the {@link Type} to be converted
     * @deprecated since 2.0.4, please use {@link #to(Type)}
     */
    @Override
    public <T> T toJavaObject(Type type) {
        return super.toJavaObject(type);
    }

    /**
     * Convert all the members of this {@link DataList} into the specified Object.
     *
     * <pre>{@code
     * String json = "[{\"id\": 1, \"name\": \"fastjson\"}, {\"id\": 2, \"name\": \"fastjson2\"}]";
     * DataList array = JSON.parseArray(json);
     * List<User> users = array.toList(User.class);
     * }</pre>
     *
     * @param itemClass specify the {@code Class<T>} to be converted
     * @param features  features to be enabled in parsing
     * @since 2.0.4
     */
    @Override
    public <T> List<T> toList(Class<T> itemClass, JSONReader.Feature... features) {
        return super.toList(itemClass, features);
    }

    /**
     * Convert all the members of this {@link DataList} into the specified Object.
     *
     * <pre>{@code
     * String json = "[{\"id\": 1, \"name\": \"fastjson\"}, {\"id\": 2, \"name\": \"fastjson2\"}]";
     * DataList array = JSON.parseArray(json);
     * List<User> users = array.toList(User.class);
     * }</pre>
     *
     * @param itemClass specify the {@code Class<T>} to be converted
     * @param features  features to be enabled in parsing
     * @since 2.0.4
     */
    @Override
    public <T> T[] toArray(Class<T> itemClass, JSONReader.Feature... features) {
        return super.toArray(itemClass, features);
    }

    /**
     * Convert all the members of this {@link DataList} into the specified Object.
     *
     * @param clazz    specify the {@code Class<T>} to be converted
     * @param features features to be enabled in parsing
     * @deprecated since 2.0.4, please use {@link #toList(Class, JSONReader.Feature...)}
     */
    @Override
    public <T> List<T> toJavaList(Class<T> clazz, JSONReader.Feature... features) {
        return super.toJavaList(clazz, features);
    }

    /**
     * Returns the result of the {@link Type} converter conversion of the element at the specified position in this {@link DataList}.
     *
     * <pre>{@code
     * DataList array = ...
     * User user = array.getObject(0, TypeReference<HashMap<String ,User>>(){}.getType());
     * }</pre>
     *
     * @param index    index of the element to return
     * @param type     specify the {@link Type} to be converted
     * @param features
     * @return {@code <T>} or null
     * @throws JSONException             If no suitable conversion method is found
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public <T> T getObject(int index, Type type, JSONReader.Feature... features) {
        return super.getObject(index, type, features);
    }

    /**
     * Returns the result of the {@link Type} converter conversion of the element at the specified position in this {@link DataList}.
     * <p>
     * {@code User user = DataList.getObject(0, User.class);}
     *
     * @param index    index of the element to return
     * @param type     specify the {@link Class} to be converted
     * @param features
     * @return {@code <T>} or null
     * @throws JSONException             If no suitable conversion method is found
     * @throws IndexOutOfBoundsException if the index is out of range {@code (index < 0 || index >= size())}
     */
    @Override
    public <T> T getObject(int index, Class<T> type, JSONReader.Feature... features) {
        return super.getObject(index, type, features);
    }

    /**
     * @param index
     * @param creator
     * @since 2.0.3
     */
    @Override
    public <T> T getObject(int index, Function<JSONObject, T> creator) {
        return super.getObject(index, creator);
    }

    /**
     * Chained addition of elements
     *
     * <pre>
     * DataList array = new DataList().fluentAdd(1).fluentAdd(2).fluentAdd(3);
     * </pre>
     *
     * @param element element to be appended to this list
     */
    @Override
    public DataList fluentAdd(Object element) {
        return (DataList) super.fluentAdd(element);
    }

    /**
     * @since 2.0.3
     */
    @Override
    public DataList fluentClear() {
        return (DataList) super.fluentClear();
    }

    /**
     * @param index
     * @since 2.0.3
     */
    @Override
    public DataList fluentRemove(int index) {
        return (DataList) super.fluentRemove(index);
    }

    /**
     * @param index
     * @param element
     * @since 2.0.3
     */
    @Override
    public DataList fluentSet(int index, Object element) {
        return (DataList) super.fluentSet(index, element);
    }

    /**
     * @param o
     * @since 2.0.3
     */
    @Override
    public DataList fluentRemove(Object o) {
        return (DataList) super.fluentRemove(o);
    }

    /**
     * @param c
     * @since 2.0.3
     */
    @Override
    public DataList fluentRemoveAll(Collection<?> c) {
        return (DataList) super.fluentRemoveAll(c);
    }

    /**
     * @param c
     * @since 2.0.3
     */
    @Override
    public DataList fluentAddAll(Collection<?> c) {
        return (DataList) super.fluentAddAll(c);
    }

    /**
     * @param schema
     * @since 2.0.3
     */
    @Override
    public boolean isValid(JSONSchema schema) {
        return super.isValid(schema);
    }

    /**
     * Trims the capacity of this {@code ArrayList} instance to be the
     * list's current size.  An application can use this operation to minimize
     * the storage of an {@code ArrayList} instance.
     */
    @Override
    public void trimToSize() {
        super.trimToSize();
    }

    /**
     * Increases the capacity of this {@code ArrayList} instance, if
     * necessary, to ensure that it can hold at least the number of elements
     * specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     */
    @Override
    public void ensureCapacity(int minCapacity) {
        super.ensureCapacity(minCapacity);
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return super.size();
    }

    /**
     * Returns {@code true} if this list contains no elements.
     *
     * @return {@code true} if this list contains no elements
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    /**
     * Returns {@code true} if this list contains the specified element.
     * More formally, returns {@code true} if and only if this list contains
     * at least one element {@code e} such that
     * {@code Objects.equals(o, e)}.
     *
     * @param o element whose presence in this list is to be tested
     * @return {@code true} if this list contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        return super.contains(o);
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index {@code i} such that
     * {@code Objects.equals(o, get(i))},
     * or -1 if there is no such index.
     *
     * @param o
     */
    @Override
    public int indexOf(Object o) {
        return super.indexOf(o);
    }

    /**
     * Returns the index of the last occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the highest index {@code i} such that
     * {@code Objects.equals(o, get(i))},
     * or -1 if there is no such index.
     *
     * @param o
     */
    @Override
    public int lastIndexOf(Object o) {
        return super.lastIndexOf(o);
    }

    /**
     * Returns an array containing all of the elements in this list
     * in proper sequence (from first to last element).
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this list.  (In other words, this method must allocate
     * a new array).  The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this list in
     * proper sequence
     */
    @Override
    public Object[] toArray() {
        return super.toArray();
    }

    /**
     * Returns an array containing all of the elements in this list in proper
     * sequence (from first to last element); the runtime type of the returned
     * array is that of the specified array.  If the list fits in the
     * specified array, it is returned therein.  Otherwise, a new array is
     * allocated with the runtime type of the specified array and the size of
     * this list.
     *
     * <p>If the list fits in the specified array with room to spare
     * (i.e., the array has more elements than the list), the element in
     * the array immediately following the end of the collection is set to
     * {@code null}.  (This is useful in determining the length of the
     * list <i>only</i> if the caller knows that the list does not contain
     * any null elements.)
     *
     * @param a the array into which the elements of the list are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the elements of the list
     * @throws ArrayStoreException  if the runtime type of the specified array
     *                              is not a supertype of the runtime type of every element in
     *                              this list
     * @throws NullPointerException if the specified array is null
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return super.toArray(a);
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public Object get(int index) {
        return super.get(index);
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param o element to be appended to this list
     * @return {@code true} (as specified by {@link Collection#add})
     */
    @Override
    public boolean add(Object o) {
        return super.add(o);
    }

    /**
     * Inserts the specified element at the specified position in this
     * list. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public void add(int index, Object element) {
        super.add(index, element);
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public Object remove(int index) {
        return super.remove(index);
    }

    /**
     * {@inheritDoc}
     *
     * @param o
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present.  If the list does not contain the element, it is
     * unchanged.  More formally, removes the element with the lowest index
     * {@code i} such that
     * {@code Objects.equals(o, get(i))}
     * (if such an element exists).  Returns {@code true} if this list
     * contained the specified element (or equivalently, if this list
     * changed as a result of the call).
     *
     * @param o element to be removed from this list, if present
     * @return {@code true} if this list contained the specified element
     */
    @Override
    public boolean remove(Object o) {
        return super.remove(o);
    }

    /**
     * Removes all of the elements from this list.  The list will
     * be empty after this call returns.
     */
    @Override
    public void clear() {
        super.clear();
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the
     * specified collection's Iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified collection is this list, and this
     * list is nonempty.)
     *
     * @param c collection containing elements to be added to this list
     * @return {@code true} if this list changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean addAll(Collection<?> c) {
        return super.addAll(c);
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified collection's iterator.
     *
     * @param index index at which to insert the first element from the
     *              specified collection
     * @param c     collection containing elements to be added to this list
     * @return {@code true} if this list changed as a result of the call
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException      if the specified collection is null
     */
    @Override
    public boolean addAll(int index, Collection<?> c) {
        return super.addAll(index, c);
    }

    /**
     * Removes from this list all of the elements whose index is between
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
     * Shifts any succeeding elements to the left (reduces their index).
     * This call shortens the list by {@code (toIndex - fromIndex)} elements.
     * (If {@code toIndex==fromIndex}, this operation has no effect.)
     *
     * @param fromIndex
     * @param toIndex
     * @throws IndexOutOfBoundsException if {@code fromIndex} or
     *                                   {@code toIndex} is out of range
     *                                   ({@code fromIndex < 0 ||
     *                                   toIndex > size() ||
     *                                   toIndex < fromIndex})
     */
    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
    }

    /**
     * Removes from this list all of its elements that are contained in the
     * specified collection.
     *
     * @param c collection containing elements to be removed from this list
     * @return {@code true} if this list changed as a result of the call
     * @throws ClassCastException   if the class of an element of this list
     *                              is incompatible with the specified collection
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if this list contains a null element and the
     *                              specified collection does not permit null elements
     *                              (<a href="Collection.html#optional-restrictions">optional</a>),
     *                              or if the specified collection is null
     * @see Collection#contains(Object)
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

    /**
     * Retains only the elements in this list that are contained in the
     * specified collection.  In other words, removes from this list all
     * of its elements that are not contained in the specified collection.
     *
     * @param c collection containing elements to be retained in this list
     * @return {@code true} if this list changed as a result of the call
     * @throws ClassCastException   if the class of an element of this list
     *                              is incompatible with the specified collection
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if this list contains a null element and the
     *                              specified collection does not permit null elements
     *                              (<a href="Collection.html#optional-restrictions">optional</a>),
     *                              or if the specified collection is null
     * @see Collection#contains(Object)
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return super.retainAll(c);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * The specified index indicates the first element that would be
     * returned by an initial call to {@link ListIterator#next next}.
     * An initial call to {@link ListIterator#previous previous} would
     * return the element with the specified index minus one.
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @param index
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public ListIterator<Object> listIterator(int index) {
        return super.listIterator(index);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @see #listIterator(int)
     */
    @Override
    public ListIterator<Object> listIterator() {
        return super.listIterator();
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * <p>The returned iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    @Override
    public Iterator<Object> iterator() {
        return super.iterator();
    }

    /**
     * Returns a view of the portion of this list between the specified
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.  (If
     * {@code fromIndex} and {@code toIndex} are equal, the returned list is
     * empty.)  The returned list is backed by this list, so non-structural
     * changes in the returned list are reflected in this list, and vice-versa.
     * The returned list supports all of the optional list operations.
     *
     * <p>This method eliminates the need for explicit range operations (of
     * the sort that commonly exist for arrays).  Any operation that expects
     * a list can be used as a range operation by passing a subList view
     * instead of a whole list.  For example, the following idiom
     * removes a range of elements from a list:
     * <pre>
     *      list.subList(from, to).clear();
     * </pre>
     * Similar idioms may be constructed for {@link #indexOf(Object)} and
     * {@link #lastIndexOf(Object)}, and all of the algorithms in the
     * {@link Collections} class can be applied to a subList.
     *
     * <p>The semantics of the list returned by this method become undefined if
     * the backing list (i.e., this list) is <i>structurally modified</i> in
     * any way other than via the returned list.  (Structural modifications are
     * those that change the size of this list, or otherwise perturb it in such
     * a fashion that iterations in progress may yield incorrect results.)
     *
     * @param fromIndex
     * @param toIndex
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws IllegalArgumentException  {@inheritDoc}
     */
    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return super.subList(fromIndex, toIndex);
    }

    /**
     * @param action
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void forEach(Consumer<? super Object> action) {
        super.forEach(action);
    }

    /**
     * Creates a <em><a href="Spliterator.html#binding">late-binding</a></em>
     * and <em>fail-fast</em> {@link Spliterator} over the elements in this
     * list.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, and {@link Spliterator#ORDERED}.
     * Overriding implementations should document the reporting of additional
     * characteristic values.
     *
     * @return a {@code Spliterator} over the elements in this list
     * @since 1.8
     */
    @Override
    public Spliterator<Object> spliterator() {
        return super.spliterator();
    }

    /**
     * @param filter
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean removeIf(Predicate<? super Object> filter) {
        return super.removeIf(filter);
    }

    /**
     * @param operator the operator to apply to each element
     */
    @Override
    public void replaceAll(UnaryOperator<Object> operator) {
        super.replaceAll(operator);
    }

    /**
     * @param c the {@code Comparator} used to compare list elements.
     *          A {@code null} value indicates that the elements'
     *          {@linkplain Comparable natural ordering} should be used
     */
    @Override
    public void sort(Comparator<? super Object> c) {
        super.sort(c);
    }

    /**
     * {@inheritDoc}
     *
     * @param c
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @implSpec This implementation iterates over the specified collection,
     * checking each element returned by the iterator in turn to see
     * if it's contained in this collection.  If all elements are so
     * contained {@code true} is returned, otherwise {@code false}.
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return super.containsAll(c);
    }

    /**
     * Returns an array containing all of the elements in this collection,
     * using the provided {@code generator} function to allocate the returned array.
     *
     * <p>If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     *
     * @param generator a function which produces a new array of the desired
     *                  type and the provided length
     * @return an array containing all of the elements in this collection
     * @throws ArrayStoreException  if the runtime type of any element in this
     *                              collection is not assignable to the {@linkplain Class#getComponentType
     *                              runtime component type} of the generated array
     * @throws NullPointerException if the generator function is null
     * @apiNote This method acts as a bridge between array-based and collection-based APIs.
     * It allows creation of an array of a particular runtime type. Use
     * {@link #toArray()} to create an array whose runtime type is {@code Object[]},
     * or use {@link #toArray(Object[]) toArray(T[])} to reuse an existing array.
     *
     * <p>Suppose {@code x} is a collection known to contain only strings.
     * The following code can be used to dump the collection into a newly
     * allocated array of {@code String}:
     *
     * <pre>
     *     String[] y = x.toArray(String[]::new);</pre>
     * @implSpec The default implementation calls the generator function with zero
     * and then passes the resulting array to {@link #toArray(Object[]) toArray(T[])}.
     * @since 11
     */
    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    /**
     * Returns a sequential {@code Stream} with this collection as its source.
     *
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * @return a sequential {@code Stream} over the elements in this collection
     * @implSpec The default implementation creates a sequential {@code Stream} from the
     * collection's {@code Spliterator}.
     * @since 1.8
     */
    @Override
    public Stream<Object> stream() {
        return super.stream();
    }

    /**
     * Returns a possibly parallel {@code Stream} with this collection as its
     * source.  It is allowable for this method to return a sequential stream.
     *
     * <p>This method should be overridden when the {@link #spliterator()}
     * method cannot return a spliterator that is {@code IMMUTABLE},
     * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
     * for details.)
     *
     * @return a possibly parallel {@code Stream} over the elements in this
     * collection
     * @implSpec The default implementation creates a parallel {@code Stream} from the
     * collection's {@code Spliterator}.
     * @since 1.8
     */
    @Override
    public Stream<Object> parallelStream() {
        return super.parallelStream();
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public DataList clone() {
        return (DataList) super.clone();
    }
}