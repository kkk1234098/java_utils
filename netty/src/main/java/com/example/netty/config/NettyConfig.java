package com.example.netty.config;

import com.example.netty.initializer.ServerChannelInitializer;
import com.example.netty.vo.PoolVo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
@Component
@Configuration
@PropertySource("classpath:netty.properties")
public class NettyConfig {
    //用来存储连接池
//    public static Map<String, PoolVo> MODE_MAP = new HashMap<>();
    public static ExpiringMap<String, PoolVo> MODE_MAP = ExpiringMap.builder().expiration(30, TimeUnit.SECONDS).expirationListener((key, value) -> {
        log.info("连接池:"+key+"过期");
        PoolVo poolVo = (PoolVo)value;
        poolVo.getChannelHandlerContext().close();
    }).build();

    @Value("${netty.server.enable}")
    private Integer serverEnable;

    @Value("${netty.server.ip}")
    private String serverIp;

    @Value("${netty.server.port}")
    private Integer serverPort;

    @Value("${netty.server.timeout}")
    private Integer timeout;

    public void start(InetSocketAddress socketAddress) {
        //new 一个主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //new 一个工作线程组
        EventLoopGroup workGroup = new NioEventLoopGroup(200);
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerChannelInitializer())
                .localAddress(socketAddress)
                //设置队列大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        //绑定端口,开始接收进来的连接
        try {
            ChannelFuture future = bootstrap.bind(socketAddress).sync();
            log.info("服务器启动开始监听端口: {}", socketAddress.getPort());
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭主线程组
            bossGroup.shutdownGracefully();
            //关闭工作线程组
            workGroup.shutdownGracefully();
        }
    }


    @Bean(name = "nettyServer")
    public void nettyServerStart(){
        if(serverEnable>0) {
            //启动服务端
            log.info("netty服务端启动...");
            this.start(new InetSocketAddress(this.serverIp, this.serverPort));
        }
    }

    public void deletePool(String key){
        PoolVo poolVo = MODE_MAP.get(key);
        poolVo.getChannelHandlerContext().close();
    }
}
