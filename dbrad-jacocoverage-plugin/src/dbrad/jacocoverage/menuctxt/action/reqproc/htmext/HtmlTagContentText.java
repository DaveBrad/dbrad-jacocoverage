/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.menuctxt.action.reqproc.htmext;

/**
 * Class that represents a content-text string that is typically contained
 * between the bounds of HtmlTag. As a HtmlTag extension makes it easier to add
 * it in context/order as a sub-tag but of text format as a virtual HtmlTag.
 *
 * @author dbradley
 */
class HtmlTagContentText extends HtmlTag {

    /** the content-text string (which may contain html code text) */
    private final String contentText;

    /**
     * Create the content-text virtual HtmlTag object.
     *
     * @param contentText string text of the oontent-text
     */
    HtmlTagContentText(String contentText) {
        super(); // there is no tag for a context
        this.contentText = contentText;
    }

    /**
     * The complete HTML code to string of this HtmlTag object and its sub-tags
     * and attributes.
     *
     * @return string of HTML code
     */
    @Override
    public String toString() {
        return String.format("%s", this.contentText);
    }
}
