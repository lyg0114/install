package com.install.domain.metering.api;

import com.install.domain.metering.service.MeteringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.metering.api
 * @since : 11.06.24
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/metering/v1")
@RestController
public class MeteringApiController {

  private final MeteringService meteringService;

}