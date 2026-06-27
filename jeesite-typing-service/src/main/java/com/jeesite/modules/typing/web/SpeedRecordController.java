package com.jeesite.modules.typing.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.typing.entity.SpeedRecord;
import com.jeesite.modules.typing.service.SpeedRecordService;

/**
 * 打字速度记录 管理端 Controller（查看记录与排名，供 jeesite-vue 后台调用）
 */
@Controller
@RequestMapping(value = "${adminPath}/typing/speedRecord")
public class SpeedRecordController extends BaseController {

  @Autowired
  private SpeedRecordService speedRecordService;

  @ModelAttribute
  public SpeedRecord get(String id, boolean isNewRecord) {
    return speedRecordService.get(id, isNewRecord);
  }

  @RequiresPermissions("typing:speedRecord:view")
  @RequestMapping(value = { "list", "" })
  public String list(SpeedRecord speedRecord, Model model) {
    model.addAttribute("speedRecord", speedRecord);
    return "modules/typing/speedRecordList";
  }

  @RequiresPermissions("typing:speedRecord:view")
  @RequestMapping(value = "listData")
  @ResponseBody
  public Page<SpeedRecord> listData(SpeedRecord speedRecord, HttpServletRequest request, HttpServletResponse response) {
    speedRecord.setPage(new Page<>(request, response));
    return speedRecordService.findPage(speedRecord);
  }

  /**
   * 速度排名（管理端可视化数据源）。
   */
  @RequiresPermissions("typing:speedRecord:view")
  @RequestMapping(value = "ranking")
  @ResponseBody
  public List<SpeedRecord> ranking(SpeedRecord speedRecord, @RequestParam(defaultValue = "50") int top) {
    return speedRecordService.findRanking(speedRecord, top);
  }
}
