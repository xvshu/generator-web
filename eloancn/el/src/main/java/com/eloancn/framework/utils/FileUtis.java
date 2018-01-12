package com.eloancn.framework.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * FileUtis.java
 * </p>
 * <p>
 * 文件读写工具类
 * </p>
 * <Detail Description>
 * 
 * @author jia
 * @since 2012-8-23 下午3:37:48
 */
public class FileUtis {

	public static final int BUF_SIZE = 2 * 1024;

	public static void writeByte(String fileName, byte[] content) {
		try {
			FileOutputStream os = new FileOutputStream(fileName);
			// 写入输出流
			os.write(content);
			// 关闭输出流
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static byte[] readFile(String fileName) {
		try {
			File file = new File(fileName);
			if (file != null && file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				if (fis != null) {
					int len = fis.available();
					byte[] xml = new byte[len];
					fis.read(xml);
					return xml;
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	public static void streamToFile(InputStream input, File dst)
			throws IOException {
		OutputStream out = null;
		try {
			if (dst.exists()) {
				out = new BufferedOutputStream(new FileOutputStream(dst, true),
						BUF_SIZE);
			} else {
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUF_SIZE);
			}
			byte[] buffer = new byte[BUF_SIZE];
			int len = 0;
			while ((len = input.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void outputToInput(FileInputStream fis, FileOutputStream fos) {
		try {
			byte[] b = new byte[1024];
			try {
				int length = 0;
				while ((length = fis.read(b)) != -1) {
					fos.write(b, 0, length);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
