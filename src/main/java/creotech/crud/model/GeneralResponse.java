package creotech.crud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralResponse<T> {

    private String status;        // "success" atau "error"

    private String message;       // pesan yang bisa dibaca manusia

    private T data;               // payload utama

    private Object errors;        // bisa String atau map validasi

    private LocalDateTime timestamp; // biar tau kapan response dikirim
}
