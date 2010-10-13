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
package org.apache.directory.shared.dsmlv2.request;


import org.apache.directory.shared.dsmlv2.ParserUtils;
import org.apache.directory.shared.ldap.codec.MessageTypeEnum;
import org.apache.directory.shared.ldap.entry.Entry;
import org.apache.directory.shared.ldap.entry.EntryAttribute;
import org.apache.directory.shared.ldap.entry.Value;
import org.apache.directory.shared.ldap.exception.LdapException;
import org.apache.directory.shared.ldap.message.AddRequest;
import org.apache.directory.shared.ldap.message.AddRequestImpl;
import org.apache.directory.shared.ldap.name.DN;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;


/**
 * DSML Decorator for AddRequest
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AddRequestDsml extends AbstractRequestDsml
{
    /**
     * Creates a new instance of AddRequestDsml.
     */
    public AddRequestDsml()
    {
        super( new AddRequestImpl() );
    }


    /**
     * Creates a new instance of AddRequestDsml.
    *
    * @param ldapMessage
    *      the message to decorate
    */
    public AddRequestDsml( AddRequest ldapMessage )
    {
        super( ldapMessage );
    }


    /**
     * {@inheritDoc}
     */
    public MessageTypeEnum getType()
    {
        return instance.getType();
    }


    /**
     * {@inheritDoc}
     */
    public Element toDsml( Element root )
    {
        Element element = super.toDsml( root );

        AddRequest request = ( AddRequest ) instance;

        // DN
        if ( request.getEntry() != null )
        {
            element.addAttribute( "dn", request.getEntry().getDn().getName() );
        }

        // Attributes
        Entry entry = request.getEntry();
        if ( entry != null )
        {
            for ( EntryAttribute attribute : entry )
            {
                Element attributeElement = element.addElement( "attr" );
                attributeElement.addAttribute( "name", attribute.getId() );
                // Looping on Values
                for ( Value<?> value : attribute )
                {
                    if ( ParserUtils.needsBase64Encoding( value.get() ) )
                    {
                        Namespace xsdNamespace = new Namespace( "xsd", ParserUtils.XML_SCHEMA_URI );
                        Namespace xsiNamespace = new Namespace( "xsi", ParserUtils.XML_SCHEMA_INSTANCE_URI );
                        attributeElement.getDocument().getRootElement().add( xsdNamespace );
                        attributeElement.getDocument().getRootElement().add( xsiNamespace );

                        Element valueElement = attributeElement.addElement( "value" ).addText(
                            ParserUtils.base64Encode( value.get() ) );
                        valueElement
                            .addAttribute( new QName( "type", xsiNamespace ), "xsd:" + ParserUtils.BASE64BINARY );
                    }
                    else
                    {
                        attributeElement.addElement( "value" ).addText( value.getString() );
                    }
                }
            }
        }

        return element;
    }


    /**
     * Initialize the Entry.
     */
    public void initEntry()
    {
        //( ( AddRequest ) instance ).initEntry();
    }


    /**
     * Get the entry with its attributes.
     * 
     * @return Returns the entry.
     */
    public Entry getEntry()
    {
        return ( ( AddRequest ) instance ).getEntry();
    }


    /**
     * Create a new attributeValue
     * 
     * @param type The attribute's name (called 'type' in the grammar)
     * @throws LdapException 
     */
    public void addAttributeType( String type ) throws LdapException
    {
        ( ( AddRequest ) instance ).addAttributeType( type );
    }


    /**
     * Add a new value to the current attribute
     * 
     * @param value The value to be added
     */
    public void addAttributeValue( Object value )
    {
        if ( value instanceof Value<?> )
        {
            ( ( AddRequest ) instance ).addAttributeValue( ( Value<?> ) value );
        }
        else if ( value instanceof String )
        {
            ( ( AddRequest ) instance ).addAttributeValue( ( String ) value );
        }
        else if ( value instanceof byte[] )
        {
            ( ( AddRequest ) instance ).addAttributeValue( ( byte[] ) value );
        }
    }


    /**
     * Get the added DN
     * 
     * @return Returns the entry DN.
     */
    public DN getEntryDn()
    {
        return ( ( AddRequest ) instance ).getEntryDn();
    }


    /**
     * Set the added DN.
     * 
     * @param entryDn The entry DN to set.
     */
    public void setEntryDn( DN entryDn )
    {
        ( ( AddRequest ) instance ).setEntryDn( entryDn );
    }


    /**
     * Sets the entry.
     *
     * @param entry
     *      the entry
     */
    public void setEntry( Entry entry )
    {
        ( ( AddRequest ) instance ).setEntry( entry );
    }


    /**
     * @return Returns the currentAttribute type.
     */
    public String getCurrentAttributeType()
    {
        return ( ( AddRequest ) instance ).getCurrentAttributeType();
    }
}