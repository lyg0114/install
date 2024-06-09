package com.install.domain.consumer.entity.repository;

import com.install.domain.consumer.dto.ConsumerDto.ConsumerSearchCondition;
import com.install.domain.consumer.entity.Consumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.consumer.entity.repository
 * @since : 09.06.24
 */
public class ConsumerRepositoryImpl implements ConsumerRepositoryCustom{

  @Override
  public Page<Consumer> searchConsumer(ConsumerSearchCondition condition, Pageable pageable) {
    return null;
  }
}