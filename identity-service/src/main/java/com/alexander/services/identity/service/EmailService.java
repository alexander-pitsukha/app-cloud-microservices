package com.alexander.services.identity.service;

import java.util.UUID;

public interface EmailService {

    void sendEmailForCreateUser(String email, UUID token);

    void sendEmailForUpdateUserPassword(String email, UUID token, String password);

}
