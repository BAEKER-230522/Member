package com.baeker.member.member.in.resDto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.baeker.member.member.in.resDto.QSchedulerResDto is a Querydsl Projection type for SchedulerResDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QSchedulerResDto extends ConstructorExpression<SchedulerResDto> {

    private static final long serialVersionUID = -907767067L;

    public QSchedulerResDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> baekJoonName, com.querydsl.core.types.Expression<Integer> bronze, com.querydsl.core.types.Expression<Integer> silver, com.querydsl.core.types.Expression<Integer> gold, com.querydsl.core.types.Expression<Integer> diamond, com.querydsl.core.types.Expression<Integer> ruby, com.querydsl.core.types.Expression<Integer> platinum, com.querydsl.core.types.Expression<Integer> lstSolvedProblemId) {
        super(SchedulerResDto.class, new Class<?>[]{long.class, String.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class}, id, baekJoonName, bronze, silver, gold, diamond, ruby, platinum, lstSolvedProblemId);
    }

}

