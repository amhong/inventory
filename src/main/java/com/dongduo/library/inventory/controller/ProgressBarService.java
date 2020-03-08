package com.dongduo.library.inventory.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @ClassName ProgressBarService
 * @Description 进度条更新服务类
 * @Author zhangxiaoyan
 * @Date 2019-04-24 09:14:30
 */
public class ProgressBarService {

    private VBox vBox;
    private HBox hBox;
    private Label label;
    private ProgressBar progressBar;
    private ProgressIndicator progressIndicator;
    private String message;
    private long workDone = 0;
    private long max = 0;

    private boolean toAlert = false;
    private String alertContentText;

    public ProgressBarService(VBox vBox, HBox hBox, Label label, ProgressBar progressBar, ProgressIndicator progressIndicator) {
        this.vBox = vBox;
        this.hBox = hBox;
        this.label = label;
        this.progressBar = progressBar;
        this.progressIndicator = progressIndicator;
    }

    Service<Integer> service = new Service<Integer>() {
        @Override
        protected Task<Integer> createTask() {
            return new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    // 更新进度条及其文字
                    updateProgress(workDone, max);
                    updateMessage(message);
                    // 如果进度条已满，暂停一秒后，关闭弹框
                    if (workDone >= max) {
                        try {
                            // 暂停一秒
                            Thread.sleep(1000);
                            // 关闭弹框
                            hideVBox();
                            // 是否启动成功或失败的信息提示框
                            if (toAlert) {
                                Alert alert  = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText("");
                                alert.setContentText(alertContentText);
                                alert.showAndWait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            };
        }
    };

    /**
     * 显示VBox
     */
    public void displayVBox() {
        vBox.setManaged(true);
        vBox.setVisible(true);
    }

    /**
     * 隐藏VBox
     */
    public void hideVBox() {
        vBox.setManaged(false);
        vBox.setVisible(false);
    }

    /**
     * 显示HBox
     */
    public void displayHBox() {
        hBox.setManaged(true);
        hBox.setVisible(true);
    }

    /**
     * 隐藏HBox
     */
    public void hideHBox() {
        hBox.setManaged(false);
        hBox.setVisible(false);
    }

    public VBox getvBox() {
        return vBox;
    }

    public void setvBox(VBox vBox) {
        this.vBox = vBox;
    }

    public HBox gethBox() {
        return hBox;
    }

    public void sethBox(HBox hBox) {
        this.hBox = hBox;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public void setProgressIndicator(ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isToAlert() {
        return toAlert;
    }

    public void setToAlert(boolean toAlert) {
        this.toAlert = toAlert;
    }

    public String getAlertContentText() {
        return alertContentText;
    }

    public void setAlertContentText(String alertContentText) {
        this.alertContentText = alertContentText;
    }

    public long getWorkDone() {
        return workDone;
    }

    public void setWorkDone(long workDone) {
        this.workDone = workDone;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public Service<Integer> getService() {
        return service;
    }

    public void setService(Service<Integer> service) {
        this.service = service;
    }
}
