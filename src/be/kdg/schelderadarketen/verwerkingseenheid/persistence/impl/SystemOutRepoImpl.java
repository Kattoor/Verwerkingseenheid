package be.kdg.schelderadarketen.verwerkingseenheid.persistence.impl;

import be.kdg.schelderadarketen.verwerkingseenheid.domain.PersistenceId;
import be.kdg.schelderadarketen.verwerkingseenheid.persistence.Repository;
import com.sun.corba.se.impl.io.TypeMismatchException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class SystemOutRepoImpl<ObjectType, KeyType> implements Repository<ObjectType, KeyType> {

    private List<ObjectType> memoryDb;

    public SystemOutRepoImpl() {
        memoryDb = new ArrayList<>();
    }

    @Override
    public ObjectType create(ObjectType object) {
        Object id = getId(getIdField(object), object);
        if (!idTaken(id)) {
            System.out.printf("Wrote object of type [%s] to memory\n", object.getClass().getSimpleName());
            memoryDb.add(object);
            return object;
        } else {
            throw new UnsupportedOperationException(String.format("Object with id %s already in database", id));
        }
    }

    private boolean idTaken(Object id) {
        for (ObjectType o : memoryDb) {
            if (getId(getIdField(o), o).equals(id))
                return true;
        }
        return false;
    }

    private Object getId(Field idField, ObjectType o) {
        Object id = null;
        try {
            id = idField.get(o);
        } catch (IllegalAccessException ignored) {
            /* This never occurs since we have set the field accessible */
        }
        return id;
    }

    /* Since we work in parallel, findAny has a better performance than findFirst */
    private Field getIdField(Object object) {
        Class cls = object.getClass();
        Field[] declaredFields = cls.getDeclaredFields();
        return Arrays.asList(declaredFields).stream().parallel()
                .filter(f -> {
                    f.setAccessible(true);
                    return f.isAnnotationPresent(PersistenceId.class);
                }).findAny().get();
    }

    /* Since we work in parallel, findAny has a better performance than findFirst */
    @Override
    public ObjectType read(KeyType key) {
        ObjectType value;
        try {
            value = memoryDb.stream().parallel()
                    .filter(o -> {
                        Object id = getId(getIdField(o), o);
                        if (id.getClass() != key.getClass())
                            throw new TypeMismatchException(String.format("PersistenceId annotation on type %s but should be on type %s instead",
                                    id.getClass(), key.getClass()));
                        return id.equals(key);
                    }).findAny().get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("No such key found: %s", key.toString()));
        }
        return value;
    }

    @Override
    public void update(ObjectType object) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(KeyType key) {
        memoryDb.remove(read(key));
    }
}
