package com.parkson.utils.core.command;

import com.parkson.utils.core.cache.LocalCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Scanner;

/**
 * @ClassName CommandExecutor
 * @Description TO DO
 * @Author cheneason
 * @Date 2020/8/13 09:31
 * @Version 1.0
 */
public class CommandExecutor implements Runnable {

    private static Logger log = LoggerFactory.getLogger(CommandExecutor.class);

    @Autowired(required = false)
    private ICommand commandExecutor;

    @Override
    public void run() {
        // 初始化命令行指令输入流
        Scanner sc = new Scanner(System.in);
        try {
            while (true) {
                System.out.print(">>");
                String cmd = sc.nextLine();

                // 空输入
                if (cmd == null || cmd.length() == 0) {
                    continue;
                }
                // 内部指令
                if (internalCmd(cmd)) {
                    continue;
                }
                externalCmd(cmd);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            sc.close();
        }
    }

    private boolean internalCmd(String cmd) {
        if (cmd.trim().equalsIgnoreCase("exit")) {
            // 退出进程
            log.info("application exit");
            System.exit(0);
        } else if (cmd.trim().equalsIgnoreCase("show client count")) {
            // 显示连接中的客户端数量
            log.info("show client count");
        } else if (cmd.trim().equalsIgnoreCase("show runtime")) {
            // 显示runtime信息
            showRuntimeInfo();
        } else if (cmd.trim().equalsIgnoreCase("show memory")) {
            // 显示内存信息
            showMemoryInfo();
        } else if (cmd.trim().equalsIgnoreCase("refresh memory cache")) {
            // 刷新缓存
            LocalCacheManager.refreshAll();
        } else {
            return false;
        }
        log.info("internal command executed".toUpperCase());
        return true;
    }

    private void externalCmd(String cmd) {
        if (commandExecutor != null) {
            try {
                boolean rst = commandExecutor.executeCommand(cmd);
                if (rst) {
                    log.info("command executed!");
                } else {
                    log.info("unknown command!");
                }
            } catch (Exception e) {
                log.error("command executed error!", e);
            }
        }
    }

    /**
     * 显示运行时信息
     */
    private void showRuntimeInfo() {
        RuntimeMXBean rmb = (RuntimeMXBean) ManagementFactory.getRuntimeMXBean();
        log.info("ClassPath: " + rmb.getClassPath());
        log.info("LibraryPath: " + rmb.getLibraryPath());
        log.info("VmVersion: " + rmb.getVmVersion());
    }

    /**
     * 显示内存信息
     */
    private void showMemoryInfo() {
        int m = (int) Runtime.getRuntime().totalMemory() / 1024;
        log.info("Total VM memory is " + m);
        m = (int) Runtime.getRuntime().freeMemory() / 1024;
        log.info("Free VM memory is " + m);
        log.info("Max VM memeory is " + Runtime.getRuntime().maxMemory() / 1024);
    }
}
