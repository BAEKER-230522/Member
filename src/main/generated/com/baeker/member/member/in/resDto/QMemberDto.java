package com.baeker.member.member.in.resDto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.baeker.member.member.in.resDto.QMemberDto is a Querydsl Projection type for MemberDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberDto extends ConstructorExpression<MemberDto> {

    private static final long serialVersionUID = -96874928L;

    public QMemberDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<java.time.LocalDateTime> createDate, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifyDate, com.querydsl.core.types.Expression<Integer> bronze, com.querydsl.core.types.Expression<Integer> silver, com.querydsl.core.types.Expression<Integer> gold, com.querydsl.core.types.Expression<Integer> diamond, com.querydsl.core.types.Expression<Integer> ruby, com.querydsl.core.types.Expression<Integer> platinum, com.querydsl.core.types.Expression<String> username, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<String> baekJoonName, com.querydsl.core.types.Expression<String> about, com.querydsl.core.types.Expression<String> profileImg, com.querydsl.core.types.Expression<String> kakaoProfileImage, com.querydsl.core.types.Expression<String> provider, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> token, com.querydsl.core.types.Expression<Boolean> newMember) {
        super(MemberDto.class, new Class<?>[]{long.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, int.class, int.class, int.class, int.class, int.class, int.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, boolean.class}, id, createDate, modifyDate, bronze, silver, gold, diamond, ruby, platinum, username, nickname, baekJoonName, about, profileImg, kakaoProfileImage, provider, email, token, newMember);
    }

}

