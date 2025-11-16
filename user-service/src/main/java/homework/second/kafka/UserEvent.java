package homework.second.kafka;

import java.io.Serializable;

/**
 * Событие пользователя для Kafka.
 * operation: "CREATE" или "DELETE"
 * email: адрес пользователя
 */
public class UserEvent implements Serializable {
    private UserOperation operation;
    private String email;
    private Long userId;
    private String userName;

    public UserEvent() {}

    public UserEvent(UserOperation operation, String email, Long userId, String userName) {
        this.operation = operation;
        this.email = email;
        this.userId = userId;
        this.userName = userName;
    }

    public UserOperation getOperation() {
        return operation;
    }

    public void setOperation(UserOperation operation) {
        this.operation = operation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
