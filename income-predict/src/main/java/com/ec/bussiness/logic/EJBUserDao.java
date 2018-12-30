/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ec.bussiness.logic;

import javax.ejb.Stateful;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@Stateful
@Alternative
public class EJBUserDao implements UserDao {

	@Inject
	private EntityManager entityManager;

	@Inject
	private UserTransaction obj_ut;

	public User validateUsername(String client_username) {
		try {
			User user;
			try {
				obj_ut.begin();
				Query obj_query = entityManager.createQuery("select u from User u where u.username = :username");
				obj_query.setParameter("username", client_username);
				user = (User) obj_query.getSingleResult();
			} catch (NoResultException e) {
				user = null;
			}
			obj_ut.commit();
			return user;
		} catch (Exception e) {
			try {
				obj_ut.rollback();
			} catch (SystemException se) {
				throw new RuntimeException(se);
			}
			throw new RuntimeException(e);
		}
	}

	public User validateAdmin(String Admin_username) {
		try {
			User obj_user;
			try {
				obj_ut.begin();
				Query obj_query = entityManager.createQuery("select u from User u where u.username = :username");
				obj_query.setParameter("username", Admin_username);
				obj_user = (User) obj_query.getSingleResult();
			} catch (NoResultException e) {
				obj_user = null;
			}
			obj_ut.commit();
			return obj_user;
		} catch (Exception e) {
			try {
				obj_ut.rollback();
			} catch (SystemException se) {
				throw new RuntimeException(se);
			}
			throw new RuntimeException(e);
		}
	}

	public void roleChange(User role_user) {

		try {
			try {
				obj_ut.begin();
				Query query = entityManager.createQuery("update User r set r.role = :role where r.username= :username");
				query.setParameter("username", role_user.getUsername());
				query.setParameter("role", role_user.getRole());
				int m = query.executeUpdate();
			} finally {
				obj_ut.commit();
			}
		} catch (Exception e) {
			try {
				obj_ut.rollback();
			} catch (SystemException se) {
				throw new RuntimeException(se);
			}
			throw new RuntimeException(e);
		}
	}
}
