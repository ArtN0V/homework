package org.notification.service;

import org.notification.kafka.UserOperation;

public interface MailService {
    /**
     * Отправляет простое текстовое письмо.
     *
     * @param to      адрес получателя
     * @param subject тема письма
     * @param text    текстовое содержимое письма
     */
    void sendSimpleMessage(String to, String subject, String text);

    /**
     * Отправляет уведомление, связанное с операцией над пользователем,
     * например созданием или удалением учётной записи.
     *
     * @param email     адрес получателя
     * @param operation тип выполняемой операции над пользователем
     */
    void sendUserOperationEmail(String email, UserOperation operation);
}
