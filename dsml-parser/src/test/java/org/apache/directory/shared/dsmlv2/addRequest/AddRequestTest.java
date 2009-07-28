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

package org.apache.directory.shared.dsmlv2.addRequest;


import java.util.Iterator;

import org.apache.directory.shared.dsmlv2.AbstractTest;
import org.apache.directory.shared.dsmlv2.Dsmlv2Parser;
import org.apache.directory.shared.ldap.codec.ControlCodec;
import org.apache.directory.shared.ldap.codec.add.AddRequestCodec;
import org.apache.directory.shared.ldap.entry.Entry;
import org.apache.directory.shared.ldap.entry.EntryAttribute;
import org.apache.directory.shared.ldap.entry.Value;
import org.apache.directory.shared.ldap.util.StringTools;


/**
 * Tests for the Add Request parsing
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class AddRequestTest extends AbstractTest
{
    /**
     * Test parsing of a request without the dn attribute
     */
    public void testRequestWithoutDn()
    {
        testParsingFail( AddRequestTest.class, "request_without_dn_attribute.xml" );
    }


    /**
     * Test parsing of a request with the dn attribute
     */
    public void testRequestWithDn()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput( AddRequestTest.class.getResource( "request_with_dn_attribute.xml" ).openStream(), "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( "cn=Bob Rush,ou=Dev,dc=Example,dc=COM", addRequest.getEntryDn().toString() );
    }


    /**
     * Test parsing of a request with the (optional) requestID attribute
     */
    public void testRequestWithRequestId()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput( AddRequestTest.class.getResource( "request_with_requestID_attribute.xml" ).openStream(),
                "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( 456, addRequest.getMessageId() );
    }


    /**
     * Test parsing of a request with the (optional) requestID attribute equals to 0
     */
    public void testRequestWithRequestIdEquals0()
    {
        testParsingFail( AddRequestTest.class, "request_with_requestID_equals_0.xml" );
    }


    /**
     * Test parsing of a request with a (optional) Control element
     */
    public void testRequestWith1Control()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput( AddRequestTest.class.getResource( "request_with_1_control.xml" ).openStream(), "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( 1, addRequest.getControls().size() );

        ControlCodec control = addRequest.getCurrentControl();

        assertTrue( control.getCriticality() );

        assertEquals( "1.2.840.113556.1.4.643", control.getControlType() );

        assertEquals( "Some text", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );
    }


    /**
     * Test parsing of a request with a (optional) Control element with Base64 value
     */
    public void testRequestWith1ControlBase64Value()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput(
                AddRequestTest.class.getResource( "request_with_1_control_base64_value.xml" ).openStream(), "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();
        ControlCodec control = addRequest.getCurrentControl();

        assertEquals( 1, addRequest.getControls().size() );
        assertTrue( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.643", control.getControlType() );
        assertEquals( "DSMLv2.0 rocks!!", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );
    }


    /**
     * Test parsing of a request with a (optional) Control element with empty value
     */
    public void testRequestWith1ControlEmptyValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput( AddRequestTest.class.getResource( "request_with_1_control_empty_value.xml" ).openStream(),
                "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();
        ControlCodec control = addRequest.getCurrentControl();

        assertEquals( 1, addRequest.getControls().size() );
        assertTrue( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.643", control.getControlType() );
        assertEquals( StringTools.EMPTY_BYTES, control.getControlValue() );
    }


    /**
     * Test parsing of a request with 2 (optional) Control elements
     */
    public void testRequestWith2Controls()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput( AddRequestTest.class.getResource( "request_with_2_controls.xml" ).openStream(), "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();
        ControlCodec control = addRequest.getCurrentControl();

        assertEquals( 2, addRequest.getControls().size() );
        assertFalse( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.789", control.getControlType() );
        assertEquals( "Some other text", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );
    }


    /**
     * Test parsing of a request with 3 (optional) Control elements without value
     */
    public void testRequestWith3ControlsWithoutValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput( AddRequestTest.class.getResource( "request_with_3_controls_without_value.xml" )
                .openStream(), "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();
        ControlCodec control = addRequest.getCurrentControl();

        assertEquals( 3, addRequest.getControls().size() );
        assertTrue( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.456", control.getControlType() );
        assertEquals( StringTools.EMPTY_BYTES, control.getControlValue() );
    }


    /**
     * Test parsing of a request with an Attr elements with value
     */
    public void testRequestWith1AttrWithoutValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput( AddRequestTest.class.getResource( "request_with_1_attr_without_value.xml" ).openStream(),
                "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();

        Entry entry = addRequest.getEntry();
        assertEquals( 1, entry.size() );

        // Getting the Attribute  
        Iterator<EntryAttribute> attributeIterator = entry.iterator();
        EntryAttribute attribute = attributeIterator.next();
        assertEquals( "objectclass", attribute.getUpId() );

        // Getting the Value
        Iterator<Value<?>> valueIterator = attribute.iterator();
        assertFalse( valueIterator.hasNext() );
    }


    /**
     * Test parsing of a request with an Attr elements with empty value
     */
    public void testRequestWith1AttrEmptyValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput( AddRequestTest.class.getResource( "request_with_1_attr_empty_value.xml" ).openStream(),
                "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();

        Entry entry = addRequest.getEntry();
        assertEquals( 1, entry.size() );

        // Getting the Attribute       
        Iterator<EntryAttribute> attributeIterator = entry.iterator();
        EntryAttribute attribute = attributeIterator.next();
        assertEquals( "objectclass", attribute.getUpId() );

        // Getting the Value
        Iterator<Value<?>> valueIterator = attribute.iterator();
        assertFalse( valueIterator.hasNext() );
    }


    /**
     * Test parsing of a request with an Attr elements with value
     */
    public void testRequestWith1AttrWithValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput( AddRequestTest.class.getResource( "request_with_1_attr_with_value.xml" ).openStream(),
                "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();

        Entry entry = addRequest.getEntry();
        assertEquals( 1, entry.size() );

        // Getting the Attribute       
        Iterator<EntryAttribute> attributeIterator = entry.iterator();
        EntryAttribute attribute = attributeIterator.next();
        assertEquals( "objectclass", attribute.getUpId() );

        // Getting the Value
        Iterator<Value<?>> valueIterator = attribute.iterator();
        assertTrue( valueIterator.hasNext() );
        Value<?> value = valueIterator.next();
        assertEquals( "top", value.getString() );
    }


    /**
     * Test parsing of a request with an Attr elements with value
     */
    public void testRequestWith1AttrWithBase64Value()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput( AddRequestTest.class.getResource( "request_with_1_attr_with_base64_value.xml" )
                .openStream(), "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();

        Entry entry = addRequest.getEntry();
        assertEquals( 1, entry.size() );

        // Getting the Attribute       
        Iterator<EntryAttribute> attributeIterator = entry.iterator();
        EntryAttribute attribute = attributeIterator.next();
        assertEquals( "objectclass", attribute.getUpId() );

        // Getting the Value
        Iterator<Value<?>> valueIterator = attribute.iterator();
        assertTrue( valueIterator.hasNext() );
        Value<?> value = valueIterator.next();
        assertTrue( value.isBinary() );
        assertEquals( "DSMLv2.0 rocks!!", value.getString() );
    }


    /**
     * Test parsing of a request with 2 Attr elements with value
     */
    public void testRequestWith2AttrWithValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput( AddRequestTest.class.getResource( "request_with_2_attr_with_value.xml" ).openStream(),
                "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();

        Entry entry = addRequest.getEntry();
        assertEquals( 1, entry.size() );

        // Getting the Attribute       
        Iterator<EntryAttribute> attributeIterator = entry.iterator();
        EntryAttribute attribute = attributeIterator.next();
        assertEquals( "objectclass", attribute.getUpId() );

        // Getting the Value
        Iterator<Value<?>> valueIterator = attribute.iterator();
        assertTrue( valueIterator.hasNext() );
        Value<?> value = valueIterator.next();
        assertEquals( "top", value.getString() );
        assertTrue( valueIterator.hasNext() );
        value = valueIterator.next();
        assertEquals( "person", value.getString() );
        assertFalse( valueIterator.hasNext() );
    }


    /**
     * Test parsing of a request with 1 Attr element without attribute value
     */
    public void testRequestWith1AttrWithoutNameAttribute()
    {
        testParsingFail( AddRequestTest.class, "request_with_1_attr_without_name_attribute.xml" );
    }


    /**
     * Test parsing of a request with 1 Attr element with 2 Values
     */
    public void testRequestWith1AttrWith2Values()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInput( AddRequestTest.class.getResource( "request_with_1_attr_with_2_values.xml" ).openStream(),
                "UTF-8" );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        AddRequestCodec addRequest = ( AddRequestCodec ) parser.getBatchRequest().getCurrentRequest();

        Entry entry = addRequest.getEntry();
        assertEquals( 1, entry.size() );

        // Getting the Attribute       
        Iterator<EntryAttribute> attributeIterator = entry.iterator();
        EntryAttribute attribute = attributeIterator.next();
        assertEquals( "objectclass", attribute.getUpId() );

        // Getting the Value
        Iterator<Value<?>> valueIterator = attribute.iterator();
        assertTrue( valueIterator.hasNext() );
        Value<?> value = valueIterator.next();
        assertEquals( "top", value.getString() );
        assertTrue( valueIterator.hasNext() );
        value = valueIterator.next();
        assertEquals( "person", value.getString() );
        assertFalse( valueIterator.hasNext() );
    }


    /**
     * Test parsing of a request with a needed requestID attribute
     * 
     * DIRSTUDIO-1
     */
    public void testRequestWithNeededRequestId()
    {
        testParsingFail( AddRequestTest.class, "request_with_needed_requestID.xml" );
    }
}
