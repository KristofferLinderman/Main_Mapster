package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class with functions for sending files and splitting string
 * Created by Gustav on 2016-04-09.
 */
public class FileFunctions {

	private String building, room;
    private int x, y;

	/**
	 * Deconstructs a file and sends it with the OutputStream
	 * @param os
	 * @param fileName
	 * @throws Exception
     */
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

	public void splitRoom (String str) {
		try {
			String[] parts = str.split(":");

			String temp = parts[0];
			room = parts[1];


//      *************
//      TODO: Ändra så att databaserna heter NI, G8 och OR?? då slipper vi konverta strings
//      *************
            if(temp.equals("G8")){
                building = "gaddan";
            } else if(temp.equals("NI")) {
                building = "niagara";
            } else if(temp.equals("OR")){
                building = "orkanen";
            }
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println("Error: " + e);
		}
	}

	public String getBuildingString() {
		return building;
    }

	public String getRoomString() {
		return room;
	}



	public void splitCoor(String str) {
		String[] parts = str.split("\\.");

        x = Integer.parseInt(parts[0]);
		y = Integer.parseInt(parts[1]);
	}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
