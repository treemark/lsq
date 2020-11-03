package com.lsq.security.mid.components.usermgmt.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lsq.security.db.entities.User;

@Service
public class UserServiceImpl implements UserService {

	@PersistenceContext
	EntityManager em;

	@Autowired
	TransactionTemplate txTemp;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void deleteUser(Long userId) {
		TransactionCallback<Object> cb = (TransactionStatus txstat) -> {
			User ref = em.getReference(User.class, userId);
			em.remove(ref);
			return null;
		};
		txTemp.execute(cb);
	}

	@Override
	public User loadUser(Long id) {
		TransactionCallback<User> cb = (TransactionStatus txstat) -> {
			return em.find(User.class, id);
		};
		return txTemp.execute(cb);
	}

	@Override
	public void updateUser(User user) {
		TransactionCallback<User> cb = (TransactionStatus txstat) -> {
			if (user.getId() != null && user.getId() > 0) {
				em.merge(user);
				return user;
			} else {
				user.setId(null);
				em.persist(user);
				return user;
			}

		};
		txTemp.execute(cb);
	}

	@Override
	public List<User> loadAllUsers() {
		TransactionCallback<List<User>> cb = (TransactionStatus txstat) -> {
			return em.createQuery("select u from User u").getResultList();
		};
		return txTemp.execute(cb);
	}

	@Override
	public List<User> findUsers(String criteria, Integer start, Integer howMany) {
		logger.info("find users: " + criteria);
		TransactionCallback<List<User>> cb = (TransactionStatus txstat) -> {
			Query q = em.createQuery(
					"select u from User u where (:criteria='' or u.name like CONCAT('%',:criteria,'%')  or u.email like CONCAT('%',:criteria,'%')) ");
			q.setFirstResult(start);
			q.setMaxResults(howMany);
			q.setParameter("criteria", criteria);
			return q.getResultList();
		};
		List<User> result = txTemp.execute(cb);
		logger.info("found users: " + result.size());

		return result;
	}

	@Override
	public boolean isUniqueEmail(String email, Long excludeId) {
		Query q = em.createQuery(
				"select u.id from User u where u.id != :excludeId and u.email = :email");
		q.setParameter("email", email);
		q.setParameter("excludeId", excludeId);
		List result = q.getResultList();
		return result.size() == 0;
	}

	@Override
	public User findByEmail(String email) {
		try {
			Query q = em.createQuery("select u from User u where  u.email = :email");
			q.setParameter("email", email);
			return (User) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
