package com.stackroute.activitystream.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.activitystream.dao.CircleDAO;
import com.stackroute.activitystream.dao.UserDAO;
import com.stackroute.activitystream.model.Circle;

/*
* This class is implementing the CircleDAO interface. This class has to be annotated with 
* @Repository annotation.
* @Repository - is an annotation that marks the specific class as a Data Access Object, 
* thus clarifying it's role.
* @Transactional - The transactional annotation itself defines the scope of a single database 
* 					transaction. The database transaction happens inside the scope of a persistence 
* 					context.  
* */
@Repository("circleDAO")
@Transactional
public class CircleDAOImpl implements CircleDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.
	 */
	@Autowired
	private SessionFactory sessionFactory;
	/*
	 * Autowiring should be implemented for UserDAO.
	 */
	@Autowired
	UserDAO userDAO;

	/*
	 * Create a new circle
	 */
	public boolean save(Circle circle) {
		Session session = null;
		session = sessionFactory.openSession();
		if (get(circle.getCircleName()) == null) {

			if (userDAO.get(circle.getCreatorId()) != null) {
				session.beginTransaction();
				session.saveOrUpdate(circle);
				session.getTransaction().commit();
				System.out.println("Circle Saved");
				session.close();
				return true;
			} else
				return false;
		}

		return false;
	}

	/*
	 * Update an existing circle
	 */
	public boolean update(Circle circle) {
		Session session = null;
		session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(circle);
		session.getTransaction().commit();
		session.close();
		return true;
	}

	/*
	 * delete an existing circle
	 */
	public boolean delete(Circle circle) {
		Session session = null;
		session = sessionFactory.openSession();
		session.beginTransaction();
		session.delete(circle);
		session.getTransaction().commit();
		session.close();

		return true;
	}

	/*
	 * Retrieve a specific circle
	 */
	public Circle get(String circleName) {
		Session session = null;
		session = sessionFactory.openSession();
		Circle circle = null;
		Query<Circle> query = session.createQuery("from Circle where circleName = :circleName");
		query.setString("circleName", circleName);
		List<Circle> circleList = query.list();
		if (circleList.size() > 0)
			circle = circleList.get(0);
		session.close();

		return circle;
	}

	/*
	 * retrieving all circles
	 */
	@SuppressWarnings("unchecked")
	public List<Circle> getAllCircles() {
		try {
			List<Circle> circle = (List<Circle>) sessionFactory.getCurrentSession().createQuery("from Circle").list();
			return circle;
		} catch (HibernateException e) {
			return null;
		}
	}

	/*
	 * Retrieving all circles that matches a search string
	 */ public List<Circle> getAllCircles(String searchString) {
		Session session = null;
		session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Circle.class);
		List<Circle> circleList = criteria.list();
		session.close();

		return circleList;
	}
}