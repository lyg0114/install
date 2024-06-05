package com.install.domain.consumer.entity;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.StringUtils.hasText;

import com.install.domain.common.BaseTimeEntity;
import com.install.domain.consumer.dto.ConsumerDto.ConsumerRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.consumer.entity
 * @since : 04.06.24
 */
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "consumer")
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

  @Embedded
  private Address address;

  @Embedded
  private Location location;

  public void updateConsumer(ConsumerRequest consumerDto) {
    this.consumerNo = consumerDto.getConsumerNo();
    this.consumerName = consumerDto.getConsumerName();
    this.meterNo = consumerDto.getMeterNo();
    this.address.setCity(consumerDto.getCity());

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
}