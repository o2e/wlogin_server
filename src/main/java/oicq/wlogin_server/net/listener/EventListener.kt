package oicq.wlogin_server.net.listener

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import java.net.SocketAddress

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
class EventListener() : ChannelDuplexHandler() {

    override fun isSharable(): Boolean {
        return true
    }

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        println("handlerAdded = $ctx")
        //初始化一些东西
        super.handlerAdded(ctx)
    }


    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        println("handlerRemoved = $ctx")
        //销毁
        super.handlerRemoved(ctx)
    }


    override fun channelRegistered(ctx: ChannelHandlerContext) {
        println("channelRegistered = $ctx")
        super.channelRegistered(ctx)
    }


    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        println("channelUnregistered = $ctx")
        super.channelUnregistered(ctx)
    }


    override fun channelActive(ctx: ChannelHandlerContext) {
        println("channelActive = $ctx")
//        botConnection.send()
        super.channelActive(ctx)
    }


    override fun channelInactive(ctx: ChannelHandlerContext) {
        println("channelInactive？？ = $ctx")
        super.channelInactive(ctx)
    }


    override fun channelWritabilityChanged(ctx: ChannelHandlerContext) {
        println("channelWritabilityChanged = $ctx")
        super.channelWritabilityChanged(ctx)
    }


    override fun bind(ctx: ChannelHandlerContext, localAddress: SocketAddress, promise: ChannelPromise) {
        println("bind = $ctx, localAddress = $localAddress, promise = $promise")
        super.bind(ctx, localAddress, promise)
    }


    override fun connect(
        ctx: ChannelHandlerContext,
        remoteAddress: SocketAddress,
        localAddress: SocketAddress?,
        promise: ChannelPromise
    ) {
        println("connect = $ctx, remoteAddress = $remoteAddress, localAddress = $localAddress, promise = $promise")
        super.connect(ctx, remoteAddress, localAddress, promise)
    }


    override fun disconnect(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        println("disconnect = $ctx, promise = $promise")
        super.disconnect(ctx, promise)
    }


    override fun close(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        println("close = $ctx, promise = $promise")
        super.close(ctx, promise)
    }


    override fun deregister(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        println("deregister = $ctx, promise = $promise")
        super.deregister(ctx, promise)
    }


    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        println("channelReadComplete = $ctx")
        super.channelReadComplete(ctx)
    }


    override fun read(ctx: ChannelHandlerContext) {
        println("read = $ctx")
        super.read(ctx)
    }


    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        println("write = $ctx, msg = $msg, promise = $promise")
        super.write(ctx, msg, promise)
    }


    override fun flush(ctx: ChannelHandlerContext) {
        println("flush = $ctx")
        super.flush(ctx)
    }
}