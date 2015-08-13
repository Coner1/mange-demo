package uap.web.core.jdbc;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class BaseJdbcDao<T> implements IBaseDao<T> {

	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public BaseJdbcDao() {
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		entityClass = (Class<T>) type.getActualTypeArguments()[0];
	}

	
	public void save(T entity) {
		String sql = this.makeSql(SQL_INSERT);
		Object[] args = this.setArgs(entity, SQL_INSERT);
		int[] argTypes = this.setArgTypes(entity, SQL_INSERT);
		jdbcTemplate.update(sql.toString(), args, argTypes);
	}

	
	public void update(T entity) {
		String sql = this.makeSql(SQL_UPDATE);
		Object[] args = this.setArgs(entity, SQL_UPDATE);
		int[] argTypes = this.setArgTypes(entity, SQL_UPDATE);
		jdbcTemplate.update(sql, args, argTypes);
	}

	
	public void delete(T entity) {
		String sql = this.makeSql(SQL_DELETE);
		Object[] args = this.setArgs(entity, SQL_DELETE);
		int[] argTypes = this.setArgTypes(entity, SQL_DELETE);
		jdbcTemplate.update(sql, args, argTypes);
	}

	
	public void delete(Serializable id) {
		String sql = " DELETE FROM " + entityClass.getSimpleName() + " WHERE id=?";
		jdbcTemplate.update(sql, id);
	}

	
	public void deleteAll() {
		String sql = " TRUNCATE TABLE " + entityClass.getSimpleName();
		jdbcTemplate.execute(sql);
	}

	public T findById(Serializable id) {
		String sql = "SELECT * FROM " + entityClass.getSimpleName() + " WHERE id=?";
		RowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
		return jdbcTemplate.query(sql, rowMapper, id).get(0);
	}

	
	public List<T> findAll() {
		String sql = "SELECT * FROM " + entityClass.getSimpleName();
		RowMapper<T> rowMapper = BeanPropertyRowMapper.newInstance(entityClass);
		return jdbcTemplate.query(sql, rowMapper);
	}

	// 组装SQL
	private String makeSql(String sqlFlag) {
		StringBuffer sql = new StringBuffer();
		Field[] fields = entityClass.getDeclaredFields();
		if (sqlFlag.equals(SQL_INSERT)) {
			sql.append(" INSERT INTO " + entityClass.getSimpleName());
			sql.append("(");
			for (int i = 0; fields != null && i < fields.length; i++) {
				fields[i].setAccessible(true); // 暴力反射
				String column = fields[i].getName();
				sql.append(column).append(",");
			}
			sql = sql.deleteCharAt(sql.length() - 1);
			sql.append(") VALUES (");
			for (int i = 0; fields != null && i < fields.length; i++) {
				sql.append("?,");
			}
			sql = sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
		} else if (sqlFlag.equals(SQL_UPDATE)) {
			sql.append(" UPDATE " + entityClass.getSimpleName() + " SET ");
			for (int i = 0; fields != null && i < fields.length; i++) {
				fields[i].setAccessible(true); // 暴力反射
				String column = fields[i].getName();
				if (column.equals("id")) { // id 代表主键
					continue;
				}
				sql.append(column).append("=").append("?,");
			}
			sql = sql.deleteCharAt(sql.length() - 1);
			sql.append(" WHERE id=?");
		} else if (sqlFlag.equals(SQL_DELETE)) {
			sql.append(" DELETE FROM " + entityClass.getSimpleName() + " WHERE id=?");
		}
		System.out.println("SQL=" + sql);
		return sql.toString();

	}

	// 设置参数
	private Object[] setArgs(T entity, String sqlFlag) {
		Field[] fields = entityClass.getDeclaredFields();
		if (sqlFlag.equals(SQL_INSERT)) {
			Object[] args = new Object[fields.length];
			for (int i = 0; args != null && i < args.length; i++) {
				try {
					fields[i].setAccessible(true); // 暴力反射
					args[i] = fields[i].get(entity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return args;
		} else if (sqlFlag.equals(SQL_UPDATE)) {
			Object[] tempArr = new Object[fields.length];
			for (int i = 0; tempArr != null && i < tempArr.length; i++) {
				try {
					fields[i].setAccessible(true); // 暴力反射
					tempArr[i] = fields[i].get(entity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Object[] args = new Object[fields.length];
			System.arraycopy(tempArr, 1, args, 0, tempArr.length - 1); // 数组拷贝
			args[args.length - 1] = tempArr[0];
			return args;
		} else if (sqlFlag.equals(SQL_DELETE)) {
			Object[] args = new Object[1]; // 长度是1
			fields[0].setAccessible(true); // 暴力反射
			try {
				args[0] = fields[0].get(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return args;
		}
		return null;

	}

	// 设置参数类型（写的不全，只是一些常用的）
	private int[] setArgTypes(T entity, String sqlFlag) {
		Field[] fields = entityClass.getDeclaredFields();
		if (sqlFlag.equals(SQL_INSERT)) {
			int[] argTypes = new int[fields.length];
			try {
				for (int i = 0; argTypes != null && i < argTypes.length; i++) {
					fields[i].setAccessible(true); // 暴力反射
					if (fields[i].get(entity).getClass().getName().equals("java.lang.String")) {
						argTypes[i] = Types.VARCHAR;
					} else if (fields[i].get(entity).getClass().getName().equals("java.lang.Double")) {
						argTypes[i] = Types.DECIMAL;
					} else if (fields[i].get(entity).getClass().getName().equals("java.lang.Integer")) {
						argTypes[i] = Types.INTEGER;
					} else if (fields[i].get(entity).getClass().getName().equals("java.util.Date")) {
						argTypes[i] = Types.DATE;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return argTypes;
		} else if (sqlFlag.equals(SQL_UPDATE)) {
			int[] tempArgTypes = new int[fields.length];
			int[] argTypes = new int[fields.length];
			try {
				for (int i = 0; tempArgTypes != null && i < tempArgTypes.length; i++) {
					fields[i].setAccessible(true); // 暴力反射
					if (fields[i].get(entity).getClass().getName().equals("java.lang.String")) {
						tempArgTypes[i] = Types.VARCHAR;
					} else if (fields[i].get(entity).getClass().getName().equals("java.lang.Double")) {
						tempArgTypes[i] = Types.DECIMAL;
					} else if (fields[i].get(entity).getClass().getName().equals("java.lang.Integer")) {
						tempArgTypes[i] = Types.INTEGER;
					} else if (fields[i].get(entity).getClass().getName().equals("java.util.Date")) {
						tempArgTypes[i] = Types.DATE;
					}
				}
				System.arraycopy(tempArgTypes, 1, argTypes, 0, tempArgTypes.length - 1); // 数组拷贝
				argTypes[argTypes.length - 1] = tempArgTypes[0];

			} catch (Exception e) {
				e.printStackTrace();
			}
			return argTypes;

		} else if (sqlFlag.equals(SQL_DELETE)) {
			int[] argTypes = new int[1]; // 长度是1
			try {
				fields[0].setAccessible(true); // 暴力反射
				if (fields[0].get(entity).getClass().getName().equals("java.lang.String")) {
					argTypes[0] = Types.VARCHAR;
				} else if (fields[0].get(entity).getClass().getName().equals("java.lang.Integer")) {
					argTypes[0] = Types.INTEGER;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return argTypes;
		}
		return null;
	}
}
