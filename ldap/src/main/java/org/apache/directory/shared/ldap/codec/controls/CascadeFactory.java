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
package org.apache.directory.shared.ldap.codec.controls;


import javax.naming.ldap.BasicControl;
import javax.naming.ldap.Control;

import org.apache.directory.shared.asn1.DecoderException;
import org.apache.directory.shared.asn1.EncoderException;
import org.apache.directory.shared.ldap.codec.IControlFactory;
import org.apache.directory.shared.ldap.codec.ILdapCodecService;
import org.apache.directory.shared.ldap.model.message.controls.Cascade;
import org.apache.directory.shared.ldap.model.message.controls.CascadeImpl;
import org.apache.directory.shared.util.StringConstants;


/**
 * A codec {@link IControlFactory} implementation for {@link Cascade} controls.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class CascadeFactory implements IControlFactory<Cascade, CascadeDecorator>
{
    /** The LDAP codec responsible for encoding and decoding Cascade Controls */
    private ILdapCodecService codec;
    
    
    /**
     * Creates a new instance of CascadeFactory.
     *
     * @param codec The LDAP codec
     */
    public CascadeFactory( ILdapCodecService codec )
    {
        this.codec = codec;
    }

    
    /**
     * {@inheritDoc}
     */
    public String getOid()
    {
        return Cascade.OID;
    }

    
    /**
     * {@inheritDoc}
     */
    public CascadeDecorator newCodecControl()
    {
        return new CascadeDecorator( codec, new CascadeImpl() );
    }
   

    /**
     * {@inheritDoc}
     */
    public CascadeDecorator decorate( Cascade control )
    {
        return new CascadeDecorator( codec, control );
    }

    
    /**
     * {@inheritDoc}
     */
    public Cascade newControl()
    {
        return new CascadeImpl();
    }

    
    /**
     * {@inheritDoc}
     */
    public Control toJndiControl( Cascade control ) throws EncoderException
    {
        return new BasicControl( Cascade.OID, control.isCritical(), StringConstants.EMPTY_BYTES );
    }

    
    /**
     * {@inheritDoc}
     */
    public Cascade fromJndiControl( Control control ) throws DecoderException
    {
        return new CascadeImpl( control.isCritical() );
    }
}