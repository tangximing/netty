import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by tangxm on 2016/7/13.
 */
public class Server {
    private int port;
    public Server(int port){
        this.port = port;
    }

    public void start(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind().sync();
            System.out.println(Server.class.getName() + "started and listen on " +
                    future.channel().localAddress());
            future.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                group.shutdownGracefully().sync();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args){
        new Server(12345).start();
    }
}
