package homework.second.controller;

import homework.second.dto.UserDto;
import homework.second.hateoas.UserModelAssembler;
import homework.second.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей в системе"
    )
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> list = service.listUsers()
                .stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает пользователя по его идентификатору, если он существует"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        UserDto dto = service.getUser(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @Operation(
            summary = "Создать пользователя",
            description = "Создаёт нового пользователя и возвращает его DTO с HATEOAS-ссылками"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto dto) {
        UserDto created = service.createUser(dto);

        return ResponseEntity.created(URI.create(URI_API_USERS + "/" + created.getId()))
                .body(assembler.toModel(created));
    }

    @Operation(
            summary = "Обновить пользователя по ID",
            description = "Обновляет данные существующего пользователя"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные или пользователь не найден"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @Valid @RequestBody UserDto dto) {
        UserDto updated = service.updateUser(id, dto);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(assembler.toModel(updated));
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя по его идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
