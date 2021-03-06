package test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wizsharing.dao.IJdbcDao;
import com.wizsharing.entity.User;
import com.wizsharing.service.IBaseService;
import com.wizsharing.service.IGroupService;
import com.wizsharing.service.IUserService;
import com.wizsharing.service.impl.PasswordHelper;
import com.wizsharing.util.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/applicationContext.xml", "classpath*:/springMVC.xml" })
public class UserTest {
	
	@Autowired
	private IJdbcDao jdbcDao;
	
    @Autowired
    private PasswordHelper passwordHelper;
    
    @Autowired 
	private IUserService userService;
    
    @Autowired
    private IGroupService groupService;
    
	@Autowired 
	private IBaseService<User> baseService;
	
    @Test
	public void userTest() throws Exception{
    	/*Group group = this.groupService.getGroupById("1");
    	assertEquals( group.getName(), "group1" );
    	assertEquals( group.getType(), "group_A" );
    	
		User user1 = new User();
		user1.setName("admin");
		user1.setPasswd("123");
		user1.setRegisterDate(new Date());
		user1.setIsDelete(0);
		user1.setGroup(group);
		Serializable id1 = this.userService.doAdd(user1, false);
		
		for(int i=0; i<10; i++){
			User user = new User();
			user.setName("user"+i);
			user.setPasswd("123");
			user.setRegisterDate(new Date());
			user.setIsDelete(0);
			user.setGroup(group);
			Serializable id = this.userService.doAdd(user, false);
		}*/
		
		
		//修改密码时判断，原密码是否输入正确
/*		User u = this.userService.getUserById((Integer) id);
		String oldPass = u.getPasswd();
		
		u.setPasswd("123");
		this.passwordHelper.encryptPassword(u);
		
        String newPass = u.getPasswd(); 
        assertEquals(oldPass, newPass);*/
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("name", "user3");
    	List<User> list = this.baseService.findByWhere("User", params);
    	assertEquals(list.size(), 1);
    	
    	User user = this.baseService.findUnique("User", params);
    	assertEquals(user.getName(), "user3");
	}
    public static void main(String[] args) {
		System.out.println(Constants.getPrimaryKeyByUUID());
	}
}
