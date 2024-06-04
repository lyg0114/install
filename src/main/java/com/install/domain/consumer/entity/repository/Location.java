package com.install.domain.consumer.entity.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.consumer.entity.repository
 * @since : 04.06.24
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class Location {

  @Column(name = "geo_x")
  private String geoX;

  @Column(name = "geo_y")
  private String geoY;

}
