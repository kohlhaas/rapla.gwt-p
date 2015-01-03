package org.rapla.login;

import org.rapla.client.base.View;
import org.rapla.login.LoginView.Presenter;

public interface LoginView extends View<Presenter> {
    public interface Presenter
    {
        void submit(String username, String password);
    }
    void showLogin();
    void showLoginError(String error);
}
