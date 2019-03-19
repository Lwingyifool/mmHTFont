package com.honestradesmm.mmhtfont2;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by chris on 19/12/2013
 * Project: Calligraphy
 */
public class HTFContextWrapper extends ContextWrapper {

    private HTFLayoutInflater mInflater;

    private final int mAttributeId;

    /**
     * Uses the default configuration from {@link com.honestradesmm.mmhtfont2.HTFConfig}
     *
     * Remember if you are defining default in the
     * {@link com.honestradesmm.mmhtfont2.HTFConfig} make sure this is initialised before
     * the activity is created.
     *
     * @param base ContextBase to Wrap.
     * @return ContextWrapper to pass back to the activity.
     */
    public static ContextWrapper wrap(Context base) {
        return new HTFContextWrapper(base);
    }

    /**
     * You only need to call this <b>IF</b> you call
     * {@link com.honestradesmm.mmhtfont2.HTFConfig.Builder#disablePrivateFactoryInjection()}
     * This will need to be called from the
     * {@link android.app.Activity#onCreateView(android.view.View, String, android.content.Context, android.util.AttributeSet)}
     * method to enable view font injection if the view is created inside the activity onCreateView.
     *
     * You would implement this method like so in you base activity.
     * <pre>
     * {@code
     * public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
     *   return HTFContextWrapper.onActivityCreateView(this, parent, super.onCreateView(parent, name, context, attrs), name, context, attrs);
     * }
     * }
     * </pre>
     *
     * @param activity The activity the original that the ContextWrapper was attached too.
     * @param parent   Parent view from onCreateView
     * @param view     The View Created inside onCreateView or from super.onCreateView
     * @param name     The View name from onCreateView
     * @param context  The context from onCreateView
     * @param attr     The AttributeSet from onCreateView
     * @return The same view passed in, or null if null passed in.
     */
    public static View onActivityCreateView(Activity activity, View parent, View view, String name, Context context, AttributeSet attr) {
        return get(activity).onActivityCreateView(parent, view, name, context, attr);
    }

    /**
     * Get the Calligraphy Activity Fragment Instance to allow callbacks for when views are created.
     *
     * @param activity The activity the original that the ContextWrapper was attached too.
     * @return Interface allowing you to call onActivityViewCreated
     */
    static HTFActivityFactory get(Activity activity) {
        if (!(activity.getLayoutInflater() instanceof HTFLayoutInflater)) {
            throw new RuntimeException("This activity does not wrap the Base Context! See HTFContextWrapper.wrap(Context)");
        }
        return (HTFActivityFactory) activity.getLayoutInflater();
    }

    /**
     * Uses the default configuration from {@link com.honestradesmm.mmhtfont2.HTFConfig}
     *
     * Remember if you are defining default in the
     * {@link com.honestradesmm.mmhtfont2.HTFConfig} make sure this is initialised before
     * the activity is created.
     *
     * @param base ContextBase to Wrap
     */
    HTFContextWrapper(Context base) {
        super(base);
        mAttributeId = HTFConfig.get().getAttrId();
    }

    /**
     * Override the default AttributeId, this will always take the custom attribute defined here
     * and ignore the one set in {@link com.honestradesmm.mmhtfont2.HTFConfig}.
     *
     * Remember if you are defining default in the
     * {@link com.honestradesmm.mmhtfont2.HTFConfig} make sure this is initialised before
     * the activity is created.
     *
     * @param base        ContextBase to Wrap
     * @param attributeId Attribute to lookup.
     * @deprecated use {@link #wrap(android.content.Context)}
     */
    @Deprecated
    public HTFContextWrapper(Context base, int attributeId) {
        super(base);
        mAttributeId = attributeId;
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = new HTFLayoutInflater(LayoutInflater.from(getBaseContext()), this, mAttributeId, false);
            }
            return mInflater;
        }
        return super.getSystemService(name);
    }

}
