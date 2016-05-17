package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	/**
	 * Splits the string that contains building and room
	 * @param str
     */
	public void splitRoom (String str) {
		try {
			String[] parts = str.split(":");
			String temp = parts[0];
			room = parts[1];

			/*
			 * Converts the string to correspond to the correct database
			 * '&' if the map already exist, '#' to download entire map-package
			 */
            if(temp.equals("G8")  || temp.equals("&G8")  || temp.equals("#G8")){
                building = "gaddan";
            } else if(temp.equals("NI")  || temp.equals("&NI")  || temp.equals("#NI")) {
                building = "niagara";
            } else if(temp.equals("OR") || temp.equals("&OR") || temp.equals("#OR")){
                building = "orkanen";
            }

		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println("Error: " + e);
		}
	}

	public String splitFloor(String str) {
        Matcher matcher = Pattern.compile("\\d+").matcher(str);
        matcher.find();
        String index = ""+Integer.valueOf(matcher.group());
        String floor = index.substring(0, 1);

        return floor;
    }

	/**
	 * Returns the building part of the room string
	 * @return buildingString
     */
	public String getBuildingString() {
		return building;
    }

	/**
	 * Returns the room part of the (entire) room string
	 * @return roomString
     */
	public String getRoomString() {
		return room;
	}

	/**
	 * Splits up the coordinates to two separate x and y strings
	 * @param str
     */
	public void splitCoor(String str) {
		String[] parts = str.split("\\.");

        x = Integer.parseInt(parts[0]);
		y = Integer.parseInt(parts[1]);
	}

	/**
	 * Returns the X-coordinate
	 * @return xCoordinates
     */
    public int getX() {
        return x;
    }

	/**
	 * Returns the y-coordinate
	 * @return yCoordinate
     */
    public int getY() {
        return y;
    }
}
