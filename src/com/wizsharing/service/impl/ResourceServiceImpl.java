package com.wizsharing.service.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizsharing.dao.IJdbcDao;
import com.wizsharing.entity.Parameter;
import com.wizsharing.entity.Resource;
import com.wizsharing.pagination.Page;
import com.wizsharing.service.IBaseService;
import com.wizsharing.service.IResourceService;
import com.wizsharing.util.BeanUtils;

@Service
public class ResourceServiceImpl implements IResourceService {

    @Autowired 
	private IBaseService<Resource> baseService;
	
	@Autowired
	protected IJdbcDao jdbcDao;
    
	@Override
	public Resource getPermissions(Integer id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		Resource res = this.baseService.findUnique("Resource", map);
		return res;
	}
	
	@Override
	public List<Resource> getTree(Integer groupId) throws Exception {
		if(!BeanUtils.isBlank(groupId)){
			String hql = "select r from Resource r, GroupAndResource gr where " +
					     "r.id = gr.resourceId and r.isDelete = 0 and r.type = 'menu' and gr.groupId = "+groupId +
					     " order by r.parentId, r.sort";
			return this.baseService.find(hql);
		}else{
			return Collections.emptyList();
		}
	}
	
	@Override
	public List<Resource> getAllResource() throws Exception {
		return this.baseService.findByWhere("Resource", null);
	}

	@Override
	public Serializable  doAdd(Resource entity) throws Exception {
		Serializable id = this.baseService.add(entity);
		return id;
	}
	
	@Override
	public Resource getResource(String id) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return this.baseService.findUnique("Resource", map);
	}

	@Override
	public void doUpdate(Resource entity) throws Exception {
		this.baseService.update(entity);
		
	}

	@Override
	public void doDelete(Resource entity) throws Exception {
		this.baseService.delete(entity);
		
	}

	@Override
	public List<Resource> getResourceList(Parameter param, Page<Resource> page) throws Exception {
		return this.baseService.findListPage("Resource", param, null, page);
	}

	@Override
	public Integer doDelete(String id) throws Exception {
		String sql = "delete from T_RESOURCE where id=:id ";
		Map<String, Object> paramMap = new HashMap<String, Object>();  
	    paramMap.put("id", id);  
		return this.jdbcDao.delete(sql, paramMap);
		
	}

	@Override
	public Integer doUpdateName(String id, String name) throws Exception {
		String hql = "update Resource set name='"+name+"' where id="+id;
		return this.baseService.executeHql(hql);
	}

	@Override
	public Integer doUpdateAvailable(String id) throws Exception {
		String hql = "update Resource set available=0 where id="+id;
		return this.baseService.executeHql(hql);
	}

	@Override
	public Integer doUpdateSort(String id, String sort) throws Exception {
		if(!BeanUtils.isBlank(sort)){
			String hql = "update Resource set sort="+sort.toString()+" where id=" + id;
			return this.baseService.executeHql(hql);
		}else{
			return 0;
		}
	}

}
