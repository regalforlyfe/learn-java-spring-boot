package creotech.crud.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GeneralResponse<T> {
    private T data;
    private Object errors;
    private LocalDateTime timestamp;

    // helper static biar gak ngetik panjang tiap kali
    public static <T> GeneralResponse<T> success(T data) {
        return new GeneralResponse<>(data, null, LocalDateTime.now());
    }

    public static <T> GeneralResponse<T> error(Object errors) {
        return new GeneralResponse<>(null, errors, LocalDateTime.now());
    }
}
