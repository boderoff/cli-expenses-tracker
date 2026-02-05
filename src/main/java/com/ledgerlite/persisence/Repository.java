package main.java.com.ledgerlite.persisence;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    T save(T entry);
    Optional<T> findById(String id);
    List<T> findAll();
    void delete(String id);
    boolean exists(String id);
    long count();
}
