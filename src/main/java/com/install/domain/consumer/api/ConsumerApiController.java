package com.install.domain.consumer.api;

import java.util.concurrent.ExecutorService;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.install.domain.consumer.dto.ConsumerDto;
import com.install.domain.consumer.service.ConsumerExcelService;
import com.install.domain.consumer.service.ConsumerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.consumer.api
 * @since : 03.06.24
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/consumers/v1")
@RestController
public class ConsumerApiController {

	private final ConsumerExcelService consumerExcelService;
	private final ConsumerService consumerService;
	private final ExecutorService executorService;

	/**
	 * - 고객정보 리스트 조회
	 */
	@GetMapping
	public ResponseEntity<Page<ConsumerDto.ConsumerResponse>> searchConsumers(ConsumerDto.ConsumerSearchCondition condition, Pageable pageable) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(consumerService.searchConsumers(condition, pageable));
	}

	/**
	 * - 고객정보 등록
	 */
	@PostMapping
	public ResponseEntity<Void> addConsumer(@RequestBody @Valid ConsumerDto.ConsumerRequest requestDto) {

		consumerService.addConsumer(requestDto);
		return ResponseEntity.ok().build();
	}

	/**
	 * - 고객정보 수정
	 */
	@PatchMapping("/{consumerId}")
	public ResponseEntity<Void> updateConsumer(
		@PathVariable Long consumerId,
		@RequestBody @Valid ConsumerDto.ConsumerRequest requestDto
	) {

		consumerService.updateConsumer(consumerId, requestDto);
		return ResponseEntity.ok().build();
	}

	/**
	 * - 고객정보 삭제
	 */
	@DeleteMapping("/{consumerId}")
	public ResponseEntity<Void> deleteItem(@PathVariable Long consumerId) {

		consumerService.deleteConsumer(consumerId);
		return ResponseEntity.ok().build();
	}

	/**
	 * - 고객정보 일괄 엑셀 등록
	 */
	@PostMapping("/excel")
	public ResponseEntity<Void> addConsumersByExcel(@RequestParam("file") MultipartFile file, @RequestParam("sessionId") String sessionId) {
		executorService.submit(() -> consumerExcelService.uploadConsumerExcel(file, sessionId));
		sleep(100);
		return ResponseEntity.ok()
			.build();
	}

	private static void sleep(long mill) {
		try {
			Thread.sleep(mill);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
