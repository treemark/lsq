package com.lsq.security.mid.components.usermgmt.service;

import java.util.List;

import com.lsq.security.db.entities.User;

public interface UserService {

	void deleteUser(Long userId);

	User loadUser(Long id);

	void updateUser(User user);

	List<User> loadAllUsers();

	List<User> findUsers(String criteria, Integer start, Integer howMany);

	boolean isUniqueEmail(String email, Long excludeId);

	User findByEmail(String email);

}
