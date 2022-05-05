package oicq.wlogin_server.net

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import oicq.wlogin_server.net.listener.EventListener
import oicq.wlogin_server.net.listener.TimeServerHandler
import oicq.wlogin_server.tools.Log

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
class Connection() {
    lateinit var channelFuture: ChannelFuture
    private var workerGroup: NioEventLoopGroup = NioEventLoopGroup()
    var bossGroup: NioEventLoopGroup = NioEventLoopGroup()


    @Synchronized
    fun bind(port: Int = 8080): Boolean {
        Log.d("bind port $port ...")
        init(ServerBootstrap()).bind(port).also { channelFuture = it }.sync()
//        channelFuture.channel().closeFuture().sync()
        return true
    }


    //    https://netty.io/wiki/user-guide-for-4.x.html
    private fun init(bootstrap: ServerBootstrap): ServerBootstrap {
        bootstrap.group(bossGroup, workerGroup)
//            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .channel(NioServerSocketChannel::class.java)
//            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.AUTO_READ, true)
            .option(ChannelOption.AUTO_CLOSE, true)//TODO 发送失败后自动关闭 不知道能不能触发重连
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                public override fun initChannel(socketChannel: SocketChannel) {
                    socketChannel.pipeline().addLast("time", TimeServerHandler())
                    // 注意添加顺序决定执行的先后
//                    socketChannel.pipeline().addLast("log", LoggingHandler(LogLevel.INFO))
//                    socketChannel.pipeline().addLast("ping", IdleStateHandler(baseIdleTime.toLong() + 5, baseIdleTime.toLong(), baseIdleTime.toLong() + (5 * 5), TimeUnit.SECONDS))
//                    socketChannel.pipeline().addLast("heartbeat", HeartBeatListener(this@Connection, netStatusListener)) // 注意心跳包要在IdleStateHandler后面注册 不然拦截不了事件分发
//                    socketChannel.pipeline().addLast("decoder", BotDecoder(netStatusListener.core()))
//                    socketChannel.pipeline().addLast("encoder", BotEncoder(netStatusListener.core()))
//                    socketChannel.pipeline().addLast("connectionListener", connectionListener)
//                    socketChannel.pipeline().addLast("reconnectionListener", ReconnectionListener(this@Connection, netStatusListener))
                    socketChannel.pipeline().addLast("event", EventListener()) //接受除了上面已注册的东西之外的事件
                }
            })

        return bootstrap
    }
}