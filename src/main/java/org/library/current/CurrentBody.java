package org.library.current;

import org.library.model.User;

public class CurrentBody {

    private String body = "";
    private String backPage = "/login.fxml";
    private static final CurrentBody currentBody;

    static {
        currentBody = new CurrentBody();
    }

    public static void add(String body) {
        currentBody.setBody(body);
    }

    public void setBody(String body) {
        currentBody.body = body;
    }
    public static CurrentBody getBodyCuurent() {
        return currentBody;
    }

    public String getBody() {
        return body;
    }

    public void setBackPage(String backPage) {
        this.backPage = backPage;
    }

    public String getBackPage() {
        return backPage;
    }
}
