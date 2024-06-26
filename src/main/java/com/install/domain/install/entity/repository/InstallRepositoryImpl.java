package com.install.domain.install.entity.repository;

import static com.install.domain.code.entity.CodeSet.*;
import static com.install.domain.consumer.entity.QConsumer.*;
import static com.install.domain.install.entity.QInstallInfo.*;
import static com.install.domain.modem.entity.QModem.*;
import static java.util.Objects.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.install.domain.consumer.dto.ConsumerDto.ConsumerSearchCondition;
import com.install.domain.install.entity.InstallInfo;
import com.install.domain.install.entity.QInstallInfo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.install.entity.repository
 * @since : 06.06.24
 */
@RequiredArgsConstructor
public class InstallRepositoryImpl implements InstallRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<InstallInfo> searchInstallInfos(ConsumerSearchCondition condition, Pageable pageable) {
		return null;
	}

	@Override
	public Page<InstallInfo> searchInstallInfoPageByModem(Long modemId, Pageable pageable) {
		return PageableExecutionUtils
			.getPage(
				queryFactory
					.select(installInfo)
					.from(installInfo)
					.leftJoin(installInfo.modem, modem).fetchJoin()
					.leftJoin(installInfo.consumer, consumer).fetchJoin()
					.where(modemIdEq(modemId))
					.orderBy(installInfo.workTime.desc())
					.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.fetch(),
				pageable,
				() -> queryFactory
					.select(installInfo.count())
					.from(installInfo)
					.where(modemIdEq(modemId))
					.fetchOne()
			);
	}

	@Override
	public Page<InstallInfo> searchInstallInfoPageByConsumer(Long consumerId, Pageable pageable) {
		return PageableExecutionUtils
			.getPage(
				queryFactory
					.select(installInfo)
					.from(installInfo)
					.leftJoin(installInfo.modem, modem).fetchJoin()
					.leftJoin(installInfo.consumer, consumer).fetchJoin()
					.where(consumerIdEq(consumerId))
					.orderBy(installInfo.workTime.desc())
					.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.fetch(),
				pageable,
				() -> queryFactory
					.select(installInfo.count())
					.from(installInfo)
					.where(consumerIdEq(consumerId))
					.fetchOne()
			);
	}

	@Override
	public boolean isInstalledModem(Long modemId) {
		QInstallInfo subInstallInfo = new QInstallInfo("subInstallInfo");
		InstallInfo result = queryFactory
			.selectFrom(installInfo)
			.where(
				modemIdEq(modemId),
				installInfo.workTypeCd.code.ne(MODEM_INSTALL_STATUS_DEMOLISH.getCode()),
				installInfo.workTime.in(
					JPAExpressions
						.select(installInfo.workTime.max())
						.from(installInfo)
						.where(modemIdEq(modemId))
				)
			).fetchOne();

		return !isNull(result);
	}

	private BooleanExpression modemIdEq(Long modemId) {
		return modemId > 0 ? installInfo.modem.id.eq(modemId) : null;
	}

	private BooleanExpression consumerIdEq(Long consumerId) {
		return consumerId > 0 ? installInfo.consumer.id.eq(consumerId) : null;
	}
}
