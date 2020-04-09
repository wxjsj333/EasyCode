package com.sjhy.plugin.entity;

import com.sjhy.plugin.comm.AbstractTableModel;

/**
 * 类型隐射模型
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class ServiceModuleModel extends AbstractTableModel<ServiceModule> {

  @Override
  protected String[] initColumnName() {
    return new String[]{"serviceModuleName"};
  }

  @Override
  protected Object[] toObj(ServiceModule serviceModuleName) {
    return new Object[]{serviceModuleName.getName()};
  }

  @Override
  protected void setVal(ServiceModule serviceModule, int columnIndex, Object val) {
    serviceModule.setName((String) val);
  }
}
