package be.kdg.schelderadarketen.verwerkingseenheid.persistence;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.PersistenceId;
import com.sun.corba.se.impl.io.TypeMismatchException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class SystemOutRepoImpl<V, K> implements Repository<V, K> {

    private List<V> memoryDb;

    public SystemOutRepoImpl() {
        memoryDb = new ArrayList<>();
    }

    @Override
    public V create(V value) {
        K key = getId(getIdField(value), value);
        if (!keyTaken(key)) {
            System.out.printf("Wrote value of type [%s] to memory: %s\n", value.getClass().getSimpleName(), value);
            memoryDb.add(value);
            return value;
        } else {
            throw new UnsupportedOperationException(String.format("Value with id %s already in database", key));
        }
    }

    private boolean keyTaken(K key) {
        for (V value : memoryDb) {
            if (getId(getIdField(value), value).equals(key))
                return true;
        }
        return false;
    }

    /*
    * Explicitly casting oId to K generates an unchecked cast warning.
    * Since the Reflection API does not use Generics, this warning can't be avoided.
    * Due to type erasure (K is a generic), we cannot use: id = K.class.cast(oId);
    * */
    private @SuppressWarnings("unchecked") K getId(Field idField, V value) {
        K id = null;
        try {
            idField.setAccessible(true);
            Object oId = idField.get(value);
            id = (K) oId;
        } catch (IllegalAccessException ignored) {
            /* This never occurs since we have set the field accessible */
        }
        return id;
    }

    /* Since we work in parallel, findAny has a better performance than findFirst */
    private Field getIdField(V value) {
        Class cls = value.getClass();
        Field[] declaredFields = cls.getDeclaredFields();
        return Arrays.asList(declaredFields).stream().parallel()
                .filter(f -> {
                    f.setAccessible(true);
                    return f.isAnnotationPresent(PersistenceId.class);
                }).findAny().get();
    }

    /* Since we work in parallel, findAny has a better performance than findFirst */
    @Override
    public V read(K key) {
        V value;
        try {
            value = memoryDb.stream().parallel()
                    .filter(o -> {
                        K tempKey = getId(getIdField(o), o);
                        if (tempKey.getClass() != key.getClass())
                            throw new TypeMismatchException(String.format("PersistenceId annotation on type %s but should be on type %s instead",
                                    tempKey.getClass(), key.getClass()));
                        return tempKey.equals(key);
                    }).findAny().get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("No such key found: %s", key.toString()));
        }
        return value;
    }

    @Override
    public void update(V value) {
        K key = getId(getIdField(value), value);
        delete(key);
        create(value);
    }

    @Override
    public void delete(K key) {
        memoryDb.remove(read(key));
    }
}