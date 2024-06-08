package com.install.domain.install.service;

import static com.install.domain.code.entity.CodeSet.HAS_MODEM;
import static com.install.domain.code.entity.CodeSet.HAS_NOT_MODEM;
import static com.install.domain.code.entity.CodeSet.MODEM_INSTALL_STATUS_DEMOLISH;
import static com.install.global.exception.CustomErrorCode.ALREADY_INSTALLED_MODEM;
import static com.install.global.exception.CustomErrorCode.CONSUMER_NOT_EXIST;
import static com.install.global.exception.CustomErrorCode.IMAGE_SIZE_NOT_ALLOW;
import static com.install.global.exception.CustomErrorCode.MODEM_NOT_EXIST;
import static com.install.global.exception.CustomErrorCode.NOT_FOUND_FILE_INFO;
import static com.install.global.exception.CustomErrorCode.NOT_FOUND_INSTALL_INFO;
import static com.install.global.exception.CustomErrorCode.NOT_INSTALLED_MODEM;
import static com.install.global.exception.CustomErrorCode.USER_NOT_FOUND;
import static java.util.Objects.isNull;
import static java.util.UUID.randomUUID;

import com.install.domain.code.entity.Code;
import com.install.domain.common.file.entity.FileInfo;
import com.install.domain.common.file.entity.repository.FileInfoRepository;
import com.install.domain.common.file.service.StorageService;
import com.install.domain.consumer.entity.Consumer;
import com.install.domain.consumer.entity.repository.ConsumerRepository;
import com.install.domain.install.dto.InstallDto;
import com.install.domain.install.dto.InstallDto.InstallHistoryByConsumer;
import com.install.domain.install.dto.InstallDto.InstallHistoryByModem;
import com.install.domain.install.dto.InstallDto.InstallRequest;
import com.install.domain.install.dto.InstallDto.historyInfo;
import com.install.domain.install.entity.InstallInfo;
import com.install.domain.install.entity.repository.InstallRepository;
import com.install.domain.member.entity.Member;
import com.install.domain.member.entity.repository.MemberRepository;
import com.install.domain.modem.entity.Modem;
import com.install.domain.modem.entity.repository.ModemRepository;
import com.install.global.exception.CustomException;
import com.install.global.security.service.JwtService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : iyeong-gyo
 * @package : com.install.domain.install.service
 * @since : 05.06.24
 */
// TODO : 중복 코드 제거, 설치, 교체, 유지보수,
// TODO : 철거 관련코드는 클라이언트로부터 전달받는것이 아닌 서버쪽에서 바로 처리할 수 있도록 할것
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class InstallService {

  private final MemberRepository memberRepository;

  private final InstallRepository installRepository;
  private final ModemRepository modemRepository;
  private final ConsumerRepository consumerRepository;
  private final JwtService jwtService;
  private final StorageService storageService;
  private final FileInfoRepository fileInfoRepository;

  /**
   * @param modemId
   * @param consumerId
   * @param requestDto 단말기 설치
   */
  public void installModem(
      Long modemId, Long consumerId, InstallDto.InstallRequest requestDto,
      List<MultipartFile> installImages
  ) {

    validateIsExistFiles(installImages);
    validateIsExistModem(modemId);
    validateIsExistConsumer(consumerId);
    validateShouldNotInstalledModem(modemId);

    Member worker = memberRepository.findById(jwtService.getId())
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    InstallInfo savedInstallInfo = installRepository.save(
        InstallInfo.builder()
            .modem(Modem.builder().id(modemId).build())
            .consumer(Consumer.builder().id(consumerId).build())
            .comment(requestDto.getComment())
            .worker(worker)
            .workTypeCd(Code.builder()
                .code(requestDto.getWorkTypeCd())
                .build())
            .workTime(LocalDateTime.now())
            .build());

    for (MultipartFile image : installImages) {
      workSpaceImagesUpload(savedInstallInfo, image);
    }
  }

  public void workSpaceImagesUpload(InstallInfo installInfo, MultipartFile multipartFile) {
    // 파일경로 DB 저장
    StringBuilder builder = new StringBuilder();
    String fileType = "." + multipartFile.getContentType().split("/")[1];
    String randomNum = randomUUID().toString().substring(0, 6);

    String fileName = builder.append(System.currentTimeMillis())
        .append("_").append(randomNum)
        .append(fileType)
        .toString();

    builder.delete(0, builder.length()); // builder 초기화
    String dirPath = builder
        .append("modeminstall")
        .append("/")
        .append(installInfo.getId()).append("/")
        .toString();

    FileInfo savedFileInfo = fileInfoRepository.save(FileInfo.builder()
        .fileUri(dirPath + "/" + fileName)
        .fileSize(multipartFile.getSize())
        .installInfo(installInfo)
        .build());

    // 저장소에 실제 파일 저장
    storageService.store(multipartFile, dirPath, fileName);
  }

  /**
   * @param modemId
   * @param consumerId
   * @param requestDto 단말기 교체
   */
  public void changeModem(
      Long modemId, Long consumerId, InstallRequest requestDto, List<MultipartFile> installImages
  ) {
    validateIsExistFiles(installImages);
    validateIsExistModem(modemId);
    validateIsExistConsumer(consumerId);
    validateShouldNotInstalledModem(modemId);

    Member worker = memberRepository.findById(jwtService.getId())
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    InstallInfo savedInstallInfo = installRepository.save(
        InstallInfo.builder()
            .modem(Modem.builder().id(modemId).build())
            .consumer(Consumer.builder().id(consumerId).build())
            .comment(requestDto.getComment())
            .worker(worker)
            .workTypeCd(Code.builder()
                .code(requestDto.getWorkTypeCd())
                .build())
            .workTime(LocalDateTime.now())
            .build());

    for (MultipartFile image : installImages) {
      workSpaceImagesUpload(savedInstallInfo, image);
    }
  }

  /**
   * @param modemId
   * @param requestDto 단말기 유지보수
   */
  public void maintenanceModem(Long modemId, InstallRequest requestDto, List<MultipartFile> maintenanceImages) {
    validateIsExistModem(modemId);
    validateShouldInstalledModem(modemId);

    Long installedConsumerSid = installRepository.currentInstallStateInfo(modemId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_INSTALL_INFO))
        .getConsumer()
        .getId();

    Member worker = memberRepository.findById(jwtService.getId())
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    InstallInfo savedMaintanceInfo = installRepository.save(
        InstallInfo.builder()
            .modem(Modem.builder().id(modemId).build())
            .comment(requestDto.getComment())
            .worker(worker)
            .consumer(Consumer.builder()
                .id(installedConsumerSid)
                .build())
            .workTypeCd(Code.builder()
                .code(requestDto.getWorkTypeCd())
                .build())
            .workTime(LocalDateTime.now())
            .build());

    for (MultipartFile image : maintenanceImages) {
      workSpaceImagesUpload(savedMaintanceInfo, image);
    }
  }

  /**
   * @param modemId
   * @param requestDto 단말기 철거
   */
  public void demolishModem(Long modemId, InstallRequest requestDto, List<MultipartFile> demolishImages) {
    validateIsExistModem(modemId);
    validateShouldInstalledModem(modemId);

    Long installedConsumerSid = installRepository.currentInstallStateInfo(modemId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_INSTALL_INFO))
        .getConsumer()
        .getId();

    Member worker = memberRepository.findById(jwtService.getId())
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    InstallInfo savedDemolishInfo = installRepository.save(
        InstallInfo.builder()
            .modem(Modem.builder().id(modemId).build())
            .comment(requestDto.getComment())
            .worker(worker)
            .consumer(Consumer.builder()
                .id(installedConsumerSid)
                .build())
            .workTypeCd(Code.builder()
                .code(requestDto.getWorkTypeCd())
                .build())
            .workTime(LocalDateTime.now())
            .build());

    for (MultipartFile image : demolishImages) {
      workSpaceImagesUpload(savedDemolishInfo, image);
    }
  }

  @Transactional(readOnly = true)
  public InstallHistoryByModem searchHistoryByModem(Long modemId, Pageable pageable) {
    Page<historyInfo> historyInfos = installRepository.searchInstallInfoPageByModem(modemId,
            pageable)
        .map(getInstallInfohistoryInfoFunction());

    return InstallHistoryByModem.builder()
        .historys(historyInfos)
        .currentState(checkState(historyInfos.getContent()))
        .build();
  }

  @Transactional(readOnly = true)
  public InstallHistoryByConsumer searchHistoryByConsumer(Long consumerId, Pageable pageable) {
    Page<historyInfo> historyInfos = installRepository.searchInstallInfoPageByConsumer(consumerId, pageable)
        .map(getInstallInfohistoryInfoFunction());

    return InstallHistoryByConsumer.builder()
        .historys(historyInfos)
        .currentState(checkState(historyInfos.getContent()))
        .build();
  }

  private Function<InstallInfo, historyInfo> getInstallInfohistoryInfoFunction() {
    return installInfo -> historyInfo.builder()
        .workTime(installInfo.getWorkTime())
        .workType(installInfo.getWorkTypeCd().getCode())
        .modemNo(installInfo.getModem().getModemNo())
        .consumerNo(installInfo.getConsumer().getConsumerNo())
        .consumerName(installInfo.getConsumer().getConsumerName())
        .meterNo(installInfo.getConsumer().getMeterNo())
        .city(installInfo.getConsumer().getAddress() != null ? installInfo.getConsumer().getAddress().getCity() : null)
        .build();
  }

  private String checkState(List<historyInfo> historyInfos) {
    if (historyInfos == null || historyInfos.size() == 0) {
      return HAS_NOT_MODEM.getCode(); // TODO : enum 으로 대체
    }

    historyInfo historyInfo = historyInfos.get(0);
    if (historyInfo.getWorkType().equals(MODEM_INSTALL_STATUS_DEMOLISH.getCode())) {
      return HAS_NOT_MODEM.getCode();
    }

    return HAS_MODEM.getCode();
  }

  private void validateIsExistFiles(List<MultipartFile> images) {
    if (isNull(images) || images.size() == 0) {
      throw new CustomException(NOT_FOUND_FILE_INFO);
    }
  }

  private void validateMaxImages(List<MultipartFile> images) {
    if (images.size() > 3) {
      throw new CustomException(IMAGE_SIZE_NOT_ALLOW);
    }
  }

  /**
   * 시스템에 등록된 모뎀인가
   */
  private void validateIsExistModem(Long modemId) {
    if (!modemRepository.existsById(modemId)) {
      throw new CustomException(MODEM_NOT_EXIST);
    }
  }

  /**
   * 시스템에 등록된 고객정보 인가
   */
  private void validateIsExistConsumer(Long consumerId) {
    if (!consumerRepository.existsById(consumerId)) {
      throw new CustomException(CONSUMER_NOT_EXIST);
    }
  }

  /**
   * 모뎀은 설치되어 있어있지 않아야 한다.
   */
  private void validateShouldNotInstalledModem(Long modemId) {
    if (installRepository.isInstalledModem(modemId)) {
      throw new CustomException(ALREADY_INSTALLED_MODEM);
    }
  }

  /**
   * 모뎀은 설치되어 있어야 한다.
   */
  private void validateShouldInstalledModem(Long modemId) {
    if (!installRepository.isInstalledModem(modemId)) {
      throw new CustomException(NOT_INSTALLED_MODEM);
    }
  }
}
