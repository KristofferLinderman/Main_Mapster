package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileFunctions {

	public void sendFile(OutputStream os, String fileName) throws Exception {
		// File to send
		File myFile = new File(fileName);
		int fSize = (int) myFile.length();
		if (fSize < myFile.length()) {
			System.out.println("File is too big'");
			throw new IOException("File is too big.");
		}

		// Send the file's size
		byte[] bSize = new byte[4];
		bSize[0] = (byte) ((fSize & 0xff000000) >> 24);
		bSize[1] = (byte) ((fSize & 0x00ff0000) >> 16);
		bSize[2] = (byte) ((fSize & 0x0000ff00) >> 8);
		bSize[3] = (byte) (fSize & 0x000000ff);
		// 4 bytes containing the file size
		os.write(bSize, 0, 4);

		// In case of memory limitations set this to false
		boolean noMemoryLimitation = true;

		FileInputStream fis = new FileInputStream(myFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		try {
			if (noMemoryLimitation) {
				// Use to send the whole file in one chunk
				byte[] outBuffer = new byte[fSize];
				int bRead = bis.read(outBuffer, 0, outBuffer.length);
				os.write(outBuffer, 0, bRead);
			} else {
				// Use to send in a small buffer, several chunks
				int bRead = 0;
				byte[] outBuffer = new byte[8 * 1024];
				while ((bRead = bis.read(outBuffer, 0, outBuffer.length)) > 0) {
					os.write(outBuffer, 0, bRead);
				}
			}
			os.flush();
		} finally {
			bis.close();
		}
	}

}
