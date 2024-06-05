package com.install.domain.consumer.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.install.domain.consumer.dto.ConsumerDto;
import com.install.domain.consumer.dto.ConsumerDto.ConsumerRequest;
import com.install.domain.consumer.entity.Address;
import com.install.domain.consumer.entity.Consumer;
import com.install.domain.consumer.entity.Location;
import com.install.domain.consumer.entity.repository.ConsumerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.consumer.service
 * @since : 04.06.24
 */
@Transactional
@SpringBootTest
class ConsumerServiceTest {

  @Autowired
  private ConsumerService consumerService;

  @Autowired
  private ConsumerRepository consumerRepository;

  private ConsumerRequest createConsumerRequest(Long id) {
    ConsumerDto.ConsumerRequest request = ConsumerRequest.builder()
        .consumerNo("consumerNo-" + id)
        .consumerName("consumerName" + id)
        .meterNo("meterNo-" + id)
        .city("city-" + id)
        .build();

    return request;
  }

  private Consumer createConsumer(Long id) {
    return Consumer.builder()
        .consumerNo("consumerNo-" + id)
        .consumerName("consumerName" + id)
        .meterNo("meterNo-" + id)
        .address(Address.builder()
            .street("street-" + id)
            .city("city-" + id)
            .zipcode("zipcode-" + id)
            .build())
        .location(Location.builder()
            .geoX("getX-" + id)
            .geoY("getY-" + id)
            .build())
        .build();
  }

  @Test
  void 고객정보_단건_등록에_성공한다() {
    //given
    ConsumerDto.ConsumerRequest consumerRequest = createConsumerRequest(1L);

    //when
    consumerService.addConsumer(consumerRequest);

    //then
    Consumer findConsumer = consumerRepository.findByConsumerNo("consumerNo-1").orElseThrow();

    assertThat(findConsumer.getConsumerNo()).isEqualTo(consumerRequest.getConsumerNo());
    assertThat(findConsumer.getConsumerName()).isEqualTo(consumerRequest.getConsumerName());
    assertThat(findConsumer.getMeterNo()).isEqualTo(consumerRequest.getMeterNo());
  }

  @Test
  void 고객정보_수정에_성공한다() {
    //given
    Consumer savedConsumer = consumerRepository.save(createConsumer(1L));
    ConsumerRequest consumerRequest = createConsumerRequest(2L);

    //when
    consumerService.updateConsumer(savedConsumer.getId(), consumerRequest);

    //then
    Consumer findConsumer = consumerRepository.findByConsumerNo("consumerNo-2")
        .orElseThrow();

    assertThat(findConsumer.getConsumerNo()).isEqualTo(consumerRequest.getConsumerNo());
    assertThat(findConsumer.getConsumerName()).isEqualTo(consumerRequest.getConsumerName());
    assertThat(findConsumer.getMeterNo()).isEqualTo(consumerRequest.getMeterNo());
  }
}