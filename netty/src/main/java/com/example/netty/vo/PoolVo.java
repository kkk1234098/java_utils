package com.example.netty.vo;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class PoolVo {

    private ChannelHandlerContext channelHandlerContext;
    private Long updateTime;
}
