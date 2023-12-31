/*
 * Copyright (c) 2015, 2022, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.sun.org.apache.xpath.internal.jaxp;

import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.Objects;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathNodes;
import javax.xml.xpath.XPathEvaluationResult;
import javax.xml.xpath.XPathEvaluationResult.XPathResultType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;


/**
 * This is the implementation of XPathEvaluationResult that represents the
 * result of the evaluation of an XPath expression within the context of a
 * particular node.
 */
class XPathResultImpl<T> implements XPathEvaluationResult<T> {

    XObject resultObject;
    int resultType;
    Class<T> type;
    XPathResultType mapToType;
    NodeList nodeList = null;
    int currentIndex;
    Node currentNode;

    boolean boolValue = false;
    Node node = null;
    double numValue;
    String strValue;

    /**
     * Construct an XPathEvaluationResult object.
     *
     * @param resultObject internal XPath result object
     * @param type class type
     * @throws TransformerException if there is an error reading the XPath
     * result.
     */
    public XPathResultImpl(XObject resultObject, Class<T> type)
            throws TransformerException {
        this.resultObject = resultObject;
        resultType = resultObject.getType();
        this.type = type;
        getResult(resultObject);
    }

    /**
     * Return the result type as an enum specified by {@code XPathResultType}
     * @return the result type
     */
    @Override
    public XPathResultType type() {
        return mapToType;
    }

    /**
     * Returns the value of the result as the type &lt;T&gt; specified for the class.
     *
     * @return The value of the result.
     */
    @Override
    public T value() {
        Objects.requireNonNull(type);
        try {
            return getValue(resultObject, type);
        } catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Read the XObject and set values in accordance with the result type
     * @param resultObject  internal XPath result object
     * @throws TransformerException  if there is an error reading the XPath
     * result.
     */
    private void getResult(XObject resultObject) throws TransformerException {
        switch (resultType) {
            case XObject.CLASS_BOOLEAN:
                boolValue = resultObject.bool();
                mapToType = XPathResultType.BOOLEAN;
                break;
            case XObject.CLASS_NUMBER:
                numValue = resultObject.num();
                mapToType = XPathResultType.NUMBER;
                break;
            case XObject.CLASS_STRING:
                strValue = resultObject.str();
                mapToType = XPathResultType.STRING;
                break;
            case XObject.CLASS_NODESET:
                mapToType = XPathResultType.NODESET;
                nodeList = resultObject.nodelist();
                break;
            case XObject.CLASS_RTREEFRAG:  //NODE
                mapToType = XPathResultType.NODE;
                NodeIterator ni = resultObject.nodeset();
                //Return the first node, or null
                node = ni.nextNode();
                break;
        }
    }

    /**
     * Read the internal result object and return the value in accordance with
     * the type specified.
     *
     * @param <T> The expected class type.
     * @param resultObject internal XPath result object
     * @param type the class type
     * @return The value of the result, null in case of unexpected type.
     * @throws TransformerException  if there is an error reading the XPath
     * result.
     */
    static <T> T getValue(XObject resultObject, Class<T> type) throws TransformerException {
        Objects.requireNonNull(type);
        if (type == XPathEvaluationResult.class) {
            return type.cast(new XPathResultImpl<T>(resultObject, type));
        }
        int resultType = classToInternalType(type);
        switch (resultType) {
            case XObject.CLASS_BOOLEAN:
                return type.cast(resultObject.bool());
            case XObject.CLASS_NUMBER:
                if (Double.class == type) {
                    return type.cast(resultObject.num());
                } else if (Integer.class == type) {
                    return type.cast((int)resultObject.num());
                } else if (Long.class == type) {
                    return type.cast((long)resultObject.num());
                }
                /*
                  This is to suppress warnings. By the current specification,
                among numeric types, only Double, Integer and Long are supported.
                */
                break;
            case XObject.CLASS_STRING:
                return type.cast(resultObject.str());
            case XObject.CLASS_NODESET:
                XPathNodes nodeSet = new XPathNodesImpl(resultObject.nodelist(),
                        Node.class);
                return type.cast(nodeSet);
            case XObject.CLASS_RTREEFRAG:  //NODE
                NodeIterator ni = resultObject.nodeset();
                //Return the first node, or null
                try {
                    return type.cast(ni.nextNode());
                } catch (RuntimeException e) {
                    throw new TransformerException(e.getMessage(), e.getCause());
                }
        }

        return null;
    }

    /**
     * Map the specified class type to the internal result type
     *
     * @param <T> The expected class type.
     * @param type the class type
     * @return the internal XObject type.
     */
    static <T> int classToInternalType(Class<T> type) {
        if (type == Boolean.class) {
            return XObject.CLASS_BOOLEAN;
        } else if (Number.class.isAssignableFrom(type)) {
            return XObject.CLASS_NUMBER;
        } else if (type == String.class) {
            return XObject.CLASS_STRING;
        } else if (type == XPathNodes.class) {
            return XObject.CLASS_NODESET;
        } else if (type == Node.class) {
            return XObject.CLASS_RTREEFRAG;
        }
        return XObject.CLASS_NULL;
    }
}
