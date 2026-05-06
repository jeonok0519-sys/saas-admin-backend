package com.saas.admin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> R<T> success() {
        return new R<>(200, "操作成功", null);
    }

    public static <T> R<T> success(T data) {
        return new R<>(200, "操作成功", data);
    }

    public static <T> R<T> success(String message, T data) {
        return new R<>(200, message, data);
    }

    public static <T> R<T> error(String message) {
        return new R<>(500, message, null);
    }

    public static <T> R<T> error(Integer code, String message) {
        return new R<>(code, message, null);
    }

    public static <T> R<T> unauthorized(String message) {
        return new R<>(401, message, null);
    }

    public static <T> R<T> forbidden(String message) {
        return new R<>(403, message, null);
    }
}
