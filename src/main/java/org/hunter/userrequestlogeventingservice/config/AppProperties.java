package org.hunter.userrequestlogeventingservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String userServiceBaseUrl;

    public String getUserServiceBaseUrl() {
        return userServiceBaseUrl;
    }

    public void setUserServiceBaseUrl(String userServiceBaseUrl) {
        this.userServiceBaseUrl = userServiceBaseUrl;
    }

}
