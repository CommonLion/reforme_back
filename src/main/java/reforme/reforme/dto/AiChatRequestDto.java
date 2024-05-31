package reforme.reforme.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AiChatRequestDto {
    private String model;
    private List<MessagesDto> messages;

    public AiChatRequestDto(String model, String text)
    {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new MessagesDto("user", text));
    }
}
