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
package org.apache.directory.shared.ldap.codec.extended.operations.certGeneration;


import java.nio.ByteBuffer;

import org.apache.directory.shared.asn1.AbstractAsn1Object;
import org.apache.directory.shared.asn1.EncoderException;
import org.apache.directory.shared.asn1.ber.tlv.UniversalTag;
import org.apache.directory.shared.asn1.ber.tlv.Value;
import org.apache.directory.shared.util.Strings;


/**
 * 
 * An extended operation for generating a public key Certificate.
 * <pre>
 *   CertGenerateObject ::= SEQUENCE 
 *   {
 *      targetDN        IA5String,
 *      issuerDN        IA5String,
 *      subjectDN       IA5String,
 *      keyAlgorithm    IA5String
 *   }
 * </pre>
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CertGenerationObject extends AbstractAsn1Object
{

    /** the Dn of the server entry which will be updated*/
    private String targetDN;

    /** the issuer Dn that will be set in the certificate*/
    private String issuerDN;// = "CN=ApacheDS, OU=Directory, O=ASF, C=US";

    /** the Dn of the subject that is present in the certificate*/
    private String subjectDN;// = "CN=ApacheDS, OU=Directory, O=ASF, C=US";

    /** name of the algorithm used for generating the keys*/
    private String keyAlgorithm;// = "RSA";

    /** stores the length of the request*/
    private int requestLength = 0;


    @Override
    public int computeLength()
    {
        int len = Strings.getBytesUtf8(targetDN).length;
        requestLength = 1 + Value.getNbBytes( len ) + len;

        len = Strings.getBytesUtf8(issuerDN).length;
        requestLength += 1 + Value.getNbBytes( len ) + len;

        len = Strings.getBytesUtf8(subjectDN).length;
        requestLength += 1 + Value.getNbBytes( len ) + len;

        len = Strings.getBytesUtf8(keyAlgorithm).length;
        requestLength += 1 + Value.getNbBytes( len ) + len;

        return 1 + Value.getNbBytes( requestLength ) + requestLength;
    }


    public ByteBuffer encode() throws EncoderException
    {
        ByteBuffer bb = ByteBuffer.allocate( computeLength() );

        bb.put( UniversalTag.SEQUENCE.getValue() );
        bb.put( Value.getBytes( requestLength ) );

        Value.encode( bb, targetDN );
        Value.encode( bb, issuerDN );
        Value.encode( bb, subjectDN );
        Value.encode( bb, keyAlgorithm );

        return bb;
    }


    public String getTargetDN()
    {
        return targetDN;
    }


    public void setTargetDN( String targetDN )
    {
        this.targetDN = targetDN;
    }


    public String getIssuerDN()
    {
        return issuerDN;
    }


    public void setIssuerDN( String issuerDN )
    {
        this.issuerDN = issuerDN;
    }


    public String getSubjectDN()
    {
        return subjectDN;
    }


    public void setSubjectDN( String subjectDN )
    {
        this.subjectDN = subjectDN;
    }


    public String getKeyAlgorithm()
    {
        return keyAlgorithm;
    }


    public void setKeyAlgorithm( String keyAlgorithm )
    {
        this.keyAlgorithm = keyAlgorithm;
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( "Certficate Generation Object { " ).append( " Target Dn: " ).append( targetDN ).append( ',' );
        sb.append( " Issuer Dn: " ).append( issuerDN ).append( ',' );
        sb.append( " Subject Dn: " ).append( subjectDN ).append( ',' );
        sb.append( " Key Algorithm: " ).append( keyAlgorithm ).append( " }" );

        return sb.toString();
    }
}
