package main.java.com.ledgerlite.persisence;

import main.java.com.ledgerlite.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository<T> implements Repository<T> {
    private final Map<String, T> storage = new ConcurrentHashMap<>();

    @Override
    public T save(T entry) {
        //Для MVP считаем что у каждой сущности есть getId()
        String id = extractId(entry);
        storage.put(id,entry);
        return entry;
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(String id) {
        if (!storage.containsKey(id)){
            throw new EntityNotFoundException("Entity",id);
        }
        storage.remove(id);
    }

    @Override
    public boolean exists(String id) {
        return storage.containsKey(id);
    }

    @Override
    public long count() {
        return storage.size();
    }

    private String extractId(T entry) {
        try{
            var method = entry.getClass().getMethod("getId");
            Object idObj = method.invoke(entry);
            return idObj.toString();
        } catch (Exception e){
            throw new IllegalArgumentException("getId method req", e);
        }
    }

}
