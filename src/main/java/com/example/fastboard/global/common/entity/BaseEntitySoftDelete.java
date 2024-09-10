package com.example.fastboard.global.common.entity;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntitySoftDelete extends BaseEntity {

    private LocalDateTime deletedAt;

    protected void delete() {
        deletedAt = LocalDateTime.now();
    }

    public boolean isDelete() {
        return deletedAt != null;
    }

    protected void restore() {
        deletedAt = null;
    }
}
