package zup.com.br.zplay.services.dtos;

import java.io.Serializable;

import lombok.Data;

@Data
public class RatingDTO implements Serializable {

    private String Source;
    private String Value;
}
