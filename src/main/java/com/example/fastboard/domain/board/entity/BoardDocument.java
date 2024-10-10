package com.example.fastboard.domain.board.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "boards")
@NoArgsConstructor
@AllArgsConstructor
public class BoardDocument {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;  //es 문서 식별자
    @Field(type = FieldType.Text)
    private String title;  // 게시글 제목
    @Field(type = FieldType.Text)
    private String content;  // 게시글 내용
    @Field(type = FieldType.Text)
    private String nickname;  // 작성자 이메일 (Member 정보)
    @Field(type = FieldType.Long)
    private Long wishCount;  // 위시 수
    @Field(type = FieldType.Long)
    private Long viewCount;  // 조회 수
    @Field(type = FieldType.Keyword)
    private String category;  // 카테고리
}
