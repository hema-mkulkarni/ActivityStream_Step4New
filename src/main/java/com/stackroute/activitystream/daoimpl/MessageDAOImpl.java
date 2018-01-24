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
import com.stackroute.activitystream.dao.MessageDAO;
import com.stackroute.activitystream.dao.UserCircleDAO;
import com.stackroute.activitystream.dao.UserDAO;
import com.stackroute.activitystream.model.Message;
import com.stackroute.activitystream.model.UserTag;

/*
* This class is implementing the MessageDAO interface. This class has to be annotated with 
* @Repository annotation.
* @Repository - is an annotation that marks the specific class as a Data Access Object, 
* thus clarifying it's role.
* @Transactional - The transactional annotation itself defines the scope of a single database 
* 					transaction. The database transaction happens inside the scope of a persistence 
* 					context.  
* */
@Repository("messageDAO")
@Transactional
public class MessageDAOImpl implements MessageDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.
	 */
	@Autowired
	SessionFactory sessionFactory;
	/*
	 * Autowiring should be implemented for CircleDAO
	 */
	@Autowired
	CircleDAO circleDAO;
	/*
	 * Autowiring should be implemented for UserDAO.
	 */
	@Autowired
	UserDAO userDAO;
	/*
	 * Autowiring should be implemented for UserCircleDAO.
	 */
	@Autowired
	UserCircleDAO userCircleDAO;

	/*
	 * Retrieve messages from a specific circle. For improved performace, we
	 * will implement retrieving the messages partially by implementing
	 * pagination
	 */
	public List<Message> getMessagesFromCircle(String circleName, int pageNumber) {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			Long count = (Long) session.createQuery("select count(messageId) from Message").uniqueResult();
			int pageSize = 10;
			Query query = session.createQuery("from Message where circleName=:circlename");
			query.setString("circlename", circleName);
			query.setFirstResult((pageNumber - 1) * pageSize);
			query.setMaxResults(pageSize);
			List<Message> messageList = (List<Message>) query.list();
			if (messageList.size() > 0)
				return messageList;
			else
				return null;

		} catch (HibernateException e) {
			return null;
		}
	}

	/*
	 * Retrieve messages between two users. Please note that in a one to one
	 * conversation, both users can act sometimes as a sender and sometimes as a
	 * recipient. For improved performace, we will implement retrieving the
	 * messages partially by implementing pagination
	 */
	public List<Message> getMessagesFromUser(String username, String otherUsername, int pageNumber) {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			Long count = (Long) session.createQuery("select count(messageId) from Message").uniqueResult();
			int pageSize = 10;
			Query query = session.createQuery("from Message where senderName=:username and receiverId=:otherUser");
			query.setString("usernamename", username);
			query.setString("otherUser", otherUsername);
			query.setFirstResult((pageNumber - 1) * pageSize);
			query.setMaxResults(pageSize);
			List<Message> messageList = (List<Message>) query.list();
			if (messageList.size() > 0)
				return messageList;
			else
				return null;

		} catch (HibernateException e) {
			return null;
		}
	}

	/*
	 * Retrieve messages from all circles subscribed by a specific user. For
	 * improved performace, we will implement retrieving the messages partially
	 * by implementing pagination
	 */
	public List<Message> getMessages(String username, int pageNumber) {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			Long count = (Long) session.createQuery("select count(messageId) from Message").uniqueResult();
			int pageSize = 10;
			Query query = session.createQuery("from Message where senderName=:username");
			List<Message> messageList = (List<Message>) query.setParameter("username", username).list();
			return messageList;

		} catch (HibernateException e) {
			return null;
		}
	}

	/*
	 * send messages from a specific circle. The posted message should have the
	 * current timestamp as the posted timestamp.
	 */
	public boolean sendMessageToCircle(String circleName, Message message) {
		try {
			if (circleDAO.get(circleName) != null && userDAO.get(message.getSenderName()) != null) {
				message.setCircleName(circleName);
				message.setPostedDate();
				sessionFactory.getCurrentSession().persist(message);
				return true;
			} else
				return false;

		} catch (HibernateException e) {
			return false;
		}
	}

	/*
	 * Send message to a specific user
	 */
	public boolean sendMessageToUser(String username, Message message) {
		try {
			if (userDAO.get(username) != null && userDAO.get(message.getSenderName()) != null) {
				message.setReceiverId(username);
				message.setPostedDate();
				sessionFactory.getCurrentSession().save(message);
				return true;
			} else
				return false;

		} catch (HibernateException e) {
			return false;
		}
	}

	/*
	 * Retrieve all the tags available in the messages
	 */
	public List<String> listTags() {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("select tag from Message");
			List<String> messageList = (List<String>) query.list();
			return messageList;

		} catch (HibernateException e) {
			return null;
		}
	}

	/*
	 * Retrieve all tags subscribed by a user
	 */
	public List<String> listMyTags(String username) {
		try {
			if (userDAO.get(username) != null) {
				Query query = sessionFactory.getCurrentSession().createQuery("from Message where senderName=:username");
				List<String> messageList = (List<String>) query.setParameter("username", username).list();
				return messageList;
			} else
				return null;

		} catch (HibernateException e) {
			return null;
		}
	}

	/*
	 * Retrieve all messages containing a specific tag. For improved performace,
	 * we will implement retrieving the messages partially by implementing
	 * pagination
	 */
	public List<Message> showMessagesWithTag(String tag, int pageNumber) {
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			Long count = (Long) session.createQuery("select count(messageId) from Message").uniqueResult();
			int pageSize = 10;

			Query<Message> query = session.createQuery("from Message where tag = :tag");
			query.setString("tag", tag);

			query.setFirstResult((pageNumber - 1) * pageSize);
			query.setMaxResults(pageSize);

			List<Message> messages = query.list();
			if (messages.size() > 0)
				return messages;
			else
				return null;
		} catch (HibernateException e) {
			return null;
		}
	}

	/*
	 * Subscribe user to a tag. Please implement validation to check whether the
	 * user and tag both exists.
	 */
	public boolean subscribeUserToTag(String username, String tag) {
		try {

			if (userDAO.get(username) != null) {
				UserTag userTag = new UserTag();
				userTag.setUserName(username);
				userTag.setTag(tag);
				sessionFactory.getCurrentSession().persist(userTag);
				return true;
			} else
				return false;
		} catch (HibernateException e) {
			return false;
		}
	}

	/*
	 * Unsubscribe a user from a tag. Please implement validation to check
	 * whether the user has subscribed to the tag or not
	 */
	public boolean unsubscribeUserToTag(String username, String tag) {
		try {

			if (userDAO.get(username) != null) {
				UserTag userTag = new UserTag();
				userTag.setUserName(username);
				userTag.setTag(tag);
				sessionFactory.getCurrentSession().delete(userTag);
				return true;
			} else
				return false;
		} catch (HibernateException e) {
			return false;
		}
	}

	/*
	 * Retrieve UserTag object for a username and a tag
	 */
	public UserTag getUserTag(String username, String tag) {
		try {
			if (userDAO.get(username) != null) {
				Query<UserTag> query = sessionFactory.getCurrentSession()
						.createQuery("from UserTag where username = :userName and tag = :tag");
				query.setString("userName", username);
				query.setString("tag", tag);
				List<UserTag> userTags = query.list();
				UserTag userTag = null;
				if (userTags.size() > 0)
					userTag = userTags.get(0);
				return userTag;
			} else
				return null;
		} catch (HibernateException e) {
			return null;
		}

	}
}