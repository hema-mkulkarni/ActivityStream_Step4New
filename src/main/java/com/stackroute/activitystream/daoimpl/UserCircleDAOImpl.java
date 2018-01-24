package com.stackroute.activitystream.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.activitystream.dao.CircleDAO;
import com.stackroute.activitystream.dao.UserCircleDAO;
import com.stackroute.activitystream.dao.UserDAO;
import com.stackroute.activitystream.model.Circle;
import com.stackroute.activitystream.model.UserCircle;

/*
* This class is implementing the UserCircleDAO interface. This class has to be annotated with 
* @Repository annotation.
* @Repository - is an annotation that marks the specific class as a Data Access Object, 
* thus clarifying it's role.
* @Transactional - The transactional annotation itself defines the scope of a single database 
* 					transaction. The database transaction happens inside the scope of a persistence 
* 					context.  
* */
@Repository("userCircleDAO")
@Transactional
public class UserCircleDAOImpl implements UserCircleDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.
	 */
	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CircleDAO circleDAO;

	/*
	 * Add a user to a circle
	 */
	public boolean addUser(String username, String circleName) {
		try {
			if (circleDAO.get(circleName) != null && userDAO.get(username) != null) {
				UserCircle userCircle = new UserCircle(username, circleName);
				sessionFactory.getCurrentSession().save(userCircle);
				return true;
			} else
				return false;
		} catch (HibernateException e) {
			return false;
		}
	}

	/*
	 * Remove a user from a circle
	 */
	public boolean removeUser(String username, String circleName) {
		try {
			UserCircle userCircle = get(username, circleName);
			if (userCircle != null) {
				sessionFactory.getCurrentSession().delete(userCircle);
				return true;
			} else {
				return false;
			}
		} catch (HibernateException e) {
			return false;
		}
	}

	/*
	 * Retrieve unique UserCircle object which contains a specific username and
	 * circleName
	 */
	public UserCircle get(String username, String circleName) {
		try {
			Query query = sessionFactory.getCurrentSession()
					.createQuery("from UserCircle where username= :user and circleName= :circle");
			query.setString("user", username);
			query.setString("circle", circleName);
			List userCircles = query.list();
			UserCircle userCircle = null;
			if (userCircles.size() > 0)
				userCircle = (UserCircle) userCircles.get(0);
			return userCircle;
		} catch (HibernateException e) {
			return null;
		}

	}

	/*
	 * Retrieve all subscribed circles by a user
	 */
	@SuppressWarnings("unchecked")
	public List<String> getMyCircles(String username) {
		try {
			List<String> userCircles = (List<String>) sessionFactory.getCurrentSession()
					.createQuery("select circleName from UserCircle where username=:search")
					.setParameter("search", username).list();
			return userCircles;
		} catch (HibernateException e) {
			return null;
		}

	}
}