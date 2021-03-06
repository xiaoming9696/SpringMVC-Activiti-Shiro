package com.wizsharing.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wizsharing.entity.Resource;
import com.wizsharing.entity.User;
import com.wizsharing.service.IResourceService;
import com.wizsharing.service.IUserService;
import com.wizsharing.util.UserUtil;


/**
 * 首页控制器
 *
 * @author zml
 */
@Controller
public class MainController {

	@Autowired
	private IUserService userService;
	
    @Autowired
    private IResourceService resourceService;
	
    @RequestMapping(value = "/north")
    public String north() {
        return "layout/north";
    }

    @RequestMapping(value = "/main")
    public String main() {
        return "layout/main";
    }
    
    @RequestMapping(value = "/center")
    public String center() {
    	return "layout/center";
    }
    
    @RequestMapping(value = "/south")
    public String south() throws Exception {
    	return "layout/south";
    }
    
    @RequestMapping("/menu")
    @ResponseBody
    public List<Resource> getMenu() throws Exception{
//    	String username = (String) SecurityUtils.getSubject().getPrincipal();
//    	User user = this.userService.getUserByName(username);
    	User u = UserUtil.getUserFromSession();
    	List<Resource> menus = this.resourceService.getTree(u.getGroup().getId());
    	return menus;
    }
    
    @RequestMapping("/")
    public String index() throws Exception {
        return "index";
    }
    
    @RequestMapping("/kickout")
    public String kickout() throws Exception {
    	return "error/kickout";
    }
    
}
