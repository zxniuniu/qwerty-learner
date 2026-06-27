package com.typing.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.typing.mapper.SpeedRecordMapper;
import com.typing.model.SpeedRecord;

@Service
public class SpeedRecordService {

  private final SpeedRecordMapper mapper;

  public SpeedRecordService(SpeedRecordMapper mapper) {
    this.mapper = mapper;
  }

  public SpeedRecord save(SpeedRecord r) {
    r.setId(UUID.randomUUID().toString().replace("-", ""));
    r.setCreateDate(LocalDateTime.now());
    if (r.getStatus() == null) r.setStatus("0");
    if (!StringUtils.hasText(r.getUserName())) r.setUserName("匿名用户");
    mapper.insert(r);
    return r;
  }

  public List<SpeedRecord> ranking(String mode, int top) {
    List<SpeedRecord> list = mapper.findRanking(mode);
    if (top > 0 && list.size() > top) {
      return list.subList(0, top);
    }
    return list;
  }

  public List<SpeedRecord> records(String mode) {
    return mapper.findRecords(mode);
  }
}
