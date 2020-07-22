package weatherconditions;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.27.0)",
    comments = "Source: weatherconditions.proto")
public final class WCProviderGrpc {

  private WCProviderGrpc() {}

  public static final String SERVICE_NAME = "weatherconditions.WCProvider";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<weatherconditions.Weatherconditions.Filler,
      weatherconditions.Weatherconditions.Filler> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ping",
      requestType = weatherconditions.Weatherconditions.Filler.class,
      responseType = weatherconditions.Weatherconditions.Filler.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<weatherconditions.Weatherconditions.Filler,
      weatherconditions.Weatherconditions.Filler> getPingMethod() {
    io.grpc.MethodDescriptor<weatherconditions.Weatherconditions.Filler, weatherconditions.Weatherconditions.Filler> getPingMethod;
    if ((getPingMethod = WCProviderGrpc.getPingMethod) == null) {
      synchronized (WCProviderGrpc.class) {
        if ((getPingMethod = WCProviderGrpc.getPingMethod) == null) {
          WCProviderGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<weatherconditions.Weatherconditions.Filler, weatherconditions.Weatherconditions.Filler>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  weatherconditions.Weatherconditions.Filler.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  weatherconditions.Weatherconditions.Filler.getDefaultInstance()))
              .setSchemaDescriptor(new WCProviderMethodDescriptorSupplier("ping"))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<weatherconditions.Weatherconditions.WCRequest,
      weatherconditions.Weatherconditions.WCReply> getGetWCMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getWC",
      requestType = weatherconditions.Weatherconditions.WCRequest.class,
      responseType = weatherconditions.Weatherconditions.WCReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<weatherconditions.Weatherconditions.WCRequest,
      weatherconditions.Weatherconditions.WCReply> getGetWCMethod() {
    io.grpc.MethodDescriptor<weatherconditions.Weatherconditions.WCRequest, weatherconditions.Weatherconditions.WCReply> getGetWCMethod;
    if ((getGetWCMethod = WCProviderGrpc.getGetWCMethod) == null) {
      synchronized (WCProviderGrpc.class) {
        if ((getGetWCMethod = WCProviderGrpc.getGetWCMethod) == null) {
          WCProviderGrpc.getGetWCMethod = getGetWCMethod =
              io.grpc.MethodDescriptor.<weatherconditions.Weatherconditions.WCRequest, weatherconditions.Weatherconditions.WCReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getWC"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  weatherconditions.Weatherconditions.WCRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  weatherconditions.Weatherconditions.WCReply.getDefaultInstance()))
              .setSchemaDescriptor(new WCProviderMethodDescriptorSupplier("getWC"))
              .build();
        }
      }
    }
    return getGetWCMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static WCProviderStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WCProviderStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WCProviderStub>() {
        @java.lang.Override
        public WCProviderStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WCProviderStub(channel, callOptions);
        }
      };
    return WCProviderStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static WCProviderBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WCProviderBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WCProviderBlockingStub>() {
        @java.lang.Override
        public WCProviderBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WCProviderBlockingStub(channel, callOptions);
        }
      };
    return WCProviderBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static WCProviderFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WCProviderFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WCProviderFutureStub>() {
        @java.lang.Override
        public WCProviderFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WCProviderFutureStub(channel, callOptions);
        }
      };
    return WCProviderFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class WCProviderImplBase implements io.grpc.BindableService {

    /**
     */
    public void ping(weatherconditions.Weatherconditions.Filler request,
        io.grpc.stub.StreamObserver<weatherconditions.Weatherconditions.Filler> responseObserver) {
      asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }

    /**
     */
    public void getWC(weatherconditions.Weatherconditions.WCRequest request,
        io.grpc.stub.StreamObserver<weatherconditions.Weatherconditions.WCReply> responseObserver) {
      asyncUnimplementedUnaryCall(getGetWCMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                weatherconditions.Weatherconditions.Filler,
                weatherconditions.Weatherconditions.Filler>(
                  this, METHODID_PING)))
          .addMethod(
            getGetWCMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                weatherconditions.Weatherconditions.WCRequest,
                weatherconditions.Weatherconditions.WCReply>(
                  this, METHODID_GET_WC)))
          .build();
    }
  }

  /**
   */
  public static final class WCProviderStub extends io.grpc.stub.AbstractAsyncStub<WCProviderStub> {
    private WCProviderStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WCProviderStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WCProviderStub(channel, callOptions);
    }

    /**
     */
    public void ping(weatherconditions.Weatherconditions.Filler request,
        io.grpc.stub.StreamObserver<weatherconditions.Weatherconditions.Filler> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getWC(weatherconditions.Weatherconditions.WCRequest request,
        io.grpc.stub.StreamObserver<weatherconditions.Weatherconditions.WCReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetWCMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class WCProviderBlockingStub extends io.grpc.stub.AbstractBlockingStub<WCProviderBlockingStub> {
    private WCProviderBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WCProviderBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WCProviderBlockingStub(channel, callOptions);
    }

    /**
     */
    public weatherconditions.Weatherconditions.Filler ping(weatherconditions.Weatherconditions.Filler request) {
      return blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<weatherconditions.Weatherconditions.WCReply> getWC(
        weatherconditions.Weatherconditions.WCRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getGetWCMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class WCProviderFutureStub extends io.grpc.stub.AbstractFutureStub<WCProviderFutureStub> {
    private WCProviderFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WCProviderFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WCProviderFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<weatherconditions.Weatherconditions.Filler> ping(
        weatherconditions.Weatherconditions.Filler request) {
      return futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;
  private static final int METHODID_GET_WC = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final WCProviderImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(WCProviderImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PING:
          serviceImpl.ping((weatherconditions.Weatherconditions.Filler) request,
              (io.grpc.stub.StreamObserver<weatherconditions.Weatherconditions.Filler>) responseObserver);
          break;
        case METHODID_GET_WC:
          serviceImpl.getWC((weatherconditions.Weatherconditions.WCRequest) request,
              (io.grpc.stub.StreamObserver<weatherconditions.Weatherconditions.WCReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class WCProviderBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    WCProviderBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return weatherconditions.Weatherconditions.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("WCProvider");
    }
  }

  private static final class WCProviderFileDescriptorSupplier
      extends WCProviderBaseDescriptorSupplier {
    WCProviderFileDescriptorSupplier() {}
  }

  private static final class WCProviderMethodDescriptorSupplier
      extends WCProviderBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    WCProviderMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (WCProviderGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new WCProviderFileDescriptorSupplier())
              .addMethod(getPingMethod())
              .addMethod(getGetWCMethod())
              .build();
        }
      }
    }
    return result;
  }
}
