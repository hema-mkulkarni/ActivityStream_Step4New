package com.stackroute.activitystream.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.activitystream.dao.UserDAO;
import com.stackroute.activitystream.model.User;

/*
* This class is implementing the UserCircleDAO interface. This class has to be annotated with 
* @Repository annotation.
* @Repository - is an annotation that marks the specific class as a Data Access Object, 
* thus clarifying it's role.
* @Transactional - The transactional annotation itself defines the scope of a single database 
* 					transaction. The database transaction happens inside the scope of a persistence 
* 					context.  
* */
@Repository("userDAO")
@Transactional
public class UserDAOImpl implements UserDAO {
	/*
	 * Autowiring should be implemented for the SessionFactory.
	 */
	@Autowired
	SessionFactory sessionFactory;

	public UserDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/*
	 * Create a new user
	 */
	public boolean save(User user) {
		Session session = null;
		session = sessionFactory.openSession();
		session.beginTransaction();
		session.saveOrUpdate(user);
		session.getTransaction().commit();
		session.close();
		return true;
	}

	/*
	 * Update an existing user
	 */
	public boolean update(User user) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(user);
		session.getTransaction().commit();
		session.close();
		return true;
	}

	/*
	 * Remove an existing user
	 */
	public boolean delete(User user) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.delete(user);
		session.getTransaction().commit();
		session.close();
		return true;
	}

	/*
	 * Retrieve all available user
	 */
	public List<User> list() {
		Session session = null;
		session = sessionFactory.openSession();
		Query<User> query = session.createQuery("from User");
		List<User> users = query.list();
		session.close();
		return users;
	}

	/*
	 * validate an user
	 */
	public boolean validate(String username, String password) {
		User user = null;
		Session session = null;
		session = sessionFactory.openSession();
		if (exists(username)) {
			Query<User> query = session.createQuery("from User where username = :username");
			query.setString("username", username);
			List<User> users = query.list();
			user = users.get(0);
			session.close();
			if (user.getPassword().equals(password))
				return true;
			else
				return false;
		}
		return false;
	}

	/*
	 * Retrieve details of an user
	 */
	public User get(String username) {
		User user = null;
		Session session = null;
		session = sessionFactory.openSession();
		Query<User> query = session.createQuery("from User where username = :username");
		query.setString("username", username);
		List<User> users = query.list();
		session.close();
		if (users.size() > 0)
			user = users.get(0);
		return user;
	}

	/*
	 * check whether a user exists with a given userId
	 */
	public boolean exists(String username) {
		User user = null;
		Session session = null;
		session = sessionFactory.openSession();
		Query<User> query = session.createQuery("from User where username = :username");
		query.setString("username", username);
		List<User> users = query.list();
		session.close();
		if (users.size() > 0) {
			user = users.get(0);
			return true;
		}
		return false;
	}
}