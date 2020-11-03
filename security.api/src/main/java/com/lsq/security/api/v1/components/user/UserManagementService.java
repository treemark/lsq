package com.lsq.security.api.v1.components.user;

import java.util.List;

public interface UserManagementService {

	public List<UserBean> listAllUsers();

	public void deleteUser(Long userId);

	public UserBean loadUser(Long id);

	public void updateUser(UserBean u);

	public boolean isUniqueEmail(String email, Long excludeId);

	public UserBean findByEmail(String email);

	List<UserBean> findUsers(String criteria, Integer start, Integer howMany);
}
