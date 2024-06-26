package com.install.domain.metering.entity;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.install.domain.code.entity.Code;
import com.install.domain.common.BaseTimeEntity;
import com.install.domain.metering.dto.MeteringDto.MeteringResponse;
import com.install.domain.modem.entity.Modem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.metering.entity
 * @since : 11.06.24
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "meter_info")
@Entity
public class MeterInfo extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "meter_info_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "modem_id")
	private Modem modem;

	@Column(name = "metering_date")
	private LocalDateTime meteringDate;

	@Column(name = "metering_usage", nullable = false, precision = 10, scale = 3)
	private BigDecimal meteringUsage;

	@Column(name = "metering_data", nullable = false, precision = 10, scale = 3)
	private BigDecimal meteringData;

	@Column(name = "metering_temp", nullable = false, precision = 10, scale = 3)
	private BigDecimal meteringTemp;

	@ManyToOne
	@JoinColumn(name = "metering_state", nullable = false)
	private Code meteringStateCd;

	public MeteringResponse toMeteringResponse() {
		return MeteringResponse.builder()
			.modemNo(this.modem.getModemNo())
			.meteringDate(this.meteringDate)
			.meteringData(this.meteringData)
			.meteringUsage(this.meteringUsage)
			.meteringTemp(this.meteringTemp)
			.meteringStateCd(this.meteringStateCd.toDto())
			.build();
	}
}
