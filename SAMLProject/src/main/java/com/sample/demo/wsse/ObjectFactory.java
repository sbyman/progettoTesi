//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.11.06 alle 08:44:23 AM CET 
//


package com.sample.demo.wsse;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sample.demo.wsse package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Password_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Password");
    private final static QName _Embedded_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Embedded");
    private final static QName _TransformationParameters_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "TransformationParameters");
    private final static QName _SecurityTokenReference_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "SecurityTokenReference");
    private final static QName _Expires_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Expires");
    private final static QName _Nonce_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Nonce");
    private final static QName _Security_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");
    private final static QName _UsernameToken_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "UsernameToken");
    private final static QName _Reference_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Reference");
    private final static QName _Created_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Created");
    private final static QName _KeyIdentifier_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "KeyIdentifier");
    private final static QName _Timestamp_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Timestamp");
    private final static QName _BinarySecurityToken_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "BinarySecurityToken");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sample.demo.wsse
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BinarySecurityTokenType }
     * 
     */
    public BinarySecurityTokenType createBinarySecurityTokenType() {
        return new BinarySecurityTokenType();
    }

    /**
     * Create an instance of {@link EncodedString }
     * 
     */
    public EncodedString createEncodedString() {
        return new EncodedString();
    }

    /**
     * Create an instance of {@link KeyIdentifierType }
     * 
     */
    public KeyIdentifierType createKeyIdentifierType() {
        return new KeyIdentifierType();
    }

    /**
     * Create an instance of {@link SecurityTokenReferenceType }
     * 
     */
    public SecurityTokenReferenceType createSecurityTokenReferenceType() {
        return new SecurityTokenReferenceType();
    }

    /**
     * Create an instance of {@link ReferenceType }
     * 
     */
    public ReferenceType createReferenceType() {
        return new ReferenceType();
    }

    /**
     * Create an instance of {@link UsernameTokenType }
     * 
     */
    public UsernameTokenType createUsernameTokenType() {
        return new UsernameTokenType();
    }

    /**
     * Create an instance of {@link TransformationParametersType }
     * 
     */
    public TransformationParametersType createTransformationParametersType() {
        return new TransformationParametersType();
    }

    /**
     * Create an instance of {@link EmbeddedType }
     * 
     */
    public EmbeddedType createEmbeddedType() {
        return new EmbeddedType();
    }

    /**
     * Create an instance of {@link SecurityHeaderType }
     * 
     */
    public SecurityHeaderType createSecurityHeaderType() {
        return new SecurityHeaderType();
    }

    /**
     * Create an instance of {@link PasswordString }
     * 
     */
    public PasswordString createPasswordString() {
        return new PasswordString();
    }

    /**
     * Create an instance of {@link AttributedString }
     * 
     */
    public AttributedString createAttributedString() {
        return new AttributedString();
    }

    /**
     * Create an instance of {@link AttributedDateTime }
     * 
     */
    public AttributedDateTime createAttributedDateTime() {
        return new AttributedDateTime();
    }

    /**
     * Create an instance of {@link TimestampType }
     * 
     */
    public TimestampType createTimestampType() {
        return new TimestampType();
    }

    /**
     * Create an instance of {@link AttributedURI }
     * 
     */
    public AttributedURI createAttributedURI() {
        return new AttributedURI();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PasswordString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", name = "Password")
    public JAXBElement<PasswordString> createPassword(PasswordString value) {
        return new JAXBElement<PasswordString>(_Password_QNAME, PasswordString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmbeddedType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", name = "Embedded")
    public JAXBElement<EmbeddedType> createEmbedded(EmbeddedType value) {
        return new JAXBElement<EmbeddedType>(_Embedded_QNAME, EmbeddedType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransformationParametersType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", name = "TransformationParameters")
    public JAXBElement<TransformationParametersType> createTransformationParameters(TransformationParametersType value) {
        return new JAXBElement<TransformationParametersType>(_TransformationParameters_QNAME, TransformationParametersType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecurityTokenReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", name = "SecurityTokenReference")
    public JAXBElement<SecurityTokenReferenceType> createSecurityTokenReference(SecurityTokenReferenceType value) {
        return new JAXBElement<SecurityTokenReferenceType>(_SecurityTokenReference_QNAME, SecurityTokenReferenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributedDateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", name = "Expires")
    public JAXBElement<AttributedDateTime> createExpires(AttributedDateTime value) {
        return new JAXBElement<AttributedDateTime>(_Expires_QNAME, AttributedDateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EncodedString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", name = "Nonce")
    public JAXBElement<EncodedString> createNonce(EncodedString value) {
        return new JAXBElement<EncodedString>(_Nonce_QNAME, EncodedString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecurityHeaderType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", name = "Security")
    public JAXBElement<SecurityHeaderType> createSecurity(SecurityHeaderType value) {
        return new JAXBElement<SecurityHeaderType>(_Security_QNAME, SecurityHeaderType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsernameTokenType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", name = "UsernameToken")
    public JAXBElement<UsernameTokenType> createUsernameToken(UsernameTokenType value) {
        return new JAXBElement<UsernameTokenType>(_UsernameToken_QNAME, UsernameTokenType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", name = "Reference")
    public JAXBElement<ReferenceType> createReference(ReferenceType value) {
        return new JAXBElement<ReferenceType>(_Reference_QNAME, ReferenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributedDateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", name = "Created")
    public JAXBElement<AttributedDateTime> createCreated(AttributedDateTime value) {
        return new JAXBElement<AttributedDateTime>(_Created_QNAME, AttributedDateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyIdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", name = "KeyIdentifier")
    public JAXBElement<KeyIdentifierType> createKeyIdentifier(KeyIdentifierType value) {
        return new JAXBElement<KeyIdentifierType>(_KeyIdentifier_QNAME, KeyIdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TimestampType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", name = "Timestamp")
    public JAXBElement<TimestampType> createTimestamp(TimestampType value) {
        return new JAXBElement<TimestampType>(_Timestamp_QNAME, TimestampType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BinarySecurityTokenType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", name = "BinarySecurityToken")
    public JAXBElement<BinarySecurityTokenType> createBinarySecurityToken(BinarySecurityTokenType value) {
        return new JAXBElement<BinarySecurityTokenType>(_BinarySecurityToken_QNAME, BinarySecurityTokenType.class, null, value);
    }

}