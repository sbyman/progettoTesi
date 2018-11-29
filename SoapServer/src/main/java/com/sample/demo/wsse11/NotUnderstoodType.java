//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.11.06 alle 08:54:27 AM CET 
//


package com.sample.demo.wsse11;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Classe Java per NotUnderstoodType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="NotUnderstoodType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="qname" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotUnderstoodType")
public class NotUnderstoodType {

    @XmlAttribute(name = "qname", required = true)
    protected QName qname;

    /**
     * Recupera il valore della proprietà qname.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getQname() {
        return qname;
    }

    /**
     * Imposta il valore della proprietà qname.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setQname(QName value) {
        this.qname = value;
    }

}
