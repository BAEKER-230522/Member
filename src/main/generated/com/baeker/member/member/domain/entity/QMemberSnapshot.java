package com.baeker.member.member.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberSnapshot is a Querydsl query type for MemberSnapshot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberSnapshot extends EntityPathBase<MemberSnapshot> {

    private static final long serialVersionUID = 880590734L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberSnapshot memberSnapshot = new QMemberSnapshot("memberSnapshot");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final StringPath baekJoonName = _super.baekJoonName;

    //inherited
    public final NumberPath<Integer> bronze = _super.bronze;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath dayOfWeek = createString("dayOfWeek");

    //inherited
    public final NumberPath<Integer> diamond = _super.diamond;

    //inherited
    public final NumberPath<Integer> gold = _super.gold;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final NumberPath<Integer> lastSolvedProblemId = _super.lastSolvedProblemId;

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    //inherited
    public final NumberPath<Integer> platinum = _super.platinum;

    //inherited
    public final NumberPath<Integer> ruby = _super.ruby;

    //inherited
    public final NumberPath<Integer> silver = _super.silver;

    public QMemberSnapshot(String variable) {
        this(MemberSnapshot.class, forVariable(variable), INITS);
    }

    public QMemberSnapshot(Path<? extends MemberSnapshot> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberSnapshot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberSnapshot(PathMetadata metadata, PathInits inits) {
        this(MemberSnapshot.class, metadata, inits);
    }

    public QMemberSnapshot(Class<? extends MemberSnapshot> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

