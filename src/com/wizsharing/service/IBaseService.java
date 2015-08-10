package com.wizsharing.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.wizsharing.pagination.Page;

public interface IBaseService<T> {
	
	public List<T> getAllList(String tableSimpleName, String[] orderBy, String[] orderType) throws Exception;
	 
	public T getUnique(String tableSimpleName,String[] columns,String[] values) throws Exception;
	 
	public List<T> findByWhere(String tableSimpleName,String[] columns,String[] values, String[] orderBy, String[] orderType) throws Exception;
	 
	public Integer getCount(String tableSimpleName, String[] columns, String[] values) throws Exception;
	 
	public Serializable add(T bean) throws Exception;
	 
	public void saveOrUpdate(T bean) throws Exception;
	
	public void delete(T bean) throws Exception;
	
	public void update(T bean) throws Exception;
	 
	public T getBean(final Class<T> obj,final Serializable id) throws Exception;
	 
	public List<T> getRangeDate(String tableSimpleName,String[] columns,String[] values) throws Exception;
	
	public List<T> findByQuery(final Class<T> entity, String[] columns, String[] querys) throws Exception;
	
	public List<T> findByPage(String tableSimpleName,String[] columns,String[] values, String[] orderBy, String[] orderType, Page<T> page) throws Exception;
	
	public Integer executeHql(final String hql) throws Exception;
	
	public Integer executeHql(String hql, Map<String, Object> params) throws Exception;
}
