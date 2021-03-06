/*
 * Copyright (c) 2010 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the GNU Lesser General Public License, Version 2.1. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.gnu.org/licenses/lgpl-2.1.txt. The Original Code is Pentaho 
 * Data Integration.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the GNU Lesser Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 */
package org.pentaho.di.repository;
/**
 * Repository User object
 * @author rmansoor
 *
 */
public interface IUser {
  /**
   * Set the login for the user
   * @param login
   */
  public void setLogin(String login);
  /**
   * Get the login  for a the user
   * @return user login
   */
  public String getLogin();
  /**
   * Set the password for the 
   * @param password
   */
  public void setPassword(String password);
  /**
   * Get the password for the user
   * @return user password
   */
  public String getPassword();
  /**
   * Set the user name for the user
   * @param username
   */
  public void setUsername(String username);
  /**
   * Get the user name for the user
   * @return user name
   */
  public String getUsername();
  /**
   * Set the description of the user
   * @param description
   */
  public void setDescription(String description);
  /**
   * Get the user's description
   * @return user description
   */
  public String getDescription();
  /**
   * Make the user enabled or disabled
   * @param enabled
   */
  public void setEnabled(boolean enabled);
  /**
   * Check if the user is enabled or not
   * @return the enabled
   */
  public boolean isEnabled();

  /**
   * Get the object id
   * @return Object Id
   */
  public ObjectId getObjectId();
  /**
   * Set the object id of this user
   * @param object id
   */
  public void setObjectId(ObjectId id);
  /**
   * The name of the user maps to the login id
   * @return name
   */
  public String getName();

  /**
   * Set the name of the user.
   * 
   * @param name
   * The name of the user maps to the login id.
   */
  public void setName(String name);
}
