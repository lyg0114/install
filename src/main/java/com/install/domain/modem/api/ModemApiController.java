package com.install.domain.modem.api;

import com.install.domain.modem.dto.ModemDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.modem.api
 * @since : 03.06.24
 * - 단말기 조회
 * - 단말기 등록
 * - 단말기 수정
 * - 단말기 삭제
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/modems/v1")
@RestController
public class ModemApiController {

  /**
   * - 단말기 조회
   */
  @GetMapping
  public ResponseEntity<Page<ModemDto.ModemResponse>> searchModems(
      ModemDto.ModemSearchCondition condition, Pageable pageable) {

    // business logic

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(null);
  }

  /**
   * - 단말기 등록
   */
  @PostMapping
  public ResponseEntity<Void> addModem(@RequestBody @Valid ModemDto.ModemRequest requestDto) {

    // business logic

    return ResponseEntity.ok().build();
  }

  /**
   * - 단말기 일괄 엑셀 등록
   */
  @PostMapping("/excel")
  public ResponseEntity<Void> addModemsByExcel(@RequestParam("file") MultipartFile file) {

    // business logic

    return ResponseEntity.ok().build();
  }

  /**
   * - 단말기 수정
   */
  @PatchMapping("/{modemId}")
  public ResponseEntity<ModemDto.ModemResponse> updateModem(
      @PathVariable Long modemId,
      @RequestBody @Valid ModemDto.ModemRequest requestDto) {

    // business logic

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(null);
  }

  /**
   * - 단말기 삭제
   */
  @DeleteMapping("/{modemId}")
  public ResponseEntity<Void> deleteModem(@PathVariable Long modemId) {

    // business logic

    return ResponseEntity.ok()
        .build();
  }
}
