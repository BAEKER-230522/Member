package com.baeker.member.member.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseEntity is a Querydsl query type for BaseEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseEntity extends EntityPathBase<BaseEntity> {

    private static final long serialVersionUID = 603190052L;

    public static final QBaseEntity baseEntity = new QBaseEntity("baseEntity");

    public final StringPath baekJoonName = createString("baekJoonName");

    public final NumberPath<Integer> bronze = createNumber("bronze", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> diamond = createNumber("diamond", Integer.class);

    public final NumberPath<Integer> gold = createNumber("gold", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modifyDate = createDateTime("modifyDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> platinum = createNumber("platinum", Integer.class);

    public final NumberPath<Integer> ruby = createNumber("ruby", Integer.class);

    public final NumberPath<Integer> silver = createNumber("silver", Integer.class);

    public QBaseEntity(String variable) {
        super(BaseEntity.class, forVariable(variable));
    }

    public QBaseEntity(Path<? extends BaseEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseEntity(PathMetadata metadata) {
        super(BaseEntity.class, metadata);
    }

}

