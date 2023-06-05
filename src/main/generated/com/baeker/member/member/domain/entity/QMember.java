package com.baeker.member.member.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 141873066L;

    public static final QMember member = new QMember("member1");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath about = createString("about");

    public final StringPath baekJoonName = createString("baekJoonName");

    //inherited
    public final NumberPath<Integer> bronze = _super.bronze;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Integer> diamond = _super.diamond;

    public final StringPath email = createString("email");

    //inherited
    public final NumberPath<Integer> gold = _super.gold;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath kakaoProfileImage = createString("kakaoProfileImage");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final ListPath<Long, NumberPath<Long>> myStudies = this.<Long, NumberPath<Long>>createList("myStudies", Long.class, NumberPath.class, PathInits.DIRECT2);

    public final BooleanPath newMember = createBoolean("newMember");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    //inherited
    public final NumberPath<Integer> platinum = _super.platinum;

    public final StringPath profileImg = createString("profileImg");

    public final StringPath provider = createString("provider");

    //inherited
    public final NumberPath<Integer> ruby = _super.ruby;

    //inherited
    public final NumberPath<Integer> sliver = _super.sliver;

    public final ListPath<MemberSnapshot, QMemberSnapshot> snapshotList = this.<MemberSnapshot, QMemberSnapshot>createList("snapshotList", MemberSnapshot.class, QMemberSnapshot.class, PathInits.DIRECT2);

    public final StringPath token = createString("token");

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

