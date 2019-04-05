/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.menuctxt.action.reqproc.htmext;

/**
 * Class that represents an attribute that will be appended to a HtmlTag object.
 *
 * @author dbradley
 */
class AttrItem {

    /** the attribute string name */
    private final String attribute;
    /** the string value of the attribute, and if null the attribute is
     * standalone (no value).
     */
    private final String attrValue;

    /**
     * Create and attribute object that is standalone (no value part)
     *
     * @param attribute string of attribute
     */
    AttrItem(String attribute) {
        this.attribute = attribute.toLowerCase();
        this.attrValue = null;
    }

    /**
     * Create and attribute object that has a value part.
     *
     * @param attribute string of attribute
     * @param attrValue string of attribute value
     */
    AttrItem(String attribute, String attrValue) {
        this.attribute = attribute.toLowerCase();
        this.attrValue = attrValue;
    }

    /**
     * The complete HTML code to string of this attribute object.
     *
     * @return string of HTML code
     */
    @Override
    public String toString() {
        String attrString = this.attribute;
        if (this.attrValue != null) {
            attrString = String.format("%s='%s'", attrString, attrValue);
        }
        return attrString;
    }
}
