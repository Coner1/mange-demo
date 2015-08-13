package uap.web.core.jdbc;

import java.io.Serializable;
import java.util.List;

public interface IBaseDao<T> {

	public static final String SQL_INSERT = "insert";
	public static final String SQL_UPDATE = "update";
	public static final String SQL_DELETE = "delete";

	public void save(T entity);

	public void update(T entity);

	public void delete(T entity);

	public void delete(Serializable id);
	
	public void deleteAll();
	
	public T findById(Serializable id);
	
	public List<T> findAll();

}
