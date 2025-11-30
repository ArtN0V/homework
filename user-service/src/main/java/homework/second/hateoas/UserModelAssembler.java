package homework.second.hateoas;

import homework.second.controller.UserController;
import homework.second.dto.UserDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDto, UserDto> {

    @Override
    public UserDto toModel(UserDto dto) {
        dto.add(linkTo(methodOn(UserController.class).getById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(UserController.class).getAll()).withRel("users"));
        return dto;
    }
}
