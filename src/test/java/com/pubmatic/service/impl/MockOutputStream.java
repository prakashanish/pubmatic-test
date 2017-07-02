package com.pubmatic.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock class to capture OutputStream API calls.
 *
 * @author win7
 */
public class MockOutputStream extends OutputStream {

	private List<Object> bytes = new ArrayList<Object>();
	
	@Override
	public void write(int b) throws IOException {
		
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		bytes.add(b);
	}

	public List<Object> getWritenBytes() {
		return this.bytes;
	}
}
