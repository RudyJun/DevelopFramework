package com.rudy.framework.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rudy.framework.FrameWorkApplication;
import com.rudy.framework.base.Constants;
import com.rudy.framework.base.exception.NetworkDisconnectException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by RudyJun on 2016/11/23.
 */
public class TaskManager {

    private Object lastResult = null;

    private List<Task> taskList = new ArrayList<>(8);

    public TaskManager next(Task task) {
        taskList.add(task);

        return this;
    }

    public void start() {
        try {
            executeTask(0);
        } catch (IOException | NetworkDisconnectException e) {
            e.printStackTrace();
        }
    }

    public void executeTask(final int index) throws NetworkDisconnectException, IOException {
        if (index >= taskList.size()) {
            return;
        }

        Task<Object> task = taskList.get(index);
        if (task instanceof BackgroundTask) {
            executeBackgroundTask(task, new UITask() {
                @Override
                public Object doWork(Object data) throws NetworkDisconnectException, IOException {
                    executeTask(index + 1);
                    return null;
                }
            });
        } else {
            lastResult = task.doWork(lastResult);
        }
    }

    public void executeBackgroundTask(final Task backgroundTask, final Task uiTask) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        try {
                            uiTask.doWork(msg.obj);
                        } catch (NetworkDisconnectException e) {

                        } catch (IOException e) {
                            Log.e("TaskManager", "execute dowork failed. exception:" + e.getMessage(), e);
                        }
                        break;
                    case 2:
                        CommonUtil.showToast(FrameWorkApplication.getApplication(), Constants.NETWORK_ERROR);
                        break;
                    case 3:
                        CommonUtil.showToast(FrameWorkApplication.getApplication(), Constants.NO_NETWORK);
                        break;
                }

            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lastResult = backgroundTask.doWork(lastResult);
                } catch (NetworkDisconnectException e) {
                    handler.sendEmptyMessage(3);
                    return;
                } catch (IOException e) {
                    handler.sendEmptyMessage(2);
                    return;
                }

                Message msg = handler.obtainMessage(1, lastResult);
                msg.sendToTarget();
            }
        }).start();
    }

    public interface Task<T> {
        Object doWork(T data) throws NetworkDisconnectException, IOException;
    }

    public interface BackgroundTask<T> extends Task<T> {
    }

    public interface UITask<T> extends Task<T> {
    }
}
