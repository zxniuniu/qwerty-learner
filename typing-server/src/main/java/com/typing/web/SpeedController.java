package com.typing.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.typing.model.SpeedRecord;
import com.typing.service.SpeedRecordService;

/** 速度记录与排名 */
@RestController
public class SpeedController {

  private final SpeedRecordService service;

  public SpeedController(SpeedRecordService service) {
    this.service = service;
  }

  /** 玩家端：上报速度记录 */
  @PostMapping("/api/typing/speed/save")
  public Map<String, Object> save(@RequestBody SpeedRecord record) {
    SpeedRecord saved = service.save(record);
    Map<String, Object> ret = new HashMap<>();
    ret.put("result", "true");
    ret.put("id", saved.getId());
    return ret;
  }

  /** 玩家端：速度排名 */
  @GetMapping("/api/typing/speed/ranking")
  public List<SpeedRecord> ranking(@RequestParam(required = false) String mode,
      @RequestParam(defaultValue = "50") int top) {
    return service.ranking(mode, top);
  }

  /** 管理端：记录列表 */
  @GetMapping("/api/admin/speed/list")
  public List<SpeedRecord> adminList(@RequestParam(required = false) String mode) {
    return service.records(mode);
  }
}
