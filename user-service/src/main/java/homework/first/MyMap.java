package homework.first;

/**
 * Простая карта (map) с минимальным контрактом.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public interface MyMap<K, V> {

    /**
     * Возвращает значение, соответствующее ключу, или {@code null}, если ключа нет.
     *
     * @param key ключ (может быть {@code null})
     * @return значение или {@code null}
     */
    V get(Object key);

    /**
     * Помещает пару ключ-значение в карту. Если ключ уже был — заменяет значение и возвращает старое.
     *
     * @param key   ключ (может быть {@code null})
     * @param value значение
     * @return предыдущее значение для этого ключа или {@code null}, если ключа ранее не было
     */
    V put(K key, V value);

    /**
     * Удаляет запись по ключу.
     *
     * @param key ключ
     * @return удалённое значение или {@code null}, если такого ключа не было
     */
    V remove(Object key);

    /**
     * Проверяет наличие ключа в карте.
     *
     * @param key ключ
     * @return {@code true}, если ключ присутствует
     */
    boolean containsKey(Object key);

    /**
     * Возвращает количество записей в карте.
     *
     * @return размер
     */
    int size();
}


