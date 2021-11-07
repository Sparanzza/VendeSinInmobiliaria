package com.ilerna.vendesininmobiliarias;

import static com.ilerna.vendesininmobiliarias.Utils.Utils.isEmailAddressValid;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
    public class AuthorizationTest {

    @Test
    public void checkFieldsSignUp(){

    }

    @Test
    public void checkIsEmailAddressValid () {
        assertEquals(isEmailAddressValid("usuario@ilernaonline.com"), "Correct email");
        assertEquals(!isEmailAddressValid("usuario_ilernaonline.com"), "This is not a correct email");
    }
