package com.emptypockets.spacemania.utils;

import com.emptypockets.spacemania.network.common.NetworkProtocall;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class KryoUtils {

	byte[] dataBuffer;
	Output output;
	Kryo kryo;

	static KryoUtils kryoUtils;

	public KryoUtils() {
		dataBuffer = new byte[5 * 1024 * 1024];
		output = new Output(dataBuffer);

		kryo = new Kryo();
		kryo.setRegistrationRequired(true);
		NetworkProtocall.register(kryo);
	}

	public static KryoUtils get() {
		if (kryoUtils == null) {
			synchronized (KryoUtils.class) {
				if (kryoUtils == null) {
					kryoUtils = new KryoUtils();
				}
			}
		}
		return kryoUtils;
	}

	public <C> C cloneData(C data){
		return kryo.copy(data);
	}
	public int getDataSize(Object data) {
		output.clear();
		kryo.writeObject(output, data);
		return output.position();
	}

	public static <C> C clone(C data){
		return get().cloneData(data);
	}
	public static int getSize(Object data) {

		return get().getDataSize(data);
	}
}
