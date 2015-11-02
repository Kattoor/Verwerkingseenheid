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
        System.out.printf("Wrote object of type [%s] to memory\n", object.getClass().getSimpleName());
        memoryDb.add(object);
        return object;
    }

    /* Since we work in parallel, findAny has a better performance than findFirst.
    *
    * This method iterates over each object in the arraylist.
    * For each object, it iterates over the fields.
    * If the field contains the PersistenceId annotation, we look if it's value is equal to the given key.
    *
    * If previously mentioned field has a difference type than the generic KeyType, a TypeMismatchException will be thrown.
    * */
    @Override
    public ObjectType read(KeyType key) {
        ObjectType value;
        try {
            value = memoryDb.stream().parallel()
                    .filter(o -> {
                        Class cls = o.getClass();
                        Field[] declaredFields = cls.getDeclaredFields();
                        Field idField = Arrays.asList(declaredFields).stream().parallel()
                                .filter(f -> {
                                    f.setAccessible(true);
                                    return f.isAnnotationPresent(PersistenceId.class);
                                }).findAny().get();
                        try {
                            if (idField.get(o).getClass() != key.getClass())
                                throw new TypeMismatchException(String.format("PersistenceId annotation on type %s but should be on type %s instead",
                                        idField.get(o).getClass(), key.getClass()));
                            return idField.get(o).equals(key);
                        } catch (IllegalAccessException ignored) {
                            /* This never occurs since we have set the field accessible */
                        }
                        return false;
                    }).findAny().get();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("No such key found: %s", key.toString()));
        }
        return value;
    }

    private boolean typeMatch(Class type1, Class type2) {
        return type1 == type2;
    }

    @Override
    public void update(ObjectType object) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(KeyType key) {

    }
}
