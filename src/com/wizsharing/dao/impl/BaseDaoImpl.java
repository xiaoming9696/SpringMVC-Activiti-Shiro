package com.wizsharing.dao.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wizsharing.dao.IBaseDao;

@Repository
public class BaseDaoImpl<T> implements IBaseDao<T> {
	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Session getSession() {
	    return sessionFactory.getCurrentSession();
	}
	
	@Override
	public Serializable add(T bean) throws Exception{
		return this.getSession().save(bean) ;
	}
	
	@Override
	public void saveOrUpdate(T bean) throws Exception{
		this.getSession().saveOrUpdate(bean);
	}
	
	@Override
	public void delete(T bean) throws Exception{
		this.getSession().delete(bean);
	}
	
	@Override
	public void update(T bean) throws Exception{
		this.getSession().update(bean);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String queryString) throws Exception{
		List<T> list = this.getSession().createQuery(queryString).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getBean(Class<T> obj, Serializable id) throws Exception{
		return (T) getSession().get(obj, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByPage(String hql, int firstResult, int maxResult) throws Exception{
		Session session=sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResult);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T unique(String hql) throws Exception{
		Query query = this.getSession().createQuery(hql);
	    return (T) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByQuery(Class<T> entity, String[] columns, String[] querys){
		Session session=sessionFactory.getCurrentSession();
    	Criteria criteria = session.createCriteria(entity);
    	if(columns != null && querys != null){
    		if(columns.length > 0 && columns.length==querys.length){  
                for(int i = 0; i < columns.length; i++){  
                	criteria.add(Restrictions.like(columns[i], querys[i], MatchMode.START));
                }
                criteria.add(Restrictions.eq("isDelete", new Integer(0)));
                criteria.addOrder(Order.asc(columns[0]));
//                criteria.setMaxResults(15);
            }else{
            	return Collections.emptyList();
            }
    	}else{
    		return Collections.emptyList();
    	}
        List<T> list=criteria.list();
        return list;
	}

	@Override
	public Long getCount(String hql) throws Exception {
		Query query = this.getSession().createQuery(hql);
	    return (Long) query.uniqueResult();
	}

	@Override
	public Integer executeHql(String hql) throws Exception {
		return this.getSession().createQuery(hql).executeUpdate();
	}

	@Override
	public Integer executeHql(String hql, Map<String, Object> params) throws Exception {
		Query q = this.getSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String hql, Map<String, Object> params) throws Exception {
		Query q = this.getSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q.list();
	}
}
