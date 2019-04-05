/* Copyright (c) 2017 dbradley. All rights reserved. */
package dbrad.jacocoverage.menuctxt.action.reqproc.htmext;

import java.util.ArrayList;

/**
 * Class that represents a HTML tag where the tag is provided as a string
 * ("div") to the constructor.
 * <p>
 * Supports the appending of sub-tags, attributes (via class AttrItem) and
 * content-text that is contained within the tags bounds.
 * <p>
 * Basic support is provided. Simple tags such as 'br', 'meta' which are
 * standalone none bounded tags should be included as sub-tags as content-text.
 *
 * @author dbradley
 */
class HtmlTag {

    /** the string that is the tag */
    private final String tagName;
    /** attribute list array for the tag */
    private final ArrayList<AttrItem> attrItemArr = new ArrayList<>(6);
    /** sub-tags of this tag item */
    private final ArrayList<HtmlTag> htmlInnerTagArr = new ArrayList<>(6);

    /**
     * Create a HTML-tag object that will have a closing bound. None closing
     * tags such as 'br', 'meta' should use the HtmlTagContentText extension and
     * appended to this HTML-tag.
     *
     * @param tagName string of the tag-name
     */
    HtmlTag(String tagName) {
        this.tagName = tagName.toLowerCase();
    }

    /**
     * Should be used by the HtmlTagContentText extension which has no tag-name.
     */
    protected HtmlTag() {
        this.tagName = null;
    }

    /**
     * Append attributes for this HtmlTag.
     *
     * @param attrItem an attribute object
     */
    void appendAttr(AttrItem attrItem) {
        attrItemArr.add(attrItem);
    }

    /**
     * Append a sub-tag for this HtmlTag.
     *
     * @param subTag an HtmlTag that is a sub-tag of this HtmlTag object
     *
     * @return the subTag provided
     */
    HtmlTag appendTag(HtmlTag subTag) {
        this.htmlInnerTagArr.add(subTag);
        return subTag;
    }

    /**
     * Append a content-text virtual HtmlTag (basically the text that is bound
     * between.
     *
     * @param contentText string of the text to be in this object
     */
    void appendContentText(String contentText) {
        // (contentText is a psuedo tag)
        this.htmlInnerTagArr.add(new HtmlTagContentText(contentText));
    }

    /**
     * The complete HTML code to string of this HtmlTag object and its sub-tags
     * and attributes.
     *
     * @return string of HTML code
     */
    @Override
    public String toString() {
        String tagString = String.format("<%s", this.tagName);

        // attributes need to be added
        for (AttrItem attr : attrItemArr) {
            tagString = String.format("%s %s", tagString, attr.toString());
        }
        tagString = String.format("%s>", tagString);

        // add the inner tags (contentText is a psuedo tag)
        for (HtmlTag tagItem : htmlInnerTagArr) {
            tagString = String.format("%s\n%s", tagString, tagItem.toString());
        }
        // close the tag
        tagString = String.format("%s\n</%s>", tagString, this.tagName);
        return tagString;
    }
}
