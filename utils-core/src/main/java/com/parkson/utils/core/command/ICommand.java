package com.parkson.utils.core.command;

/**
 * @ClassName ICommand
 * @Description TO DO
 * @Author cheneason
 * @Date 2020/8/13 09:29
 * @Version 1.0
 */
public interface ICommand {

    /**
     * 执行命令行指令
     *
     * @param commandline
     * @return
     */
    boolean executeCommand(String commandline) throws Exception;
}
