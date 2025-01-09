package services;

import java.util.List;

public abstract class Service<T>  {
    public abstract List<T> getAll() throws Exception;
    //public abstract T getById(int id) throws Exception;
    public abstract void store(T pojo) throws Exception;
    //public abstract void delete(Integer id) throws Exception;
    public abstract void update(T pojo) throws Exception;

}
