package com.example.fastboard.domain.board.dto.response;

public record BoardPostRes (
  String author,
  Long id,
  String title,
  String content
){}
