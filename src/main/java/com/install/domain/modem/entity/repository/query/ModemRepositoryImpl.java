package com.install.domain.modem.entity.repository.query;

import static com.install.domain.consumer.entity.QConsumer.consumer;
import static com.install.domain.modem.entity.QModem.modem;
import static java.util.Objects.isNull;

import com.install.domain.code.entity.QCode;
import com.install.domain.modem.dto.ModemDto.ModemSearchCondition;
import com.install.domain.modem.entity.Modem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.modem.entity.repository.query
 * @since : 11.06.24
 */
@RequiredArgsConstructor
public class ModemRepositoryImpl implements ModemRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private QCode code1 = new QCode("code1");
  private QCode code2 = new QCode("code2");

  @Override
  public Page<Modem> searchModems(ModemSearchCondition condition, Pageable pageable) {
    return PageableExecutionUtils
        .getPage(
            queryFactory
                .select(modem)
                .from(modem)
                .leftJoin(modem.installedConsumer, consumer).fetchJoin()
                .leftJoin(modem.modemTypeCd, code1).fetchJoin()
                .leftJoin(modem.modemStatusCd, code2).fetchJoin()
                .where(
                    modemNoEq(condition.getModemNo()),
                    consumerNoEq(condition.getConsumerNo())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(),
            pageable,
            () -> queryFactory
                .select(modem.count())
                .from(modem)
                .leftJoin(modem.installedConsumer, consumer).fetchJoin()
                .leftJoin(modem.modemTypeCd, code1).fetchJoin()
                .leftJoin(modem.modemStatusCd, code2).fetchJoin()
                .where(
                    modemNoEq(condition.getModemNo()),
                    consumerNoEq(condition.getConsumerNo())
                )
                .fetchOne()
        );
  }

  private BooleanExpression modemNoEq(String modemNo) {
    return !isNull(modemNo) ? modem.modemNo.eq(modemNo) : null;
  }

  private BooleanExpression consumerNoEq(String consumerNo) {
    return !isNull(consumerNo) ? consumer.consumerNo.eq(consumerNo) : null;
  }
}