package aaaa;


import java.io.UnsupportedEncodingException;

import lejos.nxt.remote.AsciizCodec;


public class aaaaa {
	public static void main (String[] args) throws UnsupportedEncodingException {
		byte[] a = new byte[] {(byte)18,(byte)0};
		System.out.println(AsciizCodec.decode(a));
		System.out.println(AsciizCodec.encode("49"));
	}
}
