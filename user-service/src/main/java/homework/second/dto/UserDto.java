package homework.second.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO для передачи данных о пользователе в API.
 */
public class UserDto {

    private Long id;

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotBlank(message = "email must not be blank")
    @Email(message = "email must be valid")
    private String email;

    private Integer age;

    public UserDto() {}

    public UserDto(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
