// $Id$
//
// (c) COPYRIGHT MIT, ECRIM and Keio University, 2011
// Please first read the full copyright statement in file COPYRIGHT.html

package org.w3c.css.atrules.css3.media;

import org.w3c.css.atrules.css.media.MediaFeature;
import org.w3c.css.atrules.css.media.MediaRangeFeature;
import org.w3c.css.util.ApplContext;
import org.w3c.css.util.InvalidParamException;
import org.w3c.css.values.CssComparator;
import org.w3c.css.values.CssExpression;
import org.w3c.css.values.CssResolution;
import org.w3c.css.values.CssTypes;
import org.w3c.css.values.CssValue;

/**
 * @spec https://www.w3.org/TR/2017/CR-mediaqueries-4-20170905/#descdef-media-resolution
 */
public class MediaResolution extends MediaRangeFeature {

    /**
     * Create a new MediaResolution
     */
    public MediaResolution() {
    }

    /**
     * Create a new MediaResolution
     *
     * @param expression The expression for this media feature
     * @throws org.w3c.css.util.InvalidParamException
     *          Values are incorrect
     */
    public MediaResolution(ApplContext ac, String modifier,
                           CssExpression expression, boolean check)
            throws InvalidParamException {

        if (expression != null) {
            if (expression.getCount() > 2) {
                throw new InvalidParamException("unrecognize", ac);
            }
            if (expression.getCount() == 0) {
                throw new InvalidParamException("few-value", getFeatureName(), ac);
            }
            CssValue val = expression.getValue();
            // it must be a >=0 integer only
            switch (val.getType()) {
                case CssTypes.CSS_COMPARATOR:
                    if (modifier != null) {
                        throw new InvalidParamException("nomodifierrangemedia",
                                getFeatureName(), ac);
                    }
                    CssComparator p = (CssComparator) val;
                    value = checkValue(ac, p.getParameters(), getFeatureName());
                    comparator = p.toString();
                    expression.next();
                    if (!expression.end()) {
                        val = expression.getValue();
                        if (val.getType() != CssTypes.CSS_COMPARATOR) {
                            throw new InvalidParamException("unrecognize", ac);
                        }
                        CssComparator p2;
                        p2 = (CssComparator) val;
                        otherValue = checkValue(ac, p2.getParameters(), getFeatureName());
                        otherComparator = p2.toString();
                        checkComparators(ac, p, p2, getFeatureName());
                    }
                    break;
                case CssTypes.CSS_RESOLUTION:
                    value = checkValue(ac, expression, getFeatureName());
                    break;
                default:
                    throw new InvalidParamException("unrecognize", ac);

            }
            expression.next();
            setModifier(ac, modifier);
        } else {
            if (modifier != null) {
                throw new InvalidParamException("nomodifiershortmedia",
                        getFeatureName(), ac);
            }
        }
    }

    static CssValue checkValue(ApplContext ac, CssExpression expression, String caller)
            throws InvalidParamException {
        if (expression.getCount() == 0) {
            throw new InvalidParamException("few-value", caller, ac);
        }
        CssValue val = expression.getValue();
        CssValue value = null;
        if (val.getType() == CssTypes.CSS_RESOLUTION) {
            CssResolution valnum = (CssResolution) val;
            value = valnum;
        } else {
            throw new InvalidParamException("unrecognize", ac);
        }
        return value;
    }


    public MediaResolution(ApplContext ac, String modifier, CssExpression expression)
            throws InvalidParamException {
        this(ac, modifier, expression, false);
    }

    /**
     * Returns the value of this media feature.
     */

    public Object get() {
        return value;
    }

    /**
     * Returns the name of this media feature.
     */
    public final String getFeatureName() {
        return "resolution";
    }

    /**
     * Compares two media features for equality.
     *
     * @param other The other media features.
     */
    public boolean equals(MediaFeature other) {
        try {
            MediaResolution mr = (MediaResolution) other;
            return (((value == null) && (mr.value == null)) || ((value != null) && value.equals(mr.value)))
                    && (((modifier == null) && (mr.modifier == null)) || ((modifier != null) && modifier.equals(mr.modifier)));
        } catch (ClassCastException cce) {
            return false;
        }

    }
}
