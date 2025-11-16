package homework.second.controller;

import homework.second.dto.UserDto;
import homework.second.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(UserController.URI_API_USERS)
public class UserController {
    public static final String URI_API_USERS = "/api/users";

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(service.listUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        UserDto dto = service.getUser(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto dto) {
        UserDto created = service.createUser(dto);

        if (created.getId() == null) {
            return ResponseEntity.status(201).body(created);
        }

        return ResponseEntity.created(URI.create(URI_API_USERS + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @Valid @RequestBody UserDto dto) {
        UserDto updated = service.updateUser(id, dto);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
