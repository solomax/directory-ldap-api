/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */

package org.apache.directory.ldap.client.api.callback;


import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;

import org.apache.directory.ldap.client.api.SaslRequest;
import org.apache.directory.shared.ldap.util.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The CallbackHandler implementation used by the LdapConnection during SASL mechanism based bind operations.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class SaslCallbackHandler implements CallbackHandler
{

    /** The sasl request. */
    private SaslRequest saslReq;

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger( SaslCallbackHandler.class );


    /**
     * Instantiates a new SASL callback handler.
     *
     * @param saslReq the SASL request
     */
    public SaslCallbackHandler( SaslRequest saslReq )
    {
        this.saslReq = saslReq;
    }


    /**
     * {@inheritDoc}
     */
    public void handle( Callback[] callbacks ) throws IOException, UnsupportedCallbackException
    {
        for ( Callback cb : callbacks )
        {
            if ( cb instanceof NameCallback )
            {
                NameCallback ncb = ( NameCallback ) cb;

                String name = saslReq.getUsername();
                LOG.debug( "sending name {} in the NameCallback", name );
                ncb.setName( name );
            }
            else if ( cb instanceof PasswordCallback )
            {
                PasswordCallback pcb = ( PasswordCallback ) cb;

                LOG.debug( "sending credentials in the PasswordCallback" );
                pcb.setPassword( StringTools.utf8ToString( saslReq.getCredentials() ).toCharArray() );
            }
            else if ( cb instanceof RealmCallback )
            {
                RealmCallback rcb = ( RealmCallback ) cb;

                if ( saslReq.getRealmName() != null )
                {
                    LOG.debug( "sending the user specified realm value {} in the RealmCallback", saslReq.getRealmName() );
                    rcb.setText( saslReq.getRealmName() );
                }
                else
                {
                    LOG.debug(
                        "No user specified relam value, sending the default realm value {} in the RealmCallback",
                        rcb.getDefaultText() );
                    rcb.setText( rcb.getDefaultText() );
                }
            }
        }
    }
}
