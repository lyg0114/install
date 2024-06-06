package com.install.domain.install.api;

import com.install.domain.install.dto.InstallDto;
import com.install.domain.install.service.InstallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.install.api
 * @since : 03.06.24
 *
 *  - 기간별 단말기 설치 내역 조회
 *  - 단말기 상태 판별 (외부 api 연동)
 *  - 설치내역 조회
 *    - 단말기 기준으로 설치내역 조회
 *    - 고객정보 기준으로 설치내역 조회
 *  - 단말기 설치
 *  - 단말기 유지보수
 *  - 단말기 철거
 *  - 설치 현황 카운트
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/install/v1")
@RestController
public class InstallApiController {

  private final InstallService installService;

  /**
   *  - 기간별 단말기 설치 내역 조회
   */
  @GetMapping
  public ResponseEntity<Page<InstallDto.InstallResponse>> searchInstalls(
      InstallDto.InstallSearchCondition condition, Pageable pageable) {

    // business logic

    return ResponseEntity
        .status(HttpStatus.OK).body(null);
  }

  /**
   * - 단말기 기준으로 설치내역 조회
   */
  @GetMapping("/modem/{modemId}")
  public ResponseEntity<Page<InstallDto.ModemHistory>> searchByModem(
      @PathVariable Long modemId, Pageable pageable
  ) {

    // business logic

    return ResponseEntity
        .status(HttpStatus.OK).body(null);
  }

  /**
   * - 고객정보 기준으로 설치내역 조회
   */
  @GetMapping("/consumer/{consumerId}")
  public ResponseEntity<Page<InstallDto.ConsumerHistory>> searchByConsumer(
      @PathVariable Long consumerId, Pageable pageable
  ) {

    // business logic

    return ResponseEntity
        .status(HttpStatus.OK).body(null);
  }

  /**
   *  - 설치 현황 카운트
   */
  @GetMapping("/count")
  public ResponseEntity<Page<InstallDto.InstallCount>> searchInstallCount() {

    // business logic

    return ResponseEntity
        .status(HttpStatus.OK).body(null);
  }

  /**
   * - 고객정보 기준으로 설치내역 조회
   */
  @GetMapping("/status/{modemNo}")
  public ResponseEntity<InstallDto.InstallResponse> searchByConsumer(@PathVariable Integer modemNo) {
    // business logic

    return ResponseEntity
        .status(HttpStatus.OK).body(null);
  }

  /**
   * - 단말기 설치
   */
  @PostMapping("/{modemId}/{consumerId}")
  public ResponseEntity<Void> installModem(
      @PathVariable Long modemId,
      @PathVariable Long consumerId,
      @RequestBody @Valid InstallDto.InstallRequest requestDto
  ) {

    installService.installModem(modemId, consumerId, requestDto);
    return ResponseEntity.ok().build();
  }

  /**
   * - 단말기 교체
   */
  @PatchMapping("/change/{modemId}/{consumerId}")
  public ResponseEntity<Void> changeModem(
      @PathVariable Long modemId,
      @PathVariable Long consumerId,
      @RequestBody @Valid InstallDto.InstallRequest requestDto
  ) {

    installService.changeModem(modemId, consumerId, requestDto);
    return ResponseEntity.ok().build();
  }

  /**
   * - 단말기 유지보수
   */
  @PatchMapping("/maintenance/{modemId}")
  public ResponseEntity<Void> maintenanceModem(
      @PathVariable Long modemId,
      @RequestBody @Valid InstallDto.InstallRequest requestDto
  ) {

    installService.maintenanceModem(modemId, requestDto);
    return ResponseEntity.ok().build();
  }

  /**
   * - 단말기 철거
   */
  @PatchMapping("/demolish/{modemId}")
  public ResponseEntity<Void> demolishModem(
      @PathVariable Long modemId,
      @RequestBody @Valid InstallDto.InstallRequest requestDto
  ) {

    installService.demolishModem(modemId, requestDto);
    return ResponseEntity.ok().build();
  }
}
