package tutothr.message.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import tutothr.message.Message;
import tutothr.message.MessageDTO;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapperI {

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "sender.username", target = "senderName")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(target = "type", expression = "java(tutothr.message.MessageDTO.MessageType.CHAT)")
    @Mapping(source = "read", target = "read")
    MessageDTO toDto(Message message);

    List<MessageDTO> toDtos(List<Message> messages);
}