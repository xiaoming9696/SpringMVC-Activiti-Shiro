package com.wizsharing.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizsharing.dao.IJdbcDao;
import com.wizsharing.entity.GroupAndResource;
import com.wizsharing.service.IBaseService;
import com.wizsharing.service.IGroupAndResourceService;

@Service
public class GroupAndResourceServiceImpl implements IGroupAndResourceService {

	@Autowired 
	private IBaseService<GroupAndResource> baseService;
	
	@Autowired
	protected IJdbcDao jdbcDao;
	 
	@Override
	public List<GroupAndResource> getResource(Integer groupId) throws Exception {
		List<GroupAndResource> list = this.baseService.findByWhere("GroupAndResource", new String[]{"groupId"}, new String[]{groupId.toString()},null, null);
		if (list == null) {
			return Collections.emptyList();
		}else{
			return list;
		}
	}
	
	@Override
	public void doAdd(GroupAndResource gar) throws Exception {
		this.baseService.add(gar);
	}

	@Override
	public void doDelete(GroupAndResource gar) throws Exception {
		this.baseService.delete(gar);
	}

	@Override
	public Integer doDelByGroup(Integer groupId) throws Exception {
		String sql = "delete from T_GROUP_RESOURCE where group_id=:groupId ";
		Map<String, Object> paramMap = new HashMap<String, Object>();  
	    paramMap.put("groupId", groupId);  
		return this.jdbcDao.delete(sql, paramMap);
	}

	@Override
	public void doDelByResource(String resourceId) throws Exception {
		String sql = "delete from T_GROUP_RESOURCE where resource_id=:resourceId ";
		Map<String, Object> paramMap = new HashMap<String, Object>();  
	    paramMap.put("resourceId", resourceId);  
		this.jdbcDao.delete(sql, paramMap);
	}
}
