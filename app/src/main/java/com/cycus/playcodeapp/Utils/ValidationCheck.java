package com.cycus.playcodeapp.Utils;

import com.cycus.playcodeapp.EventBeans.UserProfileStatus;

import de.greenrobot.event.EventBus;

/**
 * Created by Arun_Saini on 28-06-2016.
 */
public class ValidationCheck {
    private boolean valid=false;
    private static ValidationCheck validationCheck;

    public static ValidationCheck getInstance() {
        if (validationCheck == null)
            validationCheck = new ValidationCheck();
        return validationCheck;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
