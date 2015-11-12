package be.kdg.schelderadarketen.verwerkingseenheid.persistence.memoryDb;

import be.kdg.schelderadarketen.verwerkingseenheid.persistence.Repository;
import com.sun.corba.se.impl.io.TypeMismatchException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;

public class SystemOutRepoImpl<V> implements Repository<V, Integer> {

    private List<V> memoryDb;

    public SystemOutRepoImpl() {
        memoryDb = new CopyOnWriteArrayList<>();
    }

    @Override
    public V create(V value) {
        Field idField = getIdField(value);
        try {
            idField.set(value, memoryDb.size());
            System.out.printf("Wrote value of type [%s] to memory: %s\n", value.getClass().getSimpleName(), value);
            memoryDb.add(value);
        } catch (IllegalAccessException ignored) {
            /* This never occurs since the idField's modifier is set to accessible in the getIdField method */
        }
        return value;
    }

    private boolean keyTaken(Integer key) {
        for (V value : memoryDb) {
            if (getId(getIdField(value), value).equals(key))
                return true;
        }
        return false;
    }

    /*
    * Explicitly casting oId to K generates an unchecked cast warning.
    * Since the Reflection API does not use Generics, this warning cannot be avoided.
    * Due to type erasure (K is a generic), we cannot use: id = K.class.cast(oId);
    * */
    private @SuppressWarnings("unchecked") Integer getId(Field idField, V value) {
        Integer id = null;
        try {
            idField.setAccessible(true);
            Object oId = idField.get(value);
            id = (Integer) oId;
        } catch (IllegalAccessException ignored) {
            /* This never occurs since we have set the field accessible */
        }
        return id;
    }

    /*
    * Since we work in parallel, findAny has a better performance than findFirst
    * Class#getDeclaredFields() doesn't include the fields of it's superclass so we have to add them manually
    * This is needed when we are using a Wrapper class
    * */
    private Field getIdField(V value) {
        Class cls = value.getClass();
        List<Field> declaredFields = new ArrayList<>();
        declaredFields.addAll(Arrays.asList(cls.getDeclaredFields()));
        declaredFields.addAll(Arrays.asList(cls.getSuperclass().getDeclaredFields()));
        return declaredFields.stream().parallel()
                    .filter(f -> {
                        f.setAccessible(true);
                        return f.isAnnotationPresent(PersistenceId.class);
                    }).findAny().get();
    }

    /* Since we work in parallel, findAny has a better performance than findFirst */
    @Override
    public V read(Integer key) {
        V value;
        try {
            value = memoryDb.stream().parallel()
                    .filter(o -> {
                        Integer tempKey = getId(getIdField(o), o);
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
    public List<V> readAll() {
        return new ArrayList<>(memoryDb);
    }

    @Override
    public void update(V value) {
        Integer key = getId(getIdField(value), value);
        delete(key);
        create(value);
    }

    @Override
    public void delete(Integer key) {
        memoryDb.remove(read(key));
    }

    @Override
    public List<V> readAllBy(String field, Object fieldValue) {
        List<V> valuesToReturn = new ArrayList<>();
        memoryDb.forEach(v -> {
            try {
                Field distinguishField = getField(v, field);
                Object value = distinguishField.get(v);
                if (value != null && value.equals(fieldValue))
                    valuesToReturn.add(v);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return valuesToReturn;
    }

    private Field getField(V value, String field) {
        Class cls = value.getClass();
        List<Field> declaredFields = new ArrayList<>();
        declaredFields.addAll(Arrays.asList(cls.getDeclaredFields()));
        declaredFields.addAll(Arrays.asList(cls.getSuperclass().getDeclaredFields()));
        return declaredFields.stream().parallel()
                .filter(f -> {
                    f.setAccessible(true);
                    return f.getName().equals(field);
                }).findAny().get();
    }
}