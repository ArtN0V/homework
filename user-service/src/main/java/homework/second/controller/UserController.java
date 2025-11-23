package homework.second.controller;

import homework.second.dto.UserDto;
import homework.second.hateoas.UserModelAssembler;
import homework.second.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "users", description = "Операции с пользователями")
public class UserController {

    public static final String URI_API_USERS = "/api/users";

    private final UserService service;
    private final UserModelAssembler assembler;

    public UserController(UserService service, UserModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> list = service.listUsers()
                .stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(service.listUsers());
    }

    @Operation(summary = "Получить пользователя по ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        UserDto dto = service.getUser(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @Operation(summary = "Создать пользователя", description = "Создаёт пользователя и возвращает DTO с ссылками (HATEOAS)")
    @ApiResponse(responseCode = "201", description = "Создан")
    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto dto) {
        UserDto created = service.createUser(dto);

        return ResponseEntity.created(URI.create(URI_API_USERS + "/" + created.getId()))
                .body(assembler.toModel(created));
    }

    @Operation(summary = "Обновить пользователя по ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @Valid @RequestBody UserDto dto) {
        UserDto updated = service.updateUser(id, dto);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @Operation(summary = "Удалить пользователя")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
