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
package org.apache.directory.api.ldap.model.schema.syntaxCheckers;


import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.schema.SyntaxChecker;


/**
 * A SyntaxChecker which verifies that a value is a SubstringAssertion 
 * according to RFC 4517.
 * <p>
 * Implemented as binary right now ...
 *  
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@SuppressWarnings("serial")
public final class SubstringAssertionSyntaxChecker extends SyntaxChecker
{
    /**
     * A static instance of SubstringAssertionSyntaxChecker
     */
    public static final SubstringAssertionSyntaxChecker INSTANCE = 
        new SubstringAssertionSyntaxChecker( SchemaConstants.SUBSTRING_ASSERTION_SYNTAX );
    
    /**
     * A static Builder for this class
     */
    public static final class Builder extends SCBuilder<SubstringAssertionSyntaxChecker>
    {
        /**
         * The Builder constructor
         */
        private Builder()
        {
            super( SchemaConstants.SUBSTRING_ASSERTION_SYNTAX );
        }
        
        
        /**
         * Create a new instance of SubstringAssertionSyntaxChecker
         * @return A new instance of SubstringAssertionSyntaxChecker
         */
        @Override
        public SubstringAssertionSyntaxChecker build()
        {
            return new SubstringAssertionSyntaxChecker( oid );
        }
    }

    
    /**
     * Creates an instance of SubstringAssertionSyntaxChecker
     * 
     * @param oid The OID to use for this SyntaxChecker
     */
    private SubstringAssertionSyntaxChecker( String oid )
    {
        super( oid );
    }

    
    /**
     * @return An instance of the Builder for this class
     */
    public static Builder builder()
    {
        return new Builder();
    }
}
