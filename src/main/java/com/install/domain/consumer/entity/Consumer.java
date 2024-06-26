package com.install.domain.consumer.entity;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static org.springframework.util.StringUtils.*;

import java.time.LocalDateTime;

import com.install.domain.common.BaseTimeEntity;
import com.install.domain.consumer.dto.ConsumerDto.ConsumerRequest;
import com.install.domain.consumer.dto.ConsumerDto.ConsumerResponse;
import com.install.domain.install.dto.InstallDto.InstallRequest;
import com.install.domain.modem.entity.Modem;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.consumer.entity
 * @since : 04.06.24
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "consumer",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"consumer_no"})
	})
@Entity
public class Consumer extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "consumer_id")
	private Long id;

	@Column(name = "consumer_no")
	private String consumerNo;

	@Column(name = "consumer_nm")
	private String consumerName;

	@Column(name = "meter_no")
	private String meterNo;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "installed_modem_id")
	private Modem installedModem;

	@Column(name = "install_date")
	private LocalDateTime installDate;

	@Embedded
	private Address address;

	@Embedded
	private Location location;

	public void updateConsumer(ConsumerRequest consumerDto) {
		if (hasText(consumerDto.getConsumerNo())) {
			this.consumerNo = consumerDto.getConsumerNo();
		}
		if (hasText(consumerDto.getConsumerName())) {
			this.consumerName = consumerDto.getConsumerName();
		}
		if (hasText(consumerDto.getMeterNo())) {
			this.meterNo = consumerDto.getMeterNo();
		}
		if (hasText(consumerDto.getCity())) {
			this.address.setCity(consumerDto.getCity());
		}
		if (hasText(consumerDto.getStreet())) {
			this.address.setStreet(consumerDto.getStreet());
		}
		if (hasText(consumerDto.getZipcode())) {
			this.address.setZipcode(consumerDto.getZipcode());
		}
		if (hasText(consumerDto.getGeoX()) && hasText(consumerDto.getGeoY())) {
			this.location.setGeoX(consumerDto.getGeoX());
			this.location.setGeoY(consumerDto.getGeoY());
		}
	}

	public void installedModem(Modem installedModem, InstallRequest requestDto) {
		this.installedModem = installedModem;
		this.installDate = requestDto.getWorkTime();
		installedModem.installedConsumer(this);
	}

	public void demolishModem() {
		this.installedModem = null;
		this.installDate = null;
	}

	public ConsumerResponse toResponse() {
		return ConsumerResponse.builder()
			.installedModemNo(!isNull(installedModem) ? installedModem.getModemNo() : null)
			.consumerNo(consumerNo)
			.consumerName(consumerName)
			.meterNo(meterNo)
			.city(!isNull(getAddress()) ? getAddress().getCity() : null)
			.street(!isNull(getAddress()) ? getAddress().getStreet() : null)
			.zipcode(!isNull(getAddress()) ? getAddress().getZipcode() : null)
			.geoX(!isNull(location) ? location.getGeoX() : null)
			.geoY(!isNull(location) ? location.getGeoY() : null)
			.build();
	}

	@Override
	public String toString() {
		return "Consumer{" +
			"id=" + id +
			", consumerNo='" + consumerNo + '\'' +
			", consumerName='" + consumerName + '\'' +
			", meterNo='" + meterNo + '\'' +
			", installDate=" + installDate +
			", address=" + address +
			", location=" + location +
			'}';
	}
}