package com.example.netty.handler;
import com.example.netty.vo.PoolVo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;

import static com.example.netty.config.NettyConfig.MODE_MAP;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 客户端连接会触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.addPool(ctx);
        log.info(ctx.channel().id() + String.valueOf(ctx.channel().remoteAddress()) + "-已连接");

    }

    /**
     * 客户端发消息会触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("服务器收到{}的消息: {}",ctx.channel().id() + String.valueOf(ctx.channel().remoteAddress()), msg.toString());
        this.addPool(ctx);
        ctx.write("hello");
        ctx.flush();
    }

    /**
     * 发生异常触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 连接断开时触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        log.info(ctx.channel().id() + String.valueOf(ctx.channel().remoteAddress()) + "-连接断开");
        MODE_MAP.remove(String.valueOf(ctx.channel().id()));
        ctx.close();
    }

    public void addPool(ChannelHandlerContext ctx){
        //保存到pool
        PoolVo poolVo = new PoolVo();
        poolVo.setChannelHandlerContext(ctx);
        poolVo.setUpdateTime(new Date().getTime()/1000);
        MODE_MAP.put(String.valueOf(ctx.channel().id()),poolVo);
    }

}
