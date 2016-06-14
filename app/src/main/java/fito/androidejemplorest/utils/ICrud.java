package fito.androidejemplorest.utils;

import java.util.List;

/**
 * Created by fito on 8/12/15.
 */
public interface ICrud {
    public int create(Object item);
    public int update(Object item);
    public int delete(Object item);
    public Object findById(int id);
    public List<?> findAll();
}