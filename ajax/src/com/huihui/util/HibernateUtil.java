package com.huihui.util;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

final public class HibernateUtil {
	private static SessionFactory sessionFactory = null;
	// ʹ���ֲ߳̾�ģʽ
	private static ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

	private HibernateUtil() {
	}

	static {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}

	/**
	 *  ��ȡһ��ȫ�µ�Session
	 * @return
	 */
	public static Session openSession() {
		return sessionFactory.openSession();
	}

	/**
	 *  ��ȡ���̹߳�����Session
	 * @return
	 */
	public static Session getCurrentSession() {
		Session session = threadLocal.get();
		// �ж��Ƿ�õ���
		if (session == null) {
			session = sessionFactory.openSession();
			// ��session�������õ�threadLocal��ȥ���൱�ڸ�Session�Ѿ����̰߳�
			threadLocal.set(session);
		}
		return session;
	}
	
	/**
	 * ͳһ���޸ĺ�ɾ��
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
	 * ͳһ����ӷ���
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
	 * �ṩ����ֻ��һ������Ĳ�ѯ(һ�����)
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
	 *  �ṩһ��ͳһ�Ĳ�ѯ����������ҳ��
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
	 *  �ṩͳһ�Ĳ�ѯ����.hql��ʽ��from �� where ����=��...����������
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
