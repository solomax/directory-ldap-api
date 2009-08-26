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
package org.apache.directory.shared.ldap.schema;


import java.util.List;

import javax.naming.NamingException;


/**
 * Utility class used to generate schema object specifications. Some of the
 * latest work coming out of the LDAPBIS working body adds optional extensions
 * to these syntaxes. We have not yet added extension support to these functions
 * or the schema interfaces in this package. Descriptions can be generated for
 * the following objects:
 * <ul>
 * <li><a href="./AttributeType.html">AttributeType</a></li>
 * <li><a href="./DITContentRule.html">DITContentRule</a></li>
 * <li><a href="./MatchingRule.html">MatchingRule</a></li>
 * <li><a href="./MatchingRuleUse.html">MatchingRuleUse</a></li>
 * <li><a href="./NameForm.html">NameForm</a></li>
 * <li><a href="./ObjectClass.html">ObjectClass</a></li>
 * <li><a href="./DITStructureRule.html">DITStructureRule</a></li>
 * <li><a href="./Syntax.html">Syntax</a></li>
 * </ul>
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class DescriptionUtils
{
    /**
     * Generates the description using the AttributeTypeDescription as defined
     * by the syntax: 1.3.6.1.4.1.1466.115.121.1.3. Only the right hand side of
     * the description starting at the openning parenthesis is generated: that
     * is 'AttributeTypeDescription = ' is not generated.
     * 
     * <pre>
     *  AttributeTypeDescription = &quot;(&quot; whsp
     *     numericoid whsp                ; AttributeType identifier
     *     [ &quot;NAME&quot; qdescrs ]             ; name used in AttributeType
     *     [ &quot;DESC&quot; qdstring ]            ; description
     *     [ &quot;OBSOLETE&quot; whsp ]
     *     [ &quot;SUP&quot; woid ]                 ; derived from parent AttributeType
     *     [ &quot;EQUALITY&quot; woid              ; Matching Rule name
     *     [ &quot;ORDERING&quot; woid              ; Matching Rule name
     *     [ &quot;SUBSTR&quot; woid ]              ; Matching Rule name
     *     [ &quot;SYNTAX&quot; whsp noidlen whsp ] ; see section 4.3 RFC 2252
     *     [ &quot;SINGLE-VALUE&quot; whsp ]        ; default multi-valued
     *     [ &quot;COLLECTIVE&quot; whsp ]          ; default not collective
     *     [ &quot;NO-USER-MODIFICATION&quot; whsp ]; default user modifiable
     *     [ &quot;USAGE&quot; whsp AttributeUsage ]; default userApplications
     *     whsp &quot;)&quot;
     * </pre>
     * 
     * @param attributeType
     *            the attributeType to generate a description for
     * @return the AttributeTypeDescription Syntax for the attributeType in a
     *         pretty formated string
     * @throws NamingException If an error is raised while accessing some of the attributeType
     * data 
     */
    public static String getDescription( AttributeType attributeType ) throws NamingException
    {
        StringBuffer buf = new StringBuffer( "( " );
        buf.append( attributeType.getOid() );
        buf.append( '\n' );

        buf.append( "NAME " );
        buf.append( attributeType.getName() );
        buf.append( '\n' );

        if ( attributeType.getDescription() != null )
        {
            buf.append( "DESC " );
            buf.append( attributeType.getDescription() );
            buf.append( '\n' );
        }

        if ( attributeType.isObsolete() )
        {
            buf.append( "OBSOLETE" );
            buf.append( '\n' );
        }

        buf.append( attributeType.getSup().getOid() );

        if ( attributeType.getEquality() != null )
        {
            buf.append( "EQUALITY " );
            buf.append( attributeType.getEquality().getOid() );
            buf.append( '\n' );
        }

        if ( attributeType.getOrdering() != null )
        {
            buf.append( "ORDERING " );
            buf.append( attributeType.getOrdering().getOid() );
            buf.append( '\n' );
        }

        if ( attributeType.getSubstr() != null )
        {
            buf.append( "SUBSTR " );
            buf.append( attributeType.getSubstr().getOid() );
            buf.append( '\n' );
        }

        buf.append( "SYNTAX " );
        buf.append( attributeType.getSyntax().getOid() );
        buf.append( '\n' );

        if ( attributeType.isSingleValue() )
        {
            buf.append( "SINGLE-VALUE" );
            buf.append( '\n' );
        }

        if ( attributeType.isCollective() )
        {
            buf.append( "COLLECTIVE" );
            buf.append( '\n' );
        }

        if ( attributeType.isCanUserModify() )
        {
            buf.append( "NO-USER-MODIFICATION" );
            buf.append( '\n' );
        }

        buf.append( "USAGE " );
        buf.append( UsageEnum.render( attributeType.getUsage() ) );
        buf.append( " ) " );

        return buf.toString();
    }


    /**
     * Generates the DITContentRuleDescription for a DITContentRule as defined
     * by the syntax: 1.3.6.1.4.1.1466.115.121.1.16. Only the right hand side of
     * the description starting at the openning parenthesis is generated: that
     * is 'DITContentRuleDescription = ' is not generated.
     * 
     * <pre>
     *   DITContentRuleDescription = &quot;(&quot;
     *       numericoid         ; Structural ObjectClass identifier
     *       [ &quot;NAME&quot; qdescrs ]
     *       [ &quot;DESC&quot; qdstring ]
     *       [ &quot;OBSOLETE&quot; ]
     *       [ &quot;AUX&quot; oids ]     ; Auxiliary ObjectClasses
     *       [ &quot;MUST&quot; oids ]    ; AttributeType identifiers
     *       [ &quot;MAY&quot; oids ]     ; AttributeType identifiers
     *       [ &quot;NOT&quot; oids ]     ; AttributeType identifiers
     *      &quot;)&quot;
     * </pre>
     * 
     * @param dITContentRule
     *            the DIT content rule specification
     * @return the specification according to the DITContentRuleDescription
     *         syntax
     * @throws NamingException If an error is raised while accessing some of the dITConentRule
     * data 
     */
    public static String getDescription( DITContentRule dITContentRule ) throws NamingException
    {
        StringBuffer buf = new StringBuffer( "( " );
        buf.append( dITContentRule.getOid() );
        buf.append( '\n' );

        buf.append( "NAME " );
        buf.append( dITContentRule.getName() );
        buf.append( '\n' );

        if ( dITContentRule.getDescription() != null )
        {
            buf.append( "DESC " );
            buf.append( dITContentRule.getDescription() );
            buf.append( '\n' );
        }

        if ( dITContentRule.isObsolete() )
        {
            buf.append( "OBSOLETE" );
            buf.append( '\n' );
        }

        // print out all the auxiliary object class oids
        List<ObjectClass> aux = dITContentRule.getAuxObjectClasses();
        
        if ( ( aux != null ) && ( aux.size() > 0 ) )
        {
            buf.append( "AUX\n" );
            
            for ( ObjectClass objectClass: aux )
            {
                buf.append( '\t' );
                buf.append( objectClass.getOid() );
                buf.append( '\n' );
            }
        }

        List<AttributeType> must = dITContentRule.getMustAttributeTypes();
        
        if ( ( must != null ) && ( must.size() > 0 ) )
        {
            buf.append( "MUST\n" );
            
            for ( AttributeType attributeType:must )
            {
                buf.append( '\t' );
                buf.append( attributeType.getOid() );
                buf.append( '\n' );
            }
        }

        List<AttributeType> may = dITContentRule.getMayAttributeTypes();
        
        if ( ( may != null ) && ( may.size() > 0 ) )
        {
            buf.append( "MAY\n" );
            
            for ( AttributeType attributeType:may )
            {
                buf.append( '\t' );
                buf.append( attributeType.getOid() );
                buf.append( '\n' );
            }
        }

        List<AttributeType> not = dITContentRule.getNotAttributeTypes();
        
        if ( ( not != null ) && ( not.size() > 0 ) )
        {
            buf.append( "NOT\n" );
            
            for ( AttributeType attributeType:not )
            {
                buf.append( '\t' );
                buf.append( attributeType.getOid() );
                buf.append( '\n' );
            }
        }

        buf.append( " )" );
        return buf.toString();
    }


    /**
     * Generates the MatchingRuleDescription for a MatchingRule as defined by
     * the syntax: 1.3.6.1.4.1.1466.115.121.1.30. Only the right hand side of
     * the description starting at the openning parenthesis is generated: that
     * is 'MatchingRuleDescription = ' is not generated.
     * 
     * <pre>
     *  MatchingRuleDescription = &quot;(&quot; whsp
     *     numericoid whsp      ; MatchingRule object identifier
     *     [ &quot;NAME&quot; qdescrs ]
     *     [ &quot;DESC&quot; qdstring ]
     *     [ &quot;OBSOLETE&quot; whsp ]
     *     &quot;SYNTAX&quot; numericoid
     *  whsp &quot;)&quot;
     * </pre>
     * 
     * @param matchingRule
     *            the MatchingRule to generate the description for
     * @return the MatchingRuleDescription string
     * @throws NamingException If an error is raised while accessing some of the matchingRule
     * data 
     */
    public static String getDescription( MatchingRule matchingRule ) throws NamingException
    {
        StringBuffer buf = new StringBuffer( "( " );
        buf.append( matchingRule.getOid() );
        buf.append( '\n' );

        buf.append( "NAME " );
        buf.append( matchingRule.getName() );
        buf.append( '\n' );

        if ( matchingRule.getDescription() != null )
        {
            buf.append( "DESC " );
            buf.append( matchingRule.getDescription() );
            buf.append( '\n' );
        }

        if ( matchingRule.isObsolete() )
        {
            buf.append( "OBSOLETE" );
            buf.append( '\n' );
        }

        buf.append( "SYNTAX " );
        buf.append( matchingRule.getSyntax().getOid() );
        buf.append( " ) " );
        return buf.toString();
    }


    /**
     * Generates the MatchingRuleUseDescription for a MatchingRuleUse as defined
     * by the syntax: 1.3.6.1.4.1.1466.115.121.1.31. Only the right hand side of
     * the description starting at the openning parenthesis is generated: that
     * is 'MatchingRuleUseDescription = ' is not generated.
     * 
     * <pre>
     *      MatchingRuleUseDescription = LPAREN WSP
     *          numericoid                ; object identifier
     *          [ SP &quot;NAME&quot; SP qdescrs ]  ; short names (descriptors)
     *          [ SP &quot;DESC&quot; SP qdstring ] ; description
     *          [ SP &quot;OBSOLETE&quot; ]         ; not active
     *          SP &quot;APPLIES&quot; SP oids      ; attribute types
     *          extensions WSP RPAREN     ; extensions
     *  
     *    where:
     *      [numericoid] is the object identifier of the matching rule
     *          associated with this matching rule use description;
     *      NAME [qdescrs] are short names (descriptors) identifying this
     *          matching rule use;
     *      DESC [qdstring] is a short descriptive string;
     *      OBSOLETE indicates this matching rule use is not active;
     *      APPLIES provides a list of attribute types the matching rule applies
     *          to; and
     *      [extensions] describe extensions.
     * </pre>
     * 
     * @param matchingRuleUse The matching rule from which we want to generate
     *  a MatchingRuleUseDescription.
     * @return The generated MatchingRuleUseDescription
     * @throws NamingException If an error is raised while accessing some of the matchingRuleUse
     * data 
     */
    public static String getDescription( MatchingRuleUse matchingRuleUse ) throws NamingException
    {
        StringBuffer buf = new StringBuffer( "( " );
        buf.append( matchingRuleUse.getOid() );
        buf.append( '\n' );

        buf.append( "NAME " );
        buf.append( matchingRuleUse.getName() );
        buf.append( '\n' );

        if ( matchingRuleUse.getDescription() != null )
        {
            buf.append( "DESC " );
            buf.append( matchingRuleUse.getDescription() );
            buf.append( '\n' );
        }

        if ( matchingRuleUse.isObsolete() )
        {
            buf.append( "OBSOLETE" );
            buf.append( '\n' );
        }

        buf.append( "APPLIES " );
        List<AttributeType> attributeTypes = matchingRuleUse.getApplicableAttributes();
        
        if ( attributeTypes.size() == 1 )
        {
            buf.append( attributeTypes.get( 0 ).getOid() );
        }
        else
        // for list of oids we need a parenthesis
        {
            buf.append( "( " );
            
            boolean isFirst = true;
            
            for ( AttributeType attributeType : attributeTypes )
            {
                if ( isFirst )
                {
                    isFirst = false;
                }
                else
                {
                    buf.append( " $ " );
                }
                
                buf.append( attributeType );
            }
            
            buf.append( " ) " );
        }

        buf.append( '\n' );
        return buf.toString();
    }


    /**
     * Generates the NameFormDescription for a NameForm as defined by the
     * syntax: 1.3.6.1.4.1.1466.115.121.1.35. Only the right hand side of the
     * description starting at the openning parenthesis is generated: that is
     * 'NameFormDescription = ' is not generated.
     * 
     * <pre>
     *  NameFormDescription = &quot;(&quot; whsp
     *      numericoid whsp               ; NameForm identifier
     *      [ &quot;NAME&quot; qdescrs ]
     *      [ &quot;DESC&quot; qdstring ]
     *      [ &quot;OBSOLETE&quot; whsp ]
     *      &quot;OC&quot; woid                     ; Structural ObjectClass
     *      &quot;MUST&quot; oids                   ; AttributeTypes
     *      [ &quot;MAY&quot; oids ]                ; AttributeTypes
     *  whsp &quot;)&quot;
     * </pre>
     * 
     * @param nameForm
     *            the NameForm to generate the description for
     * @return the NameFormDescription string
     * @throws NamingException If an error is raised while accessing some of the nameForm
     * data 
     */
    public static String getDescription( NameForm nameForm ) throws NamingException
    {
        StringBuffer buf = new StringBuffer( "( " );
        buf.append( nameForm.getOid() );
        buf.append( '\n' );

        buf.append( "NAME " );
        buf.append( nameForm.getName() );
        buf.append( '\n' );

        if ( nameForm.getDescription() != null )
        {
            buf.append( "DESC " );
            buf.append( nameForm.getDescription() );
            buf.append( '\n' );
        }

        if ( nameForm.isObsolete() )
        {
            buf.append( "OBSOLETE" );
            buf.append( '\n' );
        }

        buf.append( "OC " );
        buf.append( nameForm.getStructuralObjectClassOid() );
        buf.append( '\n' );

        buf.append( "MUST\n" );
        List<AttributeType> must = nameForm.getMustAttributeTypes();
        
        for ( AttributeType attributeType:must )
        {
            buf.append( '\t' );
            buf.append( attributeType.getOid() );
            buf.append( '\n' );
        }

        List<AttributeType> may = nameForm.getMayAttributeTypes();

        if ( ( may != null ) && ( may.size() > 0 ) )
        {
            buf.append( "MAY\n" );
        
            for ( AttributeType attributeType:may )
            {
                buf.append( '\t' );
                buf.append( attributeType.getOid() );
                buf.append( '\n' );
            }
        }

        buf.append( " )" );
        return buf.toString();
    }


    /**
     * Generates the ObjectClassDescription for an ObjectClass as defined by the
     * syntax: 1.3.6.1.4.1.1466.115.121.1.37. Only the right hand side of the
     * description starting at the openning parenthesis is generated: that is
     * 'ObjectClassDescription = ' is not generated.
     * 
     * <pre>
     *  ObjectClassDescription = &quot;(&quot; whsp
     *      numericoid whsp     ; ObjectClass identifier
     *      [ &quot;NAME&quot; qdescrs ]
     *      [ &quot;DESC&quot; qdstring ]
     *      [ &quot;OBSOLETE&quot; whsp ]
     *      [ &quot;SUP&quot; oids ]      ; Superior ObjectClasses
     *      [ ( &quot;ABSTRACT&quot; / &quot;STRUCTURAL&quot; / &quot;AUXILIARY&quot; ) whsp ]
     *                          ; default structural
     *      [ &quot;MUST&quot; oids ]     ; AttributeTypes
     *      [ &quot;MAY&quot; oids ]      ; AttributeTypes
     *  whsp &quot;)&quot;
     * </pre>
     * 
     * @param objectClass
     *            the ObjectClass to generate a description for
     * @return the description in the ObjectClassDescription syntax
     * @throws NamingException If an error is raised while accessing some of the objectClass
     * data 
     */
    public static String getDescription( ObjectClass objectClass ) throws NamingException
    {
        StringBuffer buf = new StringBuffer( "( " );
        buf.append( objectClass.getOid() );
        buf.append( '\n' );

        buf.append( "NAME " );
        buf.append( objectClass.getName() );
        buf.append( '\n' );

        if ( objectClass.getDescription() != null )
        {
            buf.append( "DESC " );
            buf.append( objectClass.getDescription() );
            buf.append( '\n' );
        }

        if ( objectClass.isObsolete() )
        {
            buf.append( "OBSOLETE" );
            buf.append( '\n' );
        }

        ObjectClass[] sups = objectClass.getSuperClasses();

        if ( sups != null && sups.length > 0 )
        {
            buf.append( "SUP\n" );
            
            for ( ObjectClass sup:sups )
            {
                buf.append( '\t' );
                buf.append( sup.getOid() );
                buf.append( '\n' );
            }
        }

        if ( objectClass.getType() != null )
        {
            buf.append( objectClass.getType() );
            buf.append( '\n' );
        }

        AttributeType[] must = objectClass.getMustList();
        if ( must != null && must.length > 0 )
        {
            buf.append( "MUST\n" );
            
            for ( AttributeType attributeType:must )
            {
                buf.append( '\t' );
                buf.append( attributeType.getOid() );
                buf.append( '\n' );
            }
        }

        AttributeType[] may = objectClass.getMayList();
        
        if ( may != null && may.length > 0 )
        {
            buf.append( "MAY\n" );

            for ( AttributeType attributeType:may )
            {
                buf.append( '\t' );
                buf.append( attributeType.getOid() );
                buf.append( '\n' );
            }
        }

        buf.append( " )" );
        return buf.toString();
    }


    /**
     * Generates the DITStructureRuleDescription for a DITStructureRule as
     * defined by the syntax: 1.3.6.1.4.1.1466.115.121.1.17. Only the right hand
     * side of the description starting at the openning parenthesis is
     * generated: that is 'DITStructureRuleDescription = ' is not generated.
     * 
     * <pre>
     *  DITStructureRuleDescription = &quot;(&quot; whsp
     *      ruleidentifier whsp           ; DITStructureRule identifier
     *      [ &quot;NAME&quot; qdescrs ]
     *      [ &quot;DESC&quot; qdstring ]
     *      [ &quot;OBSOLETE&quot; whsp ]
     *      &quot;FORM&quot; woid whsp              ; NameForm
     *      [ &quot;SUP&quot; ruleidentifiers whsp ]; superior DITStructureRules
     *  &quot;)&quot;
     * </pre>
     * 
     * @param dITStructureRule
     *            the DITStructureRule to generate the description for
     * @return the description in the DITStructureRuleDescription syntax
     * @throws NamingException If an error is raised while accessing some of the dITStructureRule
     * data 
     */
    public static String getDescription( DITStructureRule dITStructureRule ) throws NamingException
    {
        StringBuffer buf = new StringBuffer( "( " );
        buf.append( dITStructureRule.getOid() );
        buf.append( '\n' );

        buf.append( "NAME " );
        buf.append( dITStructureRule.getName() );
        buf.append( '\n' );

        if ( dITStructureRule.getDescription() != null )
        {
            buf.append( "DESC " );
            buf.append( dITStructureRule.getDescription() );
            buf.append( '\n' );
        }

        if ( dITStructureRule.isObsolete() )
        {
            buf.append( "OBSOLETE" );
            buf.append( '\n' );
        }

        buf.append( "FORM " );
        buf.append( dITStructureRule.getForm() );
        buf.append( '\n' );

        List<DITStructureRule> sups = dITStructureRule.getSuperRules();
        
        if ( ( sups != null ) && ( sups.size() > 0 ) )
        {
            buf.append( "SUP\n" );
            
            for ( DITStructureRule sup:sups )
            {
                buf.append( '\t' );
                buf.append( sup.getOid() );
                buf.append( '\n' );
            }
        }

        buf.append( " )" );
        return buf.toString();
    }


    /**
     * Generates the SyntaxDescription for a Syntax as defined by the syntax:
     * 1.3.6.1.4.1.1466.115.121.1.54. Only the right hand side of the
     * description starting at the openning parenthesis is generated: that is
     * 'SyntaxDescription = ' is not generated.
     * 
     * <pre>
     *  SyntaxDescription = &quot;(&quot; whsp
     *      numericoid whsp
     *      [ &quot;DESC&quot; qdstring ]
     *  whsp &quot;)&quot;
     * </pre>
     * 
     * @param syntax
     *            the Syntax to generate a description for
     * @return the description in the SyntaxDescription syntax
     */
    public static String getDescription( LdapSyntax syntax )
    {
        StringBuffer buf = new StringBuffer( "( " );
        buf.append( syntax.getOid() );
        buf.append( '\n' );

        if ( syntax.getDescription() != null )
        {
            buf.append( "DESC " );
            buf.append( syntax.getDescription() );
            buf.append( '\n' );
        }

        buf.append( " )" );
        return buf.toString();
    }
}
