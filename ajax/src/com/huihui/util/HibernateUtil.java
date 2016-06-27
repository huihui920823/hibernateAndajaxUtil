package com.huihui.util;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

final public class HibernateUtil {
	private static SessionFactory sessionFactory = null;
	// 使用线程局部模式
	private static ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

	private HibernateUtil() {
	}

	static {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}

	/**
	 *  获取一个全新的Session
	 * @return
	 */
	public static Session openSession() {
		return sessionFactory.openSession();
	}

	/**
	 *  获取和线程关联的Session
	 * @return
	 */
	public static Session getCurrentSession() {
		Session session = threadLocal.get();
		// 判断是否得到了
		if (session == null) {
			session = sessionFactory.openSession();
			// 把session对象设置到threadLocal中去，相当于该Session已经和线程绑定
			threadLocal.set(session);
		}
		return session;
	}
	
	/**
	 * 统一的修改和删除
	 * @param hql
	 * @param parameters
	 */
	public static void executeUpdate(String hql,String[] parameters){
		Session session = null;
		Transaction transaction = null;
		try {
			session = openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			if(parameters!=null&&parameters.length>0){
				for (int i = 0; i < parameters.length; i++) {
					query.setString(i, parameters[i]);
				}
			}
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			if(transaction!=null){
				transaction.rollback();
			}
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally{
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	
	/**
	 * 统一的添加方法
	 * @param obj
	 */
	public static void save(Object obj){
		Session session = null;
		Transaction transaction = null;
		try {
			session = openSession();
			transaction = session.beginTransaction();
			session.save(obj);
			transaction.commit();
		} catch (Exception e) {
			if(transaction!=null){
				transaction.rollback();
			}
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally{
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}
	
	/**
	 * 提供返回只有一个结果的查询(一个结果)
	 * @param hql
	 * @param parameters
	 * @return
	 */
	public static Object executeQueryForOne(String hql,String[] parameters){
		Object o = null;
		Session session  = null;
		try {
			session = openSession();
			Query query = session.createQuery(hql);
			if(parameters!=null&&parameters.length>0){
				for (int i = 0; i < parameters.length; i++) {
					query.setString(i, parameters[i]);
				}
			}
			o = query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally{
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
		
		return o;
	}

	/**
	 *  提供一个统一的查询方法（带分页）
	 * @param hql
	 * @param parameters
	 * @param pageSize
	 * @param pageNow
	 * @return
	 */
	public static List executeQueryByPage(String hql, String[] parameters,
			int pageSize, int pageNow) {
		List list = null;
		Session session = null;
		try {
			session = openSession();
			Query query = session.createQuery(hql);
			if(parameters!=null&&parameters.length>0){
				for (int i = 0; i < parameters.length; i++) {
					query.setString(i, parameters[i]);
				}
			}
			query.setFirstResult((pageNow-1)*pageSize).setMaxResults(pageSize);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally{
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}

		return list;
	}

	/**
	 *  提供统一的查询方法.hql形式：from 类 where 条件=？...（多个结果）
	 * @param hql
	 * @param parameters
	 * @return
	 */
	public static List executeQuery(String hql, String[] parameters) {
		Session session = null;
		Transaction transaction = null;
		List list = null;
		try {
			session = getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			if (parameters != null && parameters.length > 0) {
				for (int i = 0; i < parameters.length; i++) {
					query.setString(i, parameters[i]);
				}
			}
			list = query.list();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
			session = null;
		}

		return list;
	}

}
