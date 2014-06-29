/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.directory.api.ldap.extras.controls.ad;

import org.apache.directory.api.ldap.model.message.Control;

/**
 * The DirSync control, as described in http://tools.ietf.org/html/draft-armijo-ldap-dirsync-00.
 * We use the same control for both the SearchRequest and the SearchResultDone. Here is the
 * ASN/1 description of the SearchRequest control :
 * 
 * <pre>
 * Repl    Control ::= SEQUENCE {
 *     controlType             1.2.840.113556.1.4.841
 *     controlValue            replControlValue
 *     criticality             TRUE
 * }
 * 
 * the control value can be one of the two structures :
 * 
 * Client side :
 * realReplControlValue ::= SEQUENCE {
 *     parentsFirst            integer
 *     maxReturnLength         integer
 *     cookie                  OCTET STRING
 * }
 * 
 * or
 * 
 * server side :
 * realReplControlValue ::= SEQUENCE {
 *     flag                  integer
 *     maxReturnLength       integer
 *     cookie                OCTET STRING
 * }
 * </pre> 
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 *
 */
public interface AdDirSync extends Control
{
    /** This control OID */
    static final String OID = "1.2.840.113556.1.4.841";
    
    /**
     * @return 1 if the parents are guaranteed to be returned before the children.
     */
    int getParentFirst();
    
    
    /**
     * @param parentFirst The parentFirst flag. A value of 1 will tell the server to return the parents first.
     */
    void setParentFirst( int parentFirst );
    
    
    /**
     * @return The maximum length of attributes to be returned
     */
    int getMaxReturnLength();
    
    
    /**
     * @param maxReturnLength The maximum length of attributes to be returned
     */
    void setMaxReturnLength( int maxReturnLength );
    
    
    /**
     * @return The cookie used while processing the successive DirSync operations
     */
    byte[] getCookie();
    
    
    /**
     * @param cookie The cookie to send to the server. It's the value found in the response control. Should be null
     * for the first control.
     */
    void setCookie( byte[] cookie );
    
    
    /**
     * @return The flag returned by the server. One of :
     * <ul>
     * <li>LDAP_DIRSYNC_OBJECT_SECURITY (0x0001)</li>
     * <li>LDAP_DIRSYNC_ANCESTORS_FIRST_ORDER (0x0800)</li>
     * <li>LDAP_DIRSYNC_PUBLIC_DATA_ONLY (0x2000)(</li>
     * <li>LDAP_DIRSYNC_INCREMENTAL_VALUES (0x7FFFFFFF)</li>
     * </ul>
     */
    AdDirSyncFlag getFlag();
    
    
    /**
     * @param flag The flag. 
     */
    void setFlag( AdDirSyncFlag flag );
}
