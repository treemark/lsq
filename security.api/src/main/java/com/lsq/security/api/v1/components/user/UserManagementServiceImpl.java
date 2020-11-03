package com.lsq.security.api.v1.components.user;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lsq.security.db.entities.User;
import com.lsq.security.mid.components.usermgmt.service.UserService;

import io.swagger.annotations.Api;

@Service
@RestController
@Api(tags = { "UserManagement" })
@RequestMapping(value = "/usermgmt", produces = "application/json; charset=UTF-8")

public class UserManagementServiceImpl implements UserManagementService {

	@PersistenceContext
	EntityManager em;

	@Autowired
	TransactionTemplate txTemp;

	@Autowired
	UserService userSrv;

	Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);

	@Override
	public void deleteUser(Long userId) {
		userSrv.deleteUser(userId);

	}

	@GetMapping("/user/{id}")
	public UserBean loadUser(@PathVariable("id") Long id) {
		return toBean(userSrv.loadUser(id));
	}

	private UserBean toBean(User user) {
		if (user == null)
			return null;
		UserBean bean = new UserBean();
		BeanUtils.copyProperties(user, bean);
		return bean;
	}

	@Override
	@PostMapping("/user")
	public void updateUser(@RequestBody UserBean user) {
		userSrv.updateUser(toEntity(user));
	}

	public User toEntity(UserBean user) {
		User u = new User();
		BeanUtils.copyProperties(user, u);
		if (u.getActive() == null)
			u.setActive(true);
		if (u.getCreateDate() == null)
			u.setCreateDate(new Date());
		return u;
	}

	@GetMapping("/users")
	@Override
	public List<UserBean> listAllUsers() {
		return toBeans(userSrv.loadAllUsers());
	}

	@GetMapping("/find")
	@Override
	public List<UserBean> findUsers(@RequestParam(defaultValue = "") String criteria,
			@RequestParam(defaultValue = "0") Integer start, @RequestParam(defaultValue = "20") Integer howMany) {
		return toBeans(userSrv.findUsers(criteria, start, howMany));
	}

	private List<UserBean> toBeans(List<User> entites) {
		return entites.stream().map(s -> toBean(s)).collect(Collectors.toList());
	}

	@Override
	@PostMapping("/isUniqueEmail")
	public boolean isUniqueEmail(@RequestParam String email, @RequestParam Long excludeId) {
		return userSrv.isUniqueEmail(email, excludeId);
	}

	@Override
	@GetMapping("/findByEmail")
	public UserBean findByEmail(@RequestParam() String email) {
		return toBean(userSrv.findByEmail(email));
	}
}
