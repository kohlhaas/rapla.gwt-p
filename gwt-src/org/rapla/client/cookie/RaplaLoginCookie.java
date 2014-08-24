package org.rapla.client.cookie;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.rapla.storage.dbrm.LoginTokens;

import com.google.gwt.user.client.Cookies;

public class RaplaLoginCookie {
	final Logger logger = Logger.getLogger("RaplaLoginCookie");
	public static final String LOGIN_COOKIE = "raplaLoginToken";

	private static final RaplaLoginCookie INSTANCE = new RaplaLoginCookie();

	public static RaplaLoginCookie getCookie() {
		return INSTANCE;
	}

	public void updateLogin(LoginTokens result) {
		Cookies.setCookie(LOGIN_COOKIE, result.toString());
	}

	public boolean isAlreadyLoggedIn() {
		logger.log(Level.INFO, "Looking for cookie");
		String cookie = Cookies.getCookie(LOGIN_COOKIE);
		if (cookie != null) {
			LoginTokens token = LoginTokens.fromString(cookie);
			boolean valid = token.isValid();
			logger.log(Level.INFO, "found cookie: " + valid);
			return valid;
		}
		return false;
	}

}
