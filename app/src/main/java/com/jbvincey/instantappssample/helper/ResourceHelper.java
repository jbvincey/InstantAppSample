package com.jbvincey.instantappssample.helper;

import android.content.Context;

/**
 * Created by jean-baptistevincey on 23/06/2017.
 */

public final class ResourceHelper {

    public static int getImageResourceFromName(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

}
