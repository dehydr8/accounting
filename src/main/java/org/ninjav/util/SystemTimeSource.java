package org.ninjav.util;

import java.util.Date;

/**
 * Created by ninjav on 2016/05/09.
 */
public class SystemTimeSource implements TimeSource {
    @Override
    public Date now() {
        return new Date();
    }
}
