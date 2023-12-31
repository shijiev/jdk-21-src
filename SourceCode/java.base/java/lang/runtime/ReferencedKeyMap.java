/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.lang.runtime;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class provides management of {@link Map maps} where it is desirable to
 * remove entries automatically when the key is garbage collected. This is
 * accomplished by using a backing map where the keys are either a
 * {@link WeakReference} or a {@link SoftReference}.
 * <p>
 * To create a {@link ReferencedKeyMap} the user must provide a {@link Supplier}
 * of the backing map and whether {@link WeakReference} or
 * {@link SoftReference} is to be used.
 *
 * {@snippet :
 * // Use HashMap and WeakReference
 * Map<Long, String> map = ReferencedKeyMap.create(false, HashMap::new);
 * map.put(10_000_000L, "a");
 * map.put(10_000_001L, "b");
 * map.put(10_000_002L, "c");
 * map.put(10_000_003L, "d");
 * map.put(10_000_004L, "e");
 *
 * // Use ConcurrentHashMap and SoftReference
 * map = ReferencedKeyMap.create(true, ConcurrentHashMap::new);
 * map.put(20_000_000L, "v");
 * map.put(20_000_001L, "w");
 * map.put(20_000_002L, "x");
 * map.put(20_000_003L, "y");
 * map.put(20_000_004L, "z");
 * }
 *
 * @implNote Care must be given that the backing map does replacement by
 * replacing the value in the map entry instead of deleting the old entry and
 * adding a new entry, otherwise replaced entries may end up with a strongly
 * referenced key. {@link HashMap} and {@link ConcurrentHashMap} are known
 * to be safe.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @since 21
 *
 * Warning: This class is part of PreviewFeature.Feature.STRING_TEMPLATES.
 *          Do not rely on its availability.
 */
final class ReferencedKeyMap<K, V> implements Map<K, V> {
    /**
     * true if {@link SoftReference} keys are to be used,
     * {@link WeakReference} otherwise.
     */
    private final boolean isSoft;

    /**
     * Backing {@link Map}.
     */
    private final Map<ReferenceKey<K>, V> map;

    /**
     * {@link ReferenceQueue} for cleaning up {@link WeakReferenceKey EntryKeys}.
     */
    private final ReferenceQueue<K> stale;

    /**
     * Private constructor.
     *
     * @param isSoft  true if {@link SoftReference} keys are to
     *                be used, {@link WeakReference} otherwise.
     * @param map     backing map
     */
    private ReferencedKeyMap(boolean isSoft, Map<ReferenceKey<K>, V> map) {
        this.isSoft = isSoft;
        this.map = map;
        this.stale = new ReferenceQueue<>();
    }

    /**
     * Create a new {@link ReferencedKeyMap} map.
     *
     * @param isSoft    true if {@link SoftReference} keys are to
     *                  be used, {@link WeakReference} otherwise.
     * @param supplier  {@link Supplier} of the backing map
     *
     * @return a new map with {@link Reference} keys
     *
     * @param <K> the type of keys maintained by the new map
     * @param <V> the type of mapped values
     */
    static <K, V> ReferencedKeyMap<K, V>
    create(boolean isSoft, Supplier<Map<ReferenceKey<K>, V>> supplier) {
        return new ReferencedKeyMap<K, V>(isSoft, supplier.get());
    }

    /**
     * Create a new {@link ReferencedKeyMap} map using
     * {@link WeakReference} keys.
     *
     * @param supplier  {@link Supplier} of the backing map
     *
     * @return a new map with {@link Reference} keys
     *
     * @param <K> the type of keys maintained by the new map
     * @param <V> the type of mapped values
     */
    static <K, V> ReferencedKeyMap<K, V>
    create(Supplier<Map<ReferenceKey<K>, V>> supplier) {
        return new ReferencedKeyMap<K, V>(false, supplier.get());
    }

    /**
     * {@return a key suitable for a map entry}
     *
     * @param key unwrapped key
     */
    @SuppressWarnings("unchecked")
    private ReferenceKey<K> entryKey(Object key) {
        if (isSoft) {
            return new SoftReferenceKey<>((K)key, stale);
        } else {
            return new WeakReferenceKey<>((K)key, stale);
        }
    }

    /**
     * {@return a key suitable for lookup}
     *
     * @param key unwrapped key
     */
    @SuppressWarnings("unchecked")
    private ReferenceKey<K> lookupKey(Object key) {
        return new StrongReferenceKey<>((K)key);
    }

    @Override
    public int size() {
        removeStaleReferences();
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        removeStaleReferences();
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        Objects.requireNonNull(key, "key must not be null");
        removeStaleReferences();
        return map.containsKey(lookupKey(key));
    }

    @Override
    public boolean containsValue(Object value) {
        Objects.requireNonNull(value, "value must not be null");
        removeStaleReferences();
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        Objects.requireNonNull(key, "key must not be null");
        removeStaleReferences();
        return map.get(lookupKey(key));
    }

    @Override
    public V put(K key, V newValue) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(newValue, "value must not be null");
        removeStaleReferences();
        ReferenceKey<K> entryKey = entryKey(key);
        // If {@code put} returns non-null then was actually a {@code replace}
        // and older key was used. In that case the new key was not used and the
        // reference marked stale.
        V oldValue = map.put(entryKey, newValue);
        if (oldValue != null) {
            entryKey.unused();
        }
        return oldValue;
    }

    @Override
    public V remove(Object key) {
        // Rely on gc to clean up old key.
        return map.remove(lookupKey(key));
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        removeStaleReferences();
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            put(key, value);
        }
    }

    @Override
    public void clear() {
        removeStaleReferences();
        // Rely on gc to clean up old keys.
        map.clear();
    }

    /**
     * Common routine for collecting the current set of keys.
     *
     * @return {@link Stream} of valid keys (unwrapped)
     */
    private Stream<K> filterKeySet() {
        return map.keySet()
                .stream()
                .map(ReferenceKey::get)
                .filter(Objects::nonNull);
    }

    @Override
    public Set<K> keySet() {
        removeStaleReferences();
        return filterKeySet().collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        removeStaleReferences();
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        removeStaleReferences();
        return filterKeySet()
                .map(k -> new AbstractMap.SimpleEntry<>(k, get(k)))
                .collect(Collectors.toSet());
    }

    @Override
    public V putIfAbsent(K key, V newValue) {
        removeStaleReferences();
        ReferenceKey<K> entryKey = entryKey(key);
        // If {@code putIfAbsent} returns non-null then was actually a
        // {@code replace}  and older key was used. In that case the new key was
        // not used and the reference marked stale.
        V oldValue = map.putIfAbsent(entryKey, newValue);
        if (oldValue != null) {
            entryKey.unused();
        }
        return oldValue;
    }

    @Override
    public boolean remove(Object key, Object value) {
        // Rely on gc to clean up old key.
        return map.remove(lookupKey(key), value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        removeStaleReferences();
        // If replace is successful then the older key will be used and the
        // lookup key will suffice.
        return map.replace(lookupKey(key), oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        removeStaleReferences();
        // If replace is successful then the older key will be used and the
        // lookup key will suffice.
        return map.replace(lookupKey(key), value);
    }

    @Override
    public String toString() {
        removeStaleReferences();
        return filterKeySet()
                .map(k -> k + "=" + get(k))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    /**
     * Removes enqueued weak references from map.
     */
    @SuppressWarnings("unchecked")
    public void removeStaleReferences() {
        while (true) {
            WeakReferenceKey<K> key = (WeakReferenceKey<K>)stale.poll();
            if (key == null) {
                break;
            }
            map.remove(key);
        }
    }

}
