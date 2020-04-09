package com.sjhy.plugin.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.sjhy.plugin.config.Settings;
import com.sjhy.plugin.constants.MsgValue;
import com.sjhy.plugin.entity.ServiceModule;
import com.sjhy.plugin.entity.ServiceModuleGroup;
import com.sjhy.plugin.entity.ServiceModuleModel;
import com.sjhy.plugin.tool.CloneUtils;
import com.sjhy.plugin.tool.StringUtils;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

/**
 * @author wangXin
 * @version v1.0.0
 * @date 2020-04-09 16:07
 */
public class ServiceModuleSetting implements Configurable {

  /**
   * 主面板
   */
  private JPanel mainPanel;
  /**
   * 服务模块分组切换下拉框
   */
  private JComboBox<String> serviceModuleComboBox;
  /**
   * 分组复制按钮
   */
  private JButton serviceModuleCopyButton;
  /**
   * 类型映射表
   */
  private JTable serviceModuleTable;
  /**
   * 添加映射按钮
   */
  private JButton addButton;
  /**
   * 移除映射按钮
   */
  private JButton removeButton;
  /**
   * 删除分组按钮
   */
  private JButton deleteButton;

  /**
   * 是否初始化完成
   */
  private boolean init;
  /**
   * 类型映射表模型
   */
  private ServiceModuleModel serviceModuleModel;

  /**
   * 当前选中分组
   */
  private String currGroupName;
  /**
   * 类型映射分组集合
   */
  private Map<String, ServiceModuleGroup> serviceModuleGroupMap;
  /**
   * 全局配置服务
   */
  private Settings settings;


  public ServiceModuleSetting(Settings settings) {
    this.settings = settings;
    this.serviceModuleGroupMap = CloneUtils
        .cloneByJson(settings.getServiceModuleGroupMap(),
            new TypeReference<Map<String, ServiceModuleGroup>>() {
            });
    this.currGroupName = settings.getCurrServiceModuleGroupName();
    //添加类型
    addButton.addActionListener(
        e -> serviceModuleModel.addRow(new ServiceModule("sys")));

    //移除类型
    removeButton.addActionListener(e -> {
      int[] selectRows = serviceModuleTable.getSelectedRows();
      if (selectRows == null || selectRows.length == 0) {
        return;
      }
      if (!MessageDialogBuilder.yesNo(MsgValue.TITLE_INFO, "Confirm Delete Selected Item?")
          .isYes()) {
        return;
      }
      // 从后面往前面移除，防止下标错位问题。
      for (int i = selectRows.length - 1; i >= 0; i--) {
        serviceModuleModel.removeRow(selectRows[i]);
      }
    });

    //切换分组
    serviceModuleComboBox.addActionListener(e -> {
      if (!init) {
        return;
      }
      String value = (String) serviceModuleComboBox.getSelectedItem();
      if (value == null) {
        return;
      }
      if (currGroupName.equals(value)) {
        return;
      }
      currGroupName = value;
      refresh();
    });

    //复制分组按钮
    serviceModuleCopyButton.addActionListener(e -> {
      String value = Messages
          .showInputDialog("Group Name:", "Input Group Name:", Messages.getQuestionIcon(),
              currGroupName + " Copy", new InputValidator() {
                @Override
                public boolean checkInput(String inputString) {
                  return !StringUtils.isEmpty(inputString) && !serviceModuleGroupMap
                      .containsKey(inputString);
                }

                @Override
                public boolean canClose(String inputString) {
                  return this.checkInput(inputString);
                }
              });
      if (value == null) {
        return;
      }
      // 克隆对象
      ServiceModuleGroup serviceModuleGroup = CloneUtils
          .cloneByJson(serviceModuleGroupMap.get(currGroupName));
      serviceModuleGroup.setName(value);
      serviceModuleGroupMap.put(value, serviceModuleGroup);
      currGroupName = value;
      refresh();
    });

    //删除分组
    deleteButton.addActionListener(e -> {
      if (MessageDialogBuilder.yesNo(MsgValue.TITLE_INFO,
          "Confirm Delete Group " + serviceModuleComboBox.getSelectedItem() + "?").isYes()) {
        if (Settings.DEFAULT_NAME.equals(currGroupName)) {
          Messages.showWarningDialog("Can't Delete Default Group!", MsgValue.TITLE_INFO);
          return;
        }
        serviceModuleGroupMap.remove(currGroupName);
        currGroupName = Settings.DEFAULT_NAME;
        refresh();
      }
    });

    // 初始化操作
    init();
  }


  /**
   * 初始化方法
   */
  private void init() {
    //初始化表格
    this.serviceModuleModel = new ServiceModuleModel();
    this.serviceModuleTable.setModel(serviceModuleModel);
    refresh();
  }

  /**
   * 刷新方法
   */
  private void refresh() {
    init = false;
    //初始化下拉框
    this.serviceModuleComboBox.removeAllItems();
    serviceModuleGroupMap.keySet().forEach(this.serviceModuleComboBox::addItem);
    this.serviceModuleComboBox.setSelectedItem(this.currGroupName);
    this.serviceModuleModel.init(this.serviceModuleGroupMap.get(currGroupName).getElementList());
    init = true;
  }

  @Nls
  @Override
  public String getDisplayName() {
    return "Service Module";
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    return mainPanel;
  }

  @Override
  public boolean isModified() {
    return !serviceModuleGroupMap.equals(settings.getServiceModuleGroupMap()) || !currGroupName
        .equals(settings.getCurrServiceModuleGroupName());
  }

  @Override
  public void apply() {
    settings.setCurrServiceModuleGroupName(currGroupName);
    settings.setServiceModuleGroupMap(serviceModuleGroupMap);
  }

  @Override
  public void reset() {
    this.serviceModuleGroupMap = CloneUtils.cloneByJson(settings.getServiceModuleGroupMap(),
        new TypeReference<Map<String, ServiceModuleGroup>>() {
        });
    this.currGroupName = settings.getCurrServiceModuleGroupName();
    init();
  }

  private void createUIComponents() {
    // TODO: place custom component creation code here
  }
}
