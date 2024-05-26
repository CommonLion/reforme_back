package reforme.reforme.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBody<T> {
    private Integer statusCode;
    private T data;

    public ResponseBody(Integer statusCode) {
        this.statusCode = statusCode;
        this.data = null;
    }

    public ResponseBody(Integer statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }
}
