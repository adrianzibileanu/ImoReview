package com.imoreview.app.service.mapper;

import com.imoreview.app.domain.Attachment;
import com.imoreview.app.domain.User;
import com.imoreview.app.service.dto.AttachmentDTO;
import com.imoreview.app.service.dto.UserDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link Attachment} and its DTO {@link AttachmentDTO}.
 */

@Mapper(componentModel = "spring", uses = {})
public interface AttachmentMapper extends EntityMapper<AttachmentDTO, Attachment> {
    @Mapping(target = "manytoone", source = "manytoone", qualifiedByName = "userLogin")
    AttachmentDTO toDto(Attachment s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
