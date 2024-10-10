package com.example.fastboard.global.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class PageResponseDTO<T> {

    private final int status;
    private final List<T> data;
    private final PageInfo page;
    private final String message;

    @Builder
    private PageResponseDTO(int status, String message, PageInfo page, List<T> data) {
        this.status = status;
        this.message = message;
        this.page = page;
        this.data = data;
    }

    public static <T> PageResponseDTO<T> okWithData(Page<T> data) {
        return PageResponseDTO.<T>builder()
                .status(HttpStatus.OK.value())
                .data(data.getContent())
                .page(PageInfo.from(data))
                .message(null)
                .build();
    }

    public static <T> PageResponseDTO<T> okWithMessageAndData(String message, Page<T> data) {
        return PageResponseDTO.<T>builder()
                .status(HttpStatus.OK.value())
                .data(data.getContent())
                .page(PageInfo.from(data))
                .message(message)
                .build();
    }
}
