package com.baeker.member.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRole is a Querydsl query type for Role
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QRole extends EnumPath<Role> {

    private static final long serialVersionUID = 830713457L;

    public static final QRole role = new QRole("role");

    public QRole(String variable) {
        super(Role.class, forVariable(variable));
    }

    public QRole(Path<Role> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRole(PathMetadata metadata) {
        super(Role.class, metadata);
    }

}

