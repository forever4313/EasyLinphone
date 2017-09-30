package com.xuchongyang.easyphone.callback;

import org.linphone.core.LinphoneCore;

/**
 * Created by Mark Xu on 2017/9/21.
 * Site: http://xuchongyang.com
 */

public abstract class RegistrationCallback {
    public void registrationNone() {}

    public void registrationProgress() {}

    public void registrationOk() {}

    public void registrationCleared() {}

    public void registrationFailed() {}
}
