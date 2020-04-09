package com.sjhy.plugin.entity;

import java.util.List;
import lombok.Data;

/**
 * 类型映射分组
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
@Data
public class ServiceModuleGroup implements AbstractGroup<ServiceModule> {

  /**
   * 分组名称
   */
  private String name;
  /**
   * 元素对象
   */
  private List<ServiceModule> elementList;
}
