package com.install.domain.consumer.dto;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.consumer.dto
 * @since : 03.06.24
 */
public class ConsumerDto {


  @ToString
  @Getter
  @Builder
  @AllArgsConstructor(access = PRIVATE)
  public class ConsumerSearchCondition {
  }

  @ToString
  @Getter
  @Builder
  @AllArgsConstructor(access = PRIVATE)
  public static class ConsumerRequest {
  }

  @ToString
  @Getter
  @Builder
  @AllArgsConstructor
  public static class ConsumerResponse {
  }

}